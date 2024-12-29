package com.al.physicspracticles.ui.gallery;

import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PDF_QA_TYPE;
import static com.al.physicspracticles.DatabaseNames.PDF_RESOURCE_TYPE;
import static com.al.physicspracticles.DatabaseNames.PRACTICALS;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_NAME;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_TO_SUBJECT_TYPE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_ID;
import static com.al.physicspracticles.DatabaseNames.ROOT;
import static com.al.physicspracticles.DatabaseNames.ROOT_RESOURCE;
import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.MainActivity;
import com.al.physicspracticles.Practical;
import com.al.physicspracticles.PracticalModel;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.FragmentGalleryBinding;
import com.al.physicspracticles.databinding.FragmentPdfBinding;
import com.al.physicspracticles.practicalFragments.PdfFragment;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {
//Resource books
    private FragmentGalleryBinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;
    private static FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;
    private RewardedAd rewardedAd=null;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //ad
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Looking for Resource books...");
        final InterstitialAd[] mInterstitialAd = new InterstitialAd[1];
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(getContext(),"ca-app-pub-1262150100678081/9941494656", adRequest,
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
        /*pdf_names.put("1","Unit 1");
        pdf_names.put("2","Unit 2");
        pdf_names.put("3","Unit 3");
        pdf_names.put("4","Unit 4");
        pdf_names.put("5","Unit 5,6,7,8");
        pdf_names.put("6","Unit 9");
        pdf_names.put("7","Unit 10");
        pdf_names.put("8","Unit 11");
            for (int i=1;i<=pdf_names.size();i++){
                String id=String.valueOf(i);
                String name=pdf_names.get(id);

                titleModelList.add(new TitleModel(id,name));

            }*/
            firebaseFirestore.collection(ROOT_RESOURCE).document(RESOURCE_BOOK_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            HashMap<Object, Object> map= (HashMap<Object, Object>) task.getResult().get(RESOURCE_BOOK_ID);
                            Object [] keys=map.keySet().toArray();
                            for(int i=0;i<keys.length;i++){

                                titleModelList.add(new TitleModel("0",keys[i].toString(),map.get(keys[i]).toString()));


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
        binding.resourceBooksRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,getActivity(),RESOURCE_BOOKS);
        binding.resourceBooksRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}