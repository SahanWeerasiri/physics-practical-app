package com.al.physicspracticles.ui.shortnotes;

import static com.al.physicspracticles.DatabaseNames.DOCUMENT_NOTES;
import static com.al.physicspracticles.DatabaseNames.NO_RESULTS;
import static com.al.physicspracticles.DatabaseNames.PAST_PAPERS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.ROOT_EXTRA;
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

import com.al.physicspracticles.ExtraPapersActivity;
import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.FragmentNotesBinding;
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


public class NotesFragment extends Fragment {
    private FragmentNotesBinding binding;
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
        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ad
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for Notes...");

        final InterstitialAd[] mInterstitialAd = new InterstitialAd[1];
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),"ca-app-pub-1262150100678081/1300603210", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd[0] = interstitialAd;
                        if (mInterstitialAd[0] != null) {
                            mInterstitialAd[0].show(getActivity());
                            progressDialog.cancel();

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

        firebaseFirestore.collection(ROOT_EXTRA).document(DOCUMENT_NOTES).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.notesRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList, getActivity(),RESOURCE_BOOKS);
        binding.notesRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}