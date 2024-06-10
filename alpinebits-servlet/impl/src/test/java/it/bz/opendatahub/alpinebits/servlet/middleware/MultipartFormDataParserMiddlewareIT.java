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
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for {@link MultipartFormDataParserMiddleware}.
 */
public class MultipartFormDataParserMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(MultipartFormDataParserMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-multipart-formdata-parser-middleware-integration-test.xml", "web.xml");

        return war;
    }

    @DataProvider(name = "badContentType")
    public static Object[][] badContentType() {
        return new Object[][]{
                {""},
                {"wrong-content-type"},
        };
    }

    @Test(dataProvider = "badContentType")
    @RunAsClient
    public void testHandleContext_NoContentType(String contentType) {
        given()
                .contentType(contentType)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_MultipartFormDataParseError() {
        given()
                .contentType("multipart/form-data")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_NoActionParam() {
        given()
                .multiPart("no_action_part", "")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .and()
                .body(containsString("ERROR"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_CheckActionParamInContext() {
        given()
                .multiPart("action", "")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }

    @Test
    @RunAsClient
    public void testHandleContext_CheckRequestParamInContext() {
        given()
                .multiPart("action", "")
                .multiPart("request", "")
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }
}