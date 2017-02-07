package com.kanj.apps.hybridtextimageviews;

/**
 * Created by naraykan on 07/02/17.
 */

public enum ViewItemTypes implements ItemType {
    THUMBNAIL(ImageThumbnailItem.class);

    final Class<? extends BaseItem> itemClass;

    ViewItemTypes(Class<? extends BaseItem> baseItemClass) {
        itemClass = baseItemClass;
    }

    @Override
    public Class<? extends BaseItem> getItemClass() {
        return itemClass;
    }

    @Override
    public int getValue() {
        return ordinal();
    }
}
