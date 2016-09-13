package funcentric.com.gsupdates;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import funcentric.com.gsupdates.Network.InternetCheck;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button gold_icon, chart_icon, search_icon;
    LinearLayout mainView;
    Button share_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = (LinearLayout) findViewById(R.id.mainLayout);
        gold_icon = (Button) findViewById(R.id.gold_image);
        gold_icon.setOnClickListener(this);


        chart_icon = (Button) findViewById(R.id.chart_image);
        chart_icon.setOnClickListener(this);

        search_icon = (Button) findViewById(R.id.search_image);
        search_icon.setOnClickListener(this);

        share_icon = (Button) findViewById(R.id.share_image);
        share_icon.setOnClickListener(this);


        new AsyncTask_ProgressDialog(MainActivity.this).execute();


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (InternetCheck.isConnectingToInternet(this) == true) {

        } else {
            Log.v("Sateesh: ", "No Internet");
//            LaunchNoInternet noInternet = new LaunchNoInternet();
//            noInternet.show(getFragmentManager(), "LaunchNoInternet");
            Snackbar snackbar = Snackbar.make(mainView, "Switch ON internet to fetch latest Prices \n ", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gold_image:
                Intent data_gold = new Intent(MainActivity.this, DataActivity.class);
                startActivity(data_gold);
                finish();
                break;

//            case R.id.silver_image:
//                Intent data_silver = new Intent(MainActivity.this, DataSilver.class);
//                startActivity(data_silver);
//
//                break;

            case R.id.chart_image:
                Intent charts = new Intent(MainActivity.this, ChartsActivity.class);
                startActivity(charts);
                finish();
                break;

            case R.id.search_image:
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
                finish();
                break;

            case R.id.share_image:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
//                intent.setData(Uri.parse("market://details?id=com.triedge.ic"));
                //intent.putExtra(Intent.EXTRA_TEXT, "Now you can check Gudur Gold & and Silver Prices in Mobile App. From this URL: \n https://play.google.com/store/apps/details?id=funcentric.com.gsupdates ");
                intent.putExtra(Intent.EXTRA_TEXT, " ఇపుడు గూడూరు లోని బంగారు మరియు వెండి అసోసియేషన్ ధరలు మీ ఫోన్ లోనే చూసుకునే అవకాశం కలదు. డౌన్లోడ్ చేసుకొనవలసిన అప్లికేషన్ లింక్ : \n https://play.google.com/store/apps/details?id=funcentric.com.gsupdates ");
                startActivity(intent);
                break;


        }
    }


    public class AsyncTask_ProgressDialog extends AsyncTask<Void, Void, Void> {

        Dialog pDialog;
        Context appContext;

        public AsyncTask_ProgressDialog(Context con) {
            appContext = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null) {

//                pDialog.show(new ContextThemeWrapper(appContext, R.style.NewDialog), "", "Loading Data ... ", true);
                pDialog = new Dialog(appContext);

                pDialog.setContentView(R.layout.dialog_layout);
                pDialog.setTitle("Test Dialog");

                TextView text = (TextView) pDialog.findViewById(R.id.dialog_message);
//                text.setText("Loading Data... ");
                pDialog.show();
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setCancelable(false);
//                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                pDialog.setMessage("Please Wait Updating Data From...");
//                pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                pDialog.show();
//                pDialog.setContentView(R.layout.dialog_layout);

//                final Window window = pDialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Log.v("Dialog: ", "Dialog Displayed");

            }
//
//


        }

        @Override
        protected Void doInBackground(Void... strings) {
            FetchSheetTask task = null;
            FetchSheetTask_City city_task = null;

            try {
                city_task = new FetchSheetTask_City(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            city_task.execute();

            try {
                task = new FetchSheetTask(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            task.execute();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing()) {
//                try {
//                    Thread.sleep(5000);
//                    Log.v("Dialog: ", "i am sleeping");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                pDialog.dismiss();
                Log.v("Dialog: ", "Dialog closed");
            }
        }
    }
}
