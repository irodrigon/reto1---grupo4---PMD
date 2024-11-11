package com.example.wisdomapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wisdomapp.R;
import com.example.wisdomapp.items.Image;


import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {


    private List<Image> images;

    public ImageAdapter(List<Image> images) {
        this.images = images;
    }


    @NonNull

    @Override

    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }


    @Override

    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Image image = images.get(position);

        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        holder.imageView.setImageBitmap(bitmap);
        holder.titleTextView.setText(image.getTitle());

    }


    @Override
    public int getItemCount() {
        return images.size();
    }


    public void addImage(Image image) {
        images.add(image);
        notifyItemInserted(images.size() - 1);
    }


    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewImage);
            titleTextView = itemView.findViewById(R.id.textViewImage);
        }
    }

}