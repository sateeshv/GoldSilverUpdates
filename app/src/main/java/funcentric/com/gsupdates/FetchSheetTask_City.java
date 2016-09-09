package funcentric.com.gsupdates;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
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
 * Created by Sateesh on 05-08-2016.
 */
public class FetchSheetTask_City extends AsyncTask<Void, Void, Void> {
    Context context;
    long startTime;

    public FetchSheetTask_City(Context context) throws IOException {
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

        Uri CityUri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "14");
        // 14 Stands for last inserted S No
        Log.v("Sateesh: ", "*** URI link is: " + CityUri);
        Cursor cityLastRecord = context.getContentResolver().query(CityUri, null, null, null, null);
        if (cityLastRecord != null) {
            cityLastRecord.moveToFirst();
        }
        Log.v("Sateesh: ", "cityLastRecord cursor is: " + DatabaseUtils.dumpCursorToString(cityLastRecord));
        String lastInserted_SNo = null;
        String sheetURL = null;
        String KEY = "1edW69Md9E-qytnbHKdD9EgwrRSabwivsol7rw4tv_bI";
        Log.v("Sateesh: ", "*** are there any records: " + (cityLastRecord != null ? cityLastRecord.getCount() : 0));
//        String priceLastInsertedDate = null;

        try {
            if (cityLastRecord != null && cityLastRecord.getCount() > 0) {
                cityLastRecord.moveToFirst();
                lastInserted_SNo = cityLastRecord.getString(cityLastRecord.getColumnIndexOrThrow(DatabaseContract.CityInfo.COLUMN_S_NO));
                Log.v("Sateesh: ", "*** Last Inserted City S No is: " + lastInserted_SNo);
//                getPriceData_method(getLatestPriceData_URL, priceLastInsertedDate);
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/2/public/values?alt=json" + "&sq=sno>" + lastInserted_SNo;
            } else {
                Log.v("Sateesh: ", "*** No insertions till Now");
//                getPriceData_method(getAllPriceData_URL, "");
                sheetURL = "https://spreadsheets.google.com/feeds/list/" + KEY + "/2/public/values?alt=json";
//                https://spreadsheets.google.com/feeds/list/1edW69Md9E-qytnbHKdD9EgwrRSabwivsol7rw4tv_bI/od6/public/values?alt=json
                Log.v("Sateesh: ", "*** new Data sheet URL is: " + sheetURL);

            }

        } catch (CursorIndexOutOfBoundsException e) {
            Log.v("Sateesh: ", "*** cursor Index Out of Bound");
        }
        cityLastRecord.close();


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
        String sNo, city;
        sNo = null;
        city = null;


        List<ContentValues> data = new ArrayList<ContentValues>();

        JSONObject mainloop = new JSONObject(sheetString);
        JSONObject feed = mainloop.getJSONObject("feed");
        JSONObject searchResultsObject = feed.getJSONObject("openSearch$totalResults");

        String searchResultsValue = searchResultsObject.getString("$t");
        int value = Integer.parseInt(searchResultsValue);
        if (value > 0) {
            Log.v("Sateesh: ", "City New Data Available");
            JSONArray entry = feed.getJSONArray("entry");

            JSONObject searchTotalCount = feed.getJSONObject("openSearch$totalResults");
            numberOfRows = searchTotalCount.getInt("$t");

            for (int i = 0; i < numberOfRows; i++) {
                String[] rowData = new String[numberOfRows - 1];


                JSONObject eachRow = entry.getJSONObject(i);

                JSONObject sNoObject = eachRow.getJSONObject("gsx$sno");
                sNo = sNoObject.getString("$t");

                JSONObject cityObect = eachRow.getJSONObject("gsx$city");
                city = cityObect.getString("$t");


                Log.v("Sateesh: - Data", "sNo Values are: " + sNo);
                Log.v("Sateesh: - Data", "city Values are: " + city);

                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                ContentValues values = new ContentValues();
                values.put(DatabaseContract.CityInfo.COLUMN_S_NO, sNo);
                values.put(DatabaseContract.CityInfo.COLUMN_MODIFIED_DATETIME, timeStamp);
                values.put(DatabaseContract.CityInfo.COLUMN_CITY_NAME, city);

                data.add(values);


            }
            if (data.size() > 0) {
                Log.v("Sateesh: ", "*** getAllData_method Prices Records count is: " + data.size());
                ContentValues[] dataArray = new ContentValues[data.size()];
                ContentValues[] values = data.toArray(dataArray);
                Log.v("Sateesh: ", "**** getAllData_method content Values data " + values);

                Uri data_uri = Uri.withAppendedPath(DatabaseContract.CityInfo.CONTENT_URI, "1");
                int insertedRecords = context.getContentResolver().bulkInsert(data_uri, dataArray);
                Log.v("Sateesh: ", "*** MainActivity getAllData_method + Data Inserted Records: " + insertedRecords);
                long endTime = System.currentTimeMillis();
                Log.v("Sateesh: ", "*** City - Time taken to Check new Data Available " + (endTime - startTime));
            }
        } else {
            Log.v("Sateesh: ", "City - NO New Data Available");
            long endTime = System.currentTimeMillis();
            Log.v("Sateesh: ", "*** City - Time taken to Check new Data Available " + (endTime - startTime));
        }


    }
}

