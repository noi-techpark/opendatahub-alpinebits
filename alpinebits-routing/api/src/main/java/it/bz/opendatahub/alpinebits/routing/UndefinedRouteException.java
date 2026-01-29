// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing;

import it.bz.opendatahub.alpinebits.common.exception.AlpineBitsException;

/**
 * This exception is thrown if no route could be found for an
 * AlpineBits request.
 */
public class UndefinedRouteException extends AlpineBitsException {

    public static final int STATUS = 400;

    /**
     * Constructs a {@code UndefinedActionException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public UndefinedRouteException(String msg) {
        super(msg, STATUS, msg);
    }

}
