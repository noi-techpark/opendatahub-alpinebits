// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.servlet.AlpineBitsClientProtocolMissingException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * This middleware extracts the value of the <code>X-AlpineBits-ClientProtocolVersion</code> header
 * from the HTTP request and adds it to the {@link Context} using the
 * {@link RequestContextKey#REQUEST_VERSION} key.
 * <p>
 * The HTTP request must be present in the {@link Context}. Otherwise, a
 * {@link RequiredContextKeyMissingException} is thrown.
 * <p>
 * If the <code>X-AlpineBits-ClientProtocolVersion</code> header is not present, an
 * {@link AlpineBitsClientProtocolMissingException} is thrown.
 */
public class AlpineBitsClientProtocolMiddleware implements Middleware {

    public static final String CLIENT_PROTOCOL_VERSION_HEADER = "X-AlpineBits-ClientProtocolVersion";

    private static final Logger LOG = LoggerFactory.getLogger(AlpineBitsClientProtocolMiddleware.class);
    private static final String ERROR_MESSAGE = "no valid client protocol version provided";

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletRequest request = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);

        String clientProtocolVersion = request.getHeader(CLIENT_PROTOCOL_VERSION_HEADER);
        if (clientProtocolVersion == null) {
            throw new AlpineBitsClientProtocolMissingException(ERROR_MESSAGE);
        }

        LOG.debug("X-AlpineBits-ClientProtocolVersion header found: {}", clientProtocolVersion);

        ctx.put(RequestContextKey.REQUEST_VERSION, clientProtocolVersion);

        chain.next();
    }

}
