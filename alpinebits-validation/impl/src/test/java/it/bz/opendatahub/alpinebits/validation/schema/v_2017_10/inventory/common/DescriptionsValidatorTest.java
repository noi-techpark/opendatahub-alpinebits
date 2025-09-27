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
import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link DescriptionsValidator}.
 */
public class DescriptionsValidatorTest {

    private static final DescriptionsValidator VALIDATOR = new DescriptionsValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.DESCRIPTION_LIST);

    @Test
    public void testValidate_ShouldThrow_WhenDescriptionsIsNull() {
        validateAndAssert(VALIDATOR, null, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_DESCRIPTIONS_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenDescriptionsIsEmpty() {
        List<Description> descriptions = new ArrayList<>();

        validateAndAssert(VALIDATOR, descriptions, VALIDATION_PATH, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_DESCRIPTION_TO_EXIST);
    }

    @Test
    public void testValidate_ShouldThrow_WhenDuplicateLanguageAndTextFormat() {
        String textFormat = Names.PLAIN_TEXT;
        String language = Iso6391.ABKHAZIAN.getCode();

        List<Description> descriptions = new ArrayList<>();
        descriptions.add(new Description(null, textFormat, language));
        descriptions.add(new Description(null, textFormat, language));

        String errorMessage = String.format(
                ErrorMessage.EXPECT_NO_DUPLICATE_LANGUAGE_AND_TEXT_FORMAT,
                language,
                textFormat
        );
        validateAndAssert(VALIDATOR, descriptions, VALIDATION_PATH, ValidationException.class, errorMessage);
    }

    @Test
    public void testValidate_ShouldThrow_WhenHTMLTextFormatOnly() {
        String language = Iso6391.ABKHAZIAN.getCode();
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(new Description(null, Names.HTML, language));

        String errorMessage = String.format(ErrorMessage.EXPECT_PLAIN_TEXT_TO_EXIST, language);
        validateAndAssert(VALIDATOR, descriptions, VALIDATION_PATH, ValidationException.class, errorMessage);
    }

}