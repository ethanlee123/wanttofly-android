package com.example.wanttofly.search;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MarginItemDecoration extends RecyclerView.ItemDecoration {

    int spaceSize;
    public MarginItemDecoration(int spaceSize) {
        this.spaceSize = spaceSize;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceSize;
        }
        outRect.bottom = spaceSize;
    }
}

