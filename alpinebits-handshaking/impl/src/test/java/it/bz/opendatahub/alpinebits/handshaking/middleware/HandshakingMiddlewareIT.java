// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ValidatableResponse;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.handshaking.dto.HandshakingData;
import it.bz.opendatahub.alpinebits.handshaking.dto.SupportedAction;
import it.bz.opendatahub.alpinebits.handshaking.dto.SupportedVersion;
import it.bz.opendatahub.alpinebits.handshaking.utils.OTAPingRSExtractor;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import it.bz.opendatahub.alpinebits.servlet.impl.AlpineBitsServlet;
import it.bz.opendatahub.alpinebits.servlet.middleware.AlpineBitsClientProtocolMiddleware;
import it.bz.opendatahub.alpinebits.servlet.middleware.MultipartFormDataParserMiddleware;
import it.bz.opendatahub.alpinebits.xml.JAXBXmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlToObjectConverter;
import it.bz.opendatahub.alpinebits.xml.XmlValidationSchemaProvider;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRS;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

/**
 * Integration tests for {@link HandshakingMiddleware}.
 */
public class HandshakingMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(AlpineBitsClientProtocolMiddleware.class)
                .addClasses(MultipartFormDataParserMiddleware.class)
                .addClasses(IntegrationTestingMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-handshaking-middleware-integration-test.xml", "web.xml");
    }

    @Test
    @RunAsClient
    public void testHandleContext_InvokeHandshakingWithNoMatchingVersion() {
        String inputXml = fromResource("Handshake-OTA_PingRQ.xml");

        given()
                .header(
                        AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER,
                        AlpineBitsVersion.V_2018_10 + 1
                )
                .multiPart("action", AlpineBitsAction.HANDSHAKING)
                .multiPart("request", inputXml)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .body(containsString("ERROR:unknown or missing action"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_InvokeHandshakingWithVersionMatch() throws JsonProcessingException {
        String inputXml = fromResource("Handshake-OTA_PingRQ.xml");

        ValidatableResponse response = given()
                .header(
                        AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER,
                        AlpineBitsVersion.V_2024_10
                )
                .multiPart("action", AlpineBitsAction.HANDSHAKING)
                .multiPart("request", inputXml)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);

        XmlToObjectConverter<OTAPingRS> converter = new JAXBXmlToObjectConverter.Builder<>(OTAPingRS.class)
                .schema(XmlValidationSchemaProvider.buildXsdSchemaForAlpineBitsVersion(AlpineBitsVersion.V_2024_10))
                .build();
        OTAPingRS otaPingRS = converter.toObject(response.extract().body().asInputStream());

        ObjectMapper om = new ObjectMapper();
        Optional<String> warningData = OTAPingRSExtractor.extractWarning(otaPingRS);

        assertTrue(warningData.isPresent());

        HandshakingData handshakingData = om.readValue(warningData.get(), HandshakingData.class);

        assertEquals(handshakingData.getVersions().size(), 2);

        // Check 2018-10 version
        SupportedVersion version201810 = handshakingData.getVersions().stream()
                .filter(version -> AlpineBitsVersion.V_2018_10.equals(version.getVersion()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected version " + AlpineBitsVersion.V_2018_10 + " not found"));

        Set<SupportedAction> actions201810 = version201810.getActions();
        assertEquals(actions201810.size(), 2);
        actions201810.forEach(action -> {
            if (action.getAction().equals(Action.HANDSHAKING.getName())) {
                Set<String> capabilities = action.getSupports();
                assertNull(capabilities);
            } else if (action.getAction().equals(Action.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES.getName())) {
                Set<String> capabilities = action.getSupports();
                capabilities.forEach(capability -> assertEquals(capability, AlpineBitsCapability.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES_DELTAS));
            } else {
                throw new RuntimeException("Action not one of expected actions");
            }
        });

        // Check 2024-10 version
        SupportedVersion version202410 = handshakingData.getVersions().stream()
                .filter(version -> AlpineBitsVersion.V_2024_10.equals(version.getVersion()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Expected version " + AlpineBitsVersion.V_2024_10 + " not found"));

        Set<SupportedAction> actions202410 = version202410.getActions();
        assertEquals(actions202410.size(), 1);
        actions202410.forEach(action -> {
            if (action.getAction().equals(Action.HANDSHAKING.getName())) {
                throw new RuntimeException("As of AlpineBits 2024-10, OTA_Ping action is implicit and should not be part of result actions");
            } else if (action.getAction().equals(Action.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES.getName())) {
                Set<String> capabilities = action.getSupports();
                capabilities.forEach(capability -> assertEquals(capability, AlpineBitsCapability.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES_DELTAS));
            } else {
                throw new RuntimeException("Action not one of expected actions");
            }
        });
    }

    private String fromResource(String resource) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
        try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

}