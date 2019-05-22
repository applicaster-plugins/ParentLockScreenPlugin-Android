package com.applicaster.pinlockview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;




public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int mHorizontalSpaceWidth;
    private final int mVerticalSpaceHeight;
    private final int mSpanCount;

    public ItemSpaceDecoration(int horizontalSpaceWidth, int verticalSpaceHeight, int spanCount ) {
        this.mHorizontalSpaceWidth = horizontalSpaceWidth;
        this.mVerticalSpaceHeight = verticalSpaceHeight;
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int column = position % mSpanCount;
        outRect.left = column * mHorizontalSpaceWidth / mSpanCount;
        outRect.right = mHorizontalSpaceWidth - (column + 1) * mHorizontalSpaceWidth / mSpanCount;
        if (position >= mSpanCount) {
            outRect.top = mVerticalSpaceHeight;
        }
    }
}
