package com.kanj.apps.hybridtextimageviews;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements
    TextImageInputView.TextImageInputListener {
    private TextImageInputView mTextImageInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextImageInputView = (TextImageInputView) findViewById(R.id.hybrid_input);
        mTextImageInputView.setListener(this);
    }

    public void onThumbnailClick(int index, InputImageAsset imageAsset) {

    }

    public InputStream getInputStreamFromUri(Uri uri) throws FileNotFoundException {
        return getContentResolver().openInputStream(uri);
    }

    @Override
    public void onCameraIconClicked() {

    }

    @Override
    public void onSendClicked() {

    }
}
