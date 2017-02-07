package com.kanj.apps.hybridtextimageviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by naraykan on 06/02/17.
 */

public class TextImageInputView extends CardView {
    private EditText textInput;
    private RecyclerView imagesListView;
    private TextView sendLabel;
    private ImageView cameraIcon;

    private TextImageInputListener mListener;

    public TextImageInputView(Context context) {
        this(context, null);
    }

    public TextImageInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextImageInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.text_image_input_view, this);

        textInput = (EditText) findViewById(R.id.et_text);
        imagesListView = (RecyclerView) findViewById(R.id.list_images);
        sendLabel = (TextView) findViewById(R.id.tv_send);
        cameraIcon = (ImageView) findViewById(R.id.icon_camera);

        sendLabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onSendClicked();
                }
            }
        });

        cameraIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onCameraIconClicked();
                }
            }
        });
    }

    public void setListener (TextImageInputListener listener) {
        this.mListener = listener;
    }

    public RecyclerView getImagesRecyclerView() {
        return this.imagesListView;
    }

    public String getEnteredText() {
        return textInput.getText().toString().trim();
    }

    public interface TextImageInputListener {
        void onCameraIconClicked();
        void onSendClicked();
    }
}
