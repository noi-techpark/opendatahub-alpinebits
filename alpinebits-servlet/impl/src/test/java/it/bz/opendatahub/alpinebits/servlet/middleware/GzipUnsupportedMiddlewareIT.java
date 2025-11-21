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
import org.testng.annotations.Test;

import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for {@link AlpineBitsClientProtocolMiddleware}.
 */
public class GzipUnsupportedMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(GzipUnsupportedMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-gzip-unsupported-middleware-integration-test.xml", "web.xml");
    }

    @Test
    @RunAsClient
    public void testHandleContext_ContentEncodingIsGzip() {
        given()
                .header(GzipUnsupportedMiddleware.CONTENT_ENCODING_HEADER, GzipUnsupportedMiddleware.GZIP)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_NOT_IMPLEMENTED)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_AlpineBitsClientProtocolHeaderDefined() {
        when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }

}