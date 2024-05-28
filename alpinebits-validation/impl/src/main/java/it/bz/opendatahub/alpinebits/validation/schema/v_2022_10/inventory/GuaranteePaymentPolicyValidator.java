// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.inventory;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AcceptedPaymentsType.AcceptedPayment;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BankAcctType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PoliciesType.Policy.GuaranteePaymentPolicy;
import it.bz.opendatahub.alpinebits.xml.schema.ota.RequiredPaymentsType.GuaranteePayment;
import it.bz.opendatahub.alpinebits.xml.schema.ota.RequiredPaymentsType.GuaranteePayment.Deadline;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Use this validator to validate GuaranteePaymentPolicy in AlpineBits 2022 Inventory documents.
 *
 * @see GuaranteePaymentPolicy
 */
public class GuaranteePaymentPolicyValidator implements Validator<GuaranteePaymentPolicy, Void> {

    public static final String ELEMENT_NAME = Names.POLICIES;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    @Override
    public void validate(GuaranteePaymentPolicy guaranteePaymentPolicy, Void ctx, ValidationPath path) {
        // There must be exactly one GuaranteePayment element
        int guaranteePaymentsCount = guaranteePaymentPolicy.getGuaranteePayments().size();
        if (guaranteePaymentsCount != 1) {
            String message = String.format(ErrorMessage.EXPECT_GUARANTEE_PAYMENT_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, guaranteePaymentsCount);
            VALIDATOR.throwValidationException(message, path.withElement(Names.GUARANTEE_PAYMENT_LIST));
        }

        GuaranteePayment guaranteePayment = guaranteePaymentPolicy.getGuaranteePayments().get(0);
        ValidationPath guaranteePaymentPath = path.withElement(Names.GUARANTEE_PAYMENT).withIndex(0);

        if (guaranteePayment.getAcceptedPayments() != null) {
            validateAcceptedPayments(guaranteePayment, guaranteePaymentPath);
        }
    }

    private void validateAcceptedPayments(GuaranteePayment guaranteePayment, ValidationPath guaranteePaymentPath) {
        // The list of AcceptedPayment elements must contain at least one element
        List<AcceptedPayment> acceptedPaymentList = guaranteePayment.getAcceptedPayments().getAcceptedPayments();
        VALIDATOR.expectNonEmptyCollection(
                acceptedPaymentList,
                ErrorMessage.EXPECT_ACCEPTED_PAYMENT_TO_BE_NOT_EMPTY,
                guaranteePaymentPath.withElement(Names.ACCEPTED_PAYMENTS).withElement(Names.ACCEPTED_PAYMENT_LIST)
        );

        for (int i = 0; i < acceptedPaymentList.size(); i++) {
            AcceptedPayment acceptedPayment = acceptedPaymentList.get(i);
            ValidationPath acceptedPaymentPath = guaranteePaymentPath.withElement(Names.ACCEPTED_PAYMENTS).withElement(Names.ACCEPTED_PAYMENT).withIndex(i);

            validateExactlyOnePaymentType(acceptedPayment, acceptedPaymentPath);

            // Validate BankAcct
            if (acceptedPayment.getBankAcct() != null) {
                BankAcctType bankAcct = acceptedPayment.getBankAcct();
                ValidationPath bankAcctPath = acceptedPaymentPath.withElement(Names.BANK_ACCT);

                // BankAcctName must not be null
                VALIDATOR.expectNotNull(
                        bankAcct.getBankAcctName(),
                        ErrorMessage.EXPECT_BANK_ACCT_NAME_TO_NOT_BE_NULL,
                        bankAcctPath.withElement(Names.BANK_ACCT_NAME)
                );

                // BankAcctNumber must not be null
                VALIDATOR.expectNotNull(
                        bankAcct.getBankAcctNumber(),
                        ErrorMessage.EXPECT_BANK_ACCT_NUMBER_TO_NOT_BE_NULL,
                        bankAcctPath.withElement(Names.BANK_ACCT_NUMBER)
                );

                // BankAcctNumber -> PlainText must not be null
                VALIDATOR.expectNotNull(
                        bankAcct.getBankAcctNumber().getPlainText(),
                        ErrorMessage.EXPECT_PLAIN_TEXT_TO_NOT_BE_NULL,
                        bankAcctPath.withElement(Names.BANK_ACCT_NUMBER).withElement(Names.PLAIN_TEXT)
                );

                // BankID must not be null
                VALIDATOR.expectNotNull(
                        bankAcct.getBankID(),
                        ErrorMessage.EXPECT_BANK_ID_TO_NOT_BE_NULL,
                        bankAcctPath.withElement(Names.BANK_ID)
                );

                // BankID -> PlainText must not be null
                VALIDATOR.expectNotNull(
                        bankAcct.getBankID().getPlainText(),
                        ErrorMessage.EXPECT_PLAIN_TEXT_TO_NOT_BE_NULL,
                        bankAcctPath.withElement(Names.BANK_ID).withElement(Names.PLAIN_TEXT)
                );
            }

            // Validate Cash
            if (acceptedPayment.getCash() != null) {
                // BankID must not be null
                VALIDATOR.expectNotNull(
                        acceptedPayment.getCash().isCashIndicator(),
                        ErrorMessage.EXPECT_CASH_INDICATOR_TO_NOT_BE_NULL,
                        acceptedPaymentPath.withElement(Names.CASH).withAttribute(Names.CASH_INDICATOR)
                );
            }

            // Validate PaymentCard
            if (acceptedPayment.getPaymentCard() != null) {
                // PaymentCard must not be null
                VALIDATOR.expectNotNull(
                        acceptedPayment.getPaymentCard().getCardCode(),
                        ErrorMessage.EXPECT_CARD_CODE_TO_NOT_BE_NULL,
                        acceptedPaymentPath.withElement(Names.PAYMENT_CARD).withAttribute(Names.CARD_CODE)
                );
            }
        }

        // Validate AmountPercent
        if (guaranteePayment.getAmountPercent() != null) {
            // Percent must not be null
            VALIDATOR.expectNotNull(
                    guaranteePayment.getAmountPercent().getPercent(),
                    ErrorMessage.EXPECT_PERCENT_TO_NOT_BE_NULL,
                    guaranteePaymentPath.withElement(Names.AMOUNT_PERCENT).withAttribute(Names.PERCENT)
            );
        }

        // Validate Deadline
        if (guaranteePayment.getDeadlines() != null) {
            int deadlineCount = guaranteePayment.getDeadlines().size();
            if (deadlineCount != 1) {
                String message = String.format(ErrorMessage.EXPECT_DEADLINE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, deadlineCount);
                VALIDATOR.throwValidationException(message, guaranteePaymentPath.withElement(Names.DEADLINE_LIST));
            }

            Deadline deadline = guaranteePayment.getDeadlines().get(0);
            ValidationPath deadlinePath = guaranteePaymentPath.withElement(Names.DEADLINE).withIndex(0);

            // OffsetDropTime must not be null
            VALIDATOR.expectNotNull(
                    deadline.getOffsetDropTime(),
                    ErrorMessage.EXPECT_OFFSET_DROP_TIME_TO_NOT_BE_NULL,
                    deadlinePath.withAttribute(Names.OFFSET_DROP_TIME)
            );

            // OffsetTimeUnit must not be null
            VALIDATOR.expectNotNull(
                    deadline.getOffsetTimeUnit(),
                    ErrorMessage.EXPECT_OFFSET_TIME_UNIT_TO_NOT_BE_NULL,
                    deadlinePath.withAttribute(Names.OFFSET_TIME_UNIT)
            );

            // OffsetUnitMultiplier must not be null
            VALIDATOR.expectNotNull(
                    deadline.getOffsetUnitMultiplier(),
                    ErrorMessage.EXPECT_OFFSET_UNIT_MULTIPLIER_TO_NOT_BE_NULL,
                    deadlinePath.withAttribute(Names.OFFSET_UNIT_MULTIPLIER)
            );
        }
    }

    private void validateExactlyOnePaymentType(AcceptedPayment acceptedPayment, ValidationPath path) {
        // Each AcceptedPayment element is supposed to contain exactly one of the following elements
        // - BankAcct
        // - Cash
        // - PaymentCard

        Set<String> subElements = new HashSet<>();

        if (acceptedPayment.getBankAcct() != null) {
            subElements.add(Names.BANK_ACCT);
        }
        if (acceptedPayment.getCash() != null) {
            subElements.add(Names.CASH);
        }
        if (acceptedPayment.getPaymentCard() != null) {
            subElements.add(Names.PAYMENT_CARD);
        }

        if (subElements.size() != 1) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_ACCEPTED_PAYMENT_TO_HAVE_EXACTLY_ONE_ELEMENT, path);
        }
    }
}
