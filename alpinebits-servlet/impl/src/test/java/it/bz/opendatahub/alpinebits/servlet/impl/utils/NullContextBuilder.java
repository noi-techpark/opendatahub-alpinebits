// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl.utils;

import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.servlet.ContextBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A {@link ContextBuilder} that returns null, used for testing.
 */
public class NullContextBuilder implements ContextBuilder {

    @Override
    public Context fromRequest(HttpServletRequest request, HttpServletResponse response, String requestId) {
        // Return null, since this ContextBuilder is just used for testing
        return null;
    }

}
