package com.kanj.apps.hybridtextimageviews;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
    TextImageInputView.TextImageInputListener, ItemHandlerProvider<MainActivity> {

    private static final int SELECT_IMAGES_REQUEST = 90;

    private int thumbnailDimension;

    private TextImageInputView mTextImageInputView;
    private ArrayList<Uri> inputImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextImageInputView = (TextImageInputView) findViewById(R.id.hybrid_input);
        mTextImageInputView.setListener(this);

        inputImages = new ArrayList<>();

        thumbnailDimension = getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
    }

    public void onThumbnailClick(int index) {
        inputImages.remove(index);
        mTextImageInputView.removeInputImageThumbnail(index);
    }

    public InputStream getInputStreamFromUri(Uri uri) throws FileNotFoundException {
        return getContentResolver().openInputStream(uri);
    }

    @Override
    public void onCameraIconClicked() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        startActivityForResult(
            Intent.createChooser(i, getString(R.string.select_images_chooser_title)),
            SELECT_IMAGES_REQUEST
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == SELECT_IMAGES_REQUEST) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                int itemCount = clipData.getItemCount();
                for (int i = 0; i<itemCount; i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    addImageToInput(uri);
                }
            } else {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    addImageToInput(imageUri);
                } else {
                    Log.w("Kanj", "uri is null");
                }
            }
        }
    }

    private void addImageToInput(Uri uri) {
        inputImages.add(uri);
        mTextImageInputView.addInputImageThumbnail(uri, this);
    }

    public int getThumbnailDimension() {
        return thumbnailDimension;
    }

    private ArrayList<String> getImageUrisAsStringArray() {
        if (inputImages == null || inputImages.size() == 0) {
            return null;
        }

        ArrayList<String> array = new ArrayList<>();
        for(Uri u: inputImages) {
            array.add(u.toString());
        }

        return array;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> imageUris = getImageUrisAsStringArray();
        if (imageUris != null) {
            outState.putStringArrayList("uris", imageUris);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<String> uriArray = savedInstanceState.getStringArrayList("uris");
        if (uriArray != null) {
            for (String s: uriArray) {
                Uri uri = Uri.parse(s);
                addImageToInput(uri);
            }
        }
    }

    @Override
    public void onSendClicked() {
        Log.v("Kanj", "Send clicked");
        mTextImageInputView.scrollToBottom();
    }

    @Override
    public MainActivity getItemHandler() {
        return this;
    }
}
