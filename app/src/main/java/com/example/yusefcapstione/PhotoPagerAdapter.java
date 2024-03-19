package com.example.yusefcapstione;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PhotoPagerAdapter extends RecyclerView.Adapter<PhotoPagerAdapter.PhotoViewHolder> {

    private Context context;
    private List<String> photoPaths;

    public PhotoPagerAdapter(Context context, List<String> photoPaths) {
        this.context = context;
        this.photoPaths = photoPaths;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String photoPath = photoPaths.get(position);
        Glide.with(context)
                .load(photoPath)
                .into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }

    // Method to add a new photo path to the adapter
    public void addPhoto(String photoPath) {
        photoPaths.add(photoPath); // Add the new photo path to the list
        notifyDataSetChanged(); // Notify adapter that the dataset has changed
    }
}
