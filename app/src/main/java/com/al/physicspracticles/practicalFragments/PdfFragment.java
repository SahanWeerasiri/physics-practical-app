package com.al.physicspracticles.practicalFragments;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.al.physicspracticles.ChoosePractical;
import com.al.physicspracticles.PracticalModel;
import com.al.physicspracticles.TitleModel;
import com.al.physicspracticles.databinding.FragmentPdfBinding;
import com.al.physicspracticles.databinding.FragmentVideoBinding;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;


import static com.al.physicspracticles.DatabaseNames.*;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class PdfFragment extends Fragment {

    private FragmentPdfBinding binding;
    private static String TOPIC;
    private static String PRACTICAL;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private PracticalModel practicalModel;
    private String Url;

    public PdfFragment(PracticalModel practicalModel){
        this.practicalModel=practicalModel;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentPdfBinding.inflate(inflater,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        class RetrievePDFFromURL extends AsyncTask<String, Void, InputStream> {
            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream=null;
                try {
                    URL url=new URL(strings[0]);
                    HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                    if(urlConnection.getResponseCode()==200){
                        inputStream=new BufferedInputStream(urlConnection.getInputStream());
                    }else{
                        Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        //Toast.makeText(getContext(),"No connection",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    return null;
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream stream) {
                if(stream!=null){
                binding.pdfView.fromStream(stream).load();
                }else{
                    Snackbar.make( binding.getRoot(),"No Connection\nGo backward and reopen the item to reload", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                progressDialog.cancel();
            }
        }

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Wait for downloading...");
        progressDialog.show();

        firebaseFirestore=FirebaseFirestore.getInstance();


        Url=practicalModel.getEnglish_manual_url();
        new RetrievePDFFromURL().execute(Url);


        binding.pdfDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse(Url));
                String name= URLUtil.guessFileName(Url,null,null);
                request.setTitle(name);
                request.setDescription("Downloading Pdf. Please wait...");
                request.addRequestHeader("cookie",CookieManager.getInstance().getCookie(Url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name);

                DownloadManager downloadManager=(DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);
                Toast.makeText(getContext(), "Downloading started", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();
    }
}
