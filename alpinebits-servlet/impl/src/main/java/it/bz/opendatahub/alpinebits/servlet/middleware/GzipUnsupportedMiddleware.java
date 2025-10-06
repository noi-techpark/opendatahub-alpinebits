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
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.servlet.GzipUnsupportedException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * This middleware checks if the HTTP requests <code>Content-Encoding</code> header is
 * set to <code>gzip</code>, in which case it throws a {@link GzipUnsupportedException}
 * with HTTP status code of 501, to indicate that the server doesn't support gzip
 * compressed requests.
 * <p>
 * The HTTP request must be present in the {@link Context}. Otherwise, a
 * {@link RequiredContextKeyMissingException} is thrown.
 */
public class GzipUnsupportedMiddleware implements Middleware {

    public static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    public static final String GZIP = "gzip";

    private static final Logger LOG = LoggerFactory.getLogger(GzipUnsupportedMiddleware.class);
    private static final String ERROR_MESSAGE = "unsupported GZIP compression";

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletRequest request = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);

        String contentType = request.getHeader(CONTENT_ENCODING_HEADER);
        if (GZIP.equalsIgnoreCase(contentType)) {
            throw new GzipUnsupportedException(ERROR_MESSAGE);
        }

        LOG.debug("Content-Encoding header value: {}", contentType);

        chain.next();
    }

}
