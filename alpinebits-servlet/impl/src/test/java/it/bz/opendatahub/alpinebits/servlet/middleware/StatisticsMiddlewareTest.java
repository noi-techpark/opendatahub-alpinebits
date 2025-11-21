// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.middleware;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.Key;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test cases for {@link StatisticsMiddleware} class.
 */
public class StatisticsMiddlewareTest {

    @Test
    public void testHandleContext_nextMiddlewareIsCalled() {
        Key<String> key = Key.key("some key", String.class);
        Middleware middleware = new StatisticsMiddleware();
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_USERNAME, "user");
        ctx.put(RequestContextKey.REQUEST_VERSION, AlpineBitsVersion.V_2017_10);
        ctx.put(RequestContextKey.REQUEST_ACTION, AlpineBitsAction.GET_CAPABILITIES);
        middleware.handleContext(ctx, () -> ctx.put(key, "some value"));

        assertTrue(ctx.get(key).isPresent());
    }
}