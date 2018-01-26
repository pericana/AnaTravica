package hr.math.anatravica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 26/01/2018.
 */

public class mojaBaza {


    static final String KEY_ROWID = "_id";
    static final String KEY_NAZIV = "naziv";
    static final String KEY_AUTOR = "autor";

    static final String KEY_ROWID_PERIODA = "id_perioda";
    static final String KEY_RAZDOBLJE = "razdoblje";
    static final String KEY_GL_PRED = "glavni_predstavnik";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "DB2";

    static final String DATABASE_TABLE = "SLIKE";
    static final String DATABASE_TABLE2 = "PERIOD";

    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE =
            "create table SLIKE (_id integer primary key autoincrement, "
                    + "naziv text not null, autor text not null);";

    static final String DATABASE_CREATE2 =
            "create table PERIOD (id_perioda integer primary key autoincrement, "
                    + "razdoblje text not null, glavni_predstavnik text not null);";


    Context context;


    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public mojaBaza(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading db from" + oldVersion + "to"
                    + newVersion );
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //---opens the database---
    public mojaBaza open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }


    //---insert a contact into the database---
    public long insertSlika(String naziv, String autor)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAZIV, naziv);
        initialValues.put(KEY_AUTOR, autor);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public long insertPeriod(String razdoblje, String glPred)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_RAZDOBLJE, razdoblje);
        initialValues.put(KEY_GL_PRED, glPred);
        return db.insert(DATABASE_TABLE2, null, initialValues);
    }


    //---deletes a particular contact---
    public boolean deleteSlika(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deletePeriod(long rowId)
    {
        return db.delete(DATABASE_TABLE2, KEY_ROWID_PERIODA + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllSlike()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAZIV,
                KEY_AUTOR}, null, null, null, null, null);
    }

    public Cursor getAllPeriod()
    {
        return db.query(DATABASE_TABLE2, new String[] {KEY_ROWID_PERIODA, KEY_RAZDOBLJE,
                KEY_GL_PRED}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getSlikaByAutor(String autor) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                                KEY_NAZIV, KEY_AUTOR}, KEY_AUTOR + "='" + autor + "'", null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
