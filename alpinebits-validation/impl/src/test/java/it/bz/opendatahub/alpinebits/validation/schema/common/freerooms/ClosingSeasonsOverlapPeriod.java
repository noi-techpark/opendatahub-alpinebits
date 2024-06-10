// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.common.freerooms;

import java.time.LocalDate;

/**
 * Helper class for tests to hold closing season data.
 */
public class ClosingSeasonsOverlapPeriod {

    private final LocalDate start;
    private final LocalDate end;

    public ClosingSeasonsOverlapPeriod(String start, String end) {
        this.start = LocalDate.parse(start);
        this.end = LocalDate.parse(end);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "start=" + start + ", end=" + end;
    }

}
