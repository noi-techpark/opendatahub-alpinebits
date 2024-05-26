// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.routing.constants;


import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsAction;
import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsCapability;

import java.util.Objects;

/**
 * This class provides routing constants for all known AlpineBits actions used as
 * action name in the routing configuration.
 * <p>
 * Unfortunately, the term "action" has many meanings in AlpineBits. In the
 * context of this class, an action corresponds to the action name in the routing
 * configuration.
 */
public final class Action {

    // Handshaking

    /**
     * Handshaking action (support for this action is mandatory since AlpineBits 2018-10). This action was introduced with AlpineBits 2018-10.
     */
    public static final Action HANDSHAKING = of(
            AlpineBitsAction.HANDSHAKING,
            AlpineBitsCapability.HANDSHAKING
    );

    // Housekeeping

    /**
     * The server implements​ the getVersion​​ action. This action is mandatory for all AlpineBits versions prior to AlpineBits 2018-10.
     */
    public static final Action GET_VERSION = of(
            AlpineBitsAction.GET_VERSION,
            AlpineBitsCapability.GET_VERSION
    );

    /**
     * The server implements​ the getCapabilities action. This action is mandatory for all AlpineBits versions prior to AlpineBits 2018-10.
     */
    public static final Action GET_CAPABILITIES = of(
            AlpineBitsAction.GET_CAPABILITIES,
            AlpineBitsCapability.GET_CAPABILITIES
    );

    // FreeRooms up to AlpineBits 2018-10

    /**
     * The actor implements handling room availability notifications (FreeRooms up to AlpineBits 2018-10).
     */
    public static final Action FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF
    );

    /**
     * For room availability notifications (FreeRooms), the actor accepts booking limits for specific rooms (FreeRooms up to AlpineBits 2018-10).
     */
    public static final Action FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_ROOMS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_ROOMS
    );

    /**
     * For room availability notifications (FreeRooms), the actor accepts booking limits for categories of rooms (FreeRooms up to AlpineBits 2018-10).
     */
    public static final Action FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_CATEGORIES = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_CATEGORIES
    );

    /**
     * For room availability notifications (FreeRooms), the actor accepts partial information (deltas) (FreeRooms up to AlpineBits 2018-10).
     */
    public static final Action FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_DELTAS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_DELTAS
    );

    /**
     * For room availability notifications (FreeRooms), the actor accepts the number of rooms that are considered free but not bookable
     * (FreeRooms up to AlpineBits 2018-10).
     */
    public static final Action FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_BOOKING_THRESHOLD = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_AVAIL_NOTIF_ACCEPT_BOOKING_THRESHOLD
    );

    // FreeRooms from AlpineBits 2020-10 going on

    /**
     * The actor implements handling room availability notifications (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles availabilities for specific rooms (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_ROOMS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_ROOMS
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles availabilities for room categories (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_CATEGORIES = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CATEGORIES
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles partial information (deltas) (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_DELTAS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_DELTAS
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles the number of rooms that are considered out of order
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_OUT_OF_ORDER = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_ORDER
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles the number of rooms that are considered free but not bookable
     * (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_OUT_OF_MARKET = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_OUT_OF_MARKET
    );

    /**
     * For room availability notifications (FreeRooms), the actor handles closing seasons (FreeRooms from AlpineBits 2020-10 going on).
     */
    public static final Action FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS_ACCEPT_CLOSING_SEASONS = of(
            AlpineBitsAction.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS,
            AlpineBitsCapability.FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_ACCEPT_CLOSING_SEASONS
    );

    // GuestRequests

    /**
     * The actor implements handling quote requests, booking reservations and cancellations (GuestRequests Pull).
     */
    public static final Action GUEST_REQUESTS_READ_GUEST_REQUESTS = of(
            AlpineBitsAction.GUEST_REQUESTS_READ_GUEST_REQUESTS,
            AlpineBitsCapability.GUEST_REQUESTS_READ
    );

    /**
     * For pulling GuestRequests, the actor accepts daily rates via RoomRates.
     */
    public static final Action GUEST_REQUESTS_READ_ACCEPT_ROOM_RATES = of(
            AlpineBitsAction.GUEST_REQUESTS_NOTIF_REPORT_GUEST_REQUESTS,
            AlpineBitsCapability.GUEST_REQUESTS_READ_ACCEPT_ROOM_RATES
    );

    /**
     * The actor implements pushing quote requests, booking reservations and cancellations (GuestRequests Push).
     */
    public static final Action GUEST_REQUESTS_WRITE_GUEST_REQUESTS = of(
            AlpineBitsAction.GUEST_REQUESTS_WRITE_GUEST_REQUESTS,
            AlpineBitsCapability.GUEST_REQUESTS_WRITE
    );

    /**
     * For pushing GuestRequests, the actor accepts daily rates via RoomRates.
     */
    public static final Action GUEST_REQUESTS_WRITE_ACCEPT_ROOM_RATES = of(
            AlpineBitsAction.GUEST_REQUESTS_WRITE_GUEST_REQUESTS,
            AlpineBitsCapability.GUEST_REQUESTS_WRITE_ACCEPT_ROOM_RATES
    );

    /**
     * The actor implements pushing quote requests, booking reservations and cancellations (GuestRequests Push).
     */
    public static final Action GUEST_REQUESTS_WRITE_STATUS_UPDATE = of(
            AlpineBitsAction.GUEST_REQUESTS_WRITE_STATUS_UPDATE,
            AlpineBitsCapability.GUEST_REQUESTS_WRITE_STATUS_UPDATE
    );

    // Inventory

    /**
     * The actor implements handling Inventory/Basic (push).
     */
    public static final Action INVENTORY_BASIC_PUSH = of(
            AlpineBitsAction.INVENTORY_BASIC_PUSH,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY
    );

    /**
     * For room category information (Inventory), the actor needs information about specific rooms.
     */
    public static final Action INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY_USE_ROOMS = of(
            AlpineBitsAction.INVENTORY_BASIC_PUSH,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INVENTORY_USE_ROOMS
    );

    /**
     * For room category information (Inventory), the actor supports applying children rebates also for children below the standard occupation.
     */
    public static final Action INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_OCCUPANCY_CHILDREN = of(
            AlpineBitsAction.INVENTORY_BASIC_PUSH,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_OCCUPANCY_CHILDREN
    );

    /**
     * The actor implements handling Inventory/Basic (pull).
     */
    public static final Action INVENTORY_BASIC_PULL = of(
            AlpineBitsAction.INVENTORY_BASIC_PULL,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_INFO_INVENTORY
    );

    /**
     * The actor implements handling Inventory/HotelInfo (push).
     */
    public static final Action INVENTORY_HOTEL_INFO_PUSH = of(
            AlpineBitsAction.INVENTORY_HOTEL_INFO_PUSH,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_CONTENT_NOTIF_INFO
    );

    /**
     * The actor implements handling Inventory/HotelInfo (pull).
     */
    public static final Action INVENTORY_HOTEL_INFO_PULL = of(
            AlpineBitsAction.INVENTORY_HOTEL_INFO_PULL,
            AlpineBitsCapability.INVENTORY_HOTEL_DESCRIPTIVE_INFO_INFO
    );

    // RatePlans

    /**
     * The actor implements handling prices (RatePlans).
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS
    );

    /**
     * For prices (RatePlans), the actor accepts arrival DOW restrictions in booking rules.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_ARRIVAL_DOW = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_ARRIVAL_DOW
    );

    /**
     * For prices (RatePlans), the actor accepts departure DOW restrictions in booking rules.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_DEPARTURE_DOW = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_DEPARTURE_DOW
    );

    /**
     * For prices (RatePlans), the actor accepts "generic" booking rules.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_BOOKING_RULE = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_BOOKING_RULE
    );

    /**
     * For prices (RatePlans), the actor accepts "specific" booking rules for the given room types.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_ROOM_TYPE_BOOKING_RULE = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_ROOM_TYPE_BOOKING_RULE
    );

    /**
     * For prices (RatePlans) and within the same rate plan, the actor accepts both "specific" and "generic" booking rules. Both "generic" and
     * "specific" rules capabilities must still be announced by the actor.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_MIXED_BOOKING_RULE = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_MIXED_BOOKING_RULE
    );

    /**
     * For prices (RatePlans), the actor accepts supplements.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_SUPPLEMENTS = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_SUPPLEMENTS
    );

    /**
     * For prices (RatePlans), the actor accepts free nights offers.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FREE_NIGHTS_OFFER = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FREE_NIGHTS_OFFER
    );

    /**
     * For prices (RatePlans), the actor accepts family offers.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FAMILY_OFFERS = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_FAMILY_OFFERS
    );

    /**
     * For prices (RatePlans), the actor accepts the rate plan notif type value Overlay.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OVERLAY = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OVERLAY
    );

    /**
     * For prices (RatePlans), the actor supports grouping RatePlans with different MealPlanCodes under a single price list.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_JOIN = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_RATE_PLAN_JOIN
    );

    /**
     * For prices (RatePlans), the actor accepts the OfferRule restrictions MinAdvancedBookingOffset and MaxAdvancedBookingOffset.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_BOOKING_OFFSET = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_BOOKING_OFFSET
    );

    /**
     * For prices (RatePlans), the actor accepts the OfferRule restrictions ArrivalDaysOfWeek, DepartureDaysOfWeek, SetMinLOS and SetMaxLOS.
     */
    public static final Action RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_DOWLOS = of(
            AlpineBitsAction.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS,
            AlpineBitsCapability.RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_ACCEPT_OFFER_RULE_DOWLOS
    );

    // BaseRates

    /**
     * The actor implements handling BaseRates.
     */
    public static final Action BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES = of(
            AlpineBitsAction.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES,
            AlpineBitsCapability.BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES
    );

    // Activities

    /**
     * The actor implements handling hotel internal activities.
     */
    public static final Action ACTIVITIES_HOTEL_POST_EVENT_NOTIF_EVENT_REPORTS = of(
            AlpineBitsAction.ACTIVITIES_HOTEL_POST_EVENT_NOTIF_EVENT_REPORTS,
            AlpineBitsCapability.ACTIVITIES_HOTEL_POST_EVENT_NOTIF_EVENT_REPORTS
    );

    private String requestParameter;
    private String name;

    private Action() {
        // Empty
    }

    public static Action of(String requestParameter, String name) {
        Action result = new Action();
        result.requestParameter = requestParameter;
        result.name = name;
        return result;
    }

    public String getRequestParameter() {
        return requestParameter;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Action action = (Action) o;
        return Objects.equals(requestParameter, action.requestParameter) &&
                Objects.equals(name, action.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestParameter, name);
    }

    @Override
    public String toString() {
        return "Action{" +
                "requestParameter=" + requestParameter +
                ", name=" + name +
                '}';
    }

}
