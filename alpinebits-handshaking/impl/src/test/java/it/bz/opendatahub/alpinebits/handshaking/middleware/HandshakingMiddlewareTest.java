// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking.middleware;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.handshaking.ContextSerializer;
import it.bz.opendatahub.alpinebits.handshaking.HandshakingDataConversionException;
import it.bz.opendatahub.alpinebits.handshaking.JsonSerializer;
import it.bz.opendatahub.alpinebits.handshaking.dto.SupportedAction;
import it.bz.opendatahub.alpinebits.handshaking.dto.SupportedVersion;
import it.bz.opendatahub.alpinebits.handshaking.utils.ConfigurableContextSerializer;
import it.bz.opendatahub.alpinebits.handshaking.utils.EmptyContextSerializer;
import it.bz.opendatahub.alpinebits.handshaking.utils.HandshakingDataBuilder;
import it.bz.opendatahub.alpinebits.handshaking.utils.RouterBuilder;
import it.bz.opendatahub.alpinebits.handshaking.utils.ThrowingContextSerializer;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.routing.DefaultRouter;
import it.bz.opendatahub.alpinebits.routing.Router;
import it.bz.opendatahub.alpinebits.routing.RouterContextKey;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.WarningsType;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Test cases for {@link HandshakingMiddleware} class.
 */
public class HandshakingMiddlewareTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testConstructor_ShouldThrow_WhenContextSerializerIsNull() {
        new HandshakingMiddleware(null);
    }


    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testHandleContext_ShouldThrow_WhenAlpineBitsRouterIsNull() {
        Context ctx = new SimpleContext();
        Middleware middleware = new HandshakingMiddleware(new EmptyContextSerializer());
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testHandleContext_ShouldThrow_WhenContextSerializerThrows() {
        Context ctx = getDefaultContext();
        ContextSerializer serializer = new ThrowingContextSerializer(new RuntimeException("error"));
        Middleware middleware = new HandshakingMiddleware(serializer);
        middleware.handleContext(ctx, null);
    }

    @Test(expectedExceptions = HandshakingDataConversionException.class)
    public void testHandleContext_ShouldThrow_WhenJsonSerializerThrows() {
        Context ctx = getDefaultContext();
        OTAPingRQ otaPingRQ = new OTAPingRQ();
        otaPingRQ.setEchoData("[invalid json");
        ContextSerializer serializer = new ConfigurableContextSerializer(otaPingRQ);
        Middleware middleware = new HandshakingMiddleware(serializer);
        middleware.handleContext(ctx, null);
    }

    @Test
    public void testHandleContext_ShouldReturnEmptyJsonObjectAsWarningContent_IfNoMatch() {
        Context ctx = getContextForRouter(RouterBuilder.buildRouter(RouterBuilder.DEFAULT_VERSION + 1));

        JsonSerializer jsonSerializer = new JsonSerializer();
        String echoData = jsonSerializer.toJson(HandshakingDataBuilder.getDefaultHandshakingData());

        OTAPingRQ otaPingRQ = new OTAPingRQ();
        otaPingRQ.setEchoData(echoData);

        ContextSerializer serializer = new ConfigurableContextSerializer(otaPingRQ);
        Middleware middleware = new HandshakingMiddleware(serializer);
        middleware.handleContext(ctx, null);

        OTAPingRS otaPingRs = ctx.getOrThrow(ConfigurableContextSerializer.OTA_PING_RS_KEY);

        checkOtaPingRsEchoData(otaPingRs, echoData);

        checkOtaPingRsWarning(otaPingRs, "{}");
    }

    @Test
    public void testHandleContext_ShouldReturnIntersectionAsWarningContent_OnMatch() {
        Context ctx = getContextForRouter(RouterBuilder.buildRouter(RouterBuilder.DEFAULT_VERSION));

        JsonSerializer jsonSerializer = new JsonSerializer();
        String echoData = jsonSerializer.toJson(HandshakingDataBuilder.getDefaultHandshakingData());

        OTAPingRQ otaPingRQ = new OTAPingRQ();
        otaPingRQ.setEchoData(echoData);

        ContextSerializer serializer = new ConfigurableContextSerializer(otaPingRQ);
        Middleware middleware = new HandshakingMiddleware(serializer);
        middleware.handleContext(ctx, null);

        OTAPingRS otaPingRs = ctx.getOrThrow(ConfigurableContextSerializer.OTA_PING_RS_KEY);

        checkOtaPingRsEchoData(otaPingRs, echoData);

        checkOtaPingRsWarning(otaPingRs, echoData);
    }

    @Test
    public void testHandleContext_ShouldTreatOTAPingAsImplicit_AsOf202410() {
        Router router = new DefaultRouter.Builder()
                .version(AlpineBitsVersion.V_2024_10)
                .supportsAction(Action.of(AlpineBitsCapability.HANDSHAKING, AlpineBitsCapability.HANDSHAKING))
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .and()
                .supportsAction(Action.of(AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF, AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF))
                .withCapabilities()
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .buildRouter();

        Context ctx = getContextForRouter(router);

        // Note, that here we don't declare the HANDSHAKING action, because it
        // is considered implicit as of version 2024-10 and should therefore
        // not be included in the result.
        Set<SupportedAction> supportedActions = new HashSet<>(
                HandshakingDataBuilder.getSupportedActions(
                        AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF,
                        new HashSet<>()
                )
        );

        SupportedVersion supportedVersion = HandshakingDataBuilder.getSupportedVersion(
                AlpineBitsVersion.V_2024_10,
                supportedActions
        );

        JsonSerializer jsonSerializer = new JsonSerializer();
        String echoData = jsonSerializer.toJson(HandshakingDataBuilder.getHandshakingData(supportedVersion));

        OTAPingRQ otaPingRQ = new OTAPingRQ();
        otaPingRQ.setEchoData(echoData);

        ContextSerializer serializer = new ConfigurableContextSerializer(otaPingRQ);
        Middleware middleware = new HandshakingMiddleware(serializer);
        middleware.handleContext(ctx, null);

        OTAPingRS otaPingRs = ctx.getOrThrow(ConfigurableContextSerializer.OTA_PING_RS_KEY);

        checkOtaPingRsEchoData(otaPingRs, echoData);

        checkOtaPingRsWarning(otaPingRs, echoData);
    }

    private Context getDefaultContext() {
        return getContextForRouter(RouterBuilder.DEFAULT_ROUTER);
    }

    private Context getContextForRouter(Router router) {
        Context ctx = new SimpleContext();
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, router);
        return ctx;
    }

    private void checkOtaPingRsEchoData(OTAPingRS otaPingRs, String echoData) {
        // Extract echo data from otaPingRs
        Optional<String> otaPingRsEchoData = otaPingRs.getSuccessesAndEchoDatasAndWarnings().stream()
                .filter(String.class::isInstance)
                .map(o -> Optional.of((String) o))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Echo data expected but no No echo data found"));

        assertTrue(otaPingRsEchoData.isPresent());
        assertEquals(otaPingRsEchoData.get(), echoData);
    }

    private void checkOtaPingRsWarning(OTAPingRS otaPingRs, String warningContent) {
        // Extract warning from otaPingRs
        Optional<WarningsType> otaPingRsWarnings = otaPingRs.getSuccessesAndEchoDatasAndWarnings().stream()
                .filter(WarningsType.class::isInstance)
                .map(o -> Optional.of((WarningsType) o))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Warning expected but no warning found"));

        assertTrue(otaPingRsWarnings.isPresent());

        WarningsType wt = otaPingRsWarnings.get();
        assertEquals(wt.getWarnings().size(), 1);
        assertEquals(wt.getWarnings().get(0).getValue(), warningContent);
    }

}