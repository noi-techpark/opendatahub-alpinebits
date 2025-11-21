// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;


import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.servlet.BasicAuthenticationException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletRequest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Base64;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link BasicAuthenticationMiddleware} class.
 */
public class BasicAuthenticationMiddlewareTest {

    @DataProvider(name = "badBasicAuthentication")
    public static Object[][] badBasicAuthentication() {
        return new Object[][]{
                {null},
                {""},
                {"no authorization header"},
                {"basic "},
                {"basic wrong format"},
        };
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestIsNull() {
        Middleware middleware = new BasicAuthenticationMiddleware();

        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, null);
    }

    @Test(dataProvider = "badBasicAuthentication", expectedExceptions = BasicAuthenticationException.class)
    public void testHandleContext_BadAuthorization(String basicAuthentication) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(BasicAuthenticationMiddleware.BASIC_AUTHENTICATION_HEADER))
                .thenReturn(basicAuthentication);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new BasicAuthenticationMiddleware();
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_ValidAuthorization() {
        String username = "Aladdin";
        String password = "open sesame";
        String decodedBasicAuth = username + ":" + password;
        String encodedBasicAuth = Base64.getEncoder().encodeToString(decodedBasicAuth.getBytes());
        String authorizationString = "Basic " + encodedBasicAuth;

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(BasicAuthenticationMiddleware.BASIC_AUTHENTICATION_HEADER))
                .thenReturn(authorizationString);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new BasicAuthenticationMiddleware();
        middleware.handleContext(ctx, () -> {
        });

        String usernameValue = ctx.getOrThrow(RequestContextKey.REQUEST_USERNAME);
        assertEquals(usernameValue, username);

        Supplier<String> passwordSupplier = ctx.getOrThrow(RequestContextKey.REQUEST_PASSWORD_SUPPLIER);
        String passwordValue = passwordSupplier.get();
        assertEquals(passwordValue, password);
    }

}