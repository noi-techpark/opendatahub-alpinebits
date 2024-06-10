// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.validation.schema.common.inventory.AbstractGuestRoomBasicHeadingValidatorTest;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms.GuestRoom;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for heading {@link GuestRoom} validator.
 */
public class GuestRoomBasicHeadingValidatorTest extends AbstractGuestRoomBasicHeadingValidatorTest {

    protected void validateAndAssert(
            GuestRoom data,
            InventoryContext ctx,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        GuestRoomBasicHeadingValidator validator = new GuestRoomBasicHeadingValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, ctx, VALIDATION_PATH)
        );
        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }

}