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
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.MiddlewareChain;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A {@link Middleware} that sets a responses body, used for testing.
 */
public class ResponseStatusSettingMiddleware implements Middleware {

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletResponse response = ctx.getOrThrow(ServletContextKey.SERVLET_RESPONSE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
