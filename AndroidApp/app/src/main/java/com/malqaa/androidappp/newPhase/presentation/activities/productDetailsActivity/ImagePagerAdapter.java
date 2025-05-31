package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity;

import static com.malqaa.androidappp.newPhase.utils.ExtensionKt.extractYouTubeId;
import static com.malqaa.androidappp.newPhase.utils.ExtensionKt.isVideoLink;
import static com.malqaa.androidappp.newPhase.utils.ExtensionKt.isYouTubeLink;
import static com.malqaa.androidappp.newPhase.utils.VideosUtilsKt.setupWebViewYouTubePlayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.malqaa.androidappp.R;
import com.malqaa.androidappp.databinding.FragmentWebViewPlayerDialogBinding;
import com.malqaa.androidappp.databinding.FragmentYoutubePlayerDialogBinding;
import com.malqaa.androidappp.databinding.ItemImageBinding;
import com.malqaa.androidappp.databinding.ItemVideoBinding;
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagePagerAdapter extends PagerAdapter {
    private final Context mContext;
    private final List<ImageSelectModel> mImageIds;
    private final Map<Integer, ExoPlayer> playerMap = new HashMap<>();  // To track players by page position
    private final Map<Integer, WebView> webViewMap = new HashMap<>();   // To track WebView instances by page position

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

        if (mImageIds.get(position).getType() == 2) { // Video or Web content
            String selectedUrl = mImageIds.get(position).getUrl();
            if (!selectedUrl.isEmpty()) {
                if (isYouTubeLink(selectedUrl)) {
                    FragmentYoutubePlayerDialogBinding binding = FragmentYoutubePlayerDialogBinding.inflate(inflater, container, false);
                    itemView = binding.getRoot();
                    WebView youtubeWebView = setupWebViewYouTubePlayer(binding.webView, extractYouTubeId(selectedUrl));
                    webViewMap.put(position, youtubeWebView);  // Store the WebView for this page
                } else if (isVideoLink(selectedUrl)) {
                    ItemVideoBinding binding = ItemVideoBinding.inflate(inflater, container, false);
                    itemView = binding.getRoot();

                    ExoPlayer player = new ExoPlayer.Builder(mContext).build();
                    binding.playerView.setPlayer(player);
                    MediaItem mediaItem = MediaItem.fromUri(selectedUrl);
                    player.setMediaItem(mediaItem);
                    player.prepare();
                    player.pause();  // Start paused

                    playerMap.put(position, player); // Store player in map by position
                } else {
                    FragmentWebViewPlayerDialogBinding binding = FragmentWebViewPlayerDialogBinding.inflate(inflater, container, false);
                    itemView = binding.getRoot();
                    WebView webView = binding.webView;
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(selectedUrl);
                    webViewMap.put(position, webView); // Store the WebView for this page
                }
            } else {
                Log.e("ImagePagerAdapter", "selectedUrl is null or empty");
                Toast.makeText(mContext, R.string.the_selected_url_is_not_available_or_is_empty, Toast.LENGTH_SHORT).show();
                return null;
            }
        } else {
            // For image content
            ItemImageBinding binding = ItemImageBinding.inflate(inflater, container, false);
            itemView = binding.getRoot();
            Glide.with(mContext)
                    .load(mImageIds.get(position).getUrl())
                    .into(binding.imageView);
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);

        // Release resources for this page's media when the page is destroyed
        ExoPlayer player = playerMap.remove(position);
        if (player != null) {
            player.release();
        }

        WebView webView = webViewMap.remove(position);
        if (webView != null) {
            webView.destroy();
        }
    }

    // Start media for the active page
    public void playMedia(int position) {
        ExoPlayer player = playerMap.get(position);
        if (player != null) {
            player.play();
        }

        WebView webView = webViewMap.get(position);
        if (webView != null) {
            webView.onResume();
        }
    }

    // Pause media for inactive pages
    public void pauseMedia(int position) {
        ExoPlayer player = playerMap.get(position);
        if (player != null) {
            player.pause();
        }

        WebView webView = webViewMap.get(position);
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
