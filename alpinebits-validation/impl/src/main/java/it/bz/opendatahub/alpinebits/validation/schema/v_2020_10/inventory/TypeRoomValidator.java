// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms.GuestRoom.TypeRoom;

/**
 * Use this validator to validate the TextItem in AlpineBits 2020
 * Inventory documents.
 *
 * @see TypeRoom
 */
public class TypeRoomValidator implements Validator<TypeRoom, Boolean> {

    public static final String ELEMENT_NAME = Names.TYPE_ROOM;

    private static final Validator<TypeRoom, Boolean> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.TypeRoomValidator();

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    @Override
    public void validate(TypeRoom typeRoom, Boolean isHeadingGuestRoom, ValidationPath path) {
        // Delegate validation to AlpineBits 2017 implementation,
        // since the validation remains the same
        VALIDATION_DELEGATE.validate(typeRoom, isHeadingGuestRoom, path);

        this.validateRoomType(typeRoom, path.withAttribute(Names.ROOM_TYPE));
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    private void validateRoomType(TypeRoom typeRoom, ValidationPath path) {
        // RoomType is optional and can be accessed without additional null-check of TypeRoom,
        // because TypeRoom is mandatory and its null-check already occurred.
        if (typeRoom.getRoomType() == null) {
            return;
        }

        switch (typeRoom.getRoomType()) {
            case "1":
            case "9":
                this.throwOnRoomTypeMismatch(typeRoom, "42", path);
                break;
            case "2":
            case "3":
            case "4":
            case "5":
                this.throwOnRoomTypeMismatch(typeRoom, "13", path);
                break;
            case "6":
            case "7":
            case "8":
                this.throwOnRoomTypeMismatch(typeRoom, "5", path);
                break;
            default:
                String message = String.format(ErrorMessage.EXPECT_ROOM_TYPE_TO_BE_KNOWN, typeRoom.getRoomType());
                VALIDATOR.throwValidationException(message, path);
        }
    }

    private void throwOnRoomTypeMismatch(TypeRoom typeRoom, String expectedRoomClassificationCode, ValidationPath path) {
        if (!expectedRoomClassificationCode.equals(typeRoom.getRoomClassificationCode())) {
            String message = String.format(
                    ErrorMessage.EXPECT_ROOM_TYPE_TO_HAVE_CORRECT_ROOM_CLASSIFICATION_CODE,
                    typeRoom.getRoomType(),
                    expectedRoomClassificationCode,
                    typeRoom.getRoomClassificationCode()
            );
            VALIDATOR.throwValidationException(message, path);
        }
    }

}
