// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet.impl.utils;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class provides helper methods to build multipart/form-data requests, used in tests.
 */
public class MultipartFormDataRequestBuilder {

    public static final String CRLF = "\r\n";
    public static final String INITIAL_BOUNDARY = "----------------------------9d7042ecb251";
    public static final String BOUNDARY = "--" + INITIAL_BOUNDARY;
    public static final String CONTENT_TYPE = "multipart/form-data; boundary=" + INITIAL_BOUNDARY;
    public static final String REQUEST_TYPE = "POST";
    public static final int RESPONSE_STATUS = 200;

    public static final String ALPINEBITS_ACTION_PARAM = "OTA_HotelAvailNotif:FreeRooms";
    public static final String ALPINEBITS_REQUEST_PARAM =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<OTA_HotelAvailNotifRQ>\n" +
                    "[...äöüß...]\n" +
                    "</OTA_HotelAvailNotifRQ>";

    /**
     * Build a multipart/form-data part with an <code>action</code> element.
     * <p>
     * This is not a complete valid multipart/form-data body. See
     * {@link MultipartFormDataRequestBuilder#buildMultiPartActionOnly} for a
     * complete multipart/form-data body containing the <code>action</code> part.
     *
     * @return a multipart/form-data part with an <code>action</code> element
     */
    public static String buildActionPart() {
        return BOUNDARY + CRLF +
                "Content-Disposition: form-data; name=\"action\"" + CRLF + CRLF +
                ALPINEBITS_ACTION_PARAM + CRLF;
    }

    /**
     * Build a multipart/form-data part with a <code>request</code> element.
     * <p>
     * This is not a complete valid multipart/form-data body. See
     * {@link MultipartFormDataRequestBuilder#buildMultiPartRequestOnly()} for a
     * complete multipart/form-data body containing the <code>request</code> part.
     *
     * @return a multipart/form-data part with a <code>request</code> element
     */
    public static String buildRequestPart() {
        return BOUNDARY + CRLF +
                "Content-Disposition: form-data; name=\"request\"" + CRLF + CRLF +
                ALPINEBITS_REQUEST_PARAM + CRLF;
    }

    /**
     * Build a valid multipart/form-data body containing a single <code>action</code> part.
     *
     * @return a valid multipart/form-data body containing a single <code>action</code> part
     */
    public static String buildMultiPartActionOnly() {
        return buildActionPart() + BOUNDARY + "--";
    }

    /**
     * Build a valid multipart/form-data body containing a single <code>request</code> part.
     *
     * @return a valid multipart/form-data body containing a single <code>request</code> part
     */
    public static String buildMultiPartRequestOnly() {
        return buildRequestPart() + BOUNDARY + "--";
    }

    /**
     * Build a valid multipart/form-data body containing an <code>action</code> and <code>request</code> part.
     *
     * @return a valid multipart/form-data body containing an <code>action</code> and <code>request</code> part
     */
    public static String buildMultiPartActionAndRequest() {
        return buildActionPart() + buildRequestPart() + BOUNDARY + "--";
    }

    /**
     * Convert a <code>String</code> to a {@link ServletInputStream}.
     *
     * @param s convert this <code>String</code> to a {@link ServletInputStream}
     * @return a {@link ServletInputStream} that returns the given <code>String</code> when read from
     */
    public static ServletInputStream toServletInputStream(String s) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        return new ServletInputStream() {

            private int lastIndexRetrieved = -1;
            private ReadListener readListener;

            @Override
            public boolean isFinished() {
                return lastIndexRetrieved == bytes.length - 1;
            }

            @Override
            public boolean isReady() {
                // This implementation will never block
                // We also never need to call the readListener from this method, as this method will never return false
                return isFinished();
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                this.readListener = readListener;
                if (!isFinished()) {
                    try {
                        readListener.onDataAvailable();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                } else {
                    try {
                        readListener.onAllDataRead();
                    } catch (IOException e) {
                        readListener.onError(e);
                    }
                }
            }

            @Override
            public int read() throws IOException {
                int i;
                if (!isFinished()) {
                    i = bytes[lastIndexRetrieved + 1];
                    lastIndexRetrieved++;
                    if (isFinished() && readListener != null) {
                        try {
                            readListener.onAllDataRead();
                        } catch (IOException ex) {
                            readListener.onError(ex);
                            throw ex;
                        }
                    }
                    return i;
                } else {
                    return -1;
                }
            }
        };
    }

    /**
     * Build a mock for a {@link HttpServletRequest} that can be used for multipart/form-data tests.
     *
     * @param s this <code>String</code>, wrapped inside a {@link ServletInputStream}, is returned
     *          when {@link HttpServletRequest#getInputStream()} is called
     * @return a {@link HttpServletRequest} that returns the given <code>String</code> as stream
     * when {@link HttpServletRequest#getInputStream()} is called
     * @throws IOException if a stream operation fails
     */
    public static HttpServletRequest buildRequest(String s) throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContentType()).thenReturn(MultipartFormDataRequestBuilder.CONTENT_TYPE);
        when(request.getMethod()).thenReturn(REQUEST_TYPE);
        when(request.getContentLength()).thenReturn(RESPONSE_STATUS);

        ServletInputStream servletInputStream = MultipartFormDataRequestBuilder.toServletInputStream(s);
        when(request.getInputStream()).thenReturn(servletInputStream);
        return request;
    }

}
