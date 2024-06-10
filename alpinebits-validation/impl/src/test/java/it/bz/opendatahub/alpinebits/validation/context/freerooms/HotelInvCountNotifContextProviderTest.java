// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.context.freerooms;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.DefaultRouter;
import it.bz.opendatahub.alpinebits.routing.Router;
import it.bz.opendatahub.alpinebits.routing.RouterContextKey;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Tests for {@link HotelInvCountNotifContextProvider}.
 */
public class HotelInvCountNotifContextProviderTest {

    private static final String DEFAULT_ALPINEBITS_VERSION = AlpineBitsVersion.V_2022_10;
    private static final Action DEFAULT_ACTION = Action.of("REQUEST_PARAM", "NAME");

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuildContext_ShouldThrow_WhenContexctIsNull() {
        new HotelInvCountNotifContextProvider().buildContext(null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testBuildContext_ShouldThrow_WhenVersionIsNull() {
        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, this.buildRouterWithCapabilities());
        new HotelInvCountNotifContextProvider().buildContext(ctx);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testBuildContext_ShouldThrow_WhenRouterIsNull() {
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, AlpineBitsVersion.V_2022_10);
        new HotelInvCountNotifContextProvider().buildContext(ctx);
    }

    @Test
    public void testBuildContext_ShouldSupportCategories_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CATEGORIES);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isCategoriesSupported());
    }

    @Test
    public void testBuildContext_ShouldSupportRooms_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_ROOMS);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isRoomsSupported());
    }

    @Test
    public void testBuildContext_ShouldSupportDeltas_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_DELTAS);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isDeltasSupported());
    }

    @Test
    public void testBuildContext_ShouldSupportOutOfMarket_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_MARKET);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isOutOfMarketSupported());
    }

    @Test
    public void testBuildContext_ShouldSupportOutOfOrder_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_ORDER);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isOutOfOrderSupported());
    }

    @Test
    public void testBuildContext_ShouldSupportClosingSeasons_IfCapabilityIsDefinedInRouter() {
        Context ctx = this.buildContextWithRouterCapability(AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CLOSING_SEASONS);
        HotelInvCountNotifContext hctx = new HotelInvCountNotifContextProvider().buildContext(ctx);
        assertTrue(hctx.isClosingSeasonsSupported());
    }

    private Router buildRouterWithCapabilities(String... capabilities) {
        return new DefaultRouter.Builder()
                .version(DEFAULT_ALPINEBITS_VERSION)
                .supportsAction(DEFAULT_ACTION)
                .withCapabilities(capabilities)
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();
    }

    private Context buildContextWithRouterCapability(String... capabilities) {
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_VERSION, AlpineBitsVersion.V_2022_10);
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, this.buildRouterWithCapabilities(capabilities));
        return ctx;
    }
}