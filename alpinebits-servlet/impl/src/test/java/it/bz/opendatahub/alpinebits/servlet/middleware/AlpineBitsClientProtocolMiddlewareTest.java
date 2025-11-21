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
import it.bz.opendatahub.alpinebits.servlet.AlpineBitsClientProtocolMissingException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletRequest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * Tests cases for {@link AlpineBitsClientProtocolMiddleware} class.
 */
public class AlpineBitsClientProtocolMiddlewareTest {

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestIsNull() {
        Middleware middleware = new AlpineBitsClientProtocolMiddleware();

        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = AlpineBitsClientProtocolMissingException.class)
    public void testHandleContext_AlpineBitsClientProtocolHeaderMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER))
                .thenReturn(null);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new AlpineBitsClientProtocolMiddleware();
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_AlpineBitsClientProtocolHeaderDefined() {
        String clientProtocolVersion = "2017-10";

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(AlpineBitsClientProtocolMiddleware.CLIENT_PROTOCOL_VERSION_HEADER))
                .thenReturn(clientProtocolVersion);

        Context ctx = new SimpleContext();
        ctx.put(ServletContextKey.SERVLET_REQUEST, request);

        Middleware middleware = new AlpineBitsClientProtocolMiddleware();
        middleware.handleContext(ctx, () -> {
        });

        String clientProtocolVersionValue = ctx.getOrThrow(RequestContextKey.REQUEST_VERSION);
        assertEquals(clientProtocolVersionValue, clientProtocolVersion);
    }
}