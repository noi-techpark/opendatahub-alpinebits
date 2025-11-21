// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.common.context;

import it.bz.opendatahub.alpinebits.middleware.Key;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * This class contains key definitions, that may be used e.g. in a middleware context.
 */
public final class RequestContextKey {

    /**
     * Context key for request ID.
     */
    public static final Key<String> REQUEST_ID = Key.key(
            "request.id", String.class
    );

    /**
     * Context key for username.
     */
    public static final Key<String> REQUEST_USERNAME = Key.key(
            "request.username", String.class
    );

    /**
     * Context key for password. The {@link Supplier} type of this key represents
     * the intent to not store the password as String in the context, since this
     * could have security implications, e.g. if the context is logged somewhere.
     * Instead, the password is wrapped with a {@link Supplier}.
     */
    @SuppressWarnings("rawtypes")
    public static final Key<Supplier> REQUEST_PASSWORD_SUPPLIER = Key.key(
            "request.password.supplier", Supplier.class
    );

    /**
     * Context key for AlpineBits version.
     */
    public static final Key<String> REQUEST_VERSION = Key.key(
            "request.version", String.class
    );

    /**
     * Context key for AlpineBits action.
     */
    public static final Key<String> REQUEST_ACTION = Key.key(
            "request.action", String.class
    );

    /**
     * Context key for AlpineBits request content as an InputStream, i.e. the XML as stream.
     */
    public static final Key<InputStream> REQUEST_CONTENT_STREAM = Key.key(
            "request.content.stream", InputStream.class
    );

    private RequestContextKey() {
        // Empty
    }

}
