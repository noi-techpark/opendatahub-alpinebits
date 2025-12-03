// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.middleware;

import it.bz.opendatahub.alpinebits.servlet.impl.AlpineBitsServlet;
import it.bz.opendatahub.alpinebits.xml.middleware.utils.NotValidatingXmlRequestMappingMiddleware;
import it.bz.opendatahub.alpinebits.xml.middleware.utils.RngValidatingXmlRequestMappingMiddleware;
import it.bz.opendatahub.alpinebits.xml.middleware.utils.XsdValidatingXmlRequestMappingMiddleware;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAReadRQ;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

/**
 * Integration tests for {@link XmlRequestMappingMiddleware}.
 */
public class XmlRequestMappingMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(name = "NoValidation", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createNotValidatingDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-no-validation.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(NotValidatingXmlRequestMappingMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("web/context.xml", "context.xml")
                .addAsWebInfResource("web/web-not-validating-xml-request-wrapping-middleware-integration-test.xml", "web.xml");
    }

    @Deployment(name = "RngValidation", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createRngValidatingDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-rng-validation.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(RngValidatingXmlRequestMappingMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("web/context.xml", "context.xml")
                .addAsWebInfResource("web/web-rng-validating-xml-request-wrapping-middleware-integration-test.xml", "web.xml");
    }

    @Deployment(name = "XsdValidation", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createXsdValidatingDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-xsd-validation.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(XsdValidatingXmlRequestMappingMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("web/context.xml", "context.xml")
                .addAsWebInfResource("web/web-xsd-validating-xml-request-wrapping-middleware-integration-test.xml", "web.xml");
    }

    @Test
    @OperateOnDeployment("NoValidation")
    @RunAsClient
    public void testHandleContext_NoXml() {
        given()
                .multiPart("action", "some action")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ERROR:"));
    }

    @Test
    @OperateOnDeployment("NoValidation")
    @RunAsClient
    public void testHandleContext_NotXml() {
        given()
                .multiPart("action", "some action")
                .multiPart("request", "this is not an XML")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .body(containsString("ERROR:"));
    }

    @Test
    @OperateOnDeployment("NoValidation")
    @RunAsClient
    public void testHandleContext_Ok() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");

        given()
                .multiPart("action", "some action")
                .multiPart("request", "request", is)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK)
                .body(containsString(OTAReadRQ.class.toString()));
    }

    @Test
    @OperateOnDeployment("RngValidation")
    @RunAsClient
    public void testHandleContext_RngValidation_InvalidXML() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ-invalid.xml");

        given()
                .multiPart("action", "some action")
                .multiPart("request", "request", is)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .body(containsString("ERROR:"));
    }

    @Test
    @OperateOnDeployment("RngValidation")
    @RunAsClient
    public void testHandleContext_RngValidation_Ok() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");

        given()
                .multiPart("action", "some action")
                .multiPart("request", "request", is)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK)
                .body(containsString(OTAReadRQ.class.toString()));
    }

    @Test
    @OperateOnDeployment("XsdValidation")
    @RunAsClient
    public void testHandleContext_XsdValidation_InvalidXML() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ-invalid.xml");

        given()
                .multiPart("action", "some action")
                .multiPart("request", "request", is)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .body(containsString("ERROR:"));
    }

    @Test
    @OperateOnDeployment("XsdValidation")
    @RunAsClient
    public void testHandleContext_XsdValidation_Ok() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("examples/v_2017_10/GuestRequests-OTA_ReadRQ.xml");

        given()
                .multiPart("action", "some action")
                .multiPart("request", "request", is)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK)
                .body(containsString(OTAReadRQ.class.toString()));
    }
}