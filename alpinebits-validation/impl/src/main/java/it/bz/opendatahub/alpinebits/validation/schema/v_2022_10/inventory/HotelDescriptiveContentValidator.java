// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.FacilityInfoValidator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRQ.HotelDescriptiveContents.HotelDescriptiveContent;

/**
 * Use this validator to validate the HotelDescriptiveContent in AlpineBits 2022
 * Inventory documents.
 *
 * @see HotelDescriptiveContent
 */
public class HotelDescriptiveContentValidator implements Validator<HotelDescriptiveContent, InventoryContext> {

    public static final String ELEMENT_NAME = Names.HOTEL_DESCRIPTIVE_CONTENT;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private final FacilityInfoValidator facilityInfoValidator
            = new FacilityInfoValidator();

    private final HotelInfoValidator hotelInfoValidator = new HotelInfoValidator();
    private final PoliciesValidator policiesValidator = new PoliciesValidator();
    private final AffiliationInfoValidator affiliationInfoValidator = new AffiliationInfoValidator();
    private final ContactInfosValidator contactInfosValidator = new ContactInfosValidator();

    @Override
    public void validate(HotelDescriptiveContent hotelDescriptiveContent, InventoryContext ctx, ValidationPath path) {
        VALIDATOR.expectNotNull(hotelDescriptiveContent, ErrorMessage.EXPECT_HOTEL_DESCRIPTIVE_CONTENT_TO_BE_NOT_NULL, path);
        VALIDATOR.expectNotNull(ctx, ErrorMessage.EXPECT_CONTEXT_TO_BE_NOT_NULL);

        VALIDATOR.expectHotelCodeAndNameNotBothNull(
                hotelDescriptiveContent.getHotelCode(),
                hotelDescriptiveContent.getHotelName(),
                ErrorMessage.EXPECT_HOTEL_CODE_AND_HOTEL_NAME_TO_BE_NOT_BOTH_NULL,
                path.withAttribute(String.format("%s/%s", Names.HOTEL_CODE, Names.HOTEL_NAME))
        );

        if (isInventoryBasicAction(ctx)) {
            // In Inventory/Basic, the following elements must not be present
            // - HotelInfo
            // - Policies
            // - AffilitationInfo
            // - ContactInfos

            VALIDATOR.expectNull(
                    hotelDescriptiveContent.getHotelInfo(),
                    ErrorMessage.EXPECT_HOTEL_INFO_TO_BE_NULL,
                    path.withElement(Names.HOTEL_INFO)
            );
            VALIDATOR.expectNull(
                    hotelDescriptiveContent.getPolicies(),
                    ErrorMessage.EXPECT_POLICIES_TO_BE_NULL,
                    path.withElement(Names.POLICIES)
            );
            VALIDATOR.expectNull(
                    hotelDescriptiveContent.getAffiliationInfo(),
                    ErrorMessage.EXPECT_AFFILIATION_INFO_TO_BE_NULL,
                    path.withElement(Names.AFFILIATION_INFO)
            );
            VALIDATOR.expectNull(
                    hotelDescriptiveContent.getContactInfos(),
                    ErrorMessage.EXPECT_CONTACT_INFOS_TO_BE_NULL,
                    path.withElement(Names.POLICIES)
            );

            // Validate FacilityInfo
            this.facilityInfoValidator.validate(
                    hotelDescriptiveContent.getFacilityInfo(),
                    ctx,
                    path.withElement(FacilityInfoValidator.ELEMENT_NAME)
            );
        } else {
            // In Inventory/HotelInfo, the following elements must not be present

            VALIDATOR.expectNull(
                    hotelDescriptiveContent.getFacilityInfo(),
                    ErrorMessage.EXPECT_FACILITY_INFO_TO_BE_NULL,
                    path.withElement(Names.FACILITY_INFO)
            );

            // HotelInfo is optional
            if (hotelDescriptiveContent.getHotelInfo() != null) {
                this.hotelInfoValidator.validate(hotelDescriptiveContent.getHotelInfo(), null, path.withElement(Names.HOTEL_INFO));
            }

            // Policies is optional
            if (hotelDescriptiveContent.getPolicies() != null) {
                this.policiesValidator.validate(hotelDescriptiveContent.getPolicies(), ctx, path.withElement(Names.POLICIES));
            }

            // AffiliationInfo is optional
            if (hotelDescriptiveContent.getAffiliationInfo() != null) {
                this.affiliationInfoValidator.validate(hotelDescriptiveContent.getAffiliationInfo(), ctx, path.withElement(Names.AFFILIATION_INFO));
            }

            // ContactInfos is optional
            if (hotelDescriptiveContent.getContactInfos() != null) {
                this.contactInfosValidator.validate(hotelDescriptiveContent.getContactInfos(), ctx, path.withElement(Names.CONTACT_INFOS));
            }
        }
    }

    private boolean isInventoryBasicAction(InventoryContext ctx) {
        return AlpineBitsAction.INVENTORY_BASIC_PUSH.equals(ctx.getAction());
    }

}
