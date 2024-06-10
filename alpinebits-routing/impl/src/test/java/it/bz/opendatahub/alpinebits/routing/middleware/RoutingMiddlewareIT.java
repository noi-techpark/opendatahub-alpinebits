// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing.middleware;

import it.bz.opendatahub.alpinebits.routing.utils.DefaultRouterMiddleware;
import it.bz.opendatahub.alpinebits.servlet.impl.AlpineBitsServlet;
import it.bz.opendatahub.alpinebits.servlet.middleware.AlpineBitsClientProtocolMiddleware;
import it.bz.opendatahub.alpinebits.servlet.middleware.MultipartFormDataParserMiddleware;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration tests for {@link RoutingMiddleware}.
 */
public class RoutingMiddlewareIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(name = "DefaultRouter", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createDeployment() {
        final WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(AlpineBitsClientProtocolMiddleware.class)
                .addClasses(MultipartFormDataParserMiddleware.class)
                .addClasses(RoutingMiddleware.class)
                .addClasses(DefaultRouterMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-routing-middleware-integration-test.xml", "web.xml");

        return war;
    }

    @Test
    @RunAsClient
    public void testHandleContext_InvalidRouteUnknownVersion() {
        given()
                .header(
                        AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER,
                        DefaultRouterMiddleware.DEFAULT_VERSION + 1
                )
                .multiPart("action", DefaultRouterMiddleware.DEFAULT_ACTION)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_InvalidRouteUnknownAction() {
        given()
                .header(
                        AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER,
                        DefaultRouterMiddleware.DEFAULT_VERSION
                )
                .multiPart("action", DefaultRouterMiddleware.DEFAULT_ACTION + 1)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_BAD_REQUEST)
                .and()
                .body(containsString("ERROR:"));
    }

    @Test
    @RunAsClient
    public void testHandleContext_InvokeRouteWithFoundMiddleware() {
        given()
                .header(
                        AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER,
                        DefaultRouterMiddleware.DEFAULT_VERSION
                )
                .multiPart("action", DefaultRouterMiddleware.DEFAULT_ACTION)
                .when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }

}