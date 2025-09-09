// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.common.constants;

/**
 * This class provides constants for all known AlpineBits capabilities for version 2022-10.
 *
 * In the context of this class, "capability" means the values exchanged as capabilities during
 * the Housekeeping -&gt; getCapabilities and Handshaking requests. They express, what the
 * client and server are capable of.
 */
public final class AlpineBitsCapability {

    // Handshaking

    /**
     * Handshaking action (support for this action is mandatory since AlpineBits 2018-10).
     * <p>
     * This action was introduced with AlpineBits 2018-10.
     */
    public static final String HANDSHAKING = "action_OTA_Ping";

    // Housekeeping

    /**
     * The server implements the getVersion action.
     * <p>
     * This action is mandatory for all AlpineBits versions prior to AlpineBits 2018-10.
     */
    public static final String GET_VERSION = "action_getVersion";

    /**
     * The server implements the getCapabilities action.
     * <p>
     * This action is mandatory for all AlpineBits versions prior to AlpineBits 2018-10.
     */
    public static final String GET_CAPABILITIES = "action_getCapabilities";

    // FreeRooms up to AlpineBits 2018-10

    /**
     * The actor implements handling room availability notifications (FreeRooms up to AlpineBits 2018-10).
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF = "action_OTA_HotelAvailNotif";

    /**
     * For room availability notifications (FreeRooms), the actor accepts booking limits for specific rooms
     * (FreeRooms up to AlpineBits 2018-10).
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_ROOMS = "OTA_HotelAvailNotif_accept_rooms";

    /**
     * For room availability notifications (FreeRooms), the actor accepts booking limits for categories of
     * rooms (FreeRooms up to AlpineBits 2018-10).
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_CATEGORIES = "OTA_HotelAvailNotif_accept_categories";

    /**
     * For room availability notifications (FreeRooms), the actor accepts partial information (deltas)
     * (FreeRooms up to AlpineBits 2018-10).
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_DELTAS = "OTA_HotelAvailNotif_accept_deltas";

    /**
     * For room availability notifications (FreeRooms), the actor accepts the number of rooms that are
     * considered free but not bookable (FreeRooms up to AlpineBits 2018-10).
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_BOOKING_THRESHOLD = "OTA_HotelAvailNotif_accept_BookingThreshold";

    // FreeRooms from AlpineBits 2020-10 going on

    /**
     * The actor implements handling room availability notifications (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF = "action_OTA_HotelInvCountNotif";

    /**
     * For room availability notifications (FreeRooms), the actor handles availabilities for specific rooms
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_ROOMS = "OTA_HotelInvCountNotif_accept_rooms";

    /**
     * For room availability notifications (FreeRooms), the actor handles availabilities for room
     * categories (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CATEGORIES = "OTA_HotelInvCountNotif_accept_categories";

    /**
     * For room availability notifications (FreeRooms), the actor handles partial information (deltas)
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_DELTAS = "OTA_HotelInvCountNotif_accept_deltas";

    /**
     * For room availability notifications (FreeRooms), the actor handles the number of rooms that are
     * considered out of order (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_ORDER = "OTA_HotelInvCountNotif_accept_out_of_order";

    /**
     * For room availability notifications (FreeRooms), the actor handles the number of rooms that are
     * considered free but not bookable (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_MARKET = "OTA_HotelInvCountNotif_accept_out_of_market";

    /**
     * For room availability notifications (FreeRooms), the actor handles closing seasons
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CLOSING_SEASONS = "OTA_HotelInvCountNotif_accept_closing_seasons";

    /**
     * For room availability notifications (FreeRooms), the actor handles complete set
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_COMPLETE_SET = "OTA_HotelInvCountNotif_accept_complete_set";

    // GuestRequests

    /**
     * The actor implements handling quote requests, booking reservations and cancellations
     * (GuestRequests Pull).
     */
    public static final String GUEST_REQUESTS_READ = "action_OTA_Read";

    /**
     * For pulling GuestRequests, the actor accepts daily rates via RoomRates.
     */
    public static final String GUEST_REQUESTS_READ_ACCEPT_ROOM_RATES = "OTA_Read_accept_RoomRates";

    /**
     * The actor implements pushing quote requests, booking reservations and cancellations
     * (GuestRequests Push).
     */
    public static final String GUEST_REQUESTS_WRITE = "action_OTA_HotelResNotif_GuestRequests";

    /**
     * For pushing GuestRequests, the actor accepts daily rates via RoomRates.
     */
    public static final String GUEST_REQUESTS_WRITE_STATUS_UPDATE = "action_OTA_HotelResNotif_GuestRequests";

    /**
     * The actor implements the status update for quote requests (GuestRequests Push status update).
     */
    public static final String GUEST_REQUESTS_WRITE_ACCEPT_ROOM_RATES = "OTA_HotelResNotif_GuestRequests_accept_RoomRates";

    // Inventory

    /**
     * The actor implements handling Inventory/Basic (push).
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY =
            "action_OTA_HotelDescriptiveContentNotif_Inventory";

    /**
     * For room category information (Inventory), the actor needs information about specific rooms.
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY_USE_ROOMS =
            "OTA_HotelDescriptiveContentNotif_Inventory_use_rooms";

    /**
     * For room category information (Inventory), the actor supports applying children rebates also
     * for children below the standard occupation.
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_OCCUPANCY_CHILDREN =
            "OTA_HotelDescriptiveContentNotif_Inventory_occupancy_children";

    /**
     * The actor implements handling Inventory/HotelInfo (push).
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INFO =
            "action_OTA_HotelDescriptiveContentNotif_Info";

    /**
     * The actor implements handling Inventory/Basic (pull).
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_INFO_INVENTORY =
            "action_OTA_HotelDescriptiveInfo_Inventory";

    /**
     * The actor implements handling Inventory/HotelInfo (pull).
     */
    public static final String INVENTORY_HOTEL_DESCRIPTIVE_INFO_INFO =
            "action_OTA_HotelDescriptiveInfo_Info";

    // RatePlans

    /**
     * The actor implements handling prices (RatePlans).
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS =
            "action_OTA_HotelRatePlanNotif_RatePlans";

    /**
     * For prices (RatePlans), the actor accepts arrival DOW restrictions in booking rules.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_ARRIVAL_DOW =
            "OTA_HotelRatePlanNotif_accept_ArrivalDOW";

    /**
     * For prices (RatePlans), the actor accepts departure DOW restrictions in booking rules.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_DEPARTURE_DOW =
            "OTA_HotelRatePlanNotif_accept_DepartureDOW";

    /**
     * For prices (RatePlans), the actor accepts "generic" booking rules.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_BOOKING_RULE =
            "OTA_HotelRatePlanNotif_accept_RatePlan_BookingRule";

    /**
     * For prices (RatePlans), the actor accepts "specific" booking rules for the given room types.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_ROOM_TYPE_BOOKING_RULE =
            "OTA_HotelRatePlanNotif_accept_RatePlan_RoomType_BookingRule";

    /**
     * For prices (RatePlans) and within the same rate plan, the actor accepts both "specific" and
     * "generic" booking rules. Both "generic" and "specific" rules capabilities must still be announced
     * by the actor.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_MIXED_BOOKING_RULE =
            "OTA_HotelRatePlanNotif_accept_RatePlan_mixed_BookingRule";

    /**
     * For prices (RatePlans), the actor accepts supplements.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_SUPPLEMENTS =
            "OTA_HotelRatePlanNotif_accept_Supplements";

    /**
     * For prices (RatePlans), the actor accepts free nights offers.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FREE_NIGHTS_OFFER =
            "OTA_HotelRatePlanNotif_accept_FreeNightsOffers";

    /**
     * For prices (RatePlans), the actor accepts family offers.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FAMILY_OFFERS =
            "OTA_HotelRatePlanNotif_accept_FamilyOffers";

    /**
     * For prices (RatePlans), the actor accepts the rate plan notif type value Overlay.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OVERLAY =
            "OTA_HotelRatePlanNotif_accept_overlay";

    /**
     * For prices (RatePlans), the actor supports grouping RatePlans with different MealPlanCodes
     * under a single price list.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_JOIN =
            "OTA_HotelRatePlanNotif_accept_RatePlanJoin";

    /**
     * For prices (RatePlans), the actor accepts the OfferRule restrictions MinAdvancedBookingOffset
     * and MaxAdvancedBookingOffset.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_BOOKING_OFFSET =
            "OTA_HotelRatePlanNotif_accept_OfferRule_BookingOffset";

    /**
     * For prices (RatePlans), the actor accepts the OfferRule restrictions ArrivalDaysOfWeek,
     * DepartureDaysOfWeek, SetMinLOS and SetMaxLOS.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_DOWLOS =
            "OTA_HotelRatePlanNotif_accept_OfferRule_DOWLOS";

    // BaseRates

    /**
     * The actor implements handling BaseRates.
     */
    public static final String BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES = "action_OTA_HotelRatePlan_BaseRates";

    /**
     * The actor supports delta information with BaseRates.
     */
    public static final String BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES_DELTAS = "OTA_HotelRatePlan_BaseRates_deltas";

    // Activities

    /**
     * The actor implements handling hotel internal activities.
     */
    public static final String ACTIVITIES_HOTEL_POST_EVENT_NOTIF_EVENT_REPORTS = "action_OTA_HotelPostEventNotif_EventReports";

    private AlpineBitsCapability() {
        // Empty
    }
}
