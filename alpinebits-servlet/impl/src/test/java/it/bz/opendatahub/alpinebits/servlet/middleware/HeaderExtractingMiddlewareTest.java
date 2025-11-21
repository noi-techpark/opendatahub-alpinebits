// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletRequest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Tests for {@link HeaderExtractingMiddleware}.
 */
public class HeaderExtractingMiddlewareTest {

    private static final String DEFAULT_HEADER_NAME = "X-Header";
    private static final Key<String> DEFAULT_CONTEXT_KEY = Key.key("header-value", String.class);

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleContext_ShouldThrow_WhenHeaderNameIsNull() {
        new HeaderExtractingMiddleware(null, DEFAULT_CONTEXT_KEY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleContext_ShouldThrow_WhenContextKeyIsNull() {
        new HeaderExtractingMiddleware(DEFAULT_HEADER_NAME, null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestIsNull() {
        Context ctx = new SimpleContext();
        Middleware middleware = new HeaderExtractingMiddleware(DEFAULT_HEADER_NAME, DEFAULT_CONTEXT_KEY);
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_ShouldNotAddKeyToContext_WhenHeaderIsMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(DEFAULT_HEADER_NAME)).thenReturn(null);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new HeaderExtractingMiddleware(DEFAULT_HEADER_NAME, DEFAULT_CONTEXT_KEY);
        middleware.handleContext(ctx, () -> {
        });

        boolean headerValueExists = ctx.contains(DEFAULT_CONTEXT_KEY);
        assertFalse(headerValueExists);
    }

    @Test
    public void testHandleContext_ShouldPutHeaderValueInContext_WhenHeaderIsDefined() {
        String headerValue = "test-value";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(DEFAULT_HEADER_NAME)).thenReturn(headerValue);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new HeaderExtractingMiddleware(DEFAULT_HEADER_NAME, DEFAULT_CONTEXT_KEY);
        middleware.handleContext(ctx, () -> {
        });

        String headerValueFromContext = ctx.getOrThrow(DEFAULT_CONTEXT_KEY);
        assertEquals(headerValueFromContext, headerValue);
    }

}