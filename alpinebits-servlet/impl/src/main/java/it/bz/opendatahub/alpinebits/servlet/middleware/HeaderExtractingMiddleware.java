// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This middleware extracts the value of the header (specified by its
 * constructor parameter) from the HTTP request and adds it to the
 * {@link Context} using the context key (also specified by its constructor
 * parameter).
 * <p>
 * If no header with the specified name exists, the context key will not
 * be placed into the context at all. Therefore, a consumer of the context
 * key should use the {@link Context#get(Key)} to get the header value from
 * the context or use the {@link Context#contains(Key)} method to check if
 * the key exists before it is accessed.
 */
public class HeaderExtractingMiddleware implements Middleware {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderExtractingMiddleware.class);

    private final String headerName;
    private final Key<String> contextKey;

    public HeaderExtractingMiddleware(String headerName, Key<String> contextKey) {
        if (headerName == null) {
            throw new IllegalArgumentException("The header name must not be null");
        }
        if (contextKey == null) {
            throw new IllegalArgumentException("The context key must not be null");
        }
        this.headerName = headerName;
        this.contextKey = contextKey;
    }

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletRequest request = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);
        String headerValue = request.getHeader(this.headerName);

        if (headerValue == null) {
            LOG.debug("No header with name \"{}\" found in request", headerName);
        } else {
            ctx.put(this.contextKey, headerValue);
        }

        chain.next();
    }

}
