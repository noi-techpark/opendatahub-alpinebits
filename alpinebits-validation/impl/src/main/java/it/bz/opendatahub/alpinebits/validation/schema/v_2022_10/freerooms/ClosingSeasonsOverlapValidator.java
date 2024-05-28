// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2022_10.freerooms;

import it.bz.opendatahub.alpinebits.validation.ErrorMessage;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.validation.ValidationHelper;
import it.bz.opendatahub.alpinebits.validation.ValidationPath;
import it.bz.opendatahub.alpinebits.validation.Validator;
import it.bz.opendatahub.alpinebits.xml.schema.ota.BaseInvCountType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Use this validator to check if closing-seasons don't overlap
 * for AlpineBits 2022-10 FreeRooms documents.
 *
 * @see BaseInvCountType
 */
public class ClosingSeasonsOverlapValidator implements Validator<List<BaseInvCountType>, Void> {

    public static final String ELEMENT_NAME = Names.STATUS_APPLICATION_CONTROL;

    private static final ValidationHelper VALIDATOR = ValidationHelper.withClientDataError();

    private final Comparator<Period> sortByStart = (o1, o2) -> {
        if (o1.getStart().isBefore(o2.getStart())) {
            return -1;
        }
        if (o1.getStart().isAfter(o2.getStart())) {
            return 1;
        }
        return 0;
    };

    @Override
    public void validate(List<BaseInvCountType> inventories, Void ctx, ValidationPath path) {

        // Check for overlapping time periods only if there are at least two elements
        // in the inventories list.
        if (inventories != null && inventories.size() > 1) {
            // Build list of time periods sorted by start that can be checked for overlap
            List<Period> periods = inventories.stream()
                    .map(closingSeasonElements -> {
                        String start = closingSeasonElements.getStatusApplicationControl().getStart();
                        String end = closingSeasonElements.getStatusApplicationControl().getEnd();
                        return new Period(start, end);
                    })
                    .sorted(sortByStart)
                    .collect(Collectors.toList());

            // Check for overlaps (see https://stackoverflow.com/questions/325933/determine-whether-two-date-ranges-overlap for discussion)
            for (int i = 0; i < periods.size() - 1; i++) {
                Period p1 = periods.get(i);
                Period p2 = periods.get(i + 1);
                if (p1.getStart().isBefore(p2.getEnd()) && p2.getStart().isBefore(p1.getEnd())) {
                    String message = String.format(ErrorMessage.EXPECT_NO_OVERLAPPING_TIME_PERIODS, p1, p2);
                    VALIDATOR.throwValidationException(message, path);
                }
            }
        }
    }

    static class Period {
        private final LocalDate start;
        private final LocalDate end;

        Period(String start, String end) {
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
}
