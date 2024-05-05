package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.malqaa.androidappp.R;
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel;

import java.util.ArrayList;

public class ImageViewLargeActivity extends AppCompatActivity {
    ArrayList<String> mImageIds;



    private ViewPager mViewPager;
    ConstraintLayout layoutCancel;
    ImageView imageCancel;
    private ImageButton mBtnPrevious, mBtnNext;
    private ImagePagerAdapter mAdapter;
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_display);

        ArrayList<ImageSelectModel> receivedList = getIntent().getParcelableArrayListExtra("imgList");




        imageCancel= findViewById(R.id.imageCancel);
        mViewPager = findViewById(R.id.viewPager);
        mBtnPrevious = findViewById(R.id.btnPrevious);
        layoutCancel = findViewById(R.id.layoutImg);
        mBtnNext = findViewById(R.id.btnNext);

        mAdapter = new ImagePagerAdapter(this, receivedList);
        mViewPager.setAdapter(mAdapter);

        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFinishOnTouchOutside(true);
                finish();
            }
        });

        layoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFinishOnTouchOutside(true);
                finish();
            }
        });
        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition > 0) {
                    mCurrentPosition--;
                    mViewPager.setCurrentItem(mCurrentPosition);
                }
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition < mAdapter.getCount() - 1) {
                    mCurrentPosition++;
                    mViewPager.setCurrentItem(mCurrentPosition);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}