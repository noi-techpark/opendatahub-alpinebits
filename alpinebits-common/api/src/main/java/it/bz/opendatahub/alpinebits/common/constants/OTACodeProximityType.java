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
 * Here you can find an enumeration of all OTA Proximity Type codes (PRX).
 */
public enum OTACodeProximityType {
    ONSITE("1"),
    OFFSITE("2"),
    NEARBY("3"),
    INFORMATION_NOT_AVAILABLE("4"),
    ONSITE_AND_OFFSITE("5");

    private final String code;

    OTACodeProximityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static boolean isCodeDefined(String code) {
        for (OTACodeProximityType value : values()) {
            if (value.code.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
