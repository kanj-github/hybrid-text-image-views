package com.kanj.apps.hybridtextimageviews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by naraykan on 07/02/17.
 */

public class ImageThumbnailItem extends BaseItem<InputImageAsset, MainActivity,
    ImageThumbnailItem.ViewHolder> implements View.OnClickListener {
    private static int THUMBNAIL_SIZE = R.dimen.thumbnail_size;

    public ImageThumbnailItem(InputImageAsset imageAsset, ItemHandlerProvider<MainActivity> itemHandlerProvider) {
        super(imageAsset, itemHandlerProvider);
    }

    @Override
    public int getLayoutId() {
        return R.layout.image_thumbnail;
    }

    @Override
    public ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void updateItemViews() {
        ViewHolder vh = getViewHolder();
        vh.thumbnail.setOnClickListener(this);
        vh.thumbnail.setImageBitmap(
            decodeSampledBitmapFromResource(getItemData().imageUri)
        );
    }

    @Override
    public ItemType getType() {
        return ViewItemTypes.THUMBNAIL;
    }

    @Override
    public void onClick(View view) {
        getPositionInAdapter();
        getItemHandler().onThumbnailClick(getPositionInAdapter(), getItemData());
    }

    private Bitmap decodeSampledBitmapFromResource(Uri imageUri) {
        InputStream inputStream;
        try {
            inputStream = getItemHandler().getInputStreamFromUri(imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, null, options);
        } catch (FileNotFoundException fnfe) {
            return null;
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > THUMBNAIL_SIZE || width > THUMBNAIL_SIZE) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= THUMBNAIL_SIZE
                && (halfWidth / inSampleSize) >= THUMBNAIL_SIZE) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    protected class ViewHolder extends BaseViewHolder {
        ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }
}
