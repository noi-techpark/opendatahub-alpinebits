// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.servlet.impl.utils.EmptyMiddleware;
import it.bz.opendatahub.alpinebits.servlet.impl.utils.ThrowingMiddleware;
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

import java.net.URL;

import static io.restassured.RestAssured.when;

/**
 * Integration tests for {@link AlpineBitsServlet}.
 */
public class AlpineBitsServletIT extends Arquillian {

    @ArquillianResource
    private URL base;

    @Deployment(name = "ServletWithEmptyMiddleware", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createDeploymentWithEmptyMiddleware() {
        final WebArchive emptyMiddlewareWar = ShrinkWrap.create(WebArchive.class, "testServletWithEmptyMiddleware.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(EmptyMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-alpinebits-servlet-with-empty-middleware-integration-test.xml", "web.xml");

        return emptyMiddlewareWar;
    }

    @Deployment(name = "ServletWithThrowingMiddleware", testable = false)
    @SuppressWarnings("ArquillianTooManyDeployment")
    public static WebArchive createDeploymentWithThrowingMiddleware() {
        final WebArchive throwingMiddlewareWar = ShrinkWrap.create(WebArchive.class, "testServletWithThrowingMiddleware.war")
                .addClasses(AlpineBitsServlet.class)
                .addClasses(ThrowingMiddleware.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("web-alpinebits-servlet-with-throwing-middleware-integration-test.xml", "web.xml");

        return throwingMiddlewareWar;
    }

    @Test(dataProvider = Arquillian.ARQUILLIAN_DATA_PROVIDER)
    @OperateOnDeployment("ServletWithEmptyMiddleware")
    @RunAsClient
    public void testDoGet_EmptyMiddleware() {
        when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_OK);
    }

    @Test(dataProvider = Arquillian.ARQUILLIAN_DATA_PROVIDER)
    @OperateOnDeployment("ServletWithThrowingMiddleware")
    @RunAsClient
    public void testDoGet_ThrowingMiddleware() {
        when()
                .post(this.base + "AlpineBits")
                .then()
                .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}