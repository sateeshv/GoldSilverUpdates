package sateesh.com.goldsilverupdates;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sateesh.com.goldsilverupdates.Data.DatabaseContract;

/**
 * Created by Sateesh on 04-08-2016.
 */
public class GoldPriceCursorAdapter extends CursorAdapter {

    String[] Month_Names = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    public GoldPriceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        Log.v("Sateesh: ", "*** CursorAdapter Constructor reached");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.v("Sateesh: ", "*** newView reached");
        Log.v("Sateesh: ", "*** Cursor Data in newView: " + DatabaseUtils.dumpCursorToString(cursor));
        View view = LayoutInflater.from(context).inflate(R.layout.gold_price_layout, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        view.setTag(holder);


        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView date = (TextView) view.findViewById(R.id.text0);
        TextView   gold_8_grams_text = (TextView) view.findViewById(R.id.text1);
        TextView   gold_1_gram_text = (TextView) view.findViewById(R.id.text2);
        ImageView change = (ImageView) view.findViewById(R.id.text3);

        String rawDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_DATE));
        Log.v("Sateesh", "*** format is: " +rawDate);
        String month_value_string = rawDate.split("-")[1];
        int month_value = Integer.parseInt(month_value_string);
        String month_name = Month_Names[(month_value-1)];
//        String formattedDate = rawDate.split("-")[2] + " - " + rawDate.split("-")[1];
        String formattedDate = rawDate.split("-")[2] + " - " + month_name;
        date.setText(formattedDate);
//        ViewHolder.rollNo.setText("");

//        String rawRollNo = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_ROLLNO)));
//        Log.v("Sateesh: ", "RawDate is: " + rawRollNo);

        int gold_1_gram = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_GOLD_1_GM));
        gold_8_grams_text.setText(String.valueOf(gold_1_gram * 8));


        gold_1_gram_text.setText(String.valueOf(gold_1_gram));

//        ViewHolder.studentName.setText("");

//        ViewHolder.gender.setText("");

        String changeStatus = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_GOLD_CHANGE));
        switch (changeStatus){
            case "No Change":
                change.setImageResource(R.drawable.nochange);
                break;
            case "Increased":
                change.setImageResource(R.drawable.increase);
                break;
            case "Decreased":
                change.setImageResource(R.drawable.decrease);
                break;
        }
//        change.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.PriceInfo.COLUMN_GOLD_CHANGE)));
//        Log.v("Sateesh: ", "*** bindView reached");
//        ViewHolder holder = (ViewHolder) view.getTag();
//        Log.v("Sateesh: ", "*** Cursor Data in bindView: " + DatabaseUtils.dumpCursorToString(cursor));
//        //holder.date.setText(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_DATE))));
//
//        String rawDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_DATE));
//        Log.v("Sateesh: ", "RawDate is: " + rawDate);
//        ViewHolder.date.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_DATE)));
//        ViewHolder.rollNo.setText("");
//
//        String rawRollNo = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_ROLLNO)));
//        Log.v("Sateesh: ", "RawDate is: " + rawRollNo);
//        ViewHolder.rollNo.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_ROLLNO))));
//
//        ViewHolder.studentName.setText("");
//        ViewHolder.studentName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_STUDENTNAME)));
//        ViewHolder.gender.setText("");
//        ViewHolder.gender.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ClassInfo.COLUMN_CITY)));
    }
//    public static class ViewHolder{
//        public static TextView date;
//        public static TextView rollNo;
//        public static TextView studentName;
//        public static TextView gender;
//
//        public ViewHolder(View view) {
//            date = (TextView) view.findViewById(R.id.text0);
//            rollNo = (TextView) view.findViewById(R.id.text1);
//            studentName = (TextView) view.findViewById(R.id.text2);
//            gender = (TextView) view.findViewById(R.id.text3);
//        }
//    }
}
