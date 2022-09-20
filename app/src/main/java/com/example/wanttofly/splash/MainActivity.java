package com.example.wanttofly.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.wanttofly.R;
import com.example.wanttofly.onboarding.OnboardingActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playSplashScreenVideo();
    }

    private void playSplashScreenVideo() {
        VideoView videoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                R.raw.want_to_fly_dark));
        videoView.setOnCompletionListener(mediaPlayer -> {
                    startActivity(OnboardingActivity.getIntent(this));
                    finish();
                }
        );
        videoView.start();
    }
}