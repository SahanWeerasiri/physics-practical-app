package com.al.physicspracticles;

import static com.al.physicspracticles.DatabaseNames.PARSING_MENU_TYPE;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PRACTICALS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_NAME;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.ROOT;
import static com.al.physicspracticles.DatabaseNames.TITLE_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.TITLE_NAME;
import static com.al.physicspracticles.DatabaseNames.TYPE_EXTRA_PAPERS;
import static com.al.physicspracticles.DatabaseNames.TYPE_MCQ;
import static com.al.physicspracticles.DatabaseNames.TYPE_SHORT_NOTES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.ActivityChoosePracticalBinding;
import com.al.physicspracticles.databinding.FragmentHomeBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChoosePractical extends AppCompatActivity {
    private ActivityChoosePracticalBinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;
    private FirebaseFirestore firebaseFirestore;
    private static String TOPIC;
    private RewardedAd rewardedAd=null;
    private InterstitialAd mInterstitialAd;
    private ProgressDialog progressDialog;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private static HashMap<String ,Integer> res=new HashMap<String ,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        binding = ActivityChoosePracticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TOPIC=getIntent().getStringExtra(PARSING_TOPIC).toString();

        res.put("P01",R.drawable.p01);res.put("P02",R.drawable.p02);res.put("P03",R.drawable.p03);res.put("P04",R.drawable.p04);

        res.put("P05",R.drawable.p05);res.put("P06",R.drawable.p06);res.put("P07",R.drawable.p07);res.put("P08",R.drawable.p08);res.put("P09",R.drawable.p09);res.put("P10",R.drawable.p10);res.put("P11",R.drawable.p11);

        res.put("P12",R.drawable.p12_13);res.put("P13",R.drawable.p12_13);res.put("P14",R.drawable.p14_15);res.put("P15",R.drawable.p14_15);

        res.put("P16",R.drawable.p16);res.put("P17",R.drawable.p17_18);res.put("P18",R.drawable.p17_18);res.put("P19",R.drawable.p19_20);res.put("P20",R.drawable.p19_20);res.put("P21_1",R.drawable.p21);res.put("P21_2",R.drawable.p22);

        res.put("P22",R.drawable.p22_2);res.put("P23",R.drawable.p23);res.put("P24",R.drawable.p24);res.put("P25",R.drawable.p25);res.put("P26",R.drawable.p26);res.put("P27",R.drawable.p27);res.put("P28",R.drawable.p28);res.put("P29",R.drawable.p29);res.put("P30",R.drawable.p30);

        res.put("P31",R.drawable.p31);res.put("P32",R.drawable.p32);res.put("P33",R.drawable.p33_34);res.put("P34",R.drawable.p33_34);

        res.put("P35",R.drawable.p35);res.put("P36",R.drawable.p36);res.put("P37",R.drawable.p37);

        res.put("P38",R.drawable.p38);res.put("P39",R.drawable.p39);res.put("P40",R.drawable.p40);res.put("P41",R.drawable.p41);res.put("P42",R.drawable.p42);

        llm=new LinearLayoutManager(this);

        firebaseFirestore=FirebaseFirestore.getInstance();

        initData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //ad
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-1262150100678081/7697135962", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(ChoosePractical.this);


                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                    }
                });
        startActivity(new Intent(ChoosePractical.this,MainActivity.class));
        finish();

    }

    private void initData() {
        titleModelList=new ArrayList<TitleModel>();

        firebaseFirestore.collection(ROOT).document(TOPIC).collection(PRACTICALS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                        String practical_name=documentSnapshot.get(PRACTICAL_NAME).toString();
                        //Uri practical_uri= Uri.parse(documentSnapshot.get(PRACTICAL_IMAGE_URL).toString());


                        titleModelList.add(new TitleModel(practical_name,res.get(documentSnapshot.getId()),TOPIC,documentSnapshot.getId()));

                    }
                    initRecyclerView();
                }else{
                    /*Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                }
                if(titleModelList.size()==0){
                    Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }

    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.practicalsRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,ChoosePractical.this,PRACTICAL_TO_SUBJECT_TYPE);
        binding.practicalsRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_extra_papers){
            openMenu("Looking for Papers...",TYPE_EXTRA_PAPERS);
        }/*else if(item.getItemId()==R.id.action_mcq){
            openMenu("Looking for MCQ...",TYPE_MCQ);
        }else if(item.getItemId()==R.id.action_notes){
            openMenu("Looking for Notes...",TYPE_SHORT_NOTES);
        }*/
        return super.onOptionsItemSelected(item);
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
    private void openMenu(String loading_msg,int type){
        //ad
        progressDialog=new ProgressDialog(ChoosePractical.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage(loading_msg);

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-1262150100678081/7697135962", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(ChoosePractical.this);


                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;

                    }
                });

        Intent intent=new Intent(ChoosePractical.this,ExtraPapersActivity.class);
        intent.putExtra(PARSING_TOPIC,TOPIC);
        intent.putExtra(PARSING_MENU_TYPE,type);
        startActivity(intent);
    }
}