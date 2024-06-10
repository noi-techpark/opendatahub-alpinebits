// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType;

import java.util.List;

/**
 * Use this validator to check if closing-seasons don't overlap
 * for AlpineBits 2022-10 FreeRooms documents.
 *
 * @see BaseInvCountType
 */
public class ClosingSeasonsOverlapValidator implements Validator<List<BaseInvCountType>, Void> {

    public static final String ELEMENT_NAME = Names.STATUS_APPLICATION_CONTROL;

    private static final Validator<List<BaseInvCountType>, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.freerooms.ClosingSeasonsOverlapValidator();

    @Override
    public void validate(List<BaseInvCountType> inventories, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(inventories, ctx, path);
    }

}
