package com.al.physicspracticles;

import static com.al.physicspracticles.DatabaseNames.APP_ID;
import static com.al.physicspracticles.DatabaseNames.APP_INFO;
import static com.al.physicspracticles.DatabaseNames.BOOK_OR_PRACTICAL_VIDEO;
import static com.al.physicspracticles.DatabaseNames.EXTRA_YOUTUBE_VIDEO;
import static com.al.physicspracticles.DatabaseNames.PARSING_BOOK_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_EXTRA_VIDEO_LABEL;
import static com.al.physicspracticles.DatabaseNames.PARSING_PRACTICAL;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PDF_QA_TYPE;
import static com.al.physicspracticles.DatabaseNames.PRACTICALS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_INDEX;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_MANUALS_SINHALA;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_PAPERS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_PAPER_NAME;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_VIDEOS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_YOUTUBE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_ID;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_NAME;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_URL;
import static com.al.physicspracticles.DatabaseNames.ROOT;
import static com.al.physicspracticles.DatabaseNames.ROOT_APP;
import static com.al.physicspracticles.DatabaseNames.ROOT_RESOURCE;

import android.app.Dialog;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.al.physicspracticles.practicalFragments.GraphFragment;
import com.al.physicspracticles.practicalFragments.PdfFragment;
import com.al.physicspracticles.practicalFragments.QAFragment;
import com.al.physicspracticles.practicalFragments.VideoFragment;
import com.al.physicspracticles.ui.gallery.GalleryFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.al.physicspracticles.ui.main.SectionsPagerAdapter;
import com.al.physicspracticles.databinding.ActivityPracticalBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Practical extends AppCompatActivity {

    private ActivityPracticalBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private boolean isGraphAdded=false;


    private static String title=null,practical=null,book = null,video=null,book_url=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityPracticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarPractical);
        String code=getIntent().getStringExtra(BOOK_OR_PRACTICAL_VIDEO);
        if(code.equals(PARSING_PRACTICAL)) {
            title = getIntent().getStringExtra(PARSING_TOPIC);
            practical = getIntent().getStringExtra(PARSING_PRACTICAL);
        }else if(code.equals(PARSING_BOOK_LABEL)){
            book= getIntent().getStringExtra(RESOURCE_BOOK_NAME);
            book_url=getIntent().getStringExtra(RESOURCE_BOOK_URL);
            binding.actionBarPractical.setVisibility(View.GONE);
        }else if(code.equals(PARSING_EXTRA_VIDEO_LABEL)){
            video=getIntent().getStringExtra(PARSING_EXTRA_VIDEO_LABEL);
            binding.actionBarPractical.setVisibility(View.GONE);
        }

        firebaseFirestore=FirebaseFirestore.getInstance();

        ViewPager viewPager = binding.viewPager;
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        /*FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        SectionsPagerAdapter ma=new SectionsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        if(code.equals(PARSING_PRACTICAL)){
            firebaseFirestore.collection(ROOT).document(title).collection(PRACTICALS).document(practical).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String url_manual_e=task.getResult().get(PRACTICAL_MANUALS_SINHALA).toString();
                        String url_video=task.getResult().get(PRACTICAL_VIDEOS).toString();
                        HashMap<Object, Object> youtube_map ;
                        List<YoutubeVideoModel> youtubeVideoModelList=new ArrayList<YoutubeVideoModel>();
                        try {
                            youtube_map = (HashMap<Object, Object>) task.getResult().get(PRACTICAL_YOUTUBE);
                            Object[] keys = youtube_map.keySet().toArray();

                            for (int i = 0; i < keys.length; i++) {
                                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel(keys[i].toString(), youtube_map.get(keys[i]).toString(),EXTRA_YOUTUBE_VIDEO);
                                youtubeVideoModelList.add(youtubeVideoModel);
                            }
                        }catch (Exception e){

                        }

                        //papers
                        HashMap<Object, Object> paper_map_base = null;
                        List<TitleModel> titleModelList=new ArrayList<TitleModel>();
                        try {
                            paper_map_base = (HashMap<Object, Object>) task.getResult().get(PRACTICAL_PAPERS);
                            Object[] keys = paper_map_base.keySet().toArray();

                            for (int i = 0; i < keys.length; i++) {
                                titleModelList.add(new TitleModel(String.valueOf(i),keys[i].toString(),paper_map_base.get(keys[i]).toString()));
                            }
                        }catch (Exception e){

                        }
                        //String url_calculation=task.getResult().get(PRACTICAL_CALCULATION).toString();
                        int index=Integer.parseInt(task.getResult().get(PRACTICAL_INDEX).toString());

                        PracticalModel practicalModel=new PracticalModel(url_manual_e,url_video,index,titleModelList);

                        ma.addFragments(new PdfFragment(practicalModel),"Manual");
                        ma.addFragments(new VideoFragment(practicalModel,youtubeVideoModelList),"Video");

                    /*switch (index){
                        //case 1:ma.addFragments(new P01_Vernier(practicalModel),"Calculation");break;
                        case 1:ma.addFragments(new P06_Principle_Of_Moments(practicalModel),"Calculation");break;
                        case 2:ma.addFragments(new P02_Micrometer(practicalModel),"Calculation");break;
                        case 3:ma.addFragments(new P03_Spherometer(practicalModel),"Calculation");break;
                        case 4:ma.addFragments(new P04_Microscope(practicalModel),"Calculation");break;
                        case 5:ma.addFragments(new P05_Law_Of_Parallelogram(practicalModel),"Calculation");break;
                        //case 6:ma.addFragments(new P06_Principle_Of_Moments(practicalModel),"Calculation");break;
                    }*/
                        int[] practicals={6,7,8,9,10,11,12,13,15,17,21,22,23,24,26,31,32,34,35,36,38};
                        //17 curve
                        //26 cure 2
                        //35 curve
                        //36 curve
                        int Linear=0;
                        int Curve=1;
                        int Curve2=2;
                        for (int i=0;i<practicals.length;i++){
                            if(index==practicals[i]){
                                if(index==17||index==35||index==36){
                                    ma.addFragments(new GraphFragment(Curve),"Graph");
                                }else if(index==26){
                                    ma.addFragments(new GraphFragment(Curve2),"Graph");
                                }else{
                                    ma.addFragments(new GraphFragment(Linear),"Graph");
                                }
                                isGraphAdded=true;

                            }else{

                            }
                        }
                        if(titleModelList.size()!=0){
                            ma.addFragments(new QAFragment(titleModelList),"Q&A");
                        }


                        viewPager.setAdapter(ma);
                    }else{
                        Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }



                }
            });
        }
        else if(code.equals(PARSING_BOOK_LABEL)){

            PracticalModel practicalModel=new PracticalModel(book_url,null,0,null);

            ma.addFragments(new PdfFragment(practicalModel),book);


            viewPager.setAdapter(ma);
        }else if(code.equals(PARSING_EXTRA_VIDEO_LABEL)){

       /* firebaseFirestore.collection(ROOT_EXTRA).document(video).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String url_video=task.getResult().get(EXTRA_VIDEO_URL).toString();
                    String name=task.getResult().get(EXTRA_VIDEO_NAME).toString();
                    int index=Integer.parseInt(video);

                    PracticalModel practicalModel=new PracticalModel(null,url_video,index);
                    ma.addFragments(new VideoFragment(practicalModel,null),name);


                    viewPager.setAdapter(ma);
                }else{
                    Toast.makeText(Practical.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }

            }
        });*/


    }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();

    }
    @Override
    protected void onStart() {


        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_info && isGraphAdded){



                            Dialog dialog_info=new Dialog(Practical.this);
                            dialog_info.setContentView(R.layout.pop_up);
                            dialog_info.setCancelable(false);
                            TextView msg=dialog_info.findViewById(R.id.txt_popup_msg);
                            ImageButton close=dialog_info.findViewById(R.id.txt_popup_info_close);

                            String info="If x1/y1 or y1/x1 is less than 0.01, then change your x values' scale or y values' scale as x/y>=0.01 or y/x>=0.01\n\nOtherwise you can't get the " +
                                    "best fitting graph";
                            msg.setText(info);

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog_info.cancel();
                                }
                            });
                            dialog_info.show();
        }else{
            Snackbar.make( binding.getRoot(),"No Info", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        return super.onOptionsItemSelected(item);
    }
}