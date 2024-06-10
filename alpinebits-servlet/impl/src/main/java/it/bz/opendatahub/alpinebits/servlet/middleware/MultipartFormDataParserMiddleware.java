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
import it.bz.opendatahub.alpinebits.servlet.InvalidRequestContentTypeException;
import it.bz.opendatahub.alpinebits.servlet.MultipartFormDataParseException;
import it.bz.opendatahub.alpinebits.servlet.ServletContextKey;
import it.bz.opendatahub.alpinebits.servlet.UndefinedActionException;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.javax.JavaxServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This middleware extracts the <code>action</code> and <code>request</code> parts from the
 * HTTP request and adds them to the {@link Context}.
 * <p>
 * The <code>action</code> part is added as <code>String</code> using
 * {@link RequestContextKey#REQUEST_ACTION} key.
 * <p>
 * The <code>request</code> part is added as {@link OutputStream} using the
 * {@link RequestContextKey#REQUEST_CONTENT_STREAM} key.
 * <p>
 * The HTTP request must be present in the {@link Context}. Otherwise, a
 * {@link RequiredContextKeyMissingException} is thrown.
 * <p>
 * If the requests content-type is not <code>multipart/form-data</code>, an
 * {@link InvalidRequestContentTypeException} is thrown.
 * <p>
 * If the multipart/form-data could not be parsed, a {@link MultipartFormDataParseException} is thrown.
 * <p>
 * If non <code>action</code> part was found in the request, a {@link UndefinedActionException} is thrown.
 */
public class MultipartFormDataParserMiddleware implements Middleware {

    private static final Logger LOG = LoggerFactory.getLogger(MultipartFormDataParserMiddleware.class);

    private static final String FORM_PART_ACTION = "action";
    private static final String FORM_PART_REQUEST = "request";

    @Override
    public void handleContext(Context ctx, MiddlewareChain chain) {
        HttpServletRequest request = ctx.getOrThrow(ServletContextKey.SERVLET_REQUEST);

        this.checkIsMultipartOrThrow(request);

        this.parseRequestAndAddToContext(request, ctx);

        chain.next();
    }

    private void checkIsMultipartOrThrow(HttpServletRequest request) {
        boolean isMultipart = JavaxServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            String contentType = request.getContentType();
            throw new InvalidRequestContentTypeException("Expecting content-type: multipart/form-data. Got: " + contentType);
        }
    }

    private void parseRequestAndAddToContext(HttpServletRequest request, Context ctx) {
        LOG.debug("Parsing multipart/form-data");

        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        JavaxServletFileUpload<DiskFileItem, DiskFileItemFactory> upload = new JavaxServletFileUpload<>(factory);

        String abAction = null;
        InputStream abRequest = null;

        List<DiskFileItem> items = null;

        try {
            // Parse the request into FileItem objects
            items = upload.parseRequest(request);

            for (FileItem<DiskFileItem> item : items) {
                if (FORM_PART_ACTION.equalsIgnoreCase(item.getFieldName())) {
                    abAction = item.getString();
                }
                if (FORM_PART_REQUEST.equalsIgnoreCase(item.getFieldName())) {
                    abRequest = IOUtils.toBufferedInputStream(item.getInputStream());
                }
            }

            if (abAction == null) {
                List<String> partNames = items.stream().map(DiskFileItem::getName).collect(Collectors.toList());
                String formPartsInfo = partNames.isEmpty()
                        ? "No multipart/form-data parts found at all"
                        : "The following multipart/form-data parts were found: " + String.join(", ");
                throw new UndefinedActionException("No action part defined in the multipart/form-data request. " + formPartsInfo);
            }

            ctx.put(RequestContextKey.REQUEST_ACTION, abAction);

            if (abRequest != null) {
                ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, abRequest);
            }
        } catch (IOException e) {
            throw new MultipartFormDataParseException("Error while parsing multipart/form-data", e);
        }

        LOG.debug("AlpineBits action parameter: {}, AlpineBits request parameter is present: {}", abAction, abRequest != null);
    }

}
