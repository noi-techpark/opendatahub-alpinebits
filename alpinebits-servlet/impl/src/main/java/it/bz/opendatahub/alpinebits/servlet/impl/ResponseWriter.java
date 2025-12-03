// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl;

import it.bz.opendatahub.alpinebits.common.constants.HttpContentTypeHeaderValues;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The methods provided by this class can be used to write
 * results to the servlet response and its output stream.
 */
public final class ResponseWriter {

    public static final String RESPONSE_CONTENT_TYPE_HEADER = "Content-Type";

    private ResponseWriter() {
        // Empty
    }

    public static void writeMessage(HttpServletResponse response, String message) throws IOException {
        response.getOutputStream().print(message);
    }

    public static void writeError(HttpServletResponse response, int httpStatus, String requestId, String message) throws IOException {
        String errorMessage = ResponseWriter.buildErrorMessage(message, requestId);
        ResponseWriter.writeMessage(response, errorMessage);
        response.setStatus(httpStatus);
        response.setHeader(RESPONSE_CONTENT_TYPE_HEADER, HttpContentTypeHeaderValues.TEXT_PLAIN);
    }

    public static String buildErrorMessage(String message, String requestId) {
        return "ERROR:" + message + " [rid=" + requestId + "]";
    }

}
