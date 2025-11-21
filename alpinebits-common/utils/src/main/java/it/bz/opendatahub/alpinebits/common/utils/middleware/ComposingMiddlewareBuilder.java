// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.common.utils.middleware;

import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Builder to compose a list of {@link Middleware} into one middleware.
 */
public final class ComposingMiddlewareBuilder {

    public static final String COMPOSING_MIDDLEWARE_NAME = "composing middleware";

    private static final Logger LOG = LoggerFactory.getLogger(ComposingMiddlewareBuilder.class);

    private ComposingMiddlewareBuilder() {
        // Empty
    }

    public static Middleware compose(Middleware ...incomingMiddlewares) {
        return compose(Arrays.asList(incomingMiddlewares));
    }

    /**
     * Compose a list of {@link Middleware} objects into a single middleware.
     * When the {@link Middleware#handleContext(Context, MiddlewareChain)} method of
     * the resulting middleware is called, than the first middleware of the
     * list is invoked. If that middleware calls {@link MiddlewareChain#next()},
     * then the next middleware in the list is called etc.
     * <p>
     * Middleware calls to {@link MiddlewareChain#next()} are respected over
     * the boundaries of the current {@code incomingMiddlewares} list, e.g.
     * if {@code incomingMiddlewares} contains two already composed middlewares
     * A and B, and the last middleware in A calls next(), then the first
     * middleware in B will be called, even if B was unknown at the time A was
     * build.
     * <p>
     * This method can be applied to ordinary middlewares, to already composed
     * middlewares and to a mix of them.
     *
     * @param incomingMiddlewares list of {@link Middleware} objects to compose into
     *                            a single middleware
     * @return a {@link Middleware} that, when its
     * {@link Middleware#handleContext(Context, MiddlewareChain)} method is called,
     * invokes all of the given middlewares
     * @throws IllegalArgumentException if the list of middlewares is null or if
     *                                  one of the middlewares in the list is null
     */
    @SuppressWarnings("checkstyle:illegalcatch")
    public static Middleware compose(List<Middleware> incomingMiddlewares) {
        if (incomingMiddlewares == null) {
            throw new IllegalArgumentException("The list of middlewares must not be null");
        }

        for (Middleware middleware : incomingMiddlewares) {
            if (middleware == null) {
                throw new IllegalArgumentException("The middleware must not be null");
            }
        }

        // Make a copy of the middleware list, such that external changes to the list
        // don't affect the composed middlewares
        List<Middleware> middlewares = new CopyOnWriteArrayList<>(incomingMiddlewares);

        // Return a new middleware. When its handleContext method is called, it invokes the
        // first middleware in the list. The next middleware is invoked, when the current
        // one calls {@link MiddlewareChain#next()}
        return new Middleware() {
            @Override
            public void handleContext(Context ctx, MiddlewareChain chain) {
                LOG.debug("Composing middleware is invoked. It consists of {} middlewares", middlewares.size());

                LOG.trace("The middlewares are: {}", middlewares);

                // Build a consumer that, when invoked (i.e. "accepted"), calls the chains
                // next method, leading to the following middlware to be invoked
                Consumer<Context> next = chain != null ? context -> chain.next() : context -> {
                };

                // Build a Consumer for the first middleware in the list. If there is no
                // element in the list, {@link ComposingMiddlewareBuilder#dispatch} returns a Consumer
                // that invokes {@code next} when itself gets invoked
                Consumer<Context> first = ComposingMiddlewareBuilder.dispatch(middlewares, 0, next);

                LOG.trace("First consumer is going to be accepted: {}", first);

                // Invoke the first Consumer. In this case it means, calling its
                // {@link Consumer#accept(Object)} method
                first.accept(ctx);
            }

            @Override
            public String toString() {
                return COMPOSING_MIDDLEWARE_NAME;
            }
        };
    }

    /**
     * Build a {@link Consumer}, that has two different outcomes when invoked through
     * {@link Consumer#accept(Object)}:
     * <p>
     * <ul>
     * <li>
     * if {@code middlewares} at {@code index} is a valid position,
     * i.e. {@code index < middlewares.size()}, build a Consumer for the successive
     * middleware and invoke the current middleware's handler, passing the Consumer
     * as argument. This way, a chain of middlewares is established, that can be
     * followed through by calling MiddlewareChain#next() in the middleware itself.
     * </li>
     * <li>
     * if {@code middlewares} at {@code index} is not a valid position,
     * i.e. {@code index >= middlewares.size()}, then the {@code next} Consumer
     * is invoked. This mechanism allows to chain middlewares over the boundaries
     * of middlewares known during the composition.
     * </li>
     * </ul>
     *
     * @param middlewares list of {@link Middleware} objects, where the current one,
     *                    identified by {@code index}, is chained either with the next
     *                    middleware in the list (if there is a next middleware), or
     *                    chained with the {@code next} {@link Consumer}, allowing
     *                    composed middlewares to invoke middlewares unknown at the time
     *                    of composition.
     * @param index       index of the current middleware
     * @param next        next {@link Consumer} to invoke, if there is no current middleware
     * @return a {@link Consumer} that can be used to chain middlewares unknown at the
     * time the middlewares were composed.
     */
    private static Consumer<Context> dispatch(List<Middleware> middlewares, int index, Consumer<Context> next) {
        LOG.trace(
                "Composing middlewares dispatch method called for middlewares {}, index {} of {}, and next {}",
                middlewares,
                index,
                middlewares.size(),
                next
        );

        Middleware currentMiddleware = index < middlewares.size() ? middlewares.get(index) : null;

        return new Consumer<>() {
            @Override
            public void accept(Context ctx) {
                if (currentMiddleware != null) {
                    LOG.trace(
                            "Consumer {} accepted. Current middleware {} found ({} of {}). Building next consumer",
                            this,
                            currentMiddleware,
                            index,
                            middlewares.size()
                    );

                    // A middleware was found. Build a consumer for the next middleware,
                    // that can be invoked later on
                    Consumer<Context> nextConsumer = ComposingMiddlewareBuilder.dispatch(middlewares, index + 1, next);

                    LOG.trace(
                            "Invoking current middleware's handler with next consumer {} as parameter to the chain",
                            nextConsumer
                    );

                    // Pass the next consumer to the handler of the current middleware.
                    // If the current middleware calls MiddlewareChain#next(), this consumer's
                    // Consumer#accept(Object) method is is called
                    currentMiddleware.handleContext(ctx, () -> nextConsumer.accept(ctx));
                } else {
                    LOG.trace(
                            "Consumer {} accepted. No middleware found, since index ({}) >= middlewares.size() ({}). Invoking next consumer",
                            this,
                            index,
                            middlewares.size()
                    );
                    // No more middleware found. Invoke the next consumer. Through this mechanism it
                    // is possible to call a middleware, that is not part of the current middlewares.
                    // This is important for the ComposingMiddlewareBuilder#compose(List<Middleware>) method
                    // to compose middlewares (ordinary and composed), where the MiddlewareChain#next()
                    // method keeps working.
                    next.accept(ctx);
                }
            }
        };
    }
}
