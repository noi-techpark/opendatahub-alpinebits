// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.validation.schema.v_2017_10.inventory.common;

import it.bz.opendatahub.alpinebits.common.constants.Iso6391;
import it.bz.opendatahub.alpinebits.validation.Names;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ImageItemsType.ImageItem;
import it.bz.opendatahub.alpinebits.xml.schema.ota.TextItemsType.TextItem;
import it.bz.opendatahub.alpinebits.xml.schema.ota.VideoItemsType.VideoItem;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Tests for {@link Description}.
 */
public class DescriptionTest {

    private static final String DEFAULT_LANGUAGE = Iso6391.ABKHAZIAN.getCode();
    private static final String DEFAULT_TEXT_FORMAT = Names.PLAIN_TEXT;
    private static final String DEFAULT_VALUE = "some text";


    @Test
    public void testFromImageItemDescription() {
        ImageItem.Description imageItemDescription = buildImageItemDescription();

        Description description = Description.fromImageItemDescription(imageItemDescription);

        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }

    @Test
    public void testFromImageItemDescriptions() {
        ImageItem.Description imageItemDescription = buildImageItemDescription();
        List<ImageItem.Description> imageItemDescriptions = new ArrayList<>();
        imageItemDescriptions.add(imageItemDescription);

        List<Description> descriptions = Description.fromImageItemDescriptions(imageItemDescriptions);

        assertEquals(descriptions.size(), 1);

        Description description = descriptions.get(0);
        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }

    @Test
    public void testFromTextItemDescription() {
        TextItem.Description textItemDescription = buildTextItemDescription();

        Description description = Description.fromTextItemDescription(textItemDescription);

        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }

    @Test
    public void testFromTextItemDescriptions() {
        TextItem.Description textItemDescription = buildTextItemDescription();
        List<TextItem.Description> textItemDescriptions = new ArrayList<>();
        textItemDescriptions.add(textItemDescription);

        List<Description> descriptions = Description.fromTextItemDescriptions(textItemDescriptions);

        assertEquals(descriptions.size(), 1);

        Description description = descriptions.get(0);
        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }

    @Test
    public void testFromVideoItemDescription() {
        VideoItem.Description videoItemDescription = buildVideoItemDescription();

        Description description = Description.fromVideoItemDescription(videoItemDescription);

        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }

    @Test
    public void testFromVideoItemDescriptions() {
        VideoItem.Description videoItemDescription = buildVideoItemDescription();
        List<VideoItem.Description> videoItemDescriptions = new ArrayList<>();
        videoItemDescriptions.add(videoItemDescription);

        List<Description> descriptions = Description.fromVideoItemDescriptions(videoItemDescriptions);

        assertEquals(descriptions.size(), 1);

        Description description = descriptions.get(0);
        assertEquals(description.getLanguage(), DEFAULT_LANGUAGE);
        assertEquals(description.getTextFormat(), DEFAULT_TEXT_FORMAT);
        assertEquals(description.getValue(), DEFAULT_VALUE);
    }


    @Test
    public void testTestEquals() {
        Description description1 = new Description(DEFAULT_VALUE, DEFAULT_TEXT_FORMAT, DEFAULT_LANGUAGE);
        Description description2 = new Description(DEFAULT_VALUE, DEFAULT_TEXT_FORMAT, DEFAULT_LANGUAGE);

        assertEquals(description1, description2);
    }

    @Test
    public void testTestHashCode() {
        Description description1 = new Description(DEFAULT_VALUE, DEFAULT_TEXT_FORMAT, DEFAULT_LANGUAGE);
        Description description2 = new Description(DEFAULT_VALUE, DEFAULT_TEXT_FORMAT, DEFAULT_LANGUAGE);

        assertEquals(description1.hashCode(), description2.hashCode());
    }

    private ImageItem.Description buildImageItemDescription() {
        ImageItem.Description description = new ImageItem.Description();
        description.setLanguage(DEFAULT_LANGUAGE);
        description.setTextFormat(DEFAULT_TEXT_FORMAT);
        description.setValue(DEFAULT_VALUE);
        return description;
    }

    private TextItem.Description buildTextItemDescription() {
        TextItem.Description description = new TextItem.Description();
        description.setLanguage(DEFAULT_LANGUAGE);
        description.setTextFormat(DEFAULT_TEXT_FORMAT);
        description.setValue(DEFAULT_VALUE);
        return description;
    }

    private VideoItem.Description buildVideoItemDescription() {
        VideoItem.Description description = new VideoItem.Description();
        description.setLanguage(DEFAULT_LANGUAGE);
        description.setTextFormat(DEFAULT_TEXT_FORMAT);
        description.setValue(DEFAULT_VALUE);
        return description;
    }
}