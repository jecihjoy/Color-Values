package com.google.developer.colorvalue.data;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.developer.colorvalue.CardDetailsActivity;
import com.google.developer.colorvalue.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    public static final String STRING_COLOR_NAME = "color_name" ;
    public static final String STRING_COLOR_HEX = "color_hex";
    private Cursor mCursor;
    private Context context;

    public CardAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);

       /* Context mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));*/
    }

    @Override
    @TargetApi(Build.VERSION_CODES.N)
    public void onBindViewHolder(final CardAdapter.ViewHolder holder, int position) {
        // TODO bind data to view

        int id = mCursor.getColumnIndex(CardProvider.Contract.Columns._ID);
        int color_name = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_NAME);
        int color_hex = mCursor.getColumnIndex(CardProvider.Contract.Columns.COLOR_HEX);

        mCursor.moveToPosition(position);

        final int color_id = mCursor.getInt(id);
        final String c_name = mCursor.getString(color_name);
        final String c_hex = mCursor.getString(color_hex);

        holder.itemView.setTag(id);
        holder.name.setText(c_hex);

        //

       holder.cardView.setBackgroundColor(Color.parseColor(c_hex));

       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(holder.itemView.getContext(), CardDetailsActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               intent.putExtra(STRING_COLOR_HEX,c_hex);
               intent.putExtra(STRING_COLOR_NAME,c_name);
               intent.putExtra("color_id",color_id);
               holder.itemView.getContext().startActivity(intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        if (mCursor == null){
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    /**
     * Return a {@link Card} represented by this item in the adapter.
     * Method is used to run machine tests.
     *
     * @param position Cursor item position
     * @return A new {@link Card}
     */
    public Card getItem(int position) {
        if (mCursor.moveToPosition(position)) {
            return new Card(mCursor);
        }
        return null;
    }

    /**
     * @param data update cursor
     */
    public void swapCursor(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
    }

    /**
     * An Recycler item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.color_name);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
