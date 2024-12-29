package com.al.physicspracticles;

import static com.al.physicspracticles.DatabaseNames.APP_ID;
import static com.al.physicspracticles.DatabaseNames.APP_INFO;
import static com.al.physicspracticles.DatabaseNames.APP_LINK;
import static com.al.physicspracticles.DatabaseNames.APP_NAV_BACKGROUND;
import static com.al.physicspracticles.DatabaseNames.APP_NEW_INFO;
import static com.al.physicspracticles.DatabaseNames.APP_VERSION_CODE;
import static com.al.physicspracticles.DatabaseNames.COLLECTION_ANNOUNCEMENTS;
import static com.al.physicspracticles.DatabaseNames.PARSING_MENU_TYPE;
import static com.al.physicspracticles.DatabaseNames.PARSING_TOPIC;
import static com.al.physicspracticles.DatabaseNames.PRACTICAL_PAPERS;
import static com.al.physicspracticles.DatabaseNames.ROOT_APP;
import static com.al.physicspracticles.DatabaseNames.TYPE_ANNOUNCEMENTS;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.al.physicspracticles.databinding.NavHeaderMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.al.physicspracticles.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseFirestore firebaseFirestore;

    private NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();

        checkUpdates();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        /*NavHeaderMainBinding navHeaderMainBinding=NavHeaderMainBinding.inflate(this.getLayoutInflater());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(ROOT_APP).document(APP_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String background_url=task.getResult().get(APP_NAV_BACKGROUND).toString();
                    Picasso.get().load(background_url).into(navHeaderMainBinding.imageViewNavBackground);
                }
            }
        });*/

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_resource_books,R.id.nav_notes, R.id.nav_extra_videos,R.id.nav_past_papers,R.id.nav_links)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_info){

            firebaseFirestore.collection(ROOT_APP).document(APP_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        try{
                        String info =task.getResult().get(APP_NEW_INFO).toString();
                        info=info.replace('#','\n');
                        info=info.replace('$','\t');

                        Dialog dialog_info=new Dialog(MainActivity.this);
                        dialog_info.setContentView(R.layout.pop_up);
                        dialog_info.setCancelable(false);
                        TextView msg=dialog_info.findViewById(R.id.txt_popup_msg);
                        ImageButton close=dialog_info.findViewById(R.id.txt_popup_info_close);

                        msg.setText(info);

                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog_info.cancel();
                            }
                        });
                        dialog_info.show();}
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
        }else if(item.getItemId()==R.id.action_announcements){

            Intent announcements=new Intent(MainActivity.this,ExtraPapersActivity.class);
            announcements.putExtra(PARSING_MENU_TYPE,TYPE_ANNOUNCEMENTS);
            announcements.putExtra(PARSING_TOPIC,"");
            startActivity(announcements);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
    private void checkUpdates(){
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),PackageInfo.INSTALL_LOCATION_AUTO);
            String versionCode=String.valueOf(packageInfo.versionCode);
            firebaseFirestore.collection(ROOT_APP).document(APP_ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String current_version_code=task.getResult().getString(APP_VERSION_CODE);
                        String link=task.getResult().getString(APP_LINK);
                        if(current_version_code!=null){
                            if(!current_version_code.equals(versionCode)){
                                Dialog dialog=new Dialog(MainActivity.this);
                                dialog.setContentView(R.layout.update);
                                dialog.setCancelable(false);
                                Button btn=(Button)dialog.findViewById(R.id.btn_pop_update);
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();

                                        Uri uri=Uri.parse(link);
                                        startActivity(new Intent(Intent.ACTION_VIEW,uri));

                                        finish();
                                    }
                                });
                                dialog.show();
                            }
                        }

                    }
                }
            });

        } catch (Exception e) {
            Snackbar.make(binding.getRoot(),"Error",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        }
    }
}