package com.al.physicspracticles.practicalFragments;


import static com.al.physicspracticles.DatabaseNames.EXTRA_YOUTUBE_VIDEO;
import static com.al.physicspracticles.DatabaseNames.PRACTICALS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_NAME;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.ROOT;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.PracticalModel;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.YoutubeVideoModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.adapters.YoutubeVideoAdapter;
import com.al.physicspracticles.databinding.FragmentVideoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {
    private FragmentVideoBinding binding;
    private String Url;

    private LinearLayoutManager llm;
    private YoutubeVideoAdapter ad;
    private List<YoutubeVideoModel> youtubeVideoModelList;

    private PracticalModel practicalModel;

    public VideoFragment(PracticalModel practicalModel, List<YoutubeVideoModel> youtubeVideoModelList){
        this.practicalModel=practicalModel;
        this.youtubeVideoModelList=youtubeVideoModelList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding=FragmentVideoBinding.inflate(inflater,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Url=practicalModel.getVideo_url();
        if(Url.equals("None")){
            binding.webViewNormalVideo.setVisibility(View.GONE);
            binding.videoDownloadBtn.setVisibility(View.GONE);
            binding.floatingActionButtonPlay.setVisibility(View.GONE);
        }
        if(youtubeVideoModelList.size()==0){
            binding.btnYoutubeLoader.setVisibility(View.GONE);
        }

        binding.floatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Url=practicalModel.getVideo_url();
                setWebView(binding.webViewNormalVideo, Url);
                    /*binding.videoViewVideo.setVideoURI(Uri.parse(Url));
                    MediaController mediaController=new MediaController(getContext());
                    binding.videoViewVideo.setMediaController(mediaController);
                    mediaController.setAnchorView(binding.videoViewVideo);*/


            }
        });
        binding.btnYoutubeLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llm=new LinearLayoutManager(getContext());
                initRecyclerView();

            }
        });




        binding.videoDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(Url));
                String name= URLUtil.guessFileName(Url,null,null);
                request.setTitle(name);
                request.setDescription("Downloading Video. Please wait...");
                request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(Url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name);

                DownloadManager downloadManager=(DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(getContext(), "Downloading started", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }


    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.youtubeRecyclerView.setLayoutManager(llm);
        ad=new YoutubeVideoAdapter(youtubeVideoModelList,getActivity());
        binding.youtubeRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }
    private void setWebView(WebView wv, String url)
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

        wv.getSettings().setBuiltInZoomControls(true);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        String html;

            html= getHTMLNormal(url);
        //html=getHTML(url);


        wv.loadDataWithBaseURL("", html, mimeType, encoding, "");

    }
    public String getHTML(String url){
        String htmlPre = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"utf-8\"></head><body style='margin:0; pading:0; background-color: black;'>";
        String htmlCode = " <embed style='width:100%; height:100%' src='@VIDEO@' " + " autoplay='true' " + " quality='high' bgcolor='#000000' " + " name='VideoPlayer' align='middle'" + // width='640' height='480'
                " allowScriptAccess='*' allowFullScreen='true'" + " type='application/x-shockwave-flash' " +
                " pluginspage='http://www.macromedia.com/go/getflashplayer' />" + "";
        String htmlPost = "</body></html>";

        htmlCode = htmlCode.replaceAll("@VIDEO@", url);
        return htmlPre+htmlCode+htmlPost;
    }
    public String getHTMLNormal(String url)
    {

        String html = "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"
                + "<iframe width=\"1024\" height=\"1024\" src=\""+url+"\" frameborder=\"0\" allowfullscreen=\"true\"></iframe>"

                + "</body>"
                + "</html>";
        /*String html= "<html>"

                + "<head>"
                + "</head>"
                + "<body style=\"border: 0; padding: 0\">"+
                "<style>" +
                "video{" +
                "display:inline;height:auto;max-width:100%;" +
                "}" +
                "</style>"
                + "</body>"
                + "</html>";*/

        return html;
    }
}