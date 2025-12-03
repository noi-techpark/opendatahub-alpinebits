// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.xmladapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * XML adapter to convert xs:dateTime values to {@link ZonedDateTime}.
 */
public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {
    @Override
    public ZonedDateTime unmarshal(String v) {

        if (this.isParsableAsZonedDateTime(v)) {
            // If the String contains a parsable timezone, it is parsed with that timezone
            return ZonedDateTime.parse(v, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        } else {
            // If the String contains no parsable timezone, it is parsed with the system timezone
            return LocalDateTime.parse(v).atZone(ZoneId.systemDefault());
        }
    }

    @Override
    public String marshal(ZonedDateTime v) {
        return v != null ? v.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }

    public boolean isParsableAsZonedDateTime(final CharSequence text) {
        ParsePosition pos = new ParsePosition(0);
        TemporalAccessor temporalAccessor = DateTimeFormatter.ISO_ZONED_DATE_TIME.parseUnresolved(text, pos);
        return temporalAccessor != null && pos.getErrorIndex() < 0 && pos.getIndex() >= text.length();
    }
}
