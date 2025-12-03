// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing.middleware;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.DefaultRouter;
import it.bz.opendatahub.alpinebits.routing.Router;
import it.bz.opendatahub.alpinebits.routing.UndefinedRouteException;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Test cases for {@link RoutingMiddleware} class.
 */
public class RoutingMiddlewareTest {

    private static final String DEFAULT_VERSION = AlpineBitsVersion.V_2017_10;
    private static final String DEFAULT_ACTION_REQUEST_PARAM = "action request param";
    private static final String DEFAULT_ACTION_NAME = "action name";
    private static final Action DEFAULT_ACTION = Action.of(DEFAULT_ACTION_REQUEST_PARAM, DEFAULT_ACTION_NAME);
    private static final String DEFAULT_CAPABILITY = "some capability";
    private static final String DEFAULT_KEY_IDENTIFIER = "test.key";

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testHandleContext_RouterIsNull() {
        new RoutingMiddleware(null);
    }


    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_AlpineBitsClientProtocolVersionIsNull() {
        Middleware middleware = this.getValidRoutingMiddleware();
        Context ctx = new SimpleContext();
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_AlpineBitsActionIsNull() {
        Middleware middleware = this.getValidRoutingMiddleware();
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, DEFAULT_VERSION);
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = UndefinedRouteException.class)
    public void testHandleContext_InvalidRouteUnknownVersion() {
        Key<String> testKey = this.getTestCtxKey(DEFAULT_KEY_IDENTIFIER);
        String testValue = "some value";

        Router router = new DefaultRouter.Builder()
                .version("some version")
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(DEFAULT_CAPABILITY)
                .using((ctx, chain) -> ctx.put(testKey, testValue))
                .versionComplete()
                .buildRouter();
        Middleware middleware = new RoutingMiddleware(router);
        Context ctx = this.getValidContext();
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = UndefinedRouteException.class)
    public void testHandleContext_InvalidRouteUnknownAction() {
        Key<String> testKey = this.getTestCtxKey(DEFAULT_KEY_IDENTIFIER);
        String testValue = "some value";

        Action otherAction = Action.of("some other", "name");

        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(otherAction)
                .withCapabilities(DEFAULT_CAPABILITY)
                .using((ctx, chain) -> ctx.put(testKey, testValue))
                .versionComplete()
                .buildRouter();
        Middleware middleware = new RoutingMiddleware(router);
        Context ctx = this.getValidContext();
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_InvokeRouteWithFoundMiddleware() {
        Key<String> testKey = this.getTestCtxKey(DEFAULT_KEY_IDENTIFIER);
        String testValue = "some value";

        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(DEFAULT_CAPABILITY)
                .using((ctx, chain) -> ctx.put(testKey, testValue))
                .versionComplete()
                .buildRouter();
        Middleware middleware = new RoutingMiddleware(router);
        Context ctx = this.getValidContext();
        middleware.handleContext(ctx, null);

        String resultValue = ctx.getOrThrow(testKey);
        assertEquals(resultValue, testValue);
    }


    @Test
    public void testHandleContext_OverwriteExistingAction() {
        Key<String> testKey1 = this.getTestCtxKey(DEFAULT_KEY_IDENTIFIER + 1);
        Key<String> testKey2 = this.getTestCtxKey(DEFAULT_KEY_IDENTIFIER + 2);
        String testValue1 = "some value1";
        String testValue2 = "some value2";

        // Configure router with two actions for the same version, where the
        // version and action specification is repeated. Therefor, the first
        // route is overwritten by the second one
        Router router = new DefaultRouter.Builder()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using((ctx, chain) -> {
                    ctx.put(testKey1, testValue1);
                    chain.next();
                })
                .versionComplete()
                .and()
                .version(DEFAULT_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using((ctx, chain) -> {
                    ctx.put(testKey2, testValue2);
                    chain.next();
                })
                .versionComplete()
                .buildRouter();

        Middleware middleware = new RoutingMiddleware(router);
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, DEFAULT_VERSION);
        ctx.put(RequestContextKey.REQUEST_ACTION, DEFAULT_ACTION.getRequestParameter());

        // The router calls the middleware, specified with
        // HttpContextKey.ALPINE_BITS_CLIENT_PROTOCOL_VERSION
        // and HttpContextKey.ALPINE_BITS_ACTION.
        middleware.handleContext(ctx, null);

        Optional<String> resultValue1 = ctx.get(testKey1);
        assertFalse(resultValue1.isPresent());

        String resultValue2 = ctx.getOrThrow(testKey2);
        assertEquals(resultValue2, testValue2);
    }

    private Middleware getValidRoutingMiddleware() {
        Router router = this.getValidRouter();
        return new RoutingMiddleware(router);
    }

    private Router getValidRouter() {
        return new DefaultRouter.Builder()
                .version("some version")
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();
    }

    /**
     * Returns a context with <code>version</code> set to DEFAULT_VERSION
     * and <code>action</code> set to DEFAULT_ACTION.
     *
     * @return context with <code>version</code> set to DEFAULT_VERSION
     * and <code>action</code> set to DEFAULT_ACTION
     */
    private Context getValidContext() {
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, DEFAULT_VERSION);
        ctx.put(RequestContextKey.REQUEST_ACTION, DEFAULT_ACTION.getRequestParameter());
        return ctx;
    }

    private Key<String> getTestCtxKey(String keyIdentifier) {
        return Key.key(keyIdentifier, String.class);
    }

}