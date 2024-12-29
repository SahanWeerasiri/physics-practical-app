package com.al.physicspracticles;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Network network = new Network();
        if (!network.isConnected(context)) {
            Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.noconnection);
            dialog.setCancelable(false);
            Button btn=(Button)dialog.findViewById(R.id.btn_pop_no_connection_try);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });
           dialog.show();
        }
    }
}
