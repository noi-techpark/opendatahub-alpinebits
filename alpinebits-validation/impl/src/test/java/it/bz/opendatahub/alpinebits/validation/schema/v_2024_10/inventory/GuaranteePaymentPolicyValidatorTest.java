// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2024_10.inventory;

import it.bz.opendatahub.alpinebits.validation.EmptyCollectionValidationException;
import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.NullValidationException;
import it.bz.opendatahub.alpinebits.validation.SimpleValidationPath;
import it.bz.opendatahub.alpinebits.validation.ValidationException;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AcceptedPaymentsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AcceptedPaymentsType.AcceptedPayment;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BankAcctType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.EncryptionTokenType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PaymentCardType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PaymentFormType.Cash;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.GuaranteePaymentPolicy;
import it.bz.opendatahub.alpinebits.xml.schema.ota.RequiredPaymentsType.GuaranteePayment;
import it.bz.opendatahub.alpinebits.xml.schema.ota.RequiredPaymentsType.GuaranteePayment.AmountPercent;
import it.bz.opendatahub.alpinebits.xml.schema.ota.RequiredPaymentsType.GuaranteePayment.Deadline;
import it.bz.opendatahub.alpinebits.xml.schema.ota.TimeUnitType;
import org.testng.annotations.Test;

import static it.bz.opendatahub.alpinebits.validation.schema.common.ValidationUtil.validateAndAssert;

/**
 * Tests for {@link GuaranteePaymentPolicyValidator}.
 */
@SuppressWarnings("checkstyle:ClassDataAbstractionCoupling")
public class GuaranteePaymentPolicyValidatorTest {

    private static final GuaranteePaymentPolicyValidator VALIDATOR = new GuaranteePaymentPolicyValidator();
    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.GUARANTEE_PAYMENT_POLICY);

    @Test
    public void testValidate_ShouldThrow_WhenGuaranteePaymentCountIsLessThanOne() {
        String message = String.format(ErrorMessage.EXPECT_GUARANTEE_PAYMENT_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 0);
        validateAndAssert(VALIDATOR, new GuaranteePaymentPolicy(), VALIDATION_PATH, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenGuaranteePaymentCountIsMoreThanOne() {
        GuaranteePaymentPolicy guaranteePaymentPolicy = new GuaranteePaymentPolicy();
        guaranteePaymentPolicy.getGuaranteePayments().add(new GuaranteePayment());
        guaranteePaymentPolicy.getGuaranteePayments().add(new GuaranteePayment());

        String message = String.format(ErrorMessage.EXPECT_GUARANTEE_PAYMENT_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAcceptedPaymentListIsEmpty() {
        GuaranteePayment guaranteePayment = new GuaranteePayment();
        guaranteePayment.setAcceptedPayments(new AcceptedPaymentsType());

        GuaranteePaymentPolicy guaranteePaymentPolicy = new GuaranteePaymentPolicy();
        guaranteePaymentPolicy.getGuaranteePayments().add(guaranteePayment);

        validateAndAssert(
                VALIDATOR,
                guaranteePaymentPolicy,
                VALIDATION_PATH,
                EmptyCollectionValidationException.class,
                ErrorMessage.EXPECT_ACCEPTED_PAYMENT_TO_BE_NOT_EMPTY
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenAcceptedPaymentContainsMoreThanOnePaymentType() {
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(new BankAcctType());
        acceptedPayment.setCash(new Cash());

        AcceptedPaymentsType acceptedPaymentsType = new AcceptedPaymentsType();
        acceptedPaymentsType.getAcceptedPayments().add(acceptedPayment);

        GuaranteePayment guaranteePayment = new GuaranteePayment();
        guaranteePayment.setAcceptedPayments(acceptedPaymentsType);

        GuaranteePaymentPolicy guaranteePaymentPolicy = new GuaranteePaymentPolicy();
        guaranteePaymentPolicy.getGuaranteePayments().add(guaranteePayment);

        validateAndAssert(
                VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, ValidationException.class, ErrorMessage.EXPECT_ACCEPTED_PAYMENT_TO_HAVE_EXACTLY_ONE_ELEMENT
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenBankAcctNameIsNull() {
        BankAcctType bankAcct = new BankAcctType();
        bankAcct.setBankAcctNumber(new EncryptionTokenType());
        bankAcct.setBankID(new EncryptionTokenType());

        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(bankAcct);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_BANK_ACCT_NAME_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenBankAcctNumberIsNull() {
        BankAcctType bankAcct = new BankAcctType();
        bankAcct.setBankAcctName("My Bank");
        bankAcct.setBankAcctNumber(null);

        EncryptionTokenType bankId = new EncryptionTokenType();
        bankId.setPlainText("SWIFT123");
        bankAcct.setBankID(bankId);

        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(bankAcct);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(
                VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_BANK_ACCT_NUMBER_TO_NOT_BE_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenBankAcctNumberPlainTextIsNull() {
        BankAcctType bankAcct = new BankAcctType();
        bankAcct.setBankAcctName("My Bank");
        bankAcct.setBankAcctNumber(new EncryptionTokenType());

        EncryptionTokenType bankId = new EncryptionTokenType();
        bankId.setPlainText("SWIFT123");
        bankAcct.setBankID(bankId);

        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(bankAcct);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_PLAIN_TEXT_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenBankIDIsNull() {
        BankAcctType bankAcct = new BankAcctType();
        bankAcct.setBankAcctName("My Bank");

        EncryptionTokenType bankAcctNumber = new EncryptionTokenType();
        bankAcctNumber.setPlainText("IBAN12345");
        bankAcct.setBankAcctNumber(bankAcctNumber);

        bankAcct.setBankID(null);

        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(bankAcct);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_BANK_ID_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenBankIDPlainTextIsNull() {
        BankAcctType bankAcct = new BankAcctType();
        bankAcct.setBankAcctName("My Bank");

        EncryptionTokenType bankAcctNumber = new EncryptionTokenType();
        bankAcctNumber.setPlainText("IBAN12345");
        bankAcct.setBankAcctNumber(bankAcctNumber);

        bankAcct.setBankID(new EncryptionTokenType());

        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setBankAcct(bankAcct);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_PLAIN_TEXT_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCachIndicatorIsNull() {
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(new Cash());

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_CASH_INDICATOR_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenCardCodeIsNull() {
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setPaymentCard(new PaymentCardType());

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_CARD_CODE_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPercentIsNull() {
        Cash cash = new Cash();
        cash.setCashIndicator(true);
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(cash);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        // Extract GuaranteePayment
        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        guaranteePayment.setAmountPercent(new AmountPercent());

        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_PERCENT_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenMoreThanOneDeadlineElementExists() {
        Cash cash = new Cash();
        cash.setCashIndicator(true);
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(cash);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        // Extract GuaranteePayment
        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        guaranteePayment.getDeadlines().add(new Deadline());
        guaranteePayment.getDeadlines().add(new Deadline());

        String message = String.format(ErrorMessage.EXPECT_DEADLINE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, 2);
        validateAndAssert(VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, ValidationException.class, message);
    }

    @Test
    public void testValidate_ShouldThrow_WhenOffsetDropTimeIsNull() {
        Cash cash = new Cash();
        cash.setCashIndicator(true);
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(cash);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        Deadline deadline = new Deadline();
        deadline.setOffsetDropTime(null);
        deadline.setOffsetTimeUnit(TimeUnitType.DAY);
        deadline.setOffsetUnitMultiplier(1);

        // Extract GuaranteePayment
        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        guaranteePayment.getDeadlines().add(deadline);

        validateAndAssert(
                VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_OFFSET_DROP_TIME_TO_NOT_BE_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenOffsetTimeUnitIsNull() {
        Cash cash = new Cash();
        cash.setCashIndicator(true);
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(cash);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        Deadline deadline = new Deadline();
        deadline.setOffsetDropTime("AfterBooking");
        deadline.setOffsetTimeUnit(null);
        deadline.setOffsetUnitMultiplier(1);

        // Extract GuaranteePayment
        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        guaranteePayment.getDeadlines().add(deadline);

        validateAndAssert(
                VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_OFFSET_TIME_UNIT_TO_NOT_BE_NULL
        );
    }

    @Test
    public void testValidate_ShouldThrow_WhenOffsetUnitMultiplierIsNull() {
        Cash cash = new Cash();
        cash.setCashIndicator(true);
        AcceptedPayment acceptedPayment = new AcceptedPayment();
        acceptedPayment.setCash(cash);

        GuaranteePaymentPolicy guaranteePaymentPolicy = buildGuaranteePaymentPolicyWithAcceptedPayment(acceptedPayment);

        Deadline deadline = new Deadline();
        deadline.setOffsetDropTime("AfterBooking");
        deadline.setOffsetTimeUnit(TimeUnitType.DAY);
        deadline.setOffsetUnitMultiplier(null);

        // Extract GuaranteePayment
        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        guaranteePayment.getDeadlines().add(deadline);

        validateAndAssert(
                VALIDATOR, guaranteePaymentPolicy, VALIDATION_PATH, NullValidationException.class, ErrorMessage.EXPECT_OFFSET_UNIT_MULTIPLIER_TO_NOT_BE_NULL
        );
    }

    private GuaranteePaymentPolicy buildGuaranteePaymentPolicyWithAcceptedPayment(AcceptedPayment acceptedPayment) {
        AcceptedPaymentsType acceptedPaymentsType = new AcceptedPaymentsType();
        acceptedPaymentsType.getAcceptedPayments().add(acceptedPayment);

        GuaranteePayment guaranteePayment = new GuaranteePayment();
        guaranteePayment.setAcceptedPayments(acceptedPaymentsType);

        GuaranteePaymentPolicy guaranteePaymentPolicy = new GuaranteePaymentPolicy();
        guaranteePaymentPolicy.getGuaranteePayments().add(guaranteePayment);

        return guaranteePaymentPolicy;
    }

}