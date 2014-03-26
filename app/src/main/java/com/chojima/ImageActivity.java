package com.chojima;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageActivity extends Activity {
    public static final String EXTRA_IMAGE_LINK = "image_link";
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        String imageLink = intent.getStringExtra(EXTRA_IMAGE_LINK);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(imageLink).into(imageView);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.slide_out_up);
    }
}
