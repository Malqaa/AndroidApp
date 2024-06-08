package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.malqaa.androidappp.R;
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel;
import com.malqaa.androidappp.newPhase.utils.Extension;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<ImageSelectModel> mImageIds;
    int pos;

    public ImagePagerAdapter(Context context, List<ImageSelectModel> imageIds, int pos) {
        mContext = context;
        mImageIds = imageIds;
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return mImageIds.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_image, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        Picasso.get()
                .load(mImageIds.get(pos).getUrl())
                .error(R.mipmap.ic_launcher)
                .into(imageView);
//        Extension.loadThumbnail(
//                imageView.getContext(),
//                mImageIds.get(position).getUrl(),
//                imageView,
//                null,
//                null
//        );
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
