package com.example.wisdomapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.wisdomapp.R;
import com.example.wisdomapp.items.Video;
import com.example.wisdomapp.activities.playactivities.WatchVideoActivity;

import java.util.List;

public class VideoAdapter extends ArrayAdapter<Video> {

    public VideoAdapter(Context context, List<Video> videos) {
        super(context, 0, videos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the video item for this position
        Video video = getItem(position);

        // Check if an existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_video, parent, false);
        }

        // Get references to the TextViews and Button
        TextView titleTextView = convertView.findViewById(R.id.videoTitle);
        TextView pathTextView = convertView.findViewById(R.id.videoPath);
        VideoView videoView = convertView.findViewById(R.id.videothumb);
        Button playButton = convertView.findViewById(R.id.playButton);

        // Set video details in TextViews
        titleTextView.setText("Title: " + video.getTitle());
        pathTextView.setText("Date " + video.getTimestamp());

        videoView.setOnPreparedListener(mp -> {
            mp.setVolume(0f, 0f);
            videoView.start();
        });

        // Set up the button click listener to open the video
        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WatchVideoActivity.class);
            intent.putExtra("videoPath", video.getPath()); // Pass video path to WatchVideoActivity
            getContext().startActivity(intent);
        });

        // Return the completed view to display in the ListView
        return convertView;
    }
}
