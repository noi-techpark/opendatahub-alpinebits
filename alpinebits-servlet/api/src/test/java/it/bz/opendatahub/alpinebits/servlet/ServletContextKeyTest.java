// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link ServletContextKey} class.
 */
public class ServletContextKeyTest {

    @Test
    public void testContextKey_ServletRequest() {
        assertEquals(ServletContextKey.SERVLET_REQUEST.getType(), HttpServletRequest.class);
    }

    @Test
    public void testContextKey_ServletResponse() {
        assertEquals(ServletContextKey.SERVLET_RESPONSE.getType(), HttpServletResponse.class);
    }

}