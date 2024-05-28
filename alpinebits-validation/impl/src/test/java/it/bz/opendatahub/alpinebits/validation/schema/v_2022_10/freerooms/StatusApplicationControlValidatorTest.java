// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.schema.common.freerooms.AbstractStatusApplicationControlValidatorTest;
import it.bz.opendatahub.alpinebits.xml.schema.ota.StatusApplicationControlType;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link StatusApplicationControlValidator}.
 */
public class StatusApplicationControlValidatorTest extends AbstractStatusApplicationControlValidatorTest {

    protected void validateAndAssert(
            StatusApplicationControlType data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        StatusApplicationControlValidator validator = new StatusApplicationControlValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }

    protected StatusApplicationControlType buildValidStatusApplicationControl() {
        StatusApplicationControlType statusApplicationControl = new StatusApplicationControlType();
        statusApplicationControl.setStart(STATUS_APPLICATION_CONTROL_START);
        statusApplicationControl.setEnd(STATUS_APPLICATION_CONTROL_END);
        statusApplicationControl.setInvTypeCode(STATUS_APPLICATION_CONTROL_INV_TYPE_CODE);
        return statusApplicationControl;
    }
}