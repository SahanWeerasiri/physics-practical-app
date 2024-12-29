package com.al.physicspracticles.adapters;

import static com.al.physicspracticles.DatabaseNames.BOOK_OR_PRACTICAL_VIDEO;
import static com.al.physicspracticles.DatabaseNames.EXTRA_VIDEOS;
import static com.al.physicspracticles.DatabaseNames.LINK_TYPE_TELEGRAM;
import static com.al.physicspracticles.DatabaseNames.LINK_TYPE_WEBSITE;
import static com.al.physicspracticles.DatabaseNames.LINK_TYPE_WHATSAPP;
import static com.al.physicspracticles.DatabaseNames.NO_RESULTS;
import static com.al.physicspracticles.DatabaseNames.PARSING_BOOK_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_EXTRA_VIDEO_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_MENU_TYPE;
import static com.al.physicspracticles.DatabaseNames.PARSING_PRACTICAL;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PAST_PAPERS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_NAME;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_URL;
import static com.al.physicspracticles.DatabaseNames.SUBJECT_TO_PRACTICAL_TYPE;
import static com.al.physicspracticles.DatabaseNames.TYPE_LINKS;
import static com.al.physicspracticles.DatabaseNames.TYPE_PAST_PAPERS_ANSWERS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.AnnouncementModel;
import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.ExtraPapersActivity;
import com.al.physicspracticles.Practical;
import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>{
    private List<AnnouncementModel> announcementModelList;
    private Context context;
    private Activity activity;
    public AnnouncementAdapter(List<AnnouncementModel> announcementModelList, Activity activity)
    {   this.announcementModelList=announcementModelList;
        this.context=activity.getBaseContext();
        this.activity=activity;
    }

    @NonNull
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.txt_title.setText(announcementModelList.get(position).getTitle());
        holder.txt_msg.setText(announcementModelList.get(position).getMsg());
        holder.txt_date.setText(announcementModelList.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        return announcementModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date,txt_title,txt_msg;
        private RelativeLayout ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll=(RelativeLayout) itemView.findViewById(R.id.announcement_item);
            txt_date=(TextView)itemView.findViewById(R.id.txt_announce_date);
            txt_msg=(TextView)itemView.findViewById(R.id.txt_announce_msg);
            txt_title=(TextView)itemView.findViewById(R.id.txt_announce_name);
        }

    }

}
