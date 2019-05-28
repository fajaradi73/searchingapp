package com.fajarproject.searchingapp.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fajarproject.searchingapp.Model.UserModel;
import com.fajarproject.searchingapp.R;
import com.fajarproject.searchingapp.Rest.JSONResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> {

    private List<UserModel> viewItemList;
    private List<UserModel> mArrayList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private String searchString = "";
    private Boolean clicked = false;

    public UserAdapter(List<UserModel> viewItemList, Context context) {
        this.viewItemList = viewItemList;
        this.mArrayList = viewItemList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user, parent, false);
        MyHolder myHolder = new MyHolder(itemView,onItemClickListener);
        return myHolder;
    }


    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        // Get car item dto in list.
        UserModel viewItem = viewItemList.get(position);
        holder.name.setText(viewItem.getNama());
        // Find charText in wp
        String nama                  = viewItem.getNama().toLowerCase(Locale.getDefault());
        if (nama.contains(searchString )) {
            int startPos    = nama.indexOf(searchString);
            int endPos      = startPos + searchString.length();
            Spannable spanText = Spannable.Factory.getInstance().newSpannable(holder.name.getText());
            spanText.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.name.setText(spanText, TextView.BufferType.SPANNABLE);
        }

        Glide.with(context).load(viewItem.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return viewItemList.size();
    }

    public Filter getFilter(String searchString) {
        this.searchString = searchString;
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    viewItemList = mArrayList;
                } else {

                    ArrayList<UserModel> filteredList = new ArrayList<>();

                    for (UserModel androidVersion : mArrayList) {

                        if (androidVersion.getNama().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    viewItemList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = viewItemList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                viewItemList = (ArrayList<UserModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name,email;
        ImageView imageView;
        OnItemClickListener onItemClickListener;

        MyHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            name        = itemView.findViewById(R.id.nama);
            imageView   = itemView.findViewById(R.id.image_user);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

}