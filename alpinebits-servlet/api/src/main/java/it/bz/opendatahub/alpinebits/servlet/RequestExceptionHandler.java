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

import java.io.IOException;

/**
 * Handle an {@link Exception} that occurred during a request.
 */
public interface RequestExceptionHandler {

    /**
     * Handle the given {@link Exception} that occured during the reqeuest.
     *
     * @param request  the exception occurred during this {@link HttpServletRequest}
     * @param response the {@link HttpServletResponse} can be used to inform the client about the exception
     * @param e        the exception, that was thrown
     */
    void handleRequestException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException;

}
