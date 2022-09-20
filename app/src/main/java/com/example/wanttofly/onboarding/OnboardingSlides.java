package com.example.wanttofly.onboarding;

import com.example.wanttofly.R;

public enum OnboardingSlides {
    SLIDE_1(
        R.drawable.ic_onboarding_illustration_1,
        R.string.onboarding_slide_1_header,
        R.string.onboarding_slide_1_subHeader
    ),
    SLIDE_2(
        R.drawable.ic_onboarding_illustration_2,
        R.string.onboarding_slide_2_header,
        R.string.onboarding_slide_2_subHeader
    ),
    SLIDE_3(
        R.drawable.ic_onboarding_illustration_3,
        R.string.onboarding_slide_3_header,
        R.string.onboarding_slide_3_subHeader
    );

    private final int drawable;
    private final int header;
    private final int subHeader;

    OnboardingSlides(int drawable, int header, int subHeader) {
        this.drawable = drawable;
        this.header = header;
        this.subHeader = subHeader;
    }

    public int getDrawable() {
        return this.drawable;
    }

    public int getHeader() {
        return this.header;
    }

    public int getSubHeader() {
        return this.subHeader;
    }
}
