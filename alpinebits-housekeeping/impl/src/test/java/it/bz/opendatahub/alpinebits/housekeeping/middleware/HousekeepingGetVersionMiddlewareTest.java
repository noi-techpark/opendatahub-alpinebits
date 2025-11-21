// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.housekeeping.middleware;

import it.bz.opendatahub.alpinebits.common.constants.HttpContentTypeHeaderValues;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.housekeeping.middleware.utils.RouterBuilder;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.RouterContextKey;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link HousekeepingGetVersionMiddleware} class.
 */
public class HousekeepingGetVersionMiddlewareTest {

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_AlpineBitsRouterIsNull() {
        Context ctx = new SimpleContext();

        Middleware middleware = new HousekeepingGetVersionMiddleware();
        middleware.handleContext(ctx, null);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestVersionIsNull() {
        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, RouterBuilder.buildDefaultRouter());

        Middleware middleware = new HousekeepingGetVersionMiddleware();
        middleware.handleContext(ctx, null);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    @Test
    public void testHandleContext_ReturnSameVersionIfMatch() {
        String version = RouterBuilder.DEFAULT_VERSION;

        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, RouterBuilder.buildRouterForVersion(version));
        ctx.put(RequestContextKey.REQUEST_VERSION, version);
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, new ByteArrayOutputStream());

        Middleware routingMiddleware = new HousekeepingGetVersionMiddleware();
        routingMiddleware.handleContext(ctx, null);

        ByteArrayOutputStream responseStream = (ByteArrayOutputStream)ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);
        String resultVersion = responseStream.toString(StandardCharsets.UTF_8).substring(3);

        assertEquals(resultVersion, version);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    @Test
    public void testHandleContext_ReturnHighestVersionIfNoMatch() {
        String version = RouterBuilder.DEFAULT_VERSION;
        String otherVersion = version + "1";

        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, RouterBuilder.buildRouterForVersion(version));
        ctx.put(RequestContextKey.REQUEST_VERSION, otherVersion);
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, new ByteArrayOutputStream());

        Middleware routingMiddleware = new HousekeepingGetVersionMiddleware();
        routingMiddleware.handleContext(ctx, null);

        ByteArrayOutputStream responseStream = (ByteArrayOutputStream)ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);
        String resultVersion = responseStream.toString(StandardCharsets.UTF_8).substring(3);

        assertEquals(resultVersion, version);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }
}