// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType.GuestRooms.GuestRoom.TypeRoom;

/**
 * Use this validator to validate the TextItem in AlpineBits 2022
 * Inventory documents.
 *
 * @see TypeRoom
 */
public class TypeRoomValidator implements Validator<TypeRoom, Boolean> {

    public static final String ELEMENT_NAME = Names.TYPE_ROOM;

    private static final Validator<TypeRoom, Boolean> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.TypeRoomValidator();

    @Override
    public void validate(TypeRoom typeRoom, Boolean isHeadingGuestRoom, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(typeRoom, isHeadingGuestRoom, path);
    }

}
