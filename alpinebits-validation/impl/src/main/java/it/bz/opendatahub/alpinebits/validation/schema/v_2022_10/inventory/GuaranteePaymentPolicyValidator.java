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
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.GuaranteePaymentPolicy;

/**
 * Use this validator to validate GuaranteePaymentPolicy in AlpineBits 2022 Inventory documents.
 *
 * @see GuaranteePaymentPolicy
 */
public class GuaranteePaymentPolicyValidator implements Validator<GuaranteePaymentPolicy, Void> {

    public static final String ELEMENT_NAME = Names.POLICIES;

    private static final Validator<GuaranteePaymentPolicy, Void> VALIDATION_DELEGATE =
            new it.bz.opendatahub.alpinebits.validation.schema.v_2020_10.inventory.GuaranteePaymentPolicyValidator();

    @Override
    public void validate(GuaranteePaymentPolicy guaranteePaymentPolicy, Void ctx, ValidationPath path) {
        // Delegate validation to AlpineBits 2020 implementation,
        // since the validation remains the same

        VALIDATION_DELEGATE.validate(guaranteePaymentPolicy, ctx, path);
    }

}
