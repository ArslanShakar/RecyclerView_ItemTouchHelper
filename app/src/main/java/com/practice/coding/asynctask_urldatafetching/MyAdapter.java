package com.practice.coding.asynctask_urldatafetching;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VHolder> {

    private ArrayList<String> arrayList;

    private Context mContext;

    public MyAdapter(Context context, ArrayList<String> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_student, viewGroup, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder vh, int position) {
        vh.tvName.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        public Button btDelete;
        public LinearLayout viewBackground, viewForeground;

        public VHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tvName);
            btDelete = view.findViewById(R.id.btDelete);

            viewBackground = view.findViewById(R.id.backgroundView);
            viewForeground = view.findViewById(R.id.foregroundView);
        }

    }

    //function for removing rows
    public void removeItem(int pos) {
        arrayList.remove(pos);
        notifyItemRemoved(pos);
    }

    //function for swapping rows
    public boolean onItemDragUpOrDown(int fromPosition, int toPosition) {
        if (fromPosition > toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(arrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(arrayList, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
