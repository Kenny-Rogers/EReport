package com.example.android.ereport.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.ereport.R;
import com.example.android.ereport.models.Announcement;

import java.util.ArrayList;

/**
 * Created by krogers on 3/1/18.
 */

public class AnnouncementAdapter extends ArrayAdapter<Announcement> {
    public AnnouncementAdapter(@NonNull Context context, ArrayList<Announcement> announcements) {
        super(context, 0, announcements);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View announcement_view = convertView;

        if (announcement_view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            announcement_view = inflater.inflate(R.layout.list_info_view, null);
        }

        ImageView image = announcement_view.findViewById(R.id.iv_image);
        TextView title = announcement_view.findViewById(R.id.tv_title);
        TextView description = announcement_view.findViewById(R.id.tv_description);

        Announcement announcement = getItem(position);

        //image.setImageURI();
        title.setText(announcement.getTitle());
        description.setText(announcement.getMessage());
        return announcement_view;
    }
}
