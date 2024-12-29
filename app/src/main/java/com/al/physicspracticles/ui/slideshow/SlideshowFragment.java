package com.al.physicspracticles.ui.slideshow;

import static com.al.physicspracticles.DatabaseNames.EXTRA_VIDEO;
import static com.al.physicspracticles.DatabaseNames.EXTRA_VIDEOS;
import static com.al.physicspracticles.DatabaseNames.EXTRA_YOUTUBE_VIDEO;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.ROOT_EXTRA;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.Practical;
import com.al.physicspracticles.PracticalModel;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.YoutubeVideoModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.adapters.YoutubeVideoAdapter;
import com.al.physicspracticles.databinding.FragmentSlideshowBinding;
import com.al.physicspracticles.practicalFragments.VideoFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlideshowFragment extends Fragment {
//Extra  video
    private FragmentSlideshowBinding binding;
    private LinearLayoutManager llm;
    private YoutubeVideoAdapter ad;
    private List<YoutubeVideoModel> youtubeVideoModelList;
    private FirebaseFirestore firebaseFirestore;
    private RewardedAd rewardedAd=null;
    private ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseFirestore=FirebaseFirestore.getInstance();


        //ad
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for videos...");
        progressDialog.show();

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getContext(), "ca-app-pub-1262150100678081/9861061031",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        rewardedAd = null;
                        progressDialog.cancel();
                       /* Snackbar.make( binding.getRoot(),"Rewarding videos are not available\nCheck your internet connection and Try again", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();*/
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        if (rewardedAd != null) {
                            rewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    progressDialog.cancel();

                                }
                            });
                        } else {
                            progressDialog.cancel();
                        }
                    }
                });
        llm=new LinearLayoutManager(getContext());

        youtubeVideoModelList=new ArrayList<YoutubeVideoModel>();


        initData();



        return root;

    }



    private void initData() {
        /*video_names.put("1","Unit 1");
        video_names.put("2","Unit 2");
        video_names.put("3","Unit 3");
        video_names.put("4","Unit 4");
        video_names.put("5","Unit 5,6,7,8");
        video_names.put("6","Unit 9");
        video_names.put("7","Unit 10");
        video_names.put("8","Unit 11");*/

        firebaseFirestore.collection(ROOT_EXTRA).document(EXTRA_VIDEO).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String,Object> map=new HashMap<String,Object>();
                    map= (HashMap<String, Object>) task.getResult().getData();
                    Object[] keys=map.keySet().toArray();
                    for(int i=0;i<map.size();i++){
                        String name=keys[i].toString();
                        youtubeVideoModelList.add(new YoutubeVideoModel(name,map.get(keys[i]).toString(),EXTRA_VIDEO));

                    }

                    firebaseFirestore.collection(ROOT_EXTRA).document(EXTRA_YOUTUBE_VIDEO).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                HashMap<String,Object> map=new HashMap<String,Object>();
                                map= (HashMap<String, Object>) task.getResult().getData();
                                Object[] keys=map.keySet().toArray();
                                for(int i=0;i<map.size();i++){
                                    String name=keys[i].toString();
                                    youtubeVideoModelList.add(new YoutubeVideoModel(name,map.get(keys[i]).toString(),EXTRA_YOUTUBE_VIDEO));

                                }


                                initRecyclerView();



                            }else{
                                Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                            if(youtubeVideoModelList.size()==0){
                                Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }
                    });



                }else{
                    Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });









    }

    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.extraVideosRecyclerView.setLayoutManager(llm);
        ad=new YoutubeVideoAdapter(youtubeVideoModelList,getActivity());
        binding.extraVideosRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}