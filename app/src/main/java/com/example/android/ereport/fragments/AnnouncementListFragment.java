package com.example.android.ereport.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.ereport.R;
import com.example.android.ereport.activities.DetailInfoViewActivity;
import com.example.android.ereport.adapters.AnnouncementAdapter;
import com.example.android.ereport.models.Announcement;
import com.example.android.ereport.models.App;

import java.util.ArrayList;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by krogers on 3/1/18.
 */

public class AnnouncementListFragment extends Fragment {
    Box<Announcement> announcements;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_announcement_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //activities of onCreate
        ListView root_view = view.findViewById(R.id.rootView);

        //ObjectBox set up
        BoxStore boxStore = ((App) getActivity().getApplication()).getBoxStore();
        announcements = boxStore.boxFor(Announcement.class);

        ArrayList<Announcement> list_announcements = (ArrayList<Announcement>) announcements.getAll();

        final AnnouncementAdapter adapter = new AnnouncementAdapter(getContext(), list_announcements);

        root_view.setAdapter(adapter);

        root_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //opening the detail view activity
                long item_id = adapter.getItem(i).getId();
//                Log.e("ITEMID", "onItemClick: item id "+item_id );
                Intent detailed_view_intent = new Intent(getContext(), DetailInfoViewActivity.class);
                detailed_view_intent.putExtra("announcement_id", item_id + "");
                startActivity(detailed_view_intent);
            }
        });

    }

}
