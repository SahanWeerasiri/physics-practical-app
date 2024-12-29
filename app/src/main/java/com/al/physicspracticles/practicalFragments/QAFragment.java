package com.al.physicspracticles.practicalFragments;

import static com.al.physicspracticles.DatabaseNames.PDF_QA_TYPE;
import static com.al.physicspracticles.DatabaseNames.PDF_RESOURCE_TYPE;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOKS;
import static com.al.physicspracticles.DatabaseNames.RESOURCE_BOOK_ID;
import static com.al.physicspracticles.DatabaseNames.ROOT_RESOURCE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.al.physicspracticles.R;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.adapters.TitleAdapter;
import com.al.physicspracticles.databinding.FragmentGalleryBinding;
import com.al.physicspracticles.databinding.FragmentQABinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class QAFragment extends Fragment {
    private FragmentQABinding binding;
    private LinearLayoutManager llm;
    private TitleAdapter ad;
    private List<TitleModel> titleModelList;
    public QAFragment(List<TitleModel> titleModelList) {
            this.titleModelList=titleModelList;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQABinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        llm=new LinearLayoutManager(getContext());



        initData();
        return root;
    }private void initData() {


        initRecyclerView();


    }

    private void initRecyclerView() {
        llm.setOrientation(RecyclerView.VERTICAL);
        binding.papersRecyclerView.setLayoutManager(llm);
        ad=new TitleAdapter(titleModelList,getActivity(),RESOURCE_BOOKS);
        binding.papersRecyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}