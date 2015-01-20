package com.gustavogoldhardt.flashback.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gustavogoldhardt.flashback.R;
import com.gustavogoldhardt.flashback.model.Flashback;
import com.gustavogoldhardt.flashback.widget.AddFlashBackView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by gustavogoldhardt on 12/22/14.
 */
public class FlashbackAdapter extends RecyclerView.Adapter<FlashbackAdapter.ViewHolder> {

    private ArrayList<Flashback> mFlashbacksList;

    public FlashbackAdapter() {
        mFlashbacksList = new ArrayList<Flashback>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;
        itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.flashback, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Flashback flashback = mFlashbacksList.get(position);

        // Set date
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        viewHolder.vDate.setText(df.format(flashback.getDate()));

        //Set index
        viewHolder.vIndex.setText("" + flashback.getIndex());

        //Set pic
        viewHolder.vImageView.setImageBitmap(flashback.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return mFlashbacksList.size();
    }

    public void setFlashbacksList(ArrayList<Flashback> list) {
        mFlashbacksList = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView vImageView;
        public TextView vDate;
        public TextView vIndex;

        public ViewHolder(View v) {
            super(v);
            if(!(v instanceof AddFlashBackView)) {
                vImageView = (ImageView) v.findViewById(R.id.thumbnail);
                vDate = (TextView) v.findViewById(R.id.date);
                vIndex = (TextView) v.findViewById(R.id.day);
            }
        }
    }
}
