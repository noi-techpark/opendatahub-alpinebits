// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking.utils;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.common.utils.middleware.ComposingMiddlewareBuilder;
import it.bz.opendatahub.alpinebits.handshaking.DefaultContextSerializer;
import it.bz.opendatahub.alpinebits.handshaking.middleware.HandshakingMiddleware;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.routing.DefaultRouter;
import it.bz.opendatahub.alpinebits.routing.RoutingBuilder;
import it.bz.opendatahub.alpinebits.routing.constants.Action;
import it.bz.opendatahub.alpinebits.routing.middleware.RoutingMiddleware;
import it.bz.opendatahub.alpinebits.servlet.middleware.AlpineBitsClientProtocolMiddleware;
import it.bz.opendatahub.alpinebits.servlet.middleware.MultipartFormDataParserMiddleware;

import java.util.Arrays;

/**
 * Helper class to build pre-defined routing {@link Middleware}.
 */
public class RouterMiddlewareBuilder {

    public static Middleware buildRoutingMiddlewareForIntegrationTest() {
        RoutingBuilder.FinalBuilder builder = new DefaultRouter.Builder()
                .version(AlpineBitsVersion.V_2017_10)
                .supportsAction(Action.GET_VERSION)
                .withCapabilities()
                .using((ctx, chain) -> {})
                .and()
                .supportsAction(Action.GET_CAPABILITIES)
                .withCapabilities()
                .using((ctx, chain) -> {})
                .versionComplete()
                .and()

                .version(AlpineBitsVersion.V_2018_10)
                .supportsAction(Action.HANDSHAKING)
                .withCapabilities(AlpineBitsCapability.HANDSHAKING)
                .using(new HandshakingMiddleware(
                        new DefaultContextSerializer(AlpineBitsVersion.V_2018_10))
                )
                .and()
                .supportsAction(Action.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES)
                .withCapabilities(AlpineBitsCapability.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES_DELTAS)
                .using((ctx, chain) -> {
                })
                .versionComplete()
                .and()

                .version(AlpineBitsVersion.V_2024_10)
                .supportsAction(Action.HANDSHAKING)
                .withCapabilities(AlpineBitsCapability.HANDSHAKING)
                .using(new HandshakingMiddleware(
                        new DefaultContextSerializer(AlpineBitsVersion.V_2024_10))
                )
                .and()
                .supportsAction(Action.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES)
                .withCapabilities(AlpineBitsCapability.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES_DELTAS)
                .using((ctx, chain) -> {
                })
                .versionComplete();

        Middleware routingMiddleware = new RoutingMiddleware(builder.buildRouter());

        return ComposingMiddlewareBuilder.compose(Arrays.asList(
                new AlpineBitsClientProtocolMiddleware(),
                new MultipartFormDataParserMiddleware(),
                routingMiddleware
        ));
    }
}
