package org.cocos2dx.lib;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Cocos2dxLocalStorage {
    private static String DATABASE_NAME = "jsb.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "data";
    private static final String TAG = "Cocos2dxLocalStorage";
    private static SQLiteDatabase mDatabase;
    private static a mDatabaseOpenHelper;

    private static class a extends SQLiteOpenHelper {
        a(Context context) {
            super(context, Cocos2dxLocalStorage.access$000(), (SQLiteDatabase.CursorFactory) null, Cocos2dxLocalStorage.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Cocos2dxLocalStorage.access$100() + "(key TEXT PRIMARY KEY,value TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.w(Cocos2dxLocalStorage.TAG, "Upgrading database from version " + i + " to " + i2 + ", which will destroy all old data");
        }
    }

    static String access$000() {
        return DATABASE_NAME;
    }

    static String access$100() {
        return TABLE_NAME;
    }

    public static void clear() {
        try {
            mDatabase.execSQL("delete from " + TABLE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy() {
        SQLiteDatabase sQLiteDatabase = mDatabase;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    public static String getItem(String str) {
        String string = null;
        try {
            Cursor cursorRawQuery = mDatabase.rawQuery("select value from " + TABLE_NAME + " where key=?", new String[]{str});
            while (cursorRawQuery.moveToNext()) {
                if (string != null) {
                    Log.e(TAG, "The key contains more than one value.");
                    break;
                }
                string = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("value"));
            }
            cursorRawQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getKey(int i) {
        Cursor cursorRawQuery;
        String string = null;
        try {
            cursorRawQuery = mDatabase.rawQuery("select key from " + TABLE_NAME + " order by rowid asc", null);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i >= 0 && i < cursorRawQuery.getCount()) {
            int i2 = 0;
            while (cursorRawQuery.moveToNext()) {
                if (i2 == i) {
                    string = cursorRawQuery.getString(cursorRawQuery.getColumnIndex("key"));
                    break;
                }
                i2 += DATABASE_VERSION;
            }
            cursorRawQuery.close();
            return string;
        }
    }

    public static int getLength() {
        try {
            Cursor cursorRawQuery = mDatabase.rawQuery("select count(*) as nums from " + TABLE_NAME, null);
            i = cursorRawQuery.moveToNext() ? cursorRawQuery.getInt(cursorRawQuery.getColumnIndex("nums")) : 0;
            cursorRawQuery.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static boolean init(String str, String str2) {
        if (Cocos2dxActivity.getContext() == null) {
            return false;
        }
        DATABASE_NAME = str;
        TABLE_NAME = str2;
        a aVar = new a(Cocos2dxActivity.getContext());
        mDatabaseOpenHelper = aVar;
        mDatabase = aVar.getWritableDatabase();
        return true;
    }

    public static void removeItem(String str) {
        try {
            mDatabase.execSQL("delete from " + TABLE_NAME + " where key=?", new Object[]{str});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setItem(String str, String str2) {
        try {
            mDatabase.execSQL("replace into " + TABLE_NAME + "(key,value)values(?,?)", new Object[]{str, str2});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
