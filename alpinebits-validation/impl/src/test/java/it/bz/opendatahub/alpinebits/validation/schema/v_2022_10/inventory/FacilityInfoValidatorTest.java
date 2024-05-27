/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.schema.common.inventory.AbstractFacilityInfoValidatorTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link FacilityInfoValidator}.
 */
public class FacilityInfoValidatorTest extends AbstractFacilityInfoValidatorTest {

    @Override
    protected void validateAndAssert(Class<? extends ValidationException> exceptionClass, String errorMessage) {
        FacilityInfoValidator validator = new FacilityInfoValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                NullValidationException.class,
                () -> validator.validate(null, null, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        String message = ErrorMessage.EXPECT_FACILITY_INFO_TO_BE_NOT_NULL;
        assertEquals(e.getMessage().substring(0, message.length()), message);
    }

}