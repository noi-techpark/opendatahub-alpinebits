// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * <p>
 * This response-phase middleware checks if the {@link Context} contains a
 * {@link ResponseContextKeys#RESPONSE_CONTENT_TYPE_HINT} key and sets
 * the HTTP response "Content-Type" header accordingly.
 * <p>
 * This middleware does nothing when there is no such key or when the
 * "Content-Type" header is already set.
 */
public class ContentTypeHintMiddleware implements Middleware {

    public static final String RESPONSE_CONTENT_TYPE_HEADER = "Content-Type";

    private static final Logger LOG = LoggerFactory.getLogger(ContentTypeHintMiddleware.class);

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        // Invoke next request-phase middlewares
        chain.next();

        HttpServletResponse response = ctx.getOrThrow(ServletContextKey.SERVLET_RESPONSE);

        // Check if Content-Type header is already set in response
        Optional<String> headerValue = getHeaderValue(response, RESPONSE_CONTENT_TYPE_HEADER);
        if (headerValue.isPresent()) {
            LOG.debug("Found the {} header with value \"{}\" in the response, the header is left as it is", RESPONSE_CONTENT_TYPE_HEADER, headerValue.get());
            return;
        }

        // If there is a Content-Type hint in the context, then set the Content-Type header accordingly
        ctx.get(ResponseContextKeys.RESPONSE_CONTENT_TYPE_HINT)
                .ifPresent(hint -> {
                    LOG.debug("Found hint for {} header with value \"{}\". Setting the HTTP header accordingly", RESPONSE_CONTENT_TYPE_HEADER, hint);
                    response.setHeader(RESPONSE_CONTENT_TYPE_HEADER, hint);
                });
    }

    public Optional<String> getHeaderValue(HttpServletResponse response, String name) {
        if (response == null) {
            throw new IllegalArgumentException("Response must not be null");
        }

        return response.getHeaderNames().stream()
                .filter(h -> h.equalsIgnoreCase(name))
                .findAny()
                .map(response::getHeader);
    }

}
