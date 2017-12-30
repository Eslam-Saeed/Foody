package com.udacity.bakingapp.recipe_details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.models.Step;

import java.util.ArrayList;

/**
 * Created by eslam on 12/30/17.
 */

public class AdapterSteps extends RecyclerView.Adapter<AdapterSteps.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Step> mListSteps;
    private ViewStepInteraction mViewStepInteraction;

    public AdapterSteps(Context context, ArrayList<Step> listSteps, ViewStepInteraction viewStepInteraction) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mListSteps = listSteps;
        this.mViewStepInteraction = viewStepInteraction;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_step, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Step step = mListSteps.get(position);
        if (step != null) {
            holder.txtStepTitle.setText(step.getShortDescription());
            holder.setListener(step);

            if (position == mListSteps.size() - 1)
                holder.viewSeparator.setVisibility(View.GONE);
            else
                holder.viewSeparator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mListSteps.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtStepTitle;
        private View viewSeparator;

        MyViewHolder(View itemView) {
            super(itemView);
            txtStepTitle = itemView.findViewById(R.id.txtStepTitle);
            viewSeparator = itemView.findViewById(R.id.viewSeparator);
        }

        void setListener(final Step step) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mViewStepInteraction != null)
                        mViewStepInteraction.onStepClicked(step);

                }
            });
        }
    }

    interface ViewStepInteraction {
        void onStepClicked(Step step);
    }
}

