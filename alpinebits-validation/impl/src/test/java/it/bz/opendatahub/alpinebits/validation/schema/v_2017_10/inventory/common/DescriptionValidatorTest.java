// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common;

import it.bz.opendatahub.alpinebits.common.constants.Iso6391;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import org.testng.annotations.Test;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link DescriptionValidator}.
 */
public class DescriptionValidatorTest {

    private static final DescriptionValidator VALIDATOR = new DescriptionValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.DESCRIPTION);

    private static final String INVALID_LANGUAGE = "some language";
    private static final String INVALID_TEXT_FORMAT = "some textFormat";

    @Test
    public void testValidate_ShouldThrow_WhenDescriptionIsNull() {
        validateAndAssert(VALIDATOR, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_DESCRIPTION_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenLanguageIsNull() {
        Description description = new Description(null, null, null);

        validateAndAssert(VALIDATOR, description, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextFormatIsNull() {
        Description description = new Description(null, null, Iso6391.ABKHAZIAN.getCode());

        validateAndAssert(VALIDATOR, description, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_TEXT_FORMAT_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextFormatIsUnknown() {
        Description description = new Description(null, INVALID_TEXT_FORMAT, Iso6391.ABKHAZIAN.getCode());


        String errorMessage = String.format(
                ErrorMessage.EXPECT_TEXT_FORMAT_TO_BE_PLAINTEXT_OR_HTML,
                INVALID_TEXT_FORMAT
        );
        validateAndAssert(VALIDATOR, description, VALIDATION_PATH, ValidationException.class, errorMessage);
    }

    @Test
    public void testValidate_ShouldThrow_WhenLanguageIsNotISO6391() {
        Description description = new Description(null, Names.PLAIN_TEXT, INVALID_LANGUAGE);

        String errorMessage = String.format(ErrorMessage.EXPECT_LANGUAGE_ISO639_1_CODE_TO_BE_DEFINED, INVALID_LANGUAGE);
        validateAndAssert(VALIDATOR, description, VALIDATION_PATH, ValidationException.class, errorMessage);
    }

}