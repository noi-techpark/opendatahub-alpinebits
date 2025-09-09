// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation;

/**
 * Validation messages.
 */
public final class ErrorMessage {

    public static final String EXPECT_ACCEPTED_PAYMENT_TO_BE_NOT_EMPTY =
            "The list of AcceptedPayment elements is expected to contain at least one element";
    public static final String EXPECT_ACCEPTED_PAYMENT_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The AcceptedPayment element is expected to have exactly one sub-element";
    public static final String EXPECT_ADDRESS_LANGUAGES_TO_BE_UNIQUE =
            "Each Address element must have a different Language attribute";
    public static final String EXPECT_ADDRESS_LIST_TO_BE_NOT_EMPTY =
            "The list of Address elements is expected to contain at least one element";
    public static final String EXPECT_AFFILIATION_INFO_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.AFFILIATION_INFO);
    public static final String EXPECT_AFFILIATION_INFO_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.AFFILIATION_INFO);
    public static final String EXPECT_AMENITIES_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.AMENITIES);
    public static final String EXPECT_AVAIL_STATUS_MESSAGE_LIST_TO_BE_NOT_EMPTY =
            "The list of AvailStatusMessage elements is expected to contain at least one element";
    public static final String EXPECT_AVAIL_STATUS_MESSAGES_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.AVAIL_STATUS_MESSAGES);
    public static final String EXPECT_AVAILABILITY_INFORMATION_SUPPORT_FOR_DISTINCT_ROOMS =
            "Server doesn't handle availability information for distinct rooms ('OTA_HotelAvailNotif_accept_rooms' capability not set)";
    public static final String EXPECT_AVAILABILITY_INFORMATION_SUPPORT_FOR_ROOM_CATEGORIES =
            "Server doesn't support availability information for room categories ('OTA_HotelAvailNotif_accept_categories' capability not set)";
    public static final String EXPECT_AWARD_LIST_TO_BE_NOT_EMPTY =
            "The list of Award elements is expected to contain elements";
    public static final String EXPECT_AWARDS_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.AWARDS);
    public static final String EXPECT_BANK_ACCT_NAME_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BANK_ACCT_NAME);
    public static final String EXPECT_BANK_ACCT_NUMBER_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BANK_ACCT_NUMBER);
    public static final String EXPECT_BANK_ID_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BANK_ID);
    public static final String EXPECT_BOOKING_LIMIT_FOR_DISTINCT_ROOM_TO_BE_0_OR_1 =
            "A distinct room can not have a booking limit different to 0 or 1 (a room can be available or not)";
    public static final String EXPECT_BOOKING_LIMIT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BOOKING_LIMIT);
    public static final String EXPECT_BOOKING_LIMIT_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.BOOKING_LIMIT);
    public static final String EXPECT_BOOKING_LIMIT_MESSAGE_TYPE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BOOKING_LIMIT_MESSAGE_TYPE);
    public static final String EXPECT_BOOKING_LIMIT_MESSAGE_TYPE_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.BOOKING_LIMIT_MESSAGE_TYPE);
    public static final String EXPECT_BOOKING_THRESHOLD_TO_BE_LESSER_OR_EQUAL_THAN_BOOKING_LIMIT =
            DefaultErrorMessage.expectArg0LesserOrEqualThanArg1ErrorMessage(Names.BOOKING_THRESHOLD, Names.BOOKING_LIMIT);
    public static final String EXPECT_BOOKING_THRESHOLD_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.BOOKING_THRESHOLD);
    public static final String EXPECT_BOOKING_THRESHOLD_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.BOOKING_THRESHOLD);
    public static final String EXPECT_CANCEL_PENALTY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of CancelPenalty elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_CARD_CODE_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CARD_CODE);
    public static final String EXPECT_CASH_INDICATOR_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CASH_INDICATOR);
    public static final String EXPECT_CATEGORY_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CATEGORY);
    public static final String EXPECT_CATEGORY_TO_BE_VALID =
            "The category \"%s\" is not valid (valid values are: %s)";
    public static final String EXPECT_CHARGE_FREQUENCY_TO_HAVE_A_VALUE_OF_1 =
            "The ChargeFrequency attribute is expected to have a value of \"1\"";
    public static final String EXPECT_CHARGE_UNIT_TO_HAVE_A_VALUE_OF_21 =
            "The ChargeUnit attribute is expected to have a value of \"21\"";
    public static final String EXPECT_CHECKOUT_CHARGE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of CheckoutCharge elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_CLOSING_SEASON_TO_BE_ON_TOP_OF_LIST =
            "All closing-seasons Inventory elements must be on top of the Inventories list";
    public static final String EXPECT_CLOSING_SEASON_TO_HAVE_ALL_INV_CODE_SET_TO_TRUE =
            "A closing-seasons StatusApplicationControl element's AllInvCode must be set to true";
    public static final String EXPECT_CODE_DETAIL_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CODE_DETAIL);
    public static final String EXPECT_CODE_DETAIL_TO_MATCH_PATTERN =
            "The CodeDetail attribute is expected to be a string composed by two elements separated by colon," +
                    " where the first element contains a \"_\" character. Found \"%s\" which doesn't match that pattern." +
                    " Valid examples would be: \"PCT2017_20:4s\", \"ASTAT2020_11:4s\".";
    public static final String EXPECT_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CODE);
    public static final String EXPECT_CODE_TO_HAVE_A_VALUE_OF_3 =
            "The Code attribute is expected to have a value of \"3\"";
    public static final String EXPECT_COMPANY_NAME_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.COMPANY_NAME);
    public static final String EXPECT_CONTACT_INFO_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of ContactInfo elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_CONTACT_INFOS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.CONTACT_INFOS);
    public static final String EXPECT_CONTACT_INFOS_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CONTACT_INFOS);
    public static final String EXPECT_CONTEXT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.CONTEXT);
    public static final String EXPECT_CURRENCY_CODE_TO_BE_VALID =
            "The currency code '%s' is not defined in ISO-4217 and is therefore rejected";
    public static final String EXPECT_CURRENCY_CODE_TO_EXIST_IF_AMOUNT_EXISTS =
            "The CurrencyCode element must be present if there is an Amount element";
    public static final String EXPECT_CURRENCY_CODE_TO_EXIST_IF_NON_REFUNDABLE_FEE_EXISTS =
            "The CurrencyCode element must be present if there is a NonRefundableFee element";
    public static final String EXPECT_DEADLINE_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of Deadline elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_DESCRIPTION_LIST_TO_BE_NOT_EMPTY =
            "TextItem must contain one ore more Description elements";
    public static final String EXPECT_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of Description elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_DESCRIPTION_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.DESCRIPTION);
    public static final String EXPECT_DESCRIPTIONS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.DESCRIPTIONS);
    public static final String EXPECT_DESCRIPTION_TO_EXIST =
            "The list of Description elements is expected to contain at least one element";
    public static final String EXPECT_DISABILITY_FEATURE_CODE_TO_BE_DEFINED =
            "The code '%s' is not defined in the list of OTA Disability Feature Codes (PHY)";
    public static final String EXPECT_EMAIL_LIST_TO_BE_NOT_EMPTY =
            "The list of Email elements is expected to contain at least one element";
    public static final String EXPECT_EMAIL_TYPE_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.EMAIL_TYPE);
    public static final String EXPECT_END_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.END);
    public static final String EXPECT_END_TO_BE_TIME_FORMAT =
            "The End value is expected to be a time format e.g. 15:00:00";
    public static final String EXPECT_FACILITY_INFO_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.FACILITY_INFO);
    public static final String EXPECT_FACILITY_INFO_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.FACILITY_INFO);
    public static final String EXPECT_GUARANTEE_PAYMENT_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of GuaranteePayment elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_GUEST_ROOM_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.GUEST_ROOM);
    public static final String EXPECT_HOTEL_AMENITY_CODE_TO_BE_DEFINED =
            "The code '%s' is not defined in the list of OTA Hotel Amenity Type Codes (HAC)";
    public static final String EXPECT_HOTEL_AMENITY_CODE_TO_BE_DEFINED_2024_10 =
            "The code '%s' is not defined neither in the list of OTA Hotel Amenity Type Codes (RMA) nor in the list of " +
                    "AlpineBits Hotel Amenity Type Codes (ABR)";
    public static final String EXPECT_HOTEL_AVAIL_NOTIF_RQ_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OTA_HOTEL_AVAIL_NOTIF_RQ);
    public static final String EXPECT_HOTEL_CATEGORIES_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.HOTEL_CATEGORY);
    public static final String EXPECT_HOTEL_CATEGORY_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of HotelCategory elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_HOTEL_CODE_AND_HOTEL_NAME_TO_BE_NOT_BOTH_NULL =
            "Expecting one of HotelCode or HotelName to be not null, but both of them are null";
    public static final String EXPECT_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_RQ_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OTA_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_RQ);
    public static final String EXPECT_HOTEL_DESCRIPTIVE_CONTENT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.HOTEL_DESCRIPTIVE_CONTENT);
    public static final String EXPECT_HOTEL_DESCRIPTIVE_CONTENTS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.HOTEL_DESCRIPTIVE_CONTENTS);
    public static final String EXPECT_HOTEL_INFO_TO_HAVE_AT_LEAST_ONE_SUBELEMENT =
            String.format(
                    DefaultErrorMessage.EXPECT_AT_LEAST_ONE_SUB_ELEMENT,
                    Names.CATEGORY_CODES + ", " +
                            Names.DESCRIPTIONS + ", " +
                            Names.POSITION + ", " +
                            Names.SERVICES
            );
    public static final String EXPECT_HOTEL_INFO_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.HOTEL_INFO);
    public static final String EXPECT_HOTEL_INFO_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.HOTEL_INFO);
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_RQ_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OTA_HOTEL_INV_COUNT_NOTIF_RQ);
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CATEGORIES =
            "The server doesn't support room categories (OTA_HotelInvCountNotif_accept_categories capability not set)";
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_CLOSING_SEASONS =
            "The server doesn't support closing seasons (OTA_HotelInvCountNotif_accept_closing_seasons capability not set)";
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_DELTAS =
            "The server doesn't support delta updates (OTA_HotelInvCountNotif_accept_deltas capability not set)." +
                    " UniqueID element required.";
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_MARKET =
            "The server doesn't support out-of-market rooms (OTA_HotelInvCountNotif_accept_out_of_market capability not set)";
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_OUT_OF_ORDER =
            "The server doesn't support out-of-order rooms (OTA_HotelInvCountNotif_accept_out_of_order capability not set)";
    public static final String EXPECT_HOTEL_INV_COUNT_NOTIF_SUPPORT_FOR_ROOMS =
            "The server doesn't support distinct rooms (OTA_HotelInvCountNotif_accept_rooms capability not set)";
    public static final String EXPECT_ID_CONTEXT_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.ID_CONTEXT);
    public static final String EXPECT_ID_TO_BE_ALL_UPPERCASE =
            "The value for the ID attribute must be all uppercase";
    public static final String EXPECT_ID_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.ID);
    public static final String EXPECT_ID_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.ID);
    public static final String EXPECT_IMAGE_FORMAT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.IMAGE_FORMAT);
    public static final String EXPECT_IMAGE_ITEM_LIST_TO_BE_NOT_EMPTY =
            "The list of ImageItem elements is expected to contain at least one element";
    public static final String EXPECT_IMAGE_ITEM_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.IMAGE_ITEM);
    public static final String EXPECT_IMAGE_ITEMS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.IMAGE_ITEMS);
    public static final String EXPECT_IMAGE_ITEMS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.IMAGE_ITEMS);
    public static final String EXPECT_INFO_CODE_25_TO_BE_DEFINED =
            "Required MultimediaDescription element with InfoCode=25 was not found";
    public static final String EXPECT_INFO_CODE_TO_BE_DEFINED =
            "The code %s is not defined in the list of OTA GuestRoom Info Codes (GRI)";
    public static final String EXPECT_INFO_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.INFO_CODE);
    public static final String EXPECT_INFO_CODE_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.INFO_CODE);
    public static final String EXPECT_INFO_CODE_TO_BE_VALID =
            "The code \"%s\" is not valid (valid values are: %s)";
    public static final String EXPECT_INFORMATION_TYPE_CODE_TO_BE_DEFINED =
            "The code %s is not defined in the list of OTA Information Type Codes (INF)";
    public static final String EXPECT_INSTANCE_TO_BE_COMPLETE_SET =
            "Instance attribute must have the value 'CompleteSet'";
    public static final String EXPECT_INSTANCE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.INSTANCE);
    public static final String EXPECT_INVENTORIES_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.INVENTORIES);
    public static final String EXPECT_INVENTORIES_LIST_TO_BE_NOT_EMPTY =
            "The list of Inventories elements is expected to contain elements";
    public static final String EXPECT_COUNT_TYPE_TO_BE_ONE_OF_2_6_9 =
            "The CountType attribute is expected to have a value of \"2\", \"6\" or \"9\"";
    public static final String EXPECT_INV_COUNTS_TO_HAVE_BETWEEN_ONE_AND_THREE_ELEMENTS =
            "The list of InvCount elements is expected to have between one and three elements, but %s elements found";
    public static final String EXPECT_INV_COUNTS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.INV_COUNTS);
    public static final String EXPECT_INV_TYPE_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.INV_TYPE_CODE);
    public static final String EXPECT_LANGUAGE_ISO639_1_CODE_TO_BE_DEFINED =
            "The language code '%s' is not defined in ISO 639-1";
    public static final String EXPECT_LANGUAGE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.LANGUAGE);
    public static final String EXPECT_LOCATION_TO_HAVE_A_VALUE_OF_6 =
            "The Location attribute is expected to have a value of \"6\"";
    public static final String EXPECT_MAX_CHILD_OCCUPANCY_TO_BE_LESSER_OR_EQUAL_THAN_MAX_OCCUPANCY =
            DefaultErrorMessage.expectArg0LesserOrEqualThanArg1ErrorMessage(Names.MAX_CHILD_OCCUPANCY, Names.MAX_OCCUPANCY);
    public static final String EXPECT_MAX_CHILD_OCCUPANCY_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.MAX_CHILD_OCCUPANCY);
    public static final String EXPECT_MAX_OCCUPANCY_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.MAX_OCCUPANCY);
    public static final String EXPECT_MAX_OCCUPANCY_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.MAX_OCCUPANCY);
    public static final String EXPECT_MIN_OCCUPANCY_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.MIN_OCCUPANCY);
    public static final String EXPECT_MIN_OCCUPANCY_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.MIN_OCCUPANCY);
    public static final String EXPECT_MULTIMEDIA_DESCRIPTION_LIST_TO_BE_NOT_EMPTY =
            "The list of MultimediaDescription elements is expected to contain at least one element";
    public static final String EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.MULTIMEDIA_DESCRIPTIONS);
    public static final String EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.MULTIMEDIA_DESCRIPTIONS);
    public static final String EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_HAVE_AT_MOST_THREE_ELEMENTS
            = "At most 3 MultimediaDescription elements are allowed per GuestRoom";
    public static final String EXPECT_MULTIMEDIA_DESCRIPTIONS_TO_HAVE_AT_MOST_X_ELEMENTS
            = "At most %s MultimediaDescription elements are allowed";
    public static final String EXPECT_NO_DUPLICATE_CATEGORY
            = "More than one elements with Category '%s' found";
    public static final String EXPECT_NO_DUPLICATE_COUNT_TYPE
            = "More than one InvCount element with CountType '%s' found";
    public static final String EXPECT_NO_DUPLICATE_INFO_CODE
            = "More than one MultimediaDescription elements with InfoCode '%s' found";
    public static final String EXPECT_NO_DUPLICATE_LANGUAGE_AND_TEXT_FORMAT =
            "The combination Language '%s' and TextFormat '%s' appears more than once in the Description elements";
    public static final String EXPECT_NO_OVERLAPPING_TIME_PERIODS =
            "The closing season time periods '%s' and '%s' overlap";
    public static final String EXPECT_PENALTY_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of PenaltyDescription elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_OFFSET_DROP_TIME_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OFFSET_DROP_TIME);
    public static final String EXPECT_OFFSET_TIME_UNIT_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OFFSET_TIME_UNIT);
    public static final String EXPECT_OFFSET_UNIT_MULTIPLIER_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.OFFSET_UNIT_MULTIPLIER);
    public static final String EXPECT_PERCENT_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PERCENT);
    public static final String EXPECT_PETS_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of PetsPolicies elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_PHONE_LIST_TO_BE_NOT_EMPTY =
            "The list of Phone elements is expected to contain at least one element";
    public static final String EXPECT_PHONE_NUMBER_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PHONE_NUMBER);
    public static final String EXPECT_PHONE_TECH_TYPE_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PHONE_TECH_TYPE);
    public static final String EXPECT_PICTURE_CATEGORY_CODE_TO_BE_DEFINED =
            "The code '%s' is not defined in the list of OTA Picture Category Codes (PIC)";
    public static final String EXPECT_PLAIN_TEXT_TO_EXIST =
            "The Language '%s' provides a single TextFormat of 'HTML', which is not allowed. A TextFormat of 'HTML'" +
                    " for a given language makes the use of an additional TextFormat of 'PlainText' mandatory" +
                    " for that language";
    public static final String EXPECT_PLAIN_TEXT_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PLAIN_TEXT);
    public static final String EXPECT_POLICIES_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.POLICIES);
    public static final String EXPECT_POLICIES_TO_NOT_BE_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.POLICIES);
    public static final String EXPECT_POLICY_LIST_TO_BE_NOT_EMPTY =
            "The list of Policy elements is expected to contain at least one element";
    public static final String EXPECT_POLICY_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The Policy element is expected to have exactly one sub-element";
    public static final String EXPECT_POSITION_WITH_AT_LEAST_ONE_COUPLE =
            "At least one of the following attribute couples is mandatory: [Latitude + Longitude] or [Altitude + AltitudeUnitOfMeasureCode]";
    public static final String EXPECT_PROVIDER_TO_BE_ALL_UPPERCASE =
            "The value for the Provider attribute must be all uppercase";
    public static final String EXPECT_PROVIDER_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PROVIDER);
    public static final String EXPECT_PROXIMITY_CODE_TO_BE_DEFINED =
            "The code '%s' is not defined in the list of OTA Proximity Type Codes (PRX)";
    public static final String EXPECT_PROXIMITY_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.PROXIMITY_CODE);
    public static final String EXPECT_RATING_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.RATING);
    public static final String EXPECT_REQUIRED_MULTIMEDIA_DESCRIPTION_TO_EXIST
            = "A MultimediaDescription element with InfoCode=25 is required";
    public static final String EXPECT_ROOM_AMENITY_CODE_TO_BE_DEFINED =
            "The code '%s' is not defined in the list of OTA Room Amenity Type Codes (RMA)";
    public static final String EXPECT_ROOM_AMENITY_CODE_TO_BE_DEFINED_2024_10 =
            "The code '%s' is not defined neither in the list of OTA Room Amenity Type Codes (RMA) nor in the list of AlpineBits Room Amenity Type Codes (ABR)";
    public static final String EXPECT_ROOM_AMENITY_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.ROOM_AMENITY_CODE);
    public static final String EXPECT_ROOM_CATEGORY_AND_DISTINCT_ROOM_TO_NOT_BE_MIXED =
            "It is not allowed to mix availability information for room categories and distinct rooms";
    public static final String EXPECT_ROOM_CLASSIFICATION_CODE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.ROOM_CLASSIFICATION_CODE);
    public static final String EXPECT_ROOM_CLASSIFICATION_CODE_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.ROOM_CLASSIFICATION_CODE);
    public static final String EXPECT_ROOM_ID_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.ROOM_ID);
    public static final String EXPECT_ROOM_TYPE_TO_BE_KNOWN = "RoomType has unsupported value \"%s\"";
    public static final String EXPECT_ROOM_TYPE_TO_HAVE_CORRECT_ROOM_CLASSIFICATION_CODE =
            "The RoomType \"%s\" is expected to to have a RoomClassificationCode of \"%s\", but found \"%s\" instead";
    public static final String EXPECT_SERVICE_LIST_TO_BE_NOT_EMPTY =
            "The list of Service elements is expected to contain at least one element";
    public static final String EXPECT_SIZE_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.SIZE);
    public static final String EXPECT_STANDARD_OCCUPANCY_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.STANDARD_OCCUPANCY);
    public static final String EXPECT_STANDARD_OCCUPANCY_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.STANDARD_OCCUPANCY);
    public static final String EXPECT_START_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.START);
    public static final String EXPECT_START_TO_BE_TIME_FORMAT =
            "The Start value is expected to be a time format e.g. 15:00:00";
    public static final String EXPECT_STATUS_APPLICATION_CONTROL_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.STATUS_APPLICATION_CONTROL);
    public static final String EXPECT_STATUS_APPLICATION_CONTROL_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.STATUS_APPLICATION_CONTROL);
    public static final String EXPECT_STAY_CONTEXT_TO_BE_CHECKIN_OR_CHECKOUT =
            "The StayContext value must be either \"Checkin\" or \"Checkout\"";
    public static final String EXPECT_STAY_REQUIREMENT_LIST_TO_HAVE_ONE_OR_TWO_ELEMENTS =
            "The list of StayRequirement elements is expected to contain one or two elements (%s found)";
    public static final String EXPECT_TAX_DESCRIPTION_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of TaxDescription elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_TAX_POLICIES_LIST_TO_HAVE_EXACTLY_ONE_ELEMENT =
            "The list of TaxPolicies elements is expected to contain exactly one element (%s found)";
    public static final String EXPECT_TEXT_FORMAT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.TEXT_FORMAT);
    public static final String EXPECT_TEXT_FORMAT_TO_BE_PLAINTEXT_OR_HTML =
            "TextFormat element expected to be 'PlainText' or 'HTML', but '%s' was found";
    public static final String EXPECT_TEXT_ITEM_LIST_TO_BE_NOT_EMPTY =
            "The list of TextItem elements is expected to contain at least one element";
    public static final String EXPECT_TEXT_ITEM_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.TEXT_ITEM);
    public static final String EXPECT_TEXT_ITEMS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.TEXT_ITEMS);
    public static final String EXPECT_TEXT_ITEMS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.TEXT_ITEMS);
    public static final String EXPECT_TEXT_LIST_TO_BE_NOT_EMPTY =
            "The list of Text elements is expected to contain at least one element";
    public static final String EXPECT_TPA_EXTENSIONS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.TPA_EXTENSIONS);
    public static final String EXPECT_TYPE_ROOM_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.TYPE_ROOM);
    public static final String EXPECT_TYPE_ROOM_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.TYPE_ROOM);
    public static final String EXPECT_TYPE_TO_BE_16_OR_35 =
            "Type attribute expected to have a value of 16 or 35";
    public static final String EXPECT_TYPE_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.TYPE);
    public static final String EXPECT_UNIQUE_ID_TO_BE_NOT_NULL =
            "The server doesn't support delta updates (OTA_HotelAvailNotif_accept_deltas capability)." +
                    " UniqueID element required.";
    public static final String EXPECT_URL_LIST_TO_BE_NOT_EMPTY =
            "The list of URL elements is expected to contain at least one element";
    public static final String EXPECT_URL_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.URL);
    public static final String EXPECT_URL_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.URL);
    public static final String EXPECT_VIDEO_FORMAT_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.VIDEO_FORMAT);
    public static final String EXPECT_VIDEO_ITEM_LIST_TO_BE_NOT_EMPTY =
            "The list of VideoItem elements is expected to contain at least one element";
    public static final String EXPECT_VIDEO_ITEM_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.VIDEO_ITEM);
    public static final String EXPECT_VIDEO_ITEMS_TO_BE_NOT_NULL =
            DefaultErrorMessage.expectNotNullErrorMessage(Names.VIDEO_ITEMS);
    public static final String EXPECT_VIDEO_ITEMS_TO_BE_NULL =
            DefaultErrorMessage.expectNullErrorMessage(Names.VIDEO_ITEMS);
    public static final String EXPECT_ZERO_TO_BE_LESSER_OR_EQUAL_THAN_BOOKING_THRESHOLD =
            DefaultErrorMessage.expectArg0LesserOrEqualThanArg1ErrorMessage(Names.ZERO, Names.BOOKING_THRESHOLD);
    public static final String EXPECT_ZERO_TO_BE_LESSER_OR_EQUAL_THAN_MAX_CHILD_OCCUPANCY =
            DefaultErrorMessage.expectArg0LesserOrEqualThanArg1ErrorMessage(Names.ZERO, Names.MAX_CHILD_OCCUPANCY);


    private ErrorMessage() {
        // Empty
    }
}
