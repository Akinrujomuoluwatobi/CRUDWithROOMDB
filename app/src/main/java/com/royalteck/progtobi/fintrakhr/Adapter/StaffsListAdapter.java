package com.royalteck.progtobi.fintrakhr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.royalteck.progtobi.fintrakhr.R;
import com.royalteck.progtobi.fintrakhr.model.UserModel;

import java.util.List;

public class StaffsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserModel> items;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnClickListener onClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, UserModel obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public StaffsListAdapter(Context context, List<UserModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView staffName, staffPosition, staffSalary;
        CardView mCardView;


        public OriginalViewHolder(View v) {
            super(v);
            //image = v.findViewById(R.id.image);
            staffName = v.findViewById(R.id.staffName);
            staffPosition = v.findViewById(R.id.positionTxtView);
            staffSalary = v.findViewById(R.id.staffSalaryTxtView);
            mCardView = v.findViewById(R.id.cardLayout);




        }

    }

    @androidx.annotation.NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslayout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final UserModel p = items.get(position);
            view.staffName.setText(p.getName());
            view.staffPosition.setText(p.getPosition());
            view.staffSalary.setText("#"+p.getSalary());
            view.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener == null) return;
                    onClickListener.onItemClick(v, p, position);
                }
            });

            //Tools.displayImageOriginal(ctx, view.image, p.image);

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnClickListener {
        void onItemClick(View view, UserModel obj, int pos);

        void onItemLongClick(View view, UserModel obj, int pos);
    }
}