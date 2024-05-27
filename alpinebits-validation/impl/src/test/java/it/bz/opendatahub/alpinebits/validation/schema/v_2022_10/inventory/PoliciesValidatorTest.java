/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
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
import org.testng.annotations.Test;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link PoliciesValidator}.
 */
public class PoliciesValidatorTest {

    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.POLICIES);

    @Test
    public void testValidate_ShouldThrow_WhenPoliciesIsNull() {
        this.validateAndAssert(null, NullValidationException.class, ErrorMessage.EXPECT_POLICIES_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPolicyListIsEmpty() {
        this.validateAndAssert(new Policies(), EmptyCollectionValidationException.class, ErrorMessage.EXPECT_POLICY_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPolicyHasMoreThanOneSubElement() {
        Policy policy = new Policy();
        policy.setCancelPolicy(new CancelPenaltiesType());
        policy.setCheckoutCharges(new CheckoutCharges());

        Policies policies = new Policies();
        policies.getPolicies().add(policy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_POLICY_TO_HAVE_EXACTLY_ONE_ELEMENT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCancelPenaltyCountIsLessThanOne() {
        CancelPenaltiesType cancelPenaltiesType = new CancelPenaltiesType();

        Policy policy = new Policy();
        policy.setCancelPolicy(cancelPenaltiesType);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_CANCEL_PENALTY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCancelPenaltyCountIsMoreThanOne() {
        CancelPenaltiesType cancelPenaltiesType = new CancelPenaltiesType();
        cancelPenaltiesType.getCancelPenalties().add(new CancelPenaltyType());
        cancelPenaltiesType.getCancelPenalties().add(new CancelPenaltyType());

        Policy policy = new Policy();
        policy.setCancelPolicy(cancelPenaltiesType);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_CANCEL_PENALTY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPenaltyDescriptionCountIsLessThanOne() {
        CancelPenaltyType cancelPenaltyType = new CancelPenaltyType();

        Policies policies = buildPoliciesWithCancelPenalty(cancelPenaltyType);

        String message = String.format(ErrorMessage.EXPECT_PENALTY_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    /**
     * The Text element validation is done by the DescriptionsValidator.
     * Therefor it is not necessary to test each property of a Text element.
     * We only test, that there is a Text element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenPenaltyDescriptionLanguageIsNull() {
        ParagraphType paragraphType = buildParagraphType();

        CancelPenaltyType cancelPenaltyType = new CancelPenaltyType();
        cancelPenaltyType.getPenaltyDescriptions().add(paragraphType);

        Policies policies = buildPoliciesWithCancelPenalty(cancelPenaltyType);

        this.validateAndAssert(policies, NullValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPenaltyDescriptionCountIsMoreThanOne() {
        CancelPenaltyType cancelPenaltyType = new CancelPenaltyType();
        cancelPenaltyType.getPenaltyDescriptions().add(new ParagraphType());
        cancelPenaltyType.getPenaltyDescriptions().add(new ParagraphType());

        Policies policies = buildPoliciesWithCancelPenalty(cancelPenaltyType);

        String message = String.format(ErrorMessage.EXPECT_PENALTY_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTextListIsEmpty() {
        CancelPenaltyType cancelPenaltyType = new CancelPenaltyType();
        cancelPenaltyType.getPenaltyDescriptions().add(new ParagraphType());

        Policies policies = buildPoliciesWithCancelPenalty(cancelPenaltyType);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_TEXT_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargesCountIsLessThanOne() {
        CheckoutCharges checkoutCharges = new CheckoutCharges();

        Policy policy = new Policy();
        policy.setCheckoutCharges(checkoutCharges);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_CHECKOUT_CHARGE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargesCountIsMoreThanOne() {
        CheckoutCharges checkoutCharges = new CheckoutCharges();
        checkoutCharges.getCheckoutCharges().add(new CheckoutCharge());
        checkoutCharges.getCheckoutCharges().add(new CheckoutCharge());

        Policy policy = new Policy();
        policy.setCheckoutCharges(checkoutCharges);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_CHECKOUT_CHARGE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAmountIsSetButCurrencyCodeIsNull() {
        CheckoutCharge checkoutCharge = new CheckoutCharge();
        checkoutCharge.setAmount(BigDecimal.ONE);

        Policies policies = buildPoliciesWithCheckoutCharge(checkoutCharge);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_AMOUNT_EXISTS);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargesAmountIsSetButCurrencyCodeIsNotIso4217() {
        String notIso4217 = "NOT_ISO_4217";

        CheckoutCharge checkoutCharge = new CheckoutCharge();
        checkoutCharge.setAmount(BigDecimal.ONE);

        Policies policies = buildPoliciesWithCheckoutCharge(checkoutCharge);
        policies.getPolicies().get(0).getCheckoutCharges().getCheckoutCharges().get(0).setCurrencyCode(notIso4217);

        String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, notIso4217);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargeDescriptionCountIsLessThanOne() {
        Policies policies = buildPoliciesWithCheckoutCharge(new CheckoutCharge());

        String message = String.format(ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargeDescriptionCountIsMoreThanOne() {
        CheckoutCharge checkoutCharge = new CheckoutCharge();
        checkoutCharge.getDescriptions().add(new ParagraphType());
        checkoutCharge.getDescriptions().add(new ParagraphType());

        Policies policies = buildPoliciesWithCheckoutCharge(checkoutCharge);

        String message = String.format(ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    /**
     * The Text element validation is done by the DescriptionsValidator.
     * Therefor it is not necessary to test each property of a Text element.
     * We only test, that there is a Text element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenCheckoutChargeDescriptionLanguageIsNull() {
        ParagraphType paragraphType = buildParagraphType();

        CheckoutCharge checkoutCharge = new CheckoutCharge();
        checkoutCharge.getDescriptions().add(paragraphType);

        Policies policies = buildPoliciesWithCheckoutCharge(checkoutCharge);

        this.validateAndAssert(policies, NullValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPoliciesCountIsLessThanOne() {
        PetsPolicies petsPolicies = new PetsPolicies();

        Policy policy = new Policy();
        policy.setPetsPolicies(petsPolicies);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_PETS_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPoliciesCountIsMoreThanOne() {
        PetsPolicies petsPolicies = new PetsPolicies();
        petsPolicies.getPetsPolicies().add(new PetsPolicy());
        petsPolicies.getPetsPolicies().add(new PetsPolicy());

        Policy policy = new Policy();
        policy.setPetsPolicies(petsPolicies);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_PETS_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPoliciesNonRefundableFeeIsSetButCurrencyCodeIsNull() {
        PetsPolicy petsPolicy = new PetsPolicy();
        petsPolicy.setNonRefundableFee(BigDecimal.ONE);

        Policies policies = buildPoliciesWithPetsPolicy(petsPolicy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_NON_REFUNDABLE_FEE_EXISTS);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPoliciesAmountIsSetButCurrencyCodeIsNotIso4217() {
        String notIso4217 = "NOT_ISO_4217";

        PetsPolicy petsPolicy = new PetsPolicy();
        petsPolicy.setNonRefundableFee(BigDecimal.ONE);

        Policies policies = buildPoliciesWithPetsPolicy(petsPolicy);
        policies.getPolicies().get(0).getPetsPolicies().getPetsPolicies().get(0).setCurrencyCode(notIso4217);

        String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, notIso4217);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPolicyDescriptionCountIsLessThanOne() {
        Policies policies = buildPoliciesWithPetsPolicy(new PetsPolicy());

        String message = String.format(ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPetsPolicyDescriptionCountIsMoreThanOne() {
        PetsPolicy petsPolicy = new PetsPolicy();
        petsPolicy.getDescriptions().add(new ParagraphType());
        petsPolicy.getDescriptions().add(new ParagraphType());

        Policies policies = buildPoliciesWithPetsPolicy(petsPolicy);

        String message = String.format(ErrorMessage.EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    /**
     * The Text element validation is done by the DescriptionsValidator.
     * Therefor it is not necessary to test each property of a Text element.
     * We only test, that there is a Text element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenPetsPolicyDescriptionLanguageIsNull() {
        ParagraphType paragraphType = buildParagraphType();

        PetsPolicy petsPolicy = new PetsPolicy();
        petsPolicy.getDescriptions().add(paragraphType);

        Policies policies = buildPoliciesWithPetsPolicy(petsPolicy);

        this.validateAndAssert(policies, NullValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPoliciesCountIsLessThanOne() {
        TaxPolicies taxPolicies = new TaxPolicies();

        Policy policy = new Policy();
        policy.setTaxPolicies(taxPolicies);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_TAX_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPoliciesCountIsMoreThanOne() {
        TaxPolicies taxPolicies = new TaxPolicies();
        taxPolicies.getTaxPolicies().add(new TaxPolicy());
        taxPolicies.getTaxPolicies().add(new TaxPolicy());

        Policy policy = new Policy();
        policy.setTaxPolicies(taxPolicies);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_TAX_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPolicyCodeIsNot3() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("abc");
        taxPolicy.setAmount(BigDecimal.ONE);
        taxPolicy.setChargeFrequency("abc");
        taxPolicy.setChargeUnit("21");
        taxPolicy.setCurrencyCode("EUR");

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CODE_TO_HAVE_A_VALUE_OF_3);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPolicyAmountIsSetButChargeFrequencyIsNot1() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.setAmount(BigDecimal.ONE);
        taxPolicy.setChargeFrequency("abc");
        taxPolicy.setChargeUnit("21");
        taxPolicy.setCurrencyCode("EUR");

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CHARGE_FREQUENCY_TO_HAVE_A_VALUE_OF_1);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPolicyAmountIsSetButChargeUnitIsNot21() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.setAmount(BigDecimal.ONE);
        taxPolicy.setChargeFrequency("1");
        taxPolicy.setChargeUnit("abc");
        taxPolicy.setCurrencyCode("EUR");

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CHARGE_UNIT_TO_HAVE_A_VALUE_OF_21);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPolicyAmountIsSetButCurrencyCodeIsNull() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.setAmount(BigDecimal.ONE);
        taxPolicy.setChargeFrequency("1");
        taxPolicy.setChargeUnit("21");
        taxPolicy.setCurrencyCode(null);

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_CURRENCY_CODE_TO_EXIST_IF_AMOUNT_EXISTS);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxPolicyAmountIsSetButCurrencyCodeIsNotIso4217() {
        String notIso4217 = "NOT_ISO_4217";

        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.setAmount(BigDecimal.ONE);
        taxPolicy.setChargeFrequency("1");
        taxPolicy.setChargeUnit("21");
        taxPolicy.setCurrencyCode(notIso4217);

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        String message = String.format(ErrorMessage.EXPECT_CURRENCY_CODE_TO_BE_VALID, notIso4217);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxDescriptionCountIsLessThanOne() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        String message = String.format(ErrorMessage.EXPECT_TAX_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenTaxDescriptionCountIsMoreThanOne() {
        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.getTaxDescriptions().add(new ParagraphType());
        taxPolicy.getTaxDescriptions().add(new ParagraphType());

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        String message = String.format(ErrorMessage.EXPECT_TAX_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    /**
     * The Text element validation is done by the DescriptionsValidator.
     * Therefor it is not necessary to test each property of a Text element.
     * We only test, that there is a Text element validation at all.
     */
    @Test
    public void testValidate_ShouldThrow_WhenTaxDescriptionLanguageIsNull() {
        ParagraphType paragraphType = buildParagraphType();

        TaxPolicy taxPolicy = new TaxPolicy();
        taxPolicy.setCode("3");
        taxPolicy.getTaxDescriptions().add(paragraphType);

        Policies policies = buildPoliciesWithTaxPolicy(taxPolicy);

        this.validateAndAssert(policies, NullValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenStayRequirementCountIsLessThanOne() {
        Policy policy = new Policy();
        policy.setStayRequirements(new StayRequirements());

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_STAY_REQUIREMENT_LIST_TO_HAVE_ONE_OR_TWO_ELEMENTS, 0);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenStayRequirementCountIsMoreThanTwo() {
        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(new StayRequirement());
        stayRequirements.getStayRequirements().add(new StayRequirement());
        stayRequirements.getStayRequirements().add(new StayRequirement());

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        String message = String.format(ErrorMessage.EXPECT_STAY_REQUIREMENT_LIST_TO_HAVE_ONE_OR_TWO_ELEMENTS, 3);
        this.validateAndAssert(policies, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenStayContextIsNeitherCheckinNorCheckout() {
        StayRequirement stayRequirement = new StayRequirement();
        stayRequirement.setStayContext("Some value");
        stayRequirement.setStart("15:00:00");
        stayRequirement.setEnd("15:00:00");

        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(stayRequirement);

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_STAY_CONTEXT_TO_BE_CHECKIN_OR_CHECKOUT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenStartIsNull() {
        StayRequirement stayRequirement = new StayRequirement();
        stayRequirement.setStayContext("Checkin");
        stayRequirement.setStart(null);
        stayRequirement.setEnd("15:00:00");

        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(stayRequirement);

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        this.validateAndAssert(policies, NullValidationException.class, ErrorMessage.EXPECT_START_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenStartHasNotTimeFormat() {
        StayRequirement stayRequirement = new StayRequirement();
        stayRequirement.setStayContext("Checkin");
        stayRequirement.setStart("Some value");
        stayRequirement.setEnd("15:00:00");

        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(stayRequirement);

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_START_TO_BE_TIME_FORMAT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenEndIsNull() {
        StayRequirement stayRequirement = new StayRequirement();
        stayRequirement.setStayContext("Checkin");
        stayRequirement.setStart("15:00:00");
        stayRequirement.setEnd(null);

        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(stayRequirement);

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_END_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenEndHasNotTimeFormat() {
        StayRequirement stayRequirement = new StayRequirement();
        stayRequirement.setStayContext("Checkin");
        stayRequirement.setStart("15:00:00");
        stayRequirement.setEnd("Some value");

        StayRequirements stayRequirements = new StayRequirements();
        stayRequirements.getStayRequirements().add(stayRequirement);

        Policy policy = new Policy();
        policy.setStayRequirements(stayRequirements);

        Policies policies = buildValidPolicies(policy);

        this.validateAndAssert(policies, ValidationException.class, ErrorMessage.EXPECT_END_TO_BE_TIME_FORMAT);
    }

    private Policies buildValidPolicies(Policy policy) {
        Policies policies = new Policies();
        policies.getPolicies().add(policy);
        return policies;
    }

    private Policies buildPoliciesWithCancelPenalty(CancelPenaltyType cancelPenaltyType) {
        CancelPenaltiesType cancelPenaltiesType = new CancelPenaltiesType();
        cancelPenaltiesType.getCancelPenalties().add(cancelPenaltyType);

        Policy policy = new Policy();
        policy.setCancelPolicy(cancelPenaltiesType);

        return buildValidPolicies(policy);
    }

    private Policies buildPoliciesWithCheckoutCharge(CheckoutCharge checkoutCharge) {
        CheckoutCharges checkoutCharges = new CheckoutCharges();
        checkoutCharges.getCheckoutCharges().add(checkoutCharge);

        Policy policy = new Policy();
        policy.setCheckoutCharges(checkoutCharges);

        return buildValidPolicies(policy);
    }

    private Policies buildPoliciesWithPetsPolicy(PetsPolicy petsPolicy) {
        PetsPolicies petsPolicies = new PetsPolicies();
        petsPolicies.getPetsPolicies().add(petsPolicy);

        Policy policy = new Policy();
        policy.setPetsPolicies(petsPolicies);

        return buildValidPolicies(policy);
    }

    private Policies buildPoliciesWithTaxPolicy(TaxPolicy taxPolicy) {
        TaxPolicies taxPolicies = new TaxPolicies();
        taxPolicies.getTaxPolicies().add(taxPolicy);

        Policy policy = new Policy();
        policy.setTaxPolicies(taxPolicies);

        return buildValidPolicies(policy);
    }

    private ParagraphType buildParagraphType() {
        JAXBElement<FormattedTextTextType> text = new JAXBElement<>(
                QName.valueOf(""),
                FormattedTextTextType.class,
                new FormattedTextTextType()
        );

        ParagraphType paragraphType = new ParagraphType();
        paragraphType.getTextsAndImagesAndURLS().add(text);
        return paragraphType;
    }

    private void validateAndAssert(
            Policies data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        PoliciesValidator validator = new PoliciesValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );

        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }
}