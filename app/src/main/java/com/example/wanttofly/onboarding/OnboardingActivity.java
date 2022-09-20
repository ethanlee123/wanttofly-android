package com.example.wanttofly.onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wanttofly.R;
import com.example.wanttofly.search.SearchActivity;
import com.example.wanttofly.sharedpreferences.UserPreferences;
import com.example.wanttofly.sharedpreferences.UserPreferencesKeys;

import java.util.Arrays;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private Button getStartedButton;
    private TextView skipTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        getStartedButton = findViewById(R.id.b_get_started);
        skipTextView = findViewById(R.id.tv_skip);

        ViewPager2 viewPager = findViewById(R.id.vp_slides);
        RecyclerView.Adapter<ScreenSlidePagerAdapter.ViewHolder> pagerAdapter = new ScreenSlidePagerAdapter(viewPager);
        viewPager.setAdapter(pagerAdapter);

        setupGetStartedButton();
    }

    private void setupGetStartedButton() {
        getStartedButton.setOnClickListener(view -> {
            UserPreferences.getInstance(this).put(UserPreferencesKeys.IS_ONBOARDING_COMPLETE, true);

            startActivity(SearchActivity.getIntent(this));
            finish();
        });
    }

    private class ScreenSlidePagerAdapter extends RecyclerView.Adapter<ScreenSlidePagerAdapter.ViewHolder> {

        private final List<OnboardingSlides> slides;
        private final ViewPager2 viewPager2;

        ScreenSlidePagerAdapter(ViewPager2 viewPager2) {
            this.viewPager2 = viewPager2;
            this.slides = Arrays.asList(
                    OnboardingSlides.SLIDE_1,
                    OnboardingSlides.SLIDE_2,
                    OnboardingSlides.SLIDE_3
            );
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView drawable;
            TextView header;
            TextView subHeader;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                drawable = itemView.findViewById(R.id.iv_illustration);
                this.header = itemView.findViewById(R.id.tv_header);
                this.subHeader = itemView.findViewById(R.id.tv_subHeader);
            }

            void setDrawable(Drawable drawable) {
                this.drawable.setImageDrawable(drawable);
            }

            void setHeader(String text) {
                this.header.setText(text);
            }

            void setSubHeader(String text) {
                this.subHeader.setText(text);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_onboarding_slide, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.setDrawable(
                    AppCompatResources.getDrawable(getApplicationContext(),
                    slides.get(position).getDrawable())
            );
            holder.setHeader(getString(slides.get(position).getHeader()));
            holder.setSubHeader(getString(slides.get(position).getSubHeader()));

            if (position == slides.size() - 2) {
                viewPager2.post(runnable);
            }
        }

        @Override
        public int getItemCount() {
            return slides.size();
        }

        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewPager2.setCurrentItem(0, true);
            }
        };

    }


    public static Intent getIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }
}
