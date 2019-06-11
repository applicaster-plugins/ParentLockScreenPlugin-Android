package com.applicaster.pinlockview;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applicaster.CustomizationOptionsBundle;
import com.applicaster.web.plugins.iai.R;

import java.util.ArrayList;

import static com.applicaster.HookActivity.ANIMATION_DURATION;


public class PinLockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_NUMBER = 0;

    private Context mContext;
    private OnNumberClickListener mOnNumberClickListener;
    private int mPinLength;

    private ArrayList<Integer> mKeyValues = new ArrayList<Integer>();

    public PinLockAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_NUMBER) {
            View view = inflater.inflate(R.layout.layout_number_item, parent, false);
            viewHolder = new NumberViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NUMBER) {
            NumberViewHolder vh1 = (NumberViewHolder) holder;
            configureNumberButtonHolder(vh1, position);
        }
    }

    private void configureNumberButtonHolder(NumberViewHolder holder, int position) {
        if (holder != null) {
            holder.mNumberButton.setText(String.valueOf(mKeyValues.get(position)));
            holder.mNumberButton.setVisibility(View.VISIBLE);
            holder.mNumberButton.setTag(mKeyValues.get(position));
            holder.mNumberButton.setBackgroundResource(R.drawable.digit_back);
            TransitionDrawable transition = (TransitionDrawable) holder.mNumberButton.getBackground();
            transition.resetTransition();

            if (CustomizationOptionsBundle.getInstance() != null) {
                holder.mNumberButton.setTextColor(CustomizationOptionsBundle.getInstance().getTextColor());
                holder.mNumberButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        CustomizationOptionsBundle.getInstance().getTextSize());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        CustomizationOptionsBundle.getInstance().getButtonSize(),
                        CustomizationOptionsBundle.getInstance().getButtonSize());
                holder.mNumberButton.setLayoutParams(params);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mKeyValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NUMBER;
    }

    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;
    }


    public void setKeyValues(ArrayList<Integer> keyValues) {
        this.mKeyValues.clear();
        this.mKeyValues.addAll(keyValues);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnNumberClickListener onNumberClickListener) {
        this.mOnNumberClickListener = onNumberClickListener;
    }


    public class NumberViewHolder extends RecyclerView.ViewHolder {
        Button mNumberButton;

        public NumberViewHolder(final View itemView) {
            super(itemView);
            mNumberButton = itemView.findViewById(R.id.button);
            mNumberButton.setTypeface(CustomizationOptionsBundle.getInstance().getTextFont());
            setTextcolor(mNumberButton, CustomizationOptionsBundle.getInstance().getTextColor());
            mNumberButton.setBackgroundResource(R.drawable.digit_back);

            mNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransitionDrawable transition = (TransitionDrawable) mNumberButton.getBackground();
                    transition.startTransition(ANIMATION_DURATION);
                    setTextcolor(mNumberButton, CustomizationOptionsBundle.getInstance().getTextColorSelected());

                    if (mOnNumberClickListener != null) {
                        mOnNumberClickListener.onNumberClicked((Integer) v.getTag());
                    }
                }
            });
        }
    }


    public interface OnNumberClickListener {
        void onNumberClicked(int keyValue);
    }

    private void setTextcolor(TextView sequenceText, int color) {
        sequenceText.setTextColor( color);

    }
}
