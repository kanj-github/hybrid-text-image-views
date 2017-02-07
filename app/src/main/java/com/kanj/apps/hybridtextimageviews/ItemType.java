package com.kanj.apps.hybridtextimageviews;

/**
 * Created by naraykan on 07/02/17.
 */

public interface ItemType {
    Class<? extends BaseItem> getItemClass();

    int getValue();
}
