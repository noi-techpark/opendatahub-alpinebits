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
import it.bz.opendatahub.alpinebits.servlet.BasicAuthenticationException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * This middleware extracts the value of the <code>Authorization</code> header from the
 * HTTP request. The header value must contain a <code>Basic Authentication</code> (refer to
 * <a href="http://www.faqs.org/rfcs/rfc1945.html">RFC 1945, Section 11.1</a> for more
 * information). Please note, that this middleware doesn't check the provided credentials
 * for validity. It just extracts the credentials and adds them to the {@link Context}
 * for later usage.
 * <p>
 * Given a <code>Basic Authentication</code> was found, the username is added to the
 * context using the {@link RequestContextKey#REQUEST_USERNAME} key, the password is added
 * using the {@link RequestContextKey#REQUEST_PASSWORD_SUPPLIER} key.
 * <p>
 * The HTTP request must be present in the {@link Context}. Otherwise, a
 * {@link RequiredContextKeyMissingException} is thrown.
 * <p>
 * If no <code>Basic Authentication</code> header was found, or the <code>Basic Authentication</code>
 * could not be parsed, a {@link BasicAuthenticationException} is thrown.
 */
public class BasicAuthenticationMiddleware implements Middleware {

    public static final String BASIC_AUTHENTICATION_HEADER = "Authorization";

    private static final Logger LOG = LoggerFactory.getLogger(BasicAuthenticationMiddleware.class);
    private static final String ERROR_MESSAGE = "invalid or missing username/password";

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletRequest request = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);

        String header = request.getHeader(BASIC_AUTHENTICATION_HEADER);
        if (header == null || !header.toLowerCase().startsWith("basic ")) {
            throw new BasicAuthenticationException(ERROR_MESSAGE);
        }

        String[] tokens = this.extractAndDecodeHeader(header);

        String username = tokens[0];

        MDC.put("username", username);

        LOG.debug("Basic authentication header found for user '{}'", username);

        // Add username and password to context
        ctx.put(RequestContextKey.REQUEST_USERNAME, username);
        ctx.put(RequestContextKey.REQUEST_PASSWORD_SUPPLIER, () -> tokens[1]);

        chain.next();
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BasicAuthenticationException if the Basic header is not present or is not valid Base64
     */
    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token;
        base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BasicAuthenticationException(ERROR_MESSAGE);
        }

        String token;
        token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(':');

        if (delim == -1) {
            throw new BasicAuthenticationException(ERROR_MESSAGE);
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

}
