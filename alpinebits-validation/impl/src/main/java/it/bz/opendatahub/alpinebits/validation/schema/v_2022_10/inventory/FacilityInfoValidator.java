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
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FacilityInfoType;

/**
 * Use this validator to validate the FacilityInfoType in AlpineBits 2022
 * Inventory documents.
 *
 * @see FacilityInfoType
 */
public class FacilityInfoValidator implements Validator<FacilityInfoType, InventoryContext> {

    public static final String ELEMENT_NAME = Names.FACILITY_INFO;

    private static final Validator<FacilityInfoType, InventoryContext> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.FacilityInfoValidator();

    @Override
    public void validate(FacilityInfoType facilityInfo, InventoryContext ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(facilityInfo, ctx, path);
    }
}
