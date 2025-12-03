// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl.utils;

import it.bz.opendatahub.alpinebits.servlet.RequestExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * An empty {@link RequestExceptionHandler} used for testing.
 */
public class EmptyRequestExceptionHandler implements RequestExceptionHandler {

    @Override
    public void handleRequestException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        // Do nothing, since this RequestExceptionHandler is just used for testing
    }

}
