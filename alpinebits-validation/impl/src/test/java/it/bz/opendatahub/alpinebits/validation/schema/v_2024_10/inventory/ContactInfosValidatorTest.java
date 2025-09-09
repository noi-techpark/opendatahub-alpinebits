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
import it.bz.opendatahub.alpinebits.xml.schema.ota.AddressesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.AddressesType.Address;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ContactInfoRootType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ContactInfosType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.EmailsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.EmailsType.Email;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PhonesType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.PhonesType.Phone;
import it.bz.opendatahub.alpinebits.xml.schema.ota.URLsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.URLsType.URL;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.expectThrows;

/**
 * Tests for {@link ContactInfosValidator}.
 */
public class ContactInfosValidatorTest {

    private static final ValidationPath VALIDATION_PATH = SimpleValidationPath.fromPath(Names.CONTACT_INFOS);

    @Test
    public void testValidate_ShouldThrow_WhenContactInfosTypeIsNull() {
        this.validateAndAssert(null, NullValidationException.class, ErrorMessage.EXPECT_CONTACT_INFOS_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenContactInfoListIsNull() {
        this.validateAndAssert(new ContactInfosType(), ValidationException.class, ErrorMessage.EXPECT_CONTACT_INFO_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenContactInfoListContainsMoreThanOneElement() {
        ContactInfosType contactInfosType = new ContactInfosType();
        contactInfosType.getContactInfos().add(new ContactInfoRootType());
        contactInfosType.getContactInfos().add(new ContactInfoRootType());

        this.validateAndAssert(contactInfosType, ValidationException.class, ErrorMessage.EXPECT_CONTACT_INFO_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT);
    }

    @Test
    public void testValidate_ShouldThrow_WhenContactInfoLocationValueIsNot6() {
        ContactInfoRootType contactInfoRootType = buildContactInfoRootType("1");

        ContactInfosType contactInfosType = new ContactInfosType();
        contactInfosType.getContactInfos().add(contactInfoRootType);

        this.validateAndAssert(contactInfosType, ValidationException.class, ErrorMessage.EXPECT_LOCATION_TO_HAVE_A_VALUE_OF_6);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAddressListIsEmpty() {
        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setAddresses(new AddressesType());

        this.validateAndAssert(contactInfosType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_ADDRESS_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAddressLanguageIsNull() {
        AddressesType addressesType = new AddressesType();
        addressesType.getAddresses().add(new Address());

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setAddresses(addressesType);

        this.validateAndAssert(contactInfosType, ValidationException.class, ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenAddressLanguagesAreNotUnique() {
        Address address = new Address();
        address.setLanguage("en");

        AddressesType addressesType = new AddressesType();
        addressesType.getAddresses().add(address);
        addressesType.getAddresses().add(address);

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setAddresses(addressesType);

        this.validateAndAssert(contactInfosType, ValidationException.class, ErrorMessage.EXPECT_ADDRESS_LANGUAGES_TO_BE_UNIQUE);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPhoneListIsEmpty() {
        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setPhones(new PhonesType());

        this.validateAndAssert(contactInfosType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_PHONE_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPhoneTechTypeIsNull() {
        Phone phone = new Phone();
        phone.setPhoneNumber("123");

        PhonesType phonesType = new PhonesType();
        phonesType.getPhones().add(phone);

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setPhones(phonesType);

        this.validateAndAssert(contactInfosType, NullValidationException.class, ErrorMessage.EXPECT_PHONE_TECH_TYPE_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenPhoneNumberIsNull() {
        Phone phone = new Phone();
        phone.setPhoneTechType("123");

        PhonesType phonesType = new PhonesType();
        phonesType.getPhones().add(phone);

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setPhones(phonesType);

        this.validateAndAssert(contactInfosType, NullValidationException.class, ErrorMessage.EXPECT_PHONE_NUMBER_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenEmailListIsEmpty() {
        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setEmails(new EmailsType());

        this.validateAndAssert(contactInfosType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_EMAIL_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenEmailTypeIsNull() {
        EmailsType emailsType = new EmailsType();
        emailsType.getEmails().add(new Email());

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setEmails(emailsType);

        this.validateAndAssert(contactInfosType, NullValidationException.class, ErrorMessage.EXPECT_EMAIL_TYPE_TO_NOT_BE_NULL);
    }

    @Test
    public void testValidate_ShouldThrow_WhenURLListIsEmpty() {
        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setURLs(new URLsType());

        this.validateAndAssert(contactInfosType, EmptyCollectionValidationException.class, ErrorMessage.EXPECT_URL_LIST_TO_BE_NOT_EMPTY);
    }

    @Test
    public void testValidate_ShouldThrow_WhenIDIsSetButNotUpperCase() {
        URL url = new URL();
        url.setID("Not all uppercase");

        URLsType urLsType = new URLsType();
        urLsType.getURLS().add(url);

        ContactInfosType contactInfosType = buildValidContactInfosType();
        contactInfosType.getContactInfos().get(0).setURLs(urLsType);

        this.validateAndAssert(contactInfosType, ValidationException.class, ErrorMessage.EXPECT_ID_TO_BE_ALL_UPPERCASE);
    }

    private ContactInfosType buildValidContactInfosType() {
        ContactInfoRootType contactInfoRootType = new ContactInfoRootType();
        contactInfoRootType.setLocation("6");

        ContactInfosType contactInfosType = new ContactInfosType();
        contactInfosType.getContactInfos().add(contactInfoRootType);
        return contactInfosType;
    }

    private ContactInfoRootType buildContactInfoRootType(String location) {
        ContactInfoRootType contactInfoRootType = new ContactInfoRootType();
        contactInfoRootType.setLocation(location);
        return contactInfoRootType;
    }

    private void validateAndAssert(
            ContactInfosType data,
            Class<? extends ValidationException> exceptionClass,
            String errorMessage
    ) {
        ContactInfosValidator validator = new ContactInfosValidator();

        // CHECKSTYLE:OFF
        Exception e = expectThrows(
                exceptionClass,
                () -> validator.validate(data, null, VALIDATION_PATH)
        );

        // CHECKSTYLE:ON
        assertEquals(e.getMessage().substring(0, errorMessage.length()), errorMessage);
    }
}