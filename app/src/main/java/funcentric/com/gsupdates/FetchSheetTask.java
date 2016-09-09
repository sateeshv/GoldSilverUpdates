package funcentric.com.gsupdates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import funcentric.com.gsupdates.Data.DatabaseContract;

/**
 * Created by Sateesh on 04-08-2016.
 */
public class FetchSheetTask extends AsyncTask<Void, Void, Void> {
    Context context;
    long startTime;

    public FetchSheetTask(Context context) throws IOException {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... params) {

        startTime = System.currentTimeMillis();

//        Uri uri = Uri.withAppendedPath(DatabaseContract.QuoteInfo.CONTENT_URI, "1");
//        Log.v("Sateesh: ", "*** URI link is: " + uri);
//
//        Cursor cursorLastRecord = context.getContentResolver().query(uri, null, null, null, null);
//        cursorLastRecord.moveToFirst();
//        String lastInserted_SNo = null;
//        String sheetURL = null;
//        String KEY = "1h9LyG7grqOeYJT7xStVnbfhjXt0r2mjl-5Ow-i13OnY";
//
//        try {
//            if (cursorLastRecord.getCount() != 0) {
//                lastInserted_SNo = cursorLastRecord.getString(cursorLastRecord.getColumnIndexOrThrow(DatabaseContract.QuoteInfo.COLUMN_SNo));
//                Log.v("Sateesh: ", "*** Last Inserted Date is: " + lastInserted_SNo);
//                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json" + "&sq=sno>" + lastInserted_SNo;
//            } else {
//                Log.v("Sateesh: ", "*** No insertions till Now");
//                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";
//                Log.v("Sateesh: ", "*** new Data sheet URL is: " + sheetURL);
//            }
//        } catch (CursorIndexOutOfBoundsException e) {
//            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
//        }

        Uri priceUri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "2");
        // 2 Stands for last inserted S No
        Log.v("Sateesh: ", "*** URI link is: " + priceUri);
        Cursor priceLastRecord = context.getContentResolver().query(priceUri, null, null, null, null);
        if (priceLastRecord != null) {
            priceLastRecord.moveToFirst();
        }
        String lastInserted_SNo = null;
        String sheetURL = null;
        String KEY = "1edW69Md9E-qytnbHKdD9EgwrRSabwivsol7rw4tv_bI";
        Log.v("Sateesh: ", "*** are there any records: " + (priceLastRecord != null ? priceLastRecord.getCount() : 0));
        String priceLastInsertedDate = null;

        try {
            if (priceLastRecord != null && priceLastRecord.getCount() > 0) {
                priceLastRecord.moveToFirst();
                lastInserted_SNo = priceLastRecord.getString(priceLastRecord.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_S_No));
                Log.v("Sateesh: ", "*** Last Inserted Date is: " + lastInserted_SNo);
//                getPriceData_method(getLatestPriceData_URL, priceLastInsertedDate);
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json" + "&sq=sno>" + lastInserted_SNo;
            } else {
                Log.v("Sateesh: ", "*** No insertions till Now");
//                getPriceData_method(getAllPriceData_URL, "");
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";
//                https://spreadsheets.google.com/feeds/list/1edW69Md9E-qytnbHKdD9EgwrRSabwivsol7rw4tv_bI/od6/public/values?alt=json
                Log.v("Sateesh: ", "*** new Data sheet URL is: " + sheetURL);

            }

        } catch (CursorIndexOutOfBoundsException e) {
            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
        }
        priceLastRecord.close();


//        String sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/od6/public/values?alt=json";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String sheetString = null;

        try {
            URL url = new URL(sheetURL.toString());
            /**
             * Create request to Google Spreadsheet
             */
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            /**
             * Read inputStream to a String
             */
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            sheetString = buffer.toString();
            Log.v("Sateesh: -sheetString", "*** " + sheetString);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            getSheetDataFromJSON(sheetString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    private void getSheetDataFromJSON(String sheetString) throws JSONException {

        int numberOfRows;
        String sNo, date, city, gold_22_ct_8_grams, gold_22_ct_1_gram, silver_1_gram, gold_change, silver_change;
        sNo = null;
        date = null;
        city = null;
        gold_22_ct_8_grams = null;
        gold_22_ct_1_gram = null;
        silver_1_gram = null;
        gold_change = null;
        silver_change = null;

        List<ContentValues> data = new ArrayList<ContentValues>();

        JSONObject mainloop = new JSONObject(sheetString);
        JSONObject feed = mainloop.getJSONObject("feed");
        JSONObject searchResultsObject = feed.getJSONObject("openSearch$totalResults");

        String searchResultsValue = searchResultsObject.getString("$t");
        int value = Integer.parseInt(searchResultsValue);
        if (value > 0) {
            Log.v("Sateesh: ", "New Data Available");
            JSONArray entry = feed.getJSONArray("entry");

            JSONObject searchTotalCount = feed.getJSONObject("openSearch$totalResults");
            numberOfRows = searchTotalCount.getInt("$t");

            for (int i = 0; i < numberOfRows; i++) {
                String[] rowData = new String[numberOfRows - 1];


                JSONObject eachRow = entry.getJSONObject(i);

                JSONObject sNoObject = eachRow.getJSONObject("gsx$sno");
                sNo = sNoObject.getString("$t");

                JSONObject dateObject = eachRow.getJSONObject("gsx$date");
                date = dateObject.getString("$t");

                JSONObject cityObect = eachRow.getJSONObject("gsx$city");
                city = cityObect.getString("$t");

                JSONObject gold_22ct_8_gramsObject = eachRow.getJSONObject("gsx$gold22ct8grams");
                gold_22_ct_8_grams = gold_22ct_8_gramsObject.getString("$t");

                JSONObject gold_22ct_1_gramObject = eachRow.getJSONObject("gsx$gold22ct1gram");
                gold_22_ct_1_gram = gold_22ct_1_gramObject.getString("$t");

                JSONObject silver_1_gramObject = eachRow.getJSONObject("gsx$silver1gram");
                silver_1_gram = silver_1_gramObject.getString("$t");

                JSONObject gold_changeObject = eachRow.getJSONObject("gsx$goldchange");
                gold_change = gold_changeObject.getString("$t");

                JSONObject silver_changeObject = eachRow.getJSONObject("gsx$silverchange");
                silver_change = silver_changeObject.getString("$t");

                Log.v("Sateesh: - Data", "sNo Values are: " + sNo);
                Log.v("Sateesh: - Data", "date Values are: " + date);
                Log.v("Sateesh: - Data", "city Values are: " + city);
                Log.v("Sateesh: - Data", "gold 22ct 8 gram Values are: " + gold_22_ct_8_grams);
                Log.v("Sateesh: - Data", "gold 22 ct 1 gram Values are: " + gold_22_ct_1_gram);
                Log.v("Sateesh: - Data", "silver 1 gram Values are: " + silver_1_gram);
                Log.v("Sateesh: - Data", "gold change Values are: " + gold_change);
                Log.v("Sateesh: - Data", "silver change Values are: " + silver_change);

                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                ContentValues values = new ContentValues();
                values.put(DatabaseContract.PriceInfo.COLUMN_S_No, sNo);
                values.put(DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME, timeStamp);
                values.put(DatabaseContract.PriceInfo.COLUMN_DATE, date);
                values.put(DatabaseContract.PriceInfo.COLUMN_CITY_NAME, city);
                values.put(DatabaseContract.PriceInfo.COLUMN_GOLD_1_GM, gold_22_ct_1_gram);
                values.put(DatabaseContract.PriceInfo.COLUMN_SILVER_1_GM, silver_1_gram);
                values.put(DatabaseContract.PriceInfo.COLUMN_GOLD_CHANGE, gold_change);
                values.put(DatabaseContract.PriceInfo.COLUMN_SILVER_CHANGE, silver_change);


                data.add(values);


            }
            if (data.size() > 0) {
                Log.v("Sateesh: ", "*** getAllData_method Prices Records count is: " + data.size());
                ContentValues[] dataArray = new ContentValues[data.size()];
                ContentValues[] values = data.toArray(dataArray);
                Log.v("Sateesh: ", "**** getAllData_method content Values data " + values);

                Uri data_uri = Uri.withAppendedPath(DatabaseContract.PriceInfo.CONTENT_URI, "0");
                int insertedRecords = context.getContentResolver().bulkInsert(data_uri, dataArray);
                Log.v("Sateesh: ", "*** MainActivity getAllData_method + Data Inserted Records: " + insertedRecords);
                long endTime = System.currentTimeMillis();
                Log.v("Sateesh: ", "*** Prices - Time taken to Check new Data Available " + (endTime - startTime));
            }
        } else {
            Log.v("Sateesh: ", "NO New Data Available");
            long endTime = System.currentTimeMillis();
            Log.v("Sateesh: ", "*** Prices - Time taken to Check new Data Available " + (endTime - startTime));
        }


    }
}
