// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.schema.common.inventory.AbstractTypeRoomValidatorTest;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms.GuestRoom.TypeRoom;
import org.testng.annotations.Test;

import java.math.BigInteger;

import static org.testng.Assert.*;

/**
 * Tests for {@link TypeRoomValidator}.
 */
public class TypeRoomValidatorTest extends AbstractTypeRoomValidatorTest {

    @Test
    public void testValidate_GivenRoomType_ShouldThrow_WhenRoomTypeIsUnsupported() {
        TypeRoom typeRoom = buildTypeRoom("1", "unsupported value");

        String message = String.format(ErrorMessage.EXPECT_ROOM_TYPE_TO_BE_KNOWN, typeRoom.getRoomType());
        this.validateAndAssert(typeRoom, true, ValidationException.class, message);
    }

    @Test
    public void testValidate_GivenRoomType_ShouldThrow_WhenRoomTypeAndRoomClassificationCodeMismatch() {
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("1", "42");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("2", "13");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("3", "13");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("4", "13");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("5", "13");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("6", "5");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("7", "5");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("8", "5");
        validateAndAssertRoomClassificationCodeAndRoomTypeMismatch("9", "42");
    }

    @Test()
    public void testValidate_ShouldAcceptEmptyRoomType() {
        TypeRoom typeRoom = buildTypeRoom("1", null);
        TypeRoomValidator validator = new TypeRoomValidator();
        validator.validate(typeRoom, true, VALIDATION_PATH);

        // Just an assertion that no exception was thrown. The test for notNull makes little sense otherwise.
        assertNotNull(typeRoom);
    }

    private void validateAndAssertRoomClassificationCodeAndRoomTypeMismatch(String roomType, String expectedRoomClassificationCode) {
        TypeRoom typeRoom = buildTypeRoom("1", roomType);
        String message = String.format(
                ErrorMessage.EXPECT_ROOM_TYPE_TO_HAVE_CORRECT_ROOM_CLASSIFICATION_CODE,
                typeRoom.getRoomType(),
                expectedRoomClassificationCode,
                typeRoom.getRoomClassificationCode()
        );
        this.validateAndAssert(typeRoom, true, ValidationException.class, message);
    }

    protected void validateAndAssert(
            TypeRoom data,
            Boolean ctx,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        TypeRoomValidator validator = new TypeRoomValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, ctx, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }

    private TypeRoom buildTypeRoom(String roomClassificationCode, String roomType) {
        TypeRoom typeRoom = new TypeRoom();
        typeRoom.setRoomID(DEFAULT_ROOM_ID);
        typeRoom.setStandardOccupancy(BigInteger.ONE);
        typeRoom.setRoomClassificationCode(roomClassificationCode);
        typeRoom.setRoomType(roomType);
        return typeRoom;
    }
}