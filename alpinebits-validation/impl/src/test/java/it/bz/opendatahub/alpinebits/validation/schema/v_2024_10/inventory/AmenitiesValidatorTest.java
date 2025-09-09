// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCodeRoomAmenityType;
import it.bz.opendatahub.alpinebits.common.constants.OTACodeRoomAmenityType;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link AmenitiesValidator}.
 */
public class AmenitiesValidatorTest {

    protected static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.AMENITIES);

    @Test
    public void testValidate_ShouldThrow_WhenRoomAmenityCodeIsNull() {
        FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity amenity = new FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity();

        FacilityInfoType.GuestRooms.GuestRoom.Amenities amenities = new FacilityInfoType.GuestRooms.GuestRoom.Amenities();
        amenities.getAmenities().add(amenity);

        this.validateAndAssert(amenities, NullValidationException.class, ErrorMessage.EXPECT_ROOM_AMENITY_CODE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenRoomAmenityCodeIsInvalid() {
        // Invalid OTA Room Amenity code
        String code = OTACodeRoomAmenityType.ADJOINING_ROOMS.getCode() + ".INVALID";

        FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity amenity = new FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity();
        amenity.setRoomAmenityCode(code);

        FacilityInfoType.GuestRooms.GuestRoom.Amenities amenities = new FacilityInfoType.GuestRooms.GuestRoom.Amenities();
        amenities.getAmenities().add(amenity);

        String errorMessage = String.format(ErrorMessage.EXPECT_ROOM_AMENITY_CODE_TO_BE_DEFINED, code);
        this.validateAndAssert(amenities, ValidationException.class, errorMessage);
    }

    @Test
    public void testValidate_ShouldPass_WhenRoomAmenityCodeIsValid() {
        String code = OTACodeRoomAmenityType.ADJOINING_ROOMS.getCode();

        FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity amenity = new FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity();
        amenity.setRoomAmenityCode(code);

        FacilityInfoType.GuestRooms.GuestRoom.Amenities amenities = new FacilityInfoType.GuestRooms.GuestRoom.Amenities();
        amenities.getAmenities().add(amenity);

        AmenitiesValidator validator = new AmenitiesValidator();
        validator.validate(amenities, null, VALIDATION_PATH);
    }

    @Test
    public void testValidate_ShouldPass_WhenAlpineBitsRoomAmenityCodeIsValid() {
        // Valid AlpineBits Room Amenity code, which is new in 2024_10
        String code = AlpineBitsCodeRoomAmenityType._2_OR_MORE_BATHROOMS.getCode();

        FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity amenity = new FacilityInfoType.GuestRooms.GuestRoom.Amenities.Amenity();
        amenity.setRoomAmenityCode(code);

        FacilityInfoType.GuestRooms.GuestRoom.Amenities amenities = new FacilityInfoType.GuestRooms.GuestRoom.Amenities();
        amenities.getAmenities().add(amenity);

        AmenitiesValidator validator = new AmenitiesValidator();
        validator.validate(amenities, null, VALIDATION_PATH);
    }

    private void validateAndAssert(
            FacilityInfoType.GuestRooms.GuestRoom.Amenities data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        AmenitiesValidator validator = new AmenitiesValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }
}