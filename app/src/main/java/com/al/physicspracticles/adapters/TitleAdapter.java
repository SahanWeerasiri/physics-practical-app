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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.ExtraPapersActivity;
import com.al.physicspracticles.Practical;
import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.ViewHolder>{
    private List<TitleModel> titleModels;
    private Context context;
    private Activity activity;
    private int holder_type;
    public TitleAdapter(List<TitleModel> titleModels, Activity activity,int type)
    {   this.titleModels=titleModels;
        this.context=activity.getBaseContext();
        this.activity=activity;
        this.holder_type=type;
    }

    @NonNull
    @Override
    public TitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.title_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title_name_txt.setText(titleModels.get(position).getTitle());



        if(holder_type==SUBJECT_TO_PRACTICAL_TYPE){
            //Picasso.get().load(titleModels.get(position).getUri()).into(holder.imageView_title);
            holder.imageView_title.setImageResource(titleModels.get(position).getRes());
        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ChoosePractical.class);
                intent.putExtra(PARSING_TOPIC,titleModels.get(position).getId());
             activity.startActivity(intent);
             activity.finish();

            }
        });
        }else if(holder_type==PRACTICAL_TO_SUBJECT_TYPE){
            String root=titleModels.get(position).getRoot();
            //Picasso.get().load(titleModels.get(position).getUri()).into(holder.imageView_title);
            holder.imageView_title.setImageResource(titleModels.get(position).getRes());
            holder.title_name_txt.setTextSize(11.0F);
            holder.title_name_txt.setMaxLines(6);
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, Practical.class);
                    intent.putExtra(PARSING_TOPIC,root);
                    intent.putExtra(BOOK_OR_PRACTICAL_VIDEO,PARSING_PRACTICAL);
                    intent.putExtra(PARSING_PRACTICAL,titleModels.get(position).getId());
                    activity.startActivity(intent);
                    //activity.finish();

                }
            });
        }else if(holder_type==RESOURCE_BOOKS){
            String Url=titleModels.get(position).getUrl();
            if(!Url.equals(NO_RESULTS)){
                holder.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(activity, Practical.class);
                        intent.putExtra(BOOK_OR_PRACTICAL_VIDEO,PARSING_BOOK_LABEL);
                        intent.putExtra(RESOURCE_BOOK_NAME,titleModels.get(position).getTitle());
                        intent.putExtra(RESOURCE_BOOK_URL,Url);

                        activity.startActivity(intent);
                        //activity.finish();

                    }
                });
            }else{
                Picasso.get().load(R.drawable.error).into(holder.imageView_title);
            }

        }
        else if(holder_type==EXTRA_VIDEOS){
            holder.imageView_title.setImageResource(R.drawable.ic_baseline_video_library_24);
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, Practical.class);
                    intent.putExtra(BOOK_OR_PRACTICAL_VIDEO,PARSING_EXTRA_VIDEO_LABEL);
                    intent.putExtra(PARSING_EXTRA_VIDEO_LABEL,titleModels.get(position).getId());

                    activity.startActivity(intent);
                    //activity.finish();

                }
            });
        }else if(holder_type==PAST_PAPERS){
            //Picasso.get().load(titleModels.get(position).getUri()).into(holder.imageView_title);
            holder.imageView_title.setImageResource(R.drawable.qa);
            holder.ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(activity, ExtraPapersActivity.class);
                    intent.putExtra(PARSING_MENU_TYPE,TYPE_PAST_PAPERS_ANSWERS);
                    intent.putExtra(PARSING_TOPIC,titleModels.get(position).getTitle());
                    activity.startActivity(intent);

                }
            });
        }else if(holder_type==TYPE_LINKS){
            //Picasso.get().load(titleModels.get(position).getUri()).into(holder.imageView_title);
            String type=titleModels.get(position).getId();
            String link=titleModels.get(position).getUrl();
            if(type.equals(LINK_TYPE_TELEGRAM)){
                holder.imageView_title.setImageResource(R.drawable.telegram);
            }else if(type.equals(LINK_TYPE_WHATSAPP)){
                holder.imageView_title.setImageResource(R.drawable.whatsapp);
            }else if(type.equals(LINK_TYPE_WEBSITE)){
                holder.imageView_title.setImageResource(R.drawable.web);
            }
            if(!link.equals(NO_RESULTS)){
                holder.ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse(link));
                        activity.startActivity(telegram);
                        //activity.finish();

                    }
                });
            }else{
                Picasso.get().load(R.drawable.error).into(holder.imageView_title);
            }


        }

        holder.imageView_title.setVisibility(View.INVISIBLE);
        holder.imageView_title.setVisibility(View.VISIBLE);

        //holder.setData(img,name,time,line);
    }

    @Override
    public int getItemCount() {
        return titleModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView imageView_title;
        private TextView title_name_txt;
        private RelativeLayout ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll=(RelativeLayout) itemView.findViewById(R.id.title_item);
            title_name_txt=(TextView)itemView.findViewById(R.id.txt_title_name);
            imageView_title=(ShapeableImageView) itemView.findViewById(R.id.shapeableImageView_title);

        }

    }

}
