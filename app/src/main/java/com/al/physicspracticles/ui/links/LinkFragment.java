package com.al.physicspracticles.ui.links;

import static com.al.physicspracticles.DatabaseNames.DOCUMENT_LINKS;
import static com.al.physicspracticles.DatabaseNames.DOCUMENT_NOTES;
import static com.al.physicspracticles.DatabaseNames.LINK_LINK;
import static com.al.physicspracticles.DatabaseNames.LINK_TYPE;
import static com.al.physicspracticles.DatabaseNames.NO_RESULTS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.ROOT_EXTRA;
import static com.al.physicspracticles.DatabaseNames.TYPE_LINKS;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.FragmentLinkBinding;
import com.al.physicspracticles.databinding.FragmentNotesBinding;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LinkFragment extends Fragment {

    private FragmentLinkBinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;
    private static FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;
    private RewardedAd rewardedAd=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLinkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ad
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for Links...");
        progressDialog.show();

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getContext(), "ca-app-pub-1262150100678081/3879457683",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        rewardedAd = null;
                        progressDialog.cancel();
                        /*Snackbar.make( binding.getRoot(),"Rewarding videos are not available\nCheck your internet connection and Try again", Snackbar.LENGTH_LONG)
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
        firebaseFirestore= FirebaseFirestore.getInstance();
        llm=new LinearLayoutManager(getContext());
        initData();

        return root;

    }


    private void initData() {
        titleModelList=new ArrayList<TitleModel>();

        firebaseFirestore.collection(ROOT_EXTRA).document(DOCUMENT_LINKS).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    try{
                        HashMap<String, Object> map= (HashMap<String, Object>) task.getResult().getData();


                        Object [] keys=map.keySet().toArray();
                        for(int i=0;i<keys.length;i++){

                            HashMap<String, Object> map_inner= (HashMap<String, Object>) map.get(keys[i]);
                            titleModelList.add(new TitleModel(map_inner.get(LINK_TYPE).toString(),keys[i].toString(),map_inner.get(LINK_LINK).toString()));


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
        binding.linksRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList, getActivity(),TYPE_LINKS);
        binding.linksRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}