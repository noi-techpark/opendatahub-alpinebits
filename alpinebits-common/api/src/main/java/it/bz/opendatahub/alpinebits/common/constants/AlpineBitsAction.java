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
 * This class provides constants for all known AlpineBits actions for version 2022-10.
 * <p>
 * In the context of this class, "action" means the value of the request parameter called "action".
 */
public final class AlpineBitsAction {

    // Handshaking

    /**
     * Value for "Handshaking" action request parameter.
     */
    public static final String HANDSHAKING = "OTA_Ping:Handshaking";

    // Housekeeping

    /**
     * Value for "Housekeeping -&gt; get version" action request parameter.
     */
    public static final String GET_VERSION = "getVersion";

    /**
     * Value for "Housekeeping -&gt; get capabilities" action request parameter.
     */
    public static final String GET_CAPABILITIES = "getCapabilities";

    // FreeRooms

    /**
     * Value for "FreeRooms" action request parameter up to AlpineBits 2018-10.
     * <p>
     * A client sendsroom availabilitynotifications to a server.
     */
    public static final String FREE_ROOMS_HOTEL_AVAIL_NOTIF_FREE_ROOMS = "OTA_HotelAvailNotif:FreeRooms";

    /**
     * Value for "FreeRooms" action request parameter from AlpineBits 2020-10 going on.
     * <p>
     * A client sends room availability notifications to a server.
     */
    public static final String FREE_ROOMS_HOTEL_INV_COUNT_NOTIF_FREE_ROOMS = "OTA_HotelInvCountNotif:FreeRooms";

    // GuestRequests

    /**
     * Value for "GuestRequests -&gt; read GuestRequests" action request parameter.
     * <p>
     * A client sends a request to receive requests for a quote or booking requests from the server.
     */
    public static final String GUEST_REQUESTS_READ_GUEST_REQUESTS = "OTA_Read:GuestRequests";

    /**
     * Value for "GuestRequests -&gt; write GuestRequests" action request parameter.
     * <p>
     * A client sends requests for a quote or booking requests to the server.
     */
    public static final String GUEST_REQUESTS_WRITE_GUEST_REQUESTS = "OTA_HotelResNotif:GuestRequests";

    /**
     * Value for "GuestRequests -&gt; confirm read GuestRequests" action request parameter.
     * <p>
     * A client acknowledges the requests it has received.
     */
    public static final String GUEST_REQUESTS_NOTIF_REPORT_GUEST_REQUESTS = "OTA_NotifReport:GuestRequests";

    /**
     * Value for "GuestRequests -&gt; write status update for GuestRequests" action request parameter.
     * <p>
     * A a client sends status updates for a quote or booking requests to the server.
     */
    public static final String GUEST_REQUESTS_WRITE_STATUS_UPDATE = "OTA_HotelResNotif:GuestRequests_StatusUpdate";

    // Inventory

    /**
     * Value for "Inventory/Basic push" action request parameter.
     * <p>
     * A client sends room category information and room lists.
     */
    public static final String INVENTORY_BASIC_PUSH = "OTA_HotelDescriptiveContentNotif:Inventory";

    /**
     * Value for "Inventory/Basic pull" action request parameter.
     * <p>
     * A client requests room category information and room lists.
     */
    public static final String INVENTORY_BASIC_PULL = "OTA_HotelDescriptiveInfo:Inventory";

    /**
     * Value for "Inventory/HotelInfo push" action request parameter.
     * <p>
     * A client sends additional descriptive content regarding room categories.
     */
    public static final String INVENTORY_HOTEL_INFO_PUSH = "OTA_HotelDescriptiveContentNotif:Info";

    /**
     * Value for "Inventory/HotelInfo pull" action request parameter.
     * <p>
     * A client requests additional descriptive content regarding room categories.
     */
    public static final String INVENTORY_HOTEL_INFO_PULL = "OTA_HotelDescriptiveInfo:Info";

    // RatePlans

    /**
     * Value for "RatePlans" action request parameter.
     * <p>
     * A client sends information about rate plans with prices and booking rules.
     */
    public static final String RATE_PLANS_HOTEL_RATE_PLAN_NOTIF_RATE_PLANS = "OTA_HotelRatePlanNotif:RatePlans";

    // BaseRates

    /**
     * Value for "BaseRates" action request parameter.
     * <p>
     * A client requests information about rate plans.
     */
    public static final String BASE_RATES_HOTEL_RATE_PLAN_BASE_RATES = "OTA_HotelRatePlan:BaseRates";

    // Activities

    /**
     * Value for "Activities" action request parameter.
     * <p>
     * A client intends to send information about hotel internal activities made available to their guests.
     */
    public static final String ACTIVITIES_HOTEL_POST_EVENT_NOTIF_EVENT_REPORTS = "OTA_HotelPostEventNotif:EventReports";

    private AlpineBitsAction() {
        // Empty
    }

}
