package funcentric.com.gsupdates.Network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import funcentric.com.gsupdates.R;

/**
 * Created by Sateesh on 09-09-2016.
 */
public class LaunchNoInternet extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.v("Sateesh: ", "*** No Internet Dialog Exit");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("No Internet");
        builder.setMessage("Device not connected to Internet");
        builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.v("Sateesh: ", "*** No Internet Dialog Dismissed");
            }
        });

        Dialog dialog = builder.create();

        return dialog;


    }
}