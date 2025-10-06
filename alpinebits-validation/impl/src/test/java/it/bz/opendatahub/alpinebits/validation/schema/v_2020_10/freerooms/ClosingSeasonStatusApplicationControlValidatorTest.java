// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NotEqualValidationException;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.StatusApplicationControlType;
import org.testng.annotations.Test;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link ClosingSeasonStatusApplicationControlValidator}.
 */
public class ClosingSeasonStatusApplicationControlValidatorTest {

    private static final ClosingSeasonStatusApplicationControlValidator VALIDATOR = new ClosingSeasonStatusApplicationControlValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.STATUS_APPLICATION_CONTROL);

    private static final String STATUS_APPLICATION_CONTROL_START = "2017-10-01";
    private static final String STATUS_APPLICATION_CONTROL_END = "2017-10-31";
    private static final String STATUS_APPLICATION_CONTROL_INV_TYPE_CODE = "DEFAULT_INV_TYPE_CODE";

    @Test
    public void testValidate_ShouldThrow_WhenStatusApplicationCodeIsNull() {
        validateAndAssert(
                VALIDATOR,
                null,
                VALIDATION_PATH,
                NullValidationException.class,
                ErrorMessage.EXPECT_STATUS_APPLICATION_CONTROL_TO_BE_NOT_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenStartIsNull() {
        StatusApplicationControlType statusApplicationControl = this.buildValidStatusApplicationControl();
        statusApplicationControl.setStart(null);

        validateAndAssert(
                VALIDATOR,
                statusApplicationControl,
                VALIDATION_PATH,
                NullValidationException.class,
                ErrorMessage.EXPECT_START_TO_BE_NOT_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenEndIsNull() {
        StatusApplicationControlType statusApplicationControl = this.buildValidStatusApplicationControl();
        statusApplicationControl.setEnd(null);

        validateAndAssert(
                VALIDATOR,
                statusApplicationControl,
                VALIDATION_PATH,
                NullValidationException.class,
                ErrorMessage.EXPECT_END_TO_BE_NOT_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenAllInvCodeNotEqualsTrue() {
        StatusApplicationControlType statusApplicationControl = this.buildValidStatusApplicationControl();
        statusApplicationControl.setAllInvCode(false);

        validateAndAssert(
                VALIDATOR,
                statusApplicationControl,
                VALIDATION_PATH,
                NotEqualValidationException.class,
                ErrorMessage.EXPECT_CLOSING_SEASON_TO_HAVE_ALL_INV_CODE_SET_TO_TRUE
        );
    }

    private StatusApplicationControlType buildValidStatusApplicationControl() {
        StatusApplicationControlType statusApplicationControl = new StatusApplicationControlType();
        statusApplicationControl.setStart(STATUS_APPLICATION_CONTROL_START);
        statusApplicationControl.setEnd(STATUS_APPLICATION_CONTROL_END);
        statusApplicationControl.setInvTypeCode(STATUS_APPLICATION_CONTROL_INV_TYPE_CODE);
        return statusApplicationControl;
    }

}