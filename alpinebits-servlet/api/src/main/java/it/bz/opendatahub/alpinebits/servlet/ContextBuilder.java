// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.servlet;

import it.bz.opendatahub.alpinebits.middleware.Context;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This interface defined methods to build a {@link Context}.
 */
public interface ContextBuilder {

    /**
     * Create a {@link Context} from the given {@link HttpServletRequest}, {@link HttpServletResponse}
     * and <code>requestId</code>.
     *
     * @param request   the {@link HttpServletRequest} can be e.g. stored in the created context
     * @param response  the {@link HttpServletResponse} can be e.g. stored in the created context
     * @param requestId the requestId  can be e.g. stored in the created context
     * @return the {@link Context} build using the parameters
     */
    Context fromRequest(HttpServletRequest request, HttpServletResponse response, String requestId);

}
