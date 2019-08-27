package com.shlomi.instagramapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Models.Comment;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.R;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private LayoutInflater layoutInflater;
    private int layaoutResource;
    private Context context;


    public CommentListAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        layaoutResource = resource;
    }
    public static class ViewHolder{

        TextView comment;
        ImageView profile_imag;
        TextView userName;
        TextView timeStamp;
        TextView reply;


    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView,  @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){

          convertView = layoutInflater.inflate(layaoutResource,parent,false);
          holder = new ViewHolder();
          holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.userName = (TextView) convertView.findViewById(R.id.commentUserName);
            holder.reply = (TextView) convertView.findViewById(R.id.commentReplay);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.commentTimePost);
            holder.profile_imag = (ImageView) convertView.findViewById(R.id.commentProfileImage);
           convertView.setTag(holder);
        }else{

            holder = (ViewHolder) convertView.getTag();
        }
       holder.comment.setText(getItem(position).getComment());

        String timeStamp = getTimeStapDiffrent(getItem(position));
        if(!timeStamp.equals("0")){
            holder.timeStamp.setText(timeStamp + " d");
        }else{

            holder.timeStamp.setText("today");

        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(context.getString(R.string.dbname_users_account_settings)).
                orderByChild(context.getString(R.string.filed_user_id)).equalTo(getItem(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singalSnapShot :dataSnapshot.getChildren()){
                  holder.userName.setText(singalSnapShot.getValue(UserAccountSetting.class).getUserName());
                    ImageLoader imageLoader  = ImageLoader.getInstance();
                    imageLoader.displayImage(singalSnapShot.getValue(UserAccountSetting.class).getProfile_photo(),holder.profile_imag);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  convertView;
    }

    private String getTimeStapDiffrent(Comment comment){
        String dif="";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss'Z", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Israel"));
        Date today = c.getTime();
        sdf.format(today);
        Date timeStamp;
        final String photoTimeStamp = comment.getDate_created();
        try {
        timeStamp = sdf.parse(photoTimeStamp);
        dif = String.valueOf(Math.round((today.getTime()-timeStamp.getTime() / 1000 / 60 / 60 / 24)));


        }catch (ParseException e){}

return  dif;

    }
}
