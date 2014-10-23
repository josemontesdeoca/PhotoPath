package com.joseonline.android.photopath;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Instagram Photo Array Adapter
 */
public class PhotosAdapter extends ArrayAdapter<Photo> {

    public PhotosAdapter(Context context, List<Photo> photos) {
        super(context, R.layout.item_photo, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        tvCaption.setText(photo.getCaption());
        ivPhoto.getLayoutParams().height = photo.getImageHeight();
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl()).into(ivPhoto);

        return convertView;
    }
}
