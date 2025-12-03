// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking.utils;

import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.WarningsType;

import java.util.Optional;

/**
 * Test utility class to extract echo and warning data from an {@link OTAPingRS}.
 */
public final class OTAPingRSExtractor {

    private OTAPingRSExtractor() {
        // Empty
    }

    public static Optional<String> extractWarning(OTAPingRS otaPingRs) {
        // Extract warning from otaPingRs
        Optional<WarningsType> otaPingRsWarnings = otaPingRs.getSuccessesAndEchoDatasAndWarnings().stream()
                .filter(WarningsType.class::isInstance)
                .map(o -> Optional.of((WarningsType) o))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Warning expected but no warning found"));

        return otaPingRsWarnings
                .filter(warningsType -> warningsType.getWarnings() != null && !warningsType.getWarnings().isEmpty())
                .map(warningsType -> warningsType.getWarnings().get(0).getValue());
    }
}
