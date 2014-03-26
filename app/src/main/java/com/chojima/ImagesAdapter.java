package com.chojima;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends ArrayAdapter<Image> {
    public ImagesAdapter(Context context, List<Image> objects) {
        super(context, 0, objects);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        DynamicHeightImageView imageView = new DynamicHeightImageView(getContext());

        Image image = getItem(position);
        Log.e("Image", image.toString());

        double scaleFactor = ((double) image.getHeight()) / ((double) image.getWidth());
        imageView.setHeightRatio(scaleFactor);
        Picasso.with(getContext()).load(image.getLink()).fit().into(imageView);

        return imageView;
    }
}
