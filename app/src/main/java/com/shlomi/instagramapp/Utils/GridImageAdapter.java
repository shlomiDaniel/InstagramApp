package com.shlomi.instagramapp.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.shlomi.instagramapp.R;

import java.util.ArrayList;

public class GridImageAdapter extends ArrayAdapter<String> {


    private Context mContext;
    private LayoutInflater mInflater;
    private String  maPpend;
    private ArrayList<String> imgUrls;
    private int layoutRes;


    public GridImageAdapter(Context context ,  int layoutRes,String maPpend, ArrayList<String> imgUrls){

        super(context,layoutRes,imgUrls);
        this.imgUrls = imgUrls;
       this.maPpend = maPpend;
       this.mContext = mContext;
          this.mInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.layoutRes = layoutRes;

    }

    private  static class  ViewHolder{
     SqaureImageView img;
     ProgressBar progressBar;





    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutRes,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.gridImgProgressbar);
            viewHolder.img = (SqaureImageView)convertView.findViewById(R.id.gridViewId);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
String imgUrl = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(maPpend + imgUrl, viewHolder.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.GONE);

                }
            }



            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if(viewHolder.progressBar != null){
                    viewHolder.progressBar.setVisibility(View.GONE);

                }
            }
        });
        return convertView;

    }
}
