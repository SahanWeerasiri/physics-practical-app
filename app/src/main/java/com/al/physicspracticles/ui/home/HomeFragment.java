package com.al.physicspracticles.ui.home;

import static com.al.physicspracticles.DatabaseNames.ROOT;
import static com.al.physicspracticles.DatabaseNames.SUBJECT_TO_PRACTICAL_TYPE;
import static com.al.physicspracticles.DatabaseNames.TITLE_IMAGE_URL;
import static com.al.physicspracticles.DatabaseNames.TITLE_NAME;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.al.physicspracticles.R;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;
    private FirebaseFirestore firebaseFirestore;
    private static final int[] res={R.drawable.t1,R.drawable.t2,R.drawable.t3,R.drawable.t4,R.drawable.t5,R.drawable.t6,
            R.drawable.t7,R.drawable.t8};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        llm=new LinearLayoutManager(getContext());

        firebaseFirestore=FirebaseFirestore.getInstance();

        initData();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initData() {
        titleModelList=new ArrayList<TitleModel>();



        firebaseFirestore.collection(ROOT).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i=0;
                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){

                        String title_name=documentSnapshot.get(TITLE_NAME).toString();
                        //Uri title_uri= Uri.parse(documentSnapshot.get(TITLE_IMAGE_URL).toString());


                        titleModelList.add(new TitleModel(title_name,res[i],null,documentSnapshot.getId()));
                        i++;

                    }

                    initRecyclerView();
                }else{
                    Toast.makeText(getContext(), "Not Successful", Toast.LENGTH_SHORT).show();
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
        binding.subjectRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,getActivity(),SUBJECT_TO_PRACTICAL_TYPE);
        binding.subjectRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }
}