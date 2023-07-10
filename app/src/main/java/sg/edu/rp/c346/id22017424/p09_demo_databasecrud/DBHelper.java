package sg.edu.rp.c346.id22017424.p09_demo_databasecrud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "simplenotes.db";

    // To make the app calls onUpgrade(), increment the variable DATABASE_VERSION from 1 to 2.
    private static final int DATABASE_VERSION = 2; //1;
    private static final String TABLE_NOTE = "note";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE_CONTENT = "note_content";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNoteTableSql = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_CONTENT + " TEXT ) ";
        db.execSQL(createNoteTableSql);
        Log.i("info", "created tables");

        //Dummy records, to be inserted when the database is created
        for (int i = 0; i< 4; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE_CONTENT, "Data number " + i);
            db.insert(TABLE_NOTE, null, values);
        }
        Log.i("info", "dummy records inserted");
    }

    // To add a column called module_name to an existing table, modify onUpgrade() to execute
    // an SQL statement as below. To make the app calls onUpgrade(), increment the variable
    // DATABASE_VERSION from 1 to 2
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
        db.execSQL("ALTER TABLE " + TABLE_NOTE + " ADD COLUMN  module_name TEXT ");
        //onCreate(db); // Delete as table already created
    }

    public long insertNote(String noteContent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, noteContent);
        // Line 42 will get a number, which is the record id (the primary key, _id) of the table for the
        // record inserted. The id will be -1 if the insert failed. Thus, we can use it to check if a
        // record is inserted successfully.
        long result = db.insert(TABLE_NOTE, null, values);
        db.close();
        Log.d("SQL Insert","ID:"+ result); //id returned, shouldn’t be -1
        return result;
    }

    // This method will retrieve the Note records.
    // Thereafter, the records are put into an ArrayList to be returned.
    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns= {COLUMN_ID, COLUMN_NOTE_CONTENT};

        Cursor cursor = db.query(TABLE_NOTE, columns, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    // To update the note
    public int updateNote(Note data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_CONTENT, data.getNoteContent());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        // Line 96 will get a number representing the number of rows affected in the table. Usually,
        // for record updates, we expect 1 or more records to be updated. In this case, we expect
        // only 1 record though. Thus, we can use it to check if a record is updated successfully
        // if the affected record is 1.
        int result = db.update(TABLE_NOTE, values, condition, args);
        db.close();
        return result;
    }

    public int deleteNote(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        // Line 79, will get a number representing the number of rows affected in the table.
        // Usually, we expect 1 or more records to be affected. In this case, we expect only 1
        // record though. Thus, we can use it to check if a record is deleted successfully if the
        // affected record is 1.
        int result = db.delete(TABLE_NOTE, condition, args);
        db.close();
        return result;
    }

    // method used to retrieve only records that contain a given keyword
    public ArrayList<Note> getAllNotes(String keyword) {
        ArrayList<Note> notes = new ArrayList<Note>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns= {COLUMN_ID, COLUMN_NOTE_CONTENT};
        String condition = COLUMN_NOTE_CONTENT + " Like ?";
        String[] args = { "%" +  keyword + "%"};
        Cursor cursor = db.query(TABLE_NOTE, columns, condition, args,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String noteContent = cursor.getString(1);
                Note note = new Note(id, noteContent);
                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }


}
