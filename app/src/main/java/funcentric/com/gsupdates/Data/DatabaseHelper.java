package funcentric.com.gsupdates.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Sateesh on 04-08-2016.
 */

//public class DatabaseHelper extends SQLiteOpenHelper {
public class DatabaseHelper extends SQLiteAssetHelper {

    public static final String DATABASE_NAME = "PriceDetails.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        final String CREATE_PRICE_INFO_TABLE = "CREATE TABLE " + DatabaseContract.PriceInfo.TABLE_NAME  +
//                "( "  + DatabaseContract.PriceInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                DatabaseContract.PriceInfo.COLUMN_S_No + " LONG NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_MODIFIED_DATETIME + " TEXT NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_DATE + " LONG NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_GOLD_1_GM + " INTEGER NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_SILVER_1_GM + " INTEGER NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_GOLD_CHANGE + " TEXT NOT NULL, " +
//                DatabaseContract.PriceInfo.COLUMN_SILVER_CHANGE + " TEXT NOT NULL, " +
//                " UNIQUE (" + DatabaseContract.PriceInfo.COLUMN_DATE + ", " +
//                DatabaseContract.PriceInfo.COLUMN_CITY_NAME + ") ON CONFLICT REPLACE " +
//                ");";
//
//        final String CREATE_CITY_INFO_TABLE = "CREATE TABLE " + DatabaseContract.CityInfo.TABLE_NAME +
//                "( " + DatabaseContract.CityInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                DatabaseContract.CityInfo.COLUMN_S_NO + " INTEGER NOT NULL, " +
//                DatabaseContract.CityInfo.COLUMN_MODIFIED_DATETIME + " TEXT NOT NULL, " +
//                DatabaseContract.CityInfo.COLUMN_CITY_NAME + "  TEXT UNIQUE NOT NULL, " +
//                " UNIQUE (" + DatabaseContract.CityInfo.COLUMN_S_NO + ") ON CONFLICT REPLACE " +
//                ");";
//
//        db.execSQL(CREATE_PRICE_INFO_TABLE);
//        db.execSQL(CREATE_CITY_INFO_TABLE);
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.PriceInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CityInfo.TABLE_NAME);
        onCreate(db);
    }
}
