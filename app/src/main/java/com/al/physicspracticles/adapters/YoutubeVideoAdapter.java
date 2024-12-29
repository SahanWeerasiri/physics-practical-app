package com.al.physicspracticles.adapters;

import static com.al.physicspracticles.DatabaseNames.BOOK_OR_PRACTICAL_VIDEO;
import static com.al.physicspracticles.DatabaseNames.EXTRA_VIDEOS;
import static com.al.physicspracticles.DatabaseNames.EXTRA_YOUTUBE_VIDEO;
import static com.al.physicspracticles.DatabaseNames.PARSING_BOOK_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_EXTRA_VIDEO_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_PRACTICAL;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.SUBJECT_TO_PRACTICAL_TYPE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.Practical;
import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.YoutubeVideoModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeVideoAdapter.ViewHolder>{
    private List<YoutubeVideoModel> youtubeVideoModelList;
    private Context context;
    private Activity activity;
    public YoutubeVideoAdapter(List<YoutubeVideoModel> youtubeVideoModelList, Activity activity)
    {   this.youtubeVideoModelList=youtubeVideoModelList;
        this.context=activity.getBaseContext();
        this.activity=activity;
    }

    @NonNull
    @Override
    public YoutubeVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_item,parent,false);
        return new YoutubeVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String type=youtubeVideoModelList.get(position).getType();

        setWebView(holder.wv,youtubeVideoModelList.get(position).getUrl(),type);
        //holder.wv.loadUrl("https://www.youtube.com/m/"+youtubeVideoModelList.get(position).getUrl());
        //setVideo(holder.wv,youtubeVideoModelList.get(position).getUrl());
        holder.youtube_name_txt.setText(youtubeVideoModelList.get(position).getName());

        //holder.setData(img,name,time,line);
    }

    @Override
    public int getItemCount() {
        return youtubeVideoModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView youtube_name_txt;
        private WebView wv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            youtube_name_txt=(TextView)itemView.findViewById(R.id.txt_youtube_video_name);
            wv=(WebView) itemView.findViewById(R.id.youtube_video);

        }

    }
    /*private void setVideo(WebView myWebView,String myYouTubeVideoUrl){

        String dataUrl =
                "<html>" +
                        "<body>" +
                        "<h2>Video From YouTube</h2>" +
                        "<br>" +
                        "<iframe width=\"560\" height=\"315\" src=\""+myYouTubeVideoUrl+"\" frameborder=\"0\" allowfullscreen/>" +
                        "</body>" +
                        "</html>";


        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.loadData(dataUrl, "text/html", "utf-8");
    }*/
    private void setWebView(WebView wv,String url,String type)
    {
        wv.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = wv.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        wv.setWebViewClient(new WebViewClient());


        wv.setBackgroundColor(0x00000000);

        wv.setKeepScreenOn(true);

        wv.setHorizontalScrollBarEnabled(false);
        wv.setVerticalScrollBarEnabled(false);

        wv.getSettings().setBuiltInZoomControls(false);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html;
        if(type.equals(EXTRA_YOUTUBE_VIDEO)){
            html= getHTMLYoutube(url);
        }else{
            html= getHTMLNormal2(url);
        }


        wv.loadDataWithBaseURL("", html, mimeType, encoding, "");

    }


    /*public String getHTML(String url)
    {

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe "
                + "type=\"text/html\" "
                + "class=\"youtube-player\" "
                + "width= 100%\""
                + "\" "
                + "height= 95%\""
                + "\" "
                + "src=\"https://www.youtube.com/embed/"
                + url
                + "?controls=0&embedded=true&showinfo=0&showsearch=0&modestbranding=0" +
                "&autoplay=1&fs=1&vq=hd720\" " + "frameborder=\"0\"></iframe>"
                + "</body>"
                + "</html>";

        return html;
    }*/
    public String getHTMLYoutube(String url)
    {

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe width=\"1280\" height=\"720\" src=\"https://www.youtube.com/embed/"+url+"\" frameborder=\"0\" allowfullscreen=\"true\"></iframe>"

                + "</body>"
                + "</html>";

        return html;
    }
    public String getHTMLNormal(String url)
    {

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe width=\"100%\" height=\"500\" src=\""+url+"\" frameborder=\"0\" allowfullscreen=\"true\"></iframe>"

                + "</body>"
                + "</html>";

        return html;
    }
    /*video:-webkit-full-page-media {
        position: absolute;
        top: 0px;
        right: 0px;
        bottom: 0px;
        left: 0px;
        max-height: 100%;
        max-width: 100%;
        margin: auto;
        margin-top: ;
        margin-right: ;
        margin-bottom: ;
        margin-left:} ;*/

    public String getHTMLNormal2(String url)
    {

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe width=\"100%\" height=\"500\" src=\""+url+"\" frameborder=\"0\" allowfullscreen=\"true\">" +
                "<video object-fit=\"contain\" overflow=\"clip\" overflow-clip-margin=\"content-box\"></video>" +
                "</iframe>"

                + "</body>"
                + "</html>";

        return html;
    }
    }

