/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType.Awards;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AffiliationInfoType.Awards.Award;

import java.util.List;

/**
 * Use this validator to validate AffiliationInfo in AlpineBits 2022 Inventory documents.
 *
 * @see AffiliationInfoType
 */
public class AffiliationInfoValidator implements Validator<AffiliationInfoType, InventoryContext> {

    public static final String ELEMENT_NAME = Names.AFFILIATION_INFO;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    @Override
    public void validate(AffiliationInfoType affiliationInfo, InventoryContext ctx, ValidationPath path) {
        // Although AffiliationInfo is optional, a null-check is performed.
        // It turned out, that it is better that the caller decides if AffiliationInfo
        // validation needs to be invoked. This makes the code more reusable
        // e.g. when required/optional elements change with different AlpineBits versions
        VALIDATOR.expectNotNull(affiliationInfo, ErrorMessage.EXPECT_AFFILIATION_INFO_TO_NOT_BE_NULL, path);

        // Awards element is expected to exist
        Awards awards = affiliationInfo.getAwards();
        VALIDATOR.expectNotNull(awards, ErrorMessage.EXPECT_AWARDS_TO_NOT_BE_NULL, path.withElement(Names.AWARDS));

        // List of Award elements must not be null
        List<Award> awardList = awards.getAwards();
        VALIDATOR.expectNonEmptyCollection(
                awardList,
                ErrorMessage.EXPECT_AWARD_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.AWARDS).withElement(Names.AWARD_LIST)
        );

        for (int i = 0; i < awardList.size(); i++) {
            Award award = awardList.get(i);
            ValidationPath indexedPath = path.withElement(Names.AWARDS).withElement(Names.AWARD).withIndex(i);

            VALIDATOR.expectNotNull(award.getRating(), ErrorMessage.EXPECT_RATING_TO_BE_NOT_NULL, indexedPath.withAttribute(Names.RATING));
            VALIDATOR.expectNotNull(award.getProvider(), ErrorMessage.EXPECT_PROVIDER_TO_BE_NOT_NULL, indexedPath.withAttribute(Names.PROVIDER));

            // Check that the provider value is all uppercase
            String upperCaseProvider = award.getProvider().toUpperCase();
            if (!upperCaseProvider.equals(award.getProvider())) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_PROVIDER_TO_BE_ALL_UPPERCASE, indexedPath.withAttribute(Names.PROVIDER));
            }
        }
    }
}
