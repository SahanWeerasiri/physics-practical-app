package com.al.physicspracticles.ui.papers;

import static com.al.physicspracticles.DatabaseNames.PAST_PAPERS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_ID;
import static com.al.physicspracticles.DatabaseNames.ROOT_PAPERS;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.FragmentPapersAnswersBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PapersAnswersFragment extends Fragment {

    private FragmentPapersAnswersBinding binding;
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
        binding = FragmentPapersAnswersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ad
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for Papers...");

        final InterstitialAd[] mInterstitialAd = new InterstitialAd[1];
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),"ca-app-pub-1262150100678081/7427011953", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd[0] = interstitialAd;
                        if (mInterstitialAd[0] != null) {
                            mInterstitialAd[0].show(getActivity());


                        }

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd[0] = null;

                    }
                });
        firebaseFirestore=FirebaseFirestore.getInstance();
        llm=new LinearLayoutManager(getContext());
        initData();

        return root;

    }


    private void initData() {

        titleModelList=new ArrayList<TitleModel>();
        firebaseFirestore.collection(ROOT_PAPERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    List<DocumentSnapshot> documents=task.getResult().getDocuments();
                    for(DocumentSnapshot documentSnapshot:documents){
                        titleModelList.add(new TitleModel("0",documentSnapshot.getId()));
                    }

                    initRecyclerView();
                }else{
                    Toast.makeText(getContext(),"Not Successful",Toast.LENGTH_SHORT).show();
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
        binding.papersAnswersRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,getActivity(),PAST_PAPERS);
        binding.papersAnswersRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}