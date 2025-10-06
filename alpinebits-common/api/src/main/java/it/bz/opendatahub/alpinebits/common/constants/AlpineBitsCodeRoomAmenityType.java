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
 * Here you can find an enumeration of all AlpineBits Room Amenity Type codes (ABR)
 * as defined since AlpineBits version 2024-10.
 */
public enum AlpineBitsCodeRoomAmenityType {
    _2_OR_MORE_BATHROOMS("1.ABR"),
    _2_OR_MORE_BEDROOMS("2.ABR"),
    ACCESSIBLE_ROOMS("3.ABR"),
    ADDITIONAL_BED("4.ABR"),
    AIR_CONDITIONING("5.ABR"),
    BABY_COT("6.ABR"),
    BALCONY("7.ABR"),
    BARRIER_FREE("8.ABR"),
    BATH_TOWELS("9.ABR"),
    BATHROBE("10.ABR"),
    BATHROOM_AMENITIES("11.ABR"),
    BATHTUB("12.ABR"),
    BED_LINEN("13.ABR"),
    BIDET("14.ABR"),
    BUNK_BED("15.ABR"),
    CLEANING_UPON_REQUEST("16.ABR"),
    COFFEE_MACHINE("17.ABR"),
    CROCKERY("18.ABR"),
    DAILY_CLEANING_INCLUDED("19.ABR"),
    DEPENDANCE("20.ABR"),
    DESK("21.ABR"),
    DISHWASHER("22.ABR"),
    DORMITORY("23.ABR"),
    DOUBLE_BED("24.ABR"),
    DOUBLE_SOFA_BED("25.ABR"),
    EAT_IN_KITCHEN("26.ABR"),
    ELECTRIC_STOVE("27.ABR"),
    ELECTRICAL_ADAPTORS_AVAILABLE("28.ABR"),
    FINAL_CLEANING_INCLUDED("29.ABR"),
    FREE_WIFI("30.ABR"),
    FULLY_EQUIPPED_EAT_IN_KITCHEN("31.ABR"),
    GARDEN("32.ABR"),
    GAS_CONNECTION("33.ABR"),
    GAS_STOVE("34.ABR"),
    HAIRDRYER("35.ABR"),
    HI_FI_SYSTEM("36.ABR"),
    HOB("37.ABR"),
    HOTEL_ANNEX("38.ABR"),
    HYPOALLERGENIC_ROOM("39.ABR"),
    INDUCTION_HOB("40.ABR"),
    INFRARED_CABIN("41.ABR"),
    INTERNET_ACCESS("42.ABR"),
    IRON("43.ABR"),
    IRONING_BOARD("44.ABR"),
    KETTLE("45.ABR"),
    LAKE_VIEW("46.ABR"),
    MAIN_BUILDING("47.ABR"),
    MICROWAVE("48.ABR"),
    MINIBAR("49.ABR"),
    MOUNTAIN_VIEW("50.ABR"),
    NON_SMOKING("51.ABR"),
    OVEN("52.ABR"),
    PANORAMA_VIEW("53.ABR"),
    PETS("54.ABR"),
    PETS_ON_REQUEST("55.ABR"),
    REFRIGERATOR("56.ABR"),
    ROOM_FOR_ALLERGY_SUFFERERS("57.ABR"),
    ROOM_WITH_CONNECTING_DOOR("58.ABR"),
    SAFE("59.ABR"),
    SATELLITE_CABLE_TV("60.ABR"),
    SAUNA("61.ABR"),
    SEA_VIEW("62.ABR"),
    SEASONAL_RENT_POSSIBLE("63.ABR"),
    SEPARATE_KITCHEN("64.ABR"),
    SEPARATE_LIVING_AREA("65.ABR"),
    SEPARATE_WC("66.ABR"),
    SHOWER_ON_THE_FLOOR("67.ABR"),
    SHOWER_WC("68.ABR"),
    SINGLE_BED("69.ABR"),
    SINGLE_ROOM("70.ABR"),
    SINGLE_SOFA_BED("71.ABR"),
    SNACK_BAR("72.ABR"),
    SOUTH_SIDE("73.ABR"),
    STOVE("74.ABR"),
    SUITE("75.ABR"),
    TEA_COFFEE_MACHINE("76.ABR"),
    TELEPHONE("77.ABR"),
    TERRACE("78.ABR"),
    TILED_STOVE_FIRPLACE("79.ABR"),
    TOWELS("80.ABR"),
    TV("81.ABR"),
    TV_UPON_REQUEST("82.ABR"),
    TWIN_ROOM("83.ABR"),
    WASHING_MACHINE("84.ABR"),
    WATER_BOILER("85.ABR"),
    WATER_WASTE_CONNECTION("86.ABR"),
    WC_ON_THE_FLOOR("87.ABR"),
    WHIRLPOOL("88.ABR"),
    WITH_HOTEL_SERVICE("89.ABR");

    private final String code;

    AlpineBitsCodeRoomAmenityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isCodeDefined(String code) {
        for (AlpineBitsCodeRoomAmenityType value : values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
