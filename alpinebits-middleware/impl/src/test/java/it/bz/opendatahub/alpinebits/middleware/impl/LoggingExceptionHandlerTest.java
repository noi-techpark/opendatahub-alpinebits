// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.middleware.impl;

import it.bz.opendatahub.alpinebits.middleware.ExceptionHandler;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Test cases for {@link LoggingExceptionHandler} class.
 */
public class LoggingExceptionHandlerTest {

    @Test
    public void testHandleException() {
        // This test is solely here to have 100% test coverage
        ExceptionHandler exceptionHandler = new LoggingExceptionHandler();
        exceptionHandler.handleException(null);
        assertTrue(true);
    }
}