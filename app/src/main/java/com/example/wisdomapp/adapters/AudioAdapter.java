package com.example.wisdomapp.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wisdomapp.items.Audio;
import com.example.wisdomapp.R;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {

    private List<Audio> audioItemList;
    private Context context;
    private MediaPlayer mediaPlayer;
    private MediaController mediaController;


    public AudioAdapter(Context context,List<Audio> audioItemList) {
        this.context = context;
        this.audioItemList = audioItemList;
    }


    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false);
        return new AudioViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        Audio audioItem = audioItemList.get(position);
        holder.textViewDescription.setText(audioItem.getTitle());
        holder.itemView.setOnClickListener(v -> {
            playAudio(audioItem.getPath());
        });
    }


    @Override
    public int getItemCount() {
        return audioItemList.size();
    }


    private void playAudio(String audioPath) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();


        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(context, "Algun error inesperado ocurrio, sentimos las molestias.", Toast.LENGTH_SHORT).show();
        }
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescription;
        AudioViewHolder(View itemView) {
            super(itemView);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
