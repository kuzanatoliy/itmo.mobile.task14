package ru.kuzmiankou.database2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Anatoliy on 12.11.2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "contacts";
    public static final String ID_FIELD = "_id";
    public static final String FIRST_NAME_FIELD = "first_name";
    public static final String LAST_NAME_FIELD = "last_name";
    public static final String PHONE_FIELD = "phone";
    public static final String EMAIL_FIELD = "email";

    public DatabaseManager(Context context){
        super(context, "contacts_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("db", "onCreate");
        String sql = "CREATE TABLE " + TABLE_NAME
                + " (" + ID_FIELD + " INTEGER, "
                + FIRST_NAME_FIELD + " TEXT,"
                + LAST_NAME_FIELD + " TEXT,"
                + PHONE_FIELD + " TEXT,"
                + EMAIL_FIELD + " TEXT,"
                + "PRIMARY KEY (" + ID_FIELD + "));";
        sqLiteDatabase.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        Log.d("db", "onUpdate");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Contact addContact (Contact contact) {
        Log.d("db", "addContact");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME_FIELD, contact.getFirstName());
        values.put(LAST_NAME_FIELD, contact.getLastName());
        values.put(PHONE_FIELD, contact.getPhone());
        values.put(EMAIL_FIELD, contact.getEmail());
        long id = db.insert(TABLE_NAME, null, values);
        contact.setId(id);
        db.close();
        return contact;
    }

    public Contact getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] {
                ID_FIELD, FIRST_NAME_FIELD, LAST_NAME_FIELD,
                PHONE_FIELD, EMAIL_FIELD }, ID_FIELD + "=?",
                new String[] {String.valueOf(id)}, null,
                null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            Contact contact = new Contact(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4));
            contact.setId(cursor.getLong(0));
            return contact;
        }
        return null;
    }

    public Cursor getContactsCursor() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public void deleteContact(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_FIELD + " = ?", new String[] { String.valueOf(id)});
        db.close();
    }
}
