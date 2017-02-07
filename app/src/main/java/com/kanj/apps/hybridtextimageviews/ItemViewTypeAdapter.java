package com.kanj.apps.hybridtextimageviews;

/**
 * Created by naraykan on 07/02/17.
 */

public interface ItemViewTypeAdapter {
    Enum<? extends ItemType> getType(int viewType);
}
