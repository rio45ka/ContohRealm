package com.rioaska.contohrealm.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rioaska.contohrealm.DataModelDb;
import com.rioaska.contohrealm.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rio on 02/11/15.
 */
public class ListDataAdapter extends RealmRecyclerViewAdapter<DataModelDb> {

    private Context mContext;
    private int lastPosition = -1;
    private UbahDataInterface mUbahDataInterface;
    private HapusDataInterface mHapusDataInterface;

    public ListDataAdapter(Context context) {
        this.mContext = context;
    }

    public void ubahData(UbahDataInterface ubahDataInterface) {
        this.mUbahDataInterface = ubahDataInterface;
    }

    public void hapusData(HapusDataInterface hapusDataInterface) {
        this.mHapusDataInterface = hapusDataInterface;
    }

    @Override
    public DataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);

        return new DataHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        DataHolder holder = (DataHolder) viewHolder;
        DataModelDb dataItem = getItem(position);

        holder.tvNama.setText(dataItem.getNama());
        holder.tvHape.setText("" + dataItem.getNo_hp());

        setAnimation(holder.cardView, position);

        holder.tvUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUbahDataInterface.ubahData(v, position);
            }
        });

        holder.tvHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHapusDataInterface.hapusData(v, position);
            }
        });

    }

    /* The inner RealmBaseAdapter
     * view count is applied here.
     *
     * getRealmAdapter is defined in RealmRecyclerViewAdapter.
     */
    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public interface UbahDataInterface {
        void ubahData(View view, int position);
    }

    public interface HapusDataInterface {
        void hapusData(View view, int position);
    }

    class DataHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_nama_item_list)
        TextView tvNama;
        @Bind(R.id.tv_nohape_item_list)
        TextView tvHape;
        @Bind(R.id.tv_ubah_item_list)
        TextView tvUbah;
        @Bind(R.id.tv_hapus_item_list)
        TextView tvHapus;
        @Bind(R.id.cv_item_kelas_list)
        CardView cardView;

        public DataHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * This for getDrawable deprecated
     */
    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}
