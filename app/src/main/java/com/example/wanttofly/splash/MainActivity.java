package com.example.wanttofly.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.wanttofly.R;
import com.example.wanttofly.network.VolleySingleton;
import com.example.wanttofly.onboarding.OnboardingActivity;
import com.example.wanttofly.search.SearchActivity;
import com.example.wanttofly.sharedpreferences.UserPreferences;
import com.example.wanttofly.sharedpreferences.UserPreferencesKeys;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playSplashScreenVideo();

        // Instantiate an instance of VolleySingleton ASAP to make network requests
        VolleySingleton.getInstance(getApplicationContext());
    }

    private void playSplashScreenVideo() {
        VideoView videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                R.raw.want_to_fly_dark));
        videoView.setOnCompletionListener(mediaPlayer -> {
                    boolean completedOnboarding =
                            UserPreferences
                                    .getInstance(this)
                                    .getBoolean(
                                            UserPreferencesKeys.IS_ONBOARDING_COMPLETE,
                                            false);

                    if (completedOnboarding) {
                        startActivity(SearchActivity.getIntent(this));
                    } else {
                        startActivity(OnboardingActivity.getIntent(this));
                    }
                    finish();
                }
        );
        videoView.start();
    }
}