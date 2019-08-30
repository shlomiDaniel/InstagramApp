package com.shlomi.instagramapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridPhotoAdapter extends ArrayAdapter<Photo> {
    private Context mContext;
    private LayoutInflater mInflater;
    private String maPpend;
    private ArrayList<Photo> images;
    private int layoutRes;

    public GridPhotoAdapter(Context context, int layoutRes, String maPpend, ArrayList<Photo> images) {
        super(context, layoutRes, images);
        this.images = images;
        this.maPpend = maPpend;
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.layoutRes = layoutRes;
    }

    private static class ViewHolder {
        SqaureImageView img;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutRes, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.progressBar = convertView.findViewById(R.id.gridImgProgressbar);
            viewHolder.img = convertView.findViewById(R.id.gridViewId);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Photo image = images.get(position);

        Picasso.get().load(image.getImage_path()).into(viewHolder.img, new Callback() {
            @Override
            public void onSuccess() {
                if (viewHolder.progressBar != null) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Exception e) {
                if (viewHolder.progressBar != null) {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }

                try {
                    images.remove(position);
                    notifyDataSetChanged();
                }catch (IndexOutOfBoundsException err){
                    err.printStackTrace();
                }
            }
        });

        return convertView;
    }
}
