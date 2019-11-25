package com.example.tomreaddle.kundalik.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tomreaddle.kundalik.entity.note;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "list";
    public static final String TABLE_NOTES = "note";
    public static final String ID_NOTES = "_id";
    public static final String TITLE_NOTES = "title";
    public static final String EMOJI_NOTES = "emoji";
    public static final String FEELING_NOTES = "feeling";
    public static final String DISCRIPTION_NOTES = "discription";
    public static final String TIME_NOTES = "time";
    private final Context mContext;
    SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NOTES + "(" + ID_NOTES + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_NOTES + " TEXT,"
                + EMOJI_NOTES + " TEXT,"
                + FEELING_NOTES + " TEXT,"
                + DISCRIPTION_NOTES + " TEXT,"
                + TIME_NOTES + " TEXT" +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long note_enter(note note){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TITLE_NOTES , note.getTitle());
        cv.put(DISCRIPTION_NOTES , note.getDescription());
        cv.put(TIME_NOTES , note.getTime());

        long notes = db.insert(TABLE_NOTES , null , cv);
        db.close();
        return notes;

    }

     public List<note> note_list(){
        List<note> list = new ArrayList<note>();
        SQLiteDatabase db = this.getReadableDatabase();
        String s = "select * from " + TABLE_NOTES ;
         Cursor cursor = db.rawQuery(s , null);

         if(cursor.moveToLast()) {
             do {
                 list.add(new note(cursor.getInt(cursor.getColumnIndex(ID_NOTES)) ,
                         cursor.getString(cursor.getColumnIndex(TITLE_NOTES)) ,
                         cursor.getString(cursor.getColumnIndex(EMOJI_NOTES)) ,
                         cursor.getString(cursor.getColumnIndex(FEELING_NOTES)) ,
                         cursor.getString(cursor.getColumnIndex(DISCRIPTION_NOTES)) ,
                         cursor.getString(cursor.getColumnIndex(TIME_NOTES))));
             } while (cursor.moveToPrevious());
         } db.close();
         return list;
    }

    public long update(note note){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE_NOTES , note.getTitle());
        cv.put(DISCRIPTION_NOTES , note.getDescription());
        cv.put(TIME_NOTES , note.getTime());

        long editted = db.update(TABLE_NOTES , cv , ID_NOTES + " =?" , new String[]{String.valueOf(note.getId())});
        db.close();
        return editted;
    }

    public  long delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        long delete = db.delete(TABLE_NOTES , ID_NOTES + " =?" , new String[]{String.valueOf(id)});
        db.close();
        return delete;
    }

}
