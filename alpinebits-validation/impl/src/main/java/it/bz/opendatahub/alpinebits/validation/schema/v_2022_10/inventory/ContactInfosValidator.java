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
import it.bz.opendatahub.alpinebits.validation.context.inventory.InventoryContext;
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

import java.util.HashSet;
import java.util.Set;

/**
 * Use this validator to validate ContactInfos in AlpineBits 2022 Inventory documents.
 *
 * @see ContactInfosType
 */
public class ContactInfosValidator implements Validator<ContactInfosType, InventoryContext> {

    public static final String ELEMENT_NAME = Names.CONTACT_INFOS;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    @Override
    public void validate(ContactInfosType contactInfos, InventoryContext ctx, ValidationPath path) {
        // Although ContactInfos is optional, a null-check is performed.
        // It turned out, that it is better that the caller decides if ContactInfos
        // validation needs to be invoked. This makes the code more reusable
        // e.g. when required/optional elements change with different AlpineBits versions
        VALIDATOR.expectNotNull(contactInfos, ErrorMessage.EXPECT_CONTACT_INFOS_TO_NOT_BE_NULL, path);

        // There must be exactly one ContactInfo element inside ContactInfos
        if (contactInfos.getContactInfos().size() != 1) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_CONTACT_INFO_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT, path.withElement(Names.CONTACT_INFO_LIST));
        }

        ContactInfoRootType contactInfoRootType = contactInfos.getContactInfos().get(0);
        ValidationPath contactInfoPath = path.withElement(Names.CONTACT_INFO).withIndex(0);

        // The Location attribute must have a value of "6" according to OTA CON codelist
        if (!"6".equals(contactInfoRootType.getLocation())) {
            VALIDATOR.throwValidationException(ErrorMessage.EXPECT_LOCATION_TO_HAVE_A_VALUE_OF_6, contactInfoPath.withAttribute(Names.LOCATION));
        }

        if (contactInfoRootType.getAddresses() != null) {
            validateAddresses(contactInfoRootType.getAddresses(), contactInfoPath.withElement(Names.ADDRESSES));
        }

        if (contactInfoRootType.getPhones() != null) {
            validatePhones(contactInfoRootType.getPhones(), contactInfoPath.withElement(Names.PHONES));
        }

        if (contactInfoRootType.getEmails() != null) {
            validateEmails(contactInfoRootType.getEmails(), contactInfoPath.withElement(Names.EMAILS));
        }

        if (contactInfoRootType.getURLs() != null) {
            validateUrls(contactInfoRootType.getURLs(), contactInfoPath.withElement(Names.URLS));
        }

    }

    private void validateAddresses(AddressesType addressesType, ValidationPath path) {
        // The list of Address elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                addressesType.getAddresses(),
                ErrorMessage.EXPECT_ADDRESS_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.ADDRESS_LIST)
        );

        // This variable traces the different Address languages
        Set<String> addressLanguages = new HashSet<>();

        for (int i = 0; i < addressesType.getAddresses().size(); i++) {
            Address address = addressesType.getAddresses().get(i);
            ValidationPath indexedPath = path.withElement(Names.ADDRESS).withIndex(i);

            // The language attribute must not be null
            VALIDATOR.expectNotNull(address.getLanguage(), ErrorMessage.EXPECT_LANGUAGE_TO_BE_NOT_NULL, indexedPath.withAttribute(Names.LANGUAGE));

            // Each Address element must have a different Language attribute
            if (addressLanguages.contains(address.getLanguage())) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_ADDRESS_LANGUAGES_TO_BE_UNIQUE, indexedPath.withAttribute(Names.LANGUAGE));
            }
            addressLanguages.add(address.getLanguage());

            // The following Address elements are optional and therefor not validated
            // - AddressLine
            // - CityName
            // - PostalCode
            // - StateProv
            // - CountryName
        }
    }

    private void validatePhones(PhonesType phonesType, ValidationPath path) {
        // The list of Phone elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                phonesType.getPhones(),
                ErrorMessage.EXPECT_PHONE_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.PHONE_LIST)
        );

        for (int i = 0; i < phonesType.getPhones().size(); i++) {
            Phone phone = phonesType.getPhones().get(i);
            ValidationPath indexedPath = path.withElement(Names.PHONE).withIndex(i);

            // The PhoneTechType attribute is mandatory
            VALIDATOR.expectNotNull(
                    phone.getPhoneTechType(),
                    ErrorMessage.EXPECT_PHONE_TECH_TYPE_TO_NOT_BE_NULL,
                    indexedPath.withAttribute(Names.PHONE_TECH_TYPE)
            );

            // The PhoneNumber attribute is mandatory
            VALIDATOR.expectNotNull(
                    phone.getPhoneNumber(),
                    ErrorMessage.EXPECT_PHONE_NUMBER_TO_NOT_BE_NULL,
                    indexedPath.withAttribute(Names.PHONE_NUMBER)
            );
        }
    }

    private void validateEmails(EmailsType emailsType, ValidationPath path) {
        // The list of Email elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                emailsType.getEmails(),
                ErrorMessage.EXPECT_EMAIL_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.EMAIL_LIST)
        );

        for (int i = 0; i < emailsType.getEmails().size(); i++) {
            Email email = emailsType.getEmails().get(i);
            ValidationPath indexedPath = path.withElement(Names.EMAIL).withIndex(i);

            // The EmailType attribute is mandatory
            VALIDATOR.expectNotNull(
                    email.getEmailType(),
                    ErrorMessage.EXPECT_EMAIL_TYPE_TO_NOT_BE_NULL,
                    indexedPath.withAttribute(Names.EMAIL_TYPE)
            );
        }
    }

    // Suppress warning for uppercase comparison
    @SuppressWarnings("java:S1157")
    private void validateUrls(URLsType urlsType, ValidationPath path) {
        // The list of URL elements must not be empty
        VALIDATOR.expectNonEmptyCollection(
                urlsType.getURLS(),
                ErrorMessage.EXPECT_URL_LIST_TO_BE_NOT_EMPTY,
                path.withElement(Names.URL_LIST)
        );

        for (int i = 0; i < urlsType.getURLS().size(); i++) {
            URL url = urlsType.getURLS().get(i);
            ValidationPath indexedPath = path.withElement(Names.URL).withIndex(i);

            // If there is an ID attribute, its value must be all uppercase
            String id = url.getID();
            if (id != null && !id.equals(id.toUpperCase())) {
                VALIDATOR.throwValidationException(ErrorMessage.EXPECT_ID_TO_BE_ALL_UPPERCASE, indexedPath.withAttribute(Names.ID));
            }
        }
    }

}
