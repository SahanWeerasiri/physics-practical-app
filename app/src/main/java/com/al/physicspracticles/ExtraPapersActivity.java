package com.al.physicspracticles;

import static com.al.physicspracticles.DatabaseNames.ANNOUNCEMENT_DATE;
import static com.al.physicspracticles.DatabaseNames.ANNOUNCEMENT_MSG;
import static com.al.physicspracticles.DatabaseNames.ANNOUNCEMENT_TITLE;
import static com.al.physicspracticles.DatabaseNames.COLLECTION_ANNOUNCEMENTS;
import static com.al.physicspracticles.DatabaseNames.COLLECTION_EXTRA;
import static com.al.physicspracticles.DatabaseNames.DOCUMENT_MCQ;
import static com.al.physicspracticles.DatabaseNames.DOCUMENT_NOTES;
import static com.al.physicspracticles.DatabaseNames.DOCUMENT_PAPERS;
import static com.al.physicspracticles.DatabaseNames.EXTRA_PAPERS;
import static com.al.physicspracticles.DatabaseNames.NO_RESULTS;
import static com.al.physicspracticles.DatabaseNames.PARSING_MENU_TYPE;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PRACTICALS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_NAME;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_ID;
import static com.al.physicspracticles.DatabaseNames.ROOT;
import static com.al.physicspracticles.DatabaseNames.ROOT_EXTRA;
import static com.al.physicspracticles.DatabaseNames.ROOT_PAPERS;
import static com.al.physicspracticles.DatabaseNames.TYPE_ANNOUNCEMENTS;
import static com.al.physicspracticles.DatabaseNames.TYPE_EXTRA_PAPERS;
import static com.al.physicspracticles.DatabaseNames.TYPE_MCQ;
import static com.al.physicspracticles.DatabaseNames.TYPE_PAST_PAPERS_ANSWERS;
import static com.al.physicspracticles.DatabaseNames.TYPE_SHORT_NOTES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.al.physicspracticles.adapters.AnnouncementAdapter;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.ActivityChoosePracticalBinding;
import com.al.physicspracticles.databinding.ActivityExtraPapersBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtraPapersActivity extends AppCompatActivity {
    private ActivityExtraPapersBinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;

    private AnnouncementAdapter announcementAdapter;
    private List<AnnouncementModel> announcementModelList;


    private FirebaseFirestore firebaseFirestore;
    private static String TOPIC;
    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    private static final int default_menu=0;
    private static int menu_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExtraPapersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu_type=getIntent().getIntExtra(PARSING_MENU_TYPE,default_menu);

        TOPIC=getIntent().getStringExtra(PARSING_TOPIC).toString();
        llm=new LinearLayoutManager(this);

        firebaseFirestore= FirebaseFirestore.getInstance();
        if(menu_type==default_menu){

        }else if(menu_type==TYPE_EXTRA_PAPERS){

            initData(COLLECTION_EXTRA,DOCUMENT_PAPERS);
        }else if(menu_type==TYPE_SHORT_NOTES){
            initData(COLLECTION_EXTRA,DOCUMENT_NOTES);
        }else if(menu_type==TYPE_MCQ){
            initData(COLLECTION_EXTRA,DOCUMENT_MCQ);
        }else if(menu_type==TYPE_PAST_PAPERS_ANSWERS){
            initDataPastPapers(TOPIC);
        }else if(menu_type==TYPE_ANNOUNCEMENTS){
            initDataAnnouncements();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initData(String collection,String document) {
        titleModelList=new ArrayList<TitleModel>();

        firebaseFirestore.collection(ROOT).document(TOPIC).collection(collection).document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        HashMap<String, Object> map= (HashMap<String, Object>) task.getResult().getData();


                    Object [] keys=map.keySet().toArray();
                    for(int i=0;i<keys.length;i++){

                        titleModelList.add(new TitleModel("0",keys[i].toString(),map.get(keys[i]).toString()));


                    }
                    }catch (Exception e){


            }
                    if(titleModelList.size()==0){
                        titleModelList.add(new TitleModel("0","No Results",NO_RESULTS));
                    }

                    initRecyclerView();
                }else{
                    Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


    }
    private void initDataAnnouncements() {
        announcementModelList=new ArrayList<AnnouncementModel>();

        firebaseFirestore.collection(COLLECTION_ANNOUNCEMENTS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                            String Date=documentSnapshot.get(ANNOUNCEMENT_DATE).toString();
                            String title=documentSnapshot.get(ANNOUNCEMENT_TITLE).toString();
                            String msg=documentSnapshot.get(ANNOUNCEMENT_MSG).toString();
                            msg=msg.replace('#','\n');
                            announcementModelList.add(new AnnouncementModel(Date,title,msg));

                        }
                        llm.setOrientation(RecyclerView.VERTICAL);
                        binding.extraPapersRecyclerView.setLayoutManager(llm);
                        announcementAdapter=new AnnouncementAdapter(announcementModelList,ExtraPapersActivity.this);
                        binding.extraPapersRecyclerView.setAdapter(announcementAdapter);
                        announcementAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Snackbar.make( binding.getRoot(),"No Connection", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else{
                    Snackbar.make( binding.getRoot(),"No Connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }
    private void initDataPastPapers(String document) {
        titleModelList=new ArrayList<TitleModel>();

        firebaseFirestore.collection(ROOT_PAPERS).document(document).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        HashMap<String, Object> map= (HashMap<String, Object>) task.getResult().getData();


                        Object [] keys=map.keySet().toArray();
                        for(int i=0;i<keys.length;i++){

                            titleModelList.add(new TitleModel("0",document+" "+keys[i].toString(),map.get(keys[i]).toString()));


                        }
                    }catch (Exception e){


                    }
                    if(titleModelList.size()==0){
                        titleModelList.add(new TitleModel("0","No Results",NO_RESULTS));
                    }

                    initRecyclerView();
                }else{
                    Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });


    }

    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.extraPapersRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,ExtraPapersActivity.this,RESOURCE_BOOKS);
        binding.extraPapersRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

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
}