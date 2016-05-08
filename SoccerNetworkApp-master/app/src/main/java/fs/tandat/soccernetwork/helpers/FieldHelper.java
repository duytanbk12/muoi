package fs.tandat.soccernetwork.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import fs.tandat.soccernetwork.bean.Field;

/**
 * Created by dracu on 27/04/2016.
 */
public class FieldHelper {
    DatabaseHelper dbHelper;

    public FieldHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Field getField(int field_id){
        Field f = null;
        String sql =  "select * from fields where field_id = " + field_id;
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getMatch", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                f = new Field();
                f.setField_id(cursor.getInt(0));
                f.setField_name(cursor.getString(1));
                f.setDistrict_id(cursor.getInt(2));
                f.setAddress(cursor.getString(3));
                f.setLatitude(cursor.getDouble(4));
                f.setLongitude(cursor.getDouble(5));
                f.setPhone_number(cursor.getString(6));
                f.setCreated(cursor.getString(7));
                f.setUpdated(cursor.getString(8));
                f.setDeleted(cursor.getString(9));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return f;

    }

    public ArrayList<Field> getListField(){
        String qr = "Select * from fields";
        SQLiteDatabase db = dbHelper.openDatabase();
        ArrayList<Field> fieldLst = new ArrayList<>();
        Field field = null;
        Cursor cursor = db.rawQuery(qr, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    field = new Field(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3),
                            cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getString(7),
                            cursor.getString(8), cursor.getString(9));
                    fieldLst.add(field);
                    cursor.moveToNext();
                }
                return fieldLst;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
