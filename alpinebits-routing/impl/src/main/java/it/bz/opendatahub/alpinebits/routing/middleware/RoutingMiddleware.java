// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing.middleware;

import it.bz.opendatahub.alpinebits.common.AlpineBitsVersionChecker;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.routing.Router;
import it.bz.opendatahub.alpinebits.routing.RouterContextKey;
import it.bz.opendatahub.alpinebits.routing.UndefinedRouteException;

import java.util.Optional;

/**
 * This {@link Middleware} provides routing capabilities for AlpineBits requests.
 * <p>
 * It tries to match the <code>version</code> and <code>action</code> of the
 * current AlpineBits request, taken from the {@link Context}.
 * <p>
 * The <code>version</code> information is read using the
 * {@link RequestContextKey#REQUEST_VERSION} key. The <code>action</code>
 * information is read using the {@link RequestContextKey#REQUEST_ACTION} key.
 * <p>
 * If a match was found, the corresponding middleware is invoked. A
 * {@link UndefinedRouteException} is thrown otherwise.
 * <p>
 * This middleware doesn't invoke {@link MiddlewareChain#next()}, i.e.
 * no other middlewares will be invoked after this one.
 */
public class RoutingMiddleware implements Middleware {

    private final Router router;

    public RoutingMiddleware(Router router) {
        if (router == null) {
            throw new IllegalArgumentException("The router must not be null");
        }
        this.router = router;
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        String version = ctx.getOrThrow(RequestContextKey.REQUEST_VERSION);
        String action = ctx.getOrThrow(RequestContextKey.REQUEST_ACTION);

        // Try to find a middleware for the given AlpineBits version and action
        Middleware middleware = this.findMiddleware(version, action);

        // Add router to context, such that other middlewares may use its information
        ctx.put(RouterContextKey.ALPINEBITS_ROUTER, this.router);

        // Invoke middleware
        middleware.handleContext(ctx, () -> {
        });
    }

    private Middleware findMiddleware(String version, String actionRequestParam) {
        // The 2024-10 spec contradicts itself when describing how to handle AlpineBits versions unknown to the server:
        // - in section "Server Validation" (p. 13) it says:
        //   "Unsupported Client Protocol Version: 400 Bad Request
        //   ERROR:your current alpinebits version does not match one of the servers supported versions"
        // - in section "Implementation tips and best practice" (p. 19) it says:
        //   "Server: if the server supports this same version, it answers with this version and the negotiation
        //   terminates successfully; otherwise the server answers with the highest version it supports."
        // Which one should we follow?
        // For now, we follow the first approach for AlpineBits 2024-10 and later and
        // we follow the second approach for AlpineBits versions prior to 2024-10 to not break existing clients.
        if (AlpineBitsVersionChecker.isVersionWithImplicitOTAPingInHandshaking(version) && !this.router.isVersionDefined(version)) {
            throw new UndefinedRouteException("your current alpinebits version does not match one of the servers supported versions");
        }

        Optional<Middleware> optional = this.router.findMiddleware(version, actionRequestParam);

        if (optional.isEmpty()) {
            throw new UndefinedRouteException("unknown or missing action");
        }

        return optional.get();
    }

}
