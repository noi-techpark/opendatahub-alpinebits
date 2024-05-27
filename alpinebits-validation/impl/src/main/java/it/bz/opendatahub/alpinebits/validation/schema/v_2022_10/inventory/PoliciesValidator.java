/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.common.constants.Iso4217;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
import it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common.Description;
import it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common.DescriptionsValidator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.CancelPenaltiesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.CancelPenaltyType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.FormattedTextTextType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.HotelDescriptiveContentType.Policies;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ParagraphType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.CheckoutCharges;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.CheckoutCharges.CheckoutCharge;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.PetsPolicies;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.PetsPolicies.PetsPolicy;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.StayRequirements;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.StayRequirements.StayRequirement;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.TaxPolicies;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.TaxPolicies.TaxPolicy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Use this validator to validate Policies in AlpineBits 2022 Inventory documents.
 *
 * @see Policies
 */
public class PoliciesValidator implements Validator<Policies, InventoryContext> {

    public static final String ELEMENT_NAME = Names.POLICIES;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private static final Pattern TIME_PATTERN = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$");

    private final DescriptionsValidator descriptionsValidator = new DescriptionsValidator();
    private final GuaranteePaymentPolicyValidator guaranteePaymentPolicyValidator = new GuaranteePaymentPolicyValidator();

    @Override
    public void validate(Policies policies, InventoryContext ctx, ValidationPath path) {
        // Although Policies is optional, a null-check is performed.
        // It turned out, that it is better that the caller decides if Policies
        // validation needs to be invoked. This makes the code more reusable
        // e.g. when required/optional elements change with different AlpineBits versions
        VALIDATOR.expectNotNull(policies, ErrorMessage.EXPECT_POLICIES_TO_NOT_BE_NULL, path);

        // The list of Policy elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                policies.getPolicies(),
                ErrorMessage.EXPECT_POLICY_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.POLICY_LIST)
        );

        for (int i = 0; i < policies.getPolicies().size(); i++) {
            Policy policy = policies.getPolicies().get(i);
            ValidationPath indexedPath = path.withElement(Names.POLICY).withIndex(i);

            validateExactlyOnePolicy(policy, indexedPath);

            if (policy.getCancelPolicy() != null) {
                validateCancelPolicy(policy.getCancelPolicy(), indexedPath.withElement(Names.CANCEL_POLICY));
            }
            if (policy.getCheckoutCharges() != null) {
                validateCheckoutCharges(policy.getCheckoutCharges(), indexedPath.withElement(Names.CHECKOUT_CHARGES));
            }
            if (policy.getPetsPolicies() != null) {
                validatePetsPolicies(policy.getPetsPolicies(), indexedPath.withElement(Names.PETS_POLICIES));
            }
            if (policy.getTaxPolicies() != null) {
                validateTaxPolicies(policy.getTaxPolicies(), indexedPath.withElement(Names.TAX_POLICIES));
            }
            if (policy.getGuaranteePaymentPolicy() != null) {
                guaranteePaymentPolicyValidator.validate(policy.getGuaranteePaymentPolicy(), null, indexedPath.withElement(Names.GUARANTEE_PAYMENT_POLICY));
            }
            if (policy.getStayRequirements() != null) {
                validateStayRequirements(policy.getStayRequirements(), indexedPath.withElement(Names.STAY_REQUIREMENTS));
            }
        }
    }

    private void validateExactlyOnePolicy(Policy policy, ValidationPath path) {
        // Each policy element is supposed to contain exactly one of the following elements
        // - CancelPolicy
        // - CheckoutCharges
        // - PetsPolicies
        // - TaxPolicies
        // - GuaranteePaymentPolicy
        // - StayRequirements

        Set<String> subElements = new HashSet<>();

        if (policy.getCancelPolicy() != null) {
            subElements.add(Names.CANCEL_POLICY);
        }
        if (policy.getCheckoutCharges() != null) {
            subElements.add(Names.CHECKOUT_CHARGES);
        }
        if (policy.getPetsPolicies() != null) {
            subElements.add(Names.PETS_POLICIES);
        }
        if (policy.getTaxPolicies() != null) {
            subElements.add(Names.TAX_POLICIES);
        }
        if (policy.getGuaranteePaymentPolicy() != null) {
            subElements.add(Names.GUARANTEE_PAYMENT_POLICY);
        }
        if (policy.getStayRequirements() != null) {
            subElements.add(Names.STAY_REQUIREMENTS);
        }

        if (subElements.size() != 1) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_POLICY_TO_HAVE_EXACTLY_ONE_ELEMENT, path);
        }
    }

    private void validateCancelPolicy(CancelPenaltiesType cancelPenaltiesType, ValidationPath path) {
        // There must be exactly one CancelPenalty element
        int cancelPenaltyCount = cancelPenaltiesType.getCancelPenalties().size();
        if (cancelPenaltyCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_CANCEL_PENALTY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, cancelPenaltyCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.CANCEL_PENALTY_LIST));
        }

        CancelPenaltyType cancelPenaltyType = cancelPenaltiesType.getCancelPenalties().get(0);
        ValidationPath cancelPenaltyPath = path.withElement(Names.CANCEL_PENALTY).withIndex(0);

        validateDescription(
                cancelPenaltyType.getPenaltyDescriptions(),
                ErrorMessage.EXPECT_PENALTY_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT,
                Names.PENALTY_DESCRIPTION_LIST,
                cancelPenaltyPath
        );
    }

    private void validateCheckoutCharges(CheckoutCharges checkoutCharges, ValidationPath path) {
        // There must be exactly one CheckoutCharges element
        int checkoutChargesCount = checkoutCharges.getCheckoutCharges().size();
        if (checkoutChargesCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_CHECKOUT_CHARGE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, checkoutChargesCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.CHECKOUT_CHARGE_LIST));
        }

        CheckoutCharge checkoutCharge = checkoutCharges.getCheckoutCharges().get(0);
        ValidationPath checkoutChargePath = path.withElement(Names.CHECKOUT_CHARGE).withIndex(0);

        // If the Amount element exists, the CurrencyCode element must be specified and its value
        // must be one of ISO-4217 currency codes
        if (checkoutCharge.getAmount() != null) {
            String currencyCode = checkoutCharge.getCurrencyCode();
            if (currencyCode == null) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_AMOUNT_EXISTS, checkoutChargePath);
            } else if (!Iso4217.isCodeDefined(currencyCode)) {
                String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, currencyCode);
                VALIDATOR.throwValidationException(message, checkoutChargePath);
            }
        }

        validateDescription(
                checkoutCharge.getDescriptions(),
                ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT,
                Names.DESCRIPTION_LIST,
                checkoutChargePath
        );
    }

    private void validatePetsPolicies(PetsPolicies petsPolicies, ValidationPath path) {
        // There must be exactly one PetsPolicies element
        int petsPoliciesCount = petsPolicies.getPetsPolicies().size();
        if (petsPoliciesCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_PETS_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, petsPoliciesCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.PETS_POLICIES_LIST));
        }

        PetsPolicy petsPolicy = petsPolicies.getPetsPolicies().get(0);
        ValidationPath petsPolicyPath = path.withElement(Names.PETS_POLICY).withIndex(0);

        // If the NonRefundableFee element exists, the CurrencyCode element must be specified and its value
        // must be one of ISO-4217 currency codes
        if (petsPolicy.getNonRefundableFee() != null) {
            String currencyCode = petsPolicy.getCurrencyCode();
            if (currencyCode == null) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_NON_REFUNDABLE_FEE_EXISTS, petsPolicyPath);
            } else if (!Iso4217.isCodeDefined(currencyCode)) {
                String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, currencyCode);
                VALIDATOR.throwValidationException(message, petsPolicyPath);
            }
        }

        validateDescription(
                petsPolicy.getDescriptions(),
                ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT,
                Names.DESCRIPTION_LIST,
                petsPolicyPath
        );
    }

    private void validateTaxPolicies(TaxPolicies taxPolicies, ValidationPath path) {
        // There must be exactly one TaxPolicies element
        int taxPoliciesCount = taxPolicies.getTaxPolicies().size();
        if (taxPoliciesCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_TAX_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, taxPoliciesCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.TAX_POLICY_LIST));
        }

        TaxPolicy taxPolicy = taxPolicies.getTaxPolicies().get(0);
        ValidationPath taxPolicyPath = path.withElement(Names.TAX_POLICY).withIndex(0);

        // The Code attribute must have a value of "3" according to OTA FTT codelist
        if (!"3".equals(taxPolicy.getCode())) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CODE_TO_HAVE_A_VALUE_OF_3, taxPolicyPath.withAttribute(Names.CODE));
        }

        // If the Amount element exists, some additional criteria must be met
        if (taxPolicy.getAmount() != null) {
            // The ChargeFrequency attribute must have a value of "1" according to OTA CHG codelist
            if (!"1".equals(taxPolicy.getChargeFrequency())) {
                VALIDATOR.throwValidationException(
                        ErrorMessage.EXPECT_CHARGE_FREQUENCY_TO_HAVE_A_VALUE_OF_1,
                        taxPolicyPath.withAttribute(Names.CHARGE_FREQUENCY)
                );
            }
            // The ChargeUnit attribute must have a value of "21" according to OTA CHG codelist
            if (!"21".equals(taxPolicy.getChargeUnit())) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CHARGE_UNIT_TO_HAVE_A_VALUE_OF_21, taxPolicyPath.withAttribute(Names.CHARGE_UNIT));
            }

            String currencyCode = taxPolicy.getCurrencyCode();

            // The CurrencyCode element must not be null
            if (currencyCode == null) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_AMOUNT_EXISTS, taxPolicyPath);
            }
            // The CurrencyCode element must be valid
            if (!Iso4217.isCodeDefined(currencyCode)) {
                String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, currencyCode);
                VALIDATOR.throwValidationException(message, taxPolicyPath);
            }
        }

        validateDescription(
                taxPolicy.getTaxDescriptions(),
                ErrorMessage.EXPECT_TAX_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT,
                Names.TAX_DESCRIPTION_LIST,
                taxPolicyPath
        );
    }

    private void validateStayRequirements(StayRequirements stayRequirements, ValidationPath path) {
        // There must be exactly one StayRequirements element
        int stayRequirementsCount = stayRequirements.getStayRequirements().size();
        if (stayRequirementsCount != 1 && stayRequirementsCount != 2) {
            String message = String.format(ErrorMessage.EXPECT_STAY_REQUIREMENT_LIST_TO_HAVE_ONE_OR_TWO_ELEMENTS, stayRequirementsCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.STAY_REQUIREMENT_LIST));
        }

        for (int i = 0; i < stayRequirements.getStayRequirements().size(); i++) {
            StayRequirement stayRequirement = stayRequirements.getStayRequirements().get(i);
            ValidationPath stayRequirementPath = path.withElement(Names.STAY_REQUIREMENT).withIndex(i);

            if (!"Checkin".equals(stayRequirement.getStayContext()) && !"Checkout".equals(stayRequirement.getStayContext())) {
                VALIDATOR.throwValidationException(
                        ErrorMessage.EXPECT_STAY_CONTEXT_TO_BE_CHECKIN_OR_CHECKOUT,
                        stayRequirementPath.withAttribute(Names.STAY_CONTEXT)
                );
            }

            // Start must not be null
            VALIDATOR.expectNotNull(
                    stayRequirement.getStart(),
                    ErrorMessage.EXPECT_START_TO_BE_NOT_NULL,
                    stayRequirementPath.withAttribute(Names.START)
            );

            if (!TIME_PATTERN.matcher(stayRequirement.getStart()).matches()) {
                VALIDATOR.throwValidationException(
                        ErrorMessage.EXPECT_START_TO_BE_TIME_FORMAT,
                        stayRequirementPath.withAttribute(Names.START)
                );
            }

            // End must not be null
            VALIDATOR.expectNotNull(
                    stayRequirement.getEnd(),
                    ErrorMessage.EXPECT_END_TO_BE_NOT_NULL,
                    stayRequirementPath.withAttribute(Names.END)
            );

            if (!TIME_PATTERN.matcher(stayRequirement.getEnd()).matches()) {
                VALIDATOR.throwValidationException(
                        ErrorMessage.EXPECT_END_TO_BE_TIME_FORMAT,
                        stayRequirementPath.withAttribute(Names.END)
                );
            }
        }
    }

    private void validateDescription(List<ParagraphType> descriptions, String errorMessageTemplate, String elementName, ValidationPath path) {
        // There must be exactly one Description element
        int count = descriptions.size();
        if (count != 1) {
            String message = String.format(errorMessageTemplate, count);
            VALIDATOR.throwValidationException(message, path.withElement(elementName));
        }

        validateParagraphType(descriptions.get(0), path.withElement(Names.DESCRIPTION));
    }

    private void validateParagraphType(ParagraphType paragraphType, ValidationPath path) {
        List<FormattedTextTextType> textElements = paragraphType.getTextsAndImagesAndURLS().stream()
                .filter(jaxbElement -> jaxbElement.getValue() instanceof FormattedTextTextType)
                .map(jaxbElement -> (FormattedTextTextType) jaxbElement.getValue())
                .collect(Collectors.toList());

        // The list of Text elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                textElements,
                ErrorMessage.EXPECT_TEXT_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.TEXT_LIST)
        );

        // Validate Text elements
        List<Description> descriptions = Description.fromFormattedTextTextTypes(textElements);
        this.descriptionsValidator.validate(descriptions, null, path.withElement(Names.TEXT_LIST));
    }
}
