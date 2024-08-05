package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jsibbold.zoomage.ZoomageView;
import com.malqaa.androidappp.R;
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<ImageSelectModel> mImageIds;
    private ExoPlayer player;

    public ImagePagerAdapter(Context context, List<ImageSelectModel> imageIds) {
        mContext = context;
        mImageIds = imageIds;
    }

    @Override
    public int getCount() {
        return mImageIds.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView;

        if (mImageIds.get(position).getType() == 2) { // Assuming type 2 is video
            itemView = inflater.inflate(R.layout.item_video, container, false);
            PlayerView playerView = itemView.findViewById(R.id.playerView);
            player = new ExoPlayer.Builder(mContext).build();
            playerView.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(mImageIds.get(position).getUrl());
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        } else {
            itemView = inflater.inflate(R.layout.item_image, container, false);
            ZoomageView imageView = itemView.findViewById(R.id.imageView);
            Glide.with(mContext)
                    .load(mImageIds.get(position).getUrl())
                    .into(imageView);
        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

        // Release the player when the item is destroyed
        if (mImageIds.get(position).getType() == 2 && player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    public void releaseStop() {
        if (player != null) {
            player.stop();
        }
    }
}
