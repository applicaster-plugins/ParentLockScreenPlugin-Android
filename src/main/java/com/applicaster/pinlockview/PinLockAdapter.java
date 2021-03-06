package com.applicaster.pinlockview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applicaster.CustomizationOptionsBundle;
import com.applicaster.web.plugins.iai.R;

import java.util.ArrayList;

import static com.applicaster.Constants.ANIMATION_DURATION;


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
            holder.mNumberText.setText(String.valueOf(mKeyValues.get(position)));
            holder.mNumberButton.setVisibility(View.VISIBLE);
            holder.mNumberButton.setTag(mKeyValues.get(position));
            holder.mNumberButton.setBackgroundResource(R.drawable.number_btn_not_selected_bg);

            if (CustomizationOptionsBundle.getInstance() != null) {

                holder.mNumberText.setTextColor(CustomizationOptionsBundle.getInstance().getTextColor());
                holder.mNumberText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        CustomizationOptionsBundle.getInstance().getTextSize());

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
        View mNumberButton;
        TextView mNumberText;

        public NumberViewHolder(final View itemView) {
            super(itemView);
            mNumberButton = itemView.findViewById(R.id.button);
            mNumberText = itemView.findViewById(R.id.textNumber);
            mNumberText.setTypeface(CustomizationOptionsBundle.getInstance().getTextFont());
            setTextcolor(mNumberText, CustomizationOptionsBundle.getInstance().getTextColor());
            mNumberButton.setBackgroundResource(R.drawable.number_btn_not_selected_bg);


            mNumberButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:{
                            mNumberButton.setBackgroundResource(R.drawable.number_btn_selected_bg);
                            mNumberText.setTextColor(CustomizationOptionsBundle.getInstance().getTextColorSelected());
                            return false;
                        }
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP: {
                            mNumberButton.setBackgroundResource(R.drawable.number_btn_not_selected_bg);
                            mNumberText.setTextColor(CustomizationOptionsBundle.getInstance().getTextColor());
                            return false;
                        }
                    }

                    return false;
                }

            });

            mNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
