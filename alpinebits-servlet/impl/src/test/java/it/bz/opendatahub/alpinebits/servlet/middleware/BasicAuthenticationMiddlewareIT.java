// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.servlet.impl.AlpineBitsServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for {@link MultipartFormDataParserMiddleware}.
 */
public class BasicAuthenticationMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(BasicAuthenticationMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-basic-authentication-middleware-integration-test.xml", "web.xml");

        return war;
    }

    @DataProvider(name = "badBasicAuthentication")
    public static Object[][] badBasicAuthentication() {
        return new Object[][]{
                {""},
                {"wrong format"},
        };
    }

    @Test
    @RunAsClient
    public void testHandleContext_NoAuthorization() {
        when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test(dataProvider = "badBasicAuthentication")
    @RunAsClient
    public void testHandleContext_BadAuthorization(String basicAuthentication) {
        given()
                .header("basic", basicAuthentication)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_ValidAuthorization() {
        given()
                .auth().preemptive().basic("Aladdin", "open sesame")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }
}