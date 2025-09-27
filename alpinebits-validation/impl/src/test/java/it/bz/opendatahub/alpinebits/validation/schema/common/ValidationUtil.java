/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.common;

import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Utility class for validation tests.
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Utility class
    }

    /**
     * Validate and assert that the expected exception is thrown with the expected message.
     *
     * @param validator      the validator to use.
     * @param data           the data to validate.
     * @param ctx            the context to use.
     * @param path           the validation path.
     * @param exceptionClass the expected exception class.
     * @param errorMessage   the expected error message.
     * @param <D>            the data type.
     * @param <C>            the context type.
     * @param <V>            the validator type.
     */
    public static <D, C, V extends Validator<D, C>> void validateAndAssert(
            V validator,
            D data,
            C ctx,
            ValidationPath path,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        // CHECKSTYLE:OFF
        Exception e = expectThrows(exceptionClass, () -> validator.validate(data, ctx, path));
        // CHECKSTYLE:ON

        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }

    /**
     * Validate and assert that the expected exception is thrown with the expected message.
     *
     * @param validator      the validator to use.
     * @param data           the data to validate.
     * @param path           the validation path.
     * @param exceptionClass the expected exception class.
     * @param errorMessage   the expected error message.
     * @param <D>            the data type.
     * @param <V>            the validator type.
     */
    public static <D, V extends Validator<D, Void>> void validateAndAssert(
            V validator,
            D data,
            ValidationPath path,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        validateAndAssert(validator, data, null, path, exceptionClass, errorMessage);
    }
}
