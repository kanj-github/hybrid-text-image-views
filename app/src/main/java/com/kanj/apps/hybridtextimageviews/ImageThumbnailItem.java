package com.kanj.apps.hybridtextimageviews;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by naraykan on 07/02/17.
 */

public class ImageThumbnailItem extends BaseItem<InputImageAsset, MainActivity,
    ImageThumbnailItem.ViewHolder> implements View.OnClickListener {

    private int mThumbnailSize;

    public ImageThumbnailItem(InputImageAsset imageAsset, ItemHandlerProvider<MainActivity> itemHandlerProvider) {
        super(imageAsset, itemHandlerProvider);
        mThumbnailSize = itemHandlerProvider.getItemHandler().getThumbnailDimension();
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
        getItemHandler().onThumbnailClick(getPositionInAdapter());
    }

    private Bitmap decodeSampledBitmapFromResource(Uri imageUri) {
        try {
            InputStream inputStream = getItemHandler().getInputStreamFromUri(imageUri);

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int rotation = getRotation(imageUri);

            // Calculate inSampleSize
            if (rotation == 90 || rotation == 270) {
                options.inSampleSize = calculateInSampleSize(options, true);
            } else {
                options.inSampleSize = calculateInSampleSize(options, false);
            }

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            inputStream = getItemHandler().getInputStreamFromUri(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (rotation != -1) {
                // Rotate
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            }

            return bitmap;
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return null;
        }
    }

    private int calculateInSampleSize(BitmapFactory.Options options, boolean rotated) {
        // Raw height and width of image
        int height, width;
        if (rotated) {
            height = options.outWidth;
            width = options.outHeight;
        } else {
            height = options.outHeight;
            width = options.outWidth;
        }

        int inSampleSize = 1;

        if (height > mThumbnailSize || width > mThumbnailSize) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= mThumbnailSize
                && (halfWidth / inSampleSize) >= mThumbnailSize) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private int getRotation(Uri imageUri) {
        ContentResolver content = getItemHandler().getContentResolver();

        Cursor cursor = content.query(imageUri,
            new String[] {MediaStore.Images.ImageColumns.ORIENTATION},
            null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        int rotation = cursor.getInt(0);
        cursor.close();
        return rotation;
    }

    protected class ViewHolder extends BaseViewHolder {
        ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
        }
    }
}
