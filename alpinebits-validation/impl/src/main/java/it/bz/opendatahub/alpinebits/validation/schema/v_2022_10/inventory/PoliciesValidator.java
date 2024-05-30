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
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelDescriptiveContentType.Policies;

/**
 * Use this validator to validate Policies in AlpineBits 2022 Inventory documents.
 *
 * @see Policies
 */
public class PoliciesValidator implements Validator<Policies, InventoryContext> {

    public static final String ELEMENT_NAME = Names.POLICIES;

    private static final Validator<Policies, InventoryContext> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.PoliciesValidator();

    @Override
    public void validate(Policies policies, InventoryContext ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(policies, ctx, path);
    }

}
