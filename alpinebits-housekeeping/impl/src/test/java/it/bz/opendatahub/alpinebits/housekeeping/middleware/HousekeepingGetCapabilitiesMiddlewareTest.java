// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.housekeeping.middleware;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.HttpContentTypeHeaderValues;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.housekeeping.middleware.utils.RouterBuilder;
import it.bz.opendatahub.alpinebits.housekeeping.middleware.utils.RouterMiddlewareBuilder;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.RouterContextKey;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link HousekeepingGetCapabilitiesMiddleware} class.
 */
public class HousekeepingGetCapabilitiesMiddlewareTest {

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_AlpineBitsRouterIsNull() {
        Middleware middleware = new HousekeepingGetCapabilitiesMiddleware();
        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_RequestVersionIsNull() {
        Middleware middleware = new HousekeepingGetCapabilitiesMiddleware();
        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, RouterBuilder.buildDefaultRouter());
        middleware.handleContext(ctx, null);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    @Test
    public void testHandleContext_ReturnSingleCapability() {
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, RouterMiddlewareBuilder.DEFAULT_VERSION);
        ctx.put(RequestContextKey.REQUEST_ACTION, AlpineBitsAction.GET_CAPABILITIES);
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, new ByteArrayOutputStream());

        // This routing middleware is configured for a single action: getCapabilitites,
        // implemented by HousekeepingGetCapabilitiesMiddleware.class. Therefore, when
        // invoked with getCapabilities as action, it returns exactly this action
        Middleware routingMiddleware = RouterMiddlewareBuilder.buildRoutingMiddlewareWithCapabilititesAction();
        routingMiddleware.handleContext(ctx, null);

        // Read the capabilities response from the context
        ByteArrayOutputStream responseStream = (ByteArrayOutputStream) ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);
        String resultCapabilities = responseStream.toString(StandardCharsets.UTF_8).substring(3);
        Collection<String> capabilities = Arrays.asList(resultCapabilities.split(","));

        assertEquals(capabilities, Collections.singletonList(AlpineBitsCapability.GET_CAPABILITIES));
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    @Test
    public void testHandleContext_ReturnManyCapabilities() {
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, RouterMiddlewareBuilder.DEFAULT_VERSION);
        ctx.put(RequestContextKey.REQUEST_ACTION, AlpineBitsAction.GET_CAPABILITIES);
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, new ByteArrayOutputStream());

        String customCapability = "cap1";

        // This routing middleware is configured for the getCapabilities and
        // RouterMiddlewareBuilder#DEFAULT_ACTION. Therefore, when invoked with
        // getCapabilities as action, it returns two actions
        Middleware routingMiddleware = RouterMiddlewareBuilder.buildRoutingMiddlewareWithCapabilitiesAndCustomAction(customCapability);
        routingMiddleware.handleContext(ctx, null);

        // Read the capabilities response from the context
        ByteArrayOutputStream responseStream = (ByteArrayOutputStream) ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_STREAM);
        String resultCapabilities = responseStream.toString(StandardCharsets.UTF_8).substring(3);
        Collection<String> capabilities = Arrays.asList(resultCapabilities.split(","));

        Set<String> expectedCapabilities = new HashSet<>(
                Arrays.asList(
                        AlpineBitsCapability.GET_CAPABILITIES,
                        customCapability
                )
        );
        assertEquals(capabilities, expectedCapabilities);
        assertEquals(ctx.getOrThrow(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT), HttpContentTypeHeaderValues.TEXT_PLAIN);
    }
}