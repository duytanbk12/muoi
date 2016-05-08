package fs.tandat.soccernetwork.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import fs.tandat.soccernetwork.bean.User;

/**
 * Created by dracu on 24/04/2016.
 */
public class UserHelper {
    DatabaseHelper dbHelper;
    public UserHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }



    public boolean checkUser(String username, String password) {
        String sql = "SELECT * FROM user_profiles WHERE username = '" + username + "' AND password = '" + password + "'";
        SQLiteDatabase db = dbHelper.openDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Log.d("checkUser", sql);
        boolean check = cursor.moveToFirst();
        db.close();
        return check;
    }

    public boolean addUser(User newUser) {
        String sql = "INSERT INTO user_profiles(username, password, email, phone_number, status, district_id, user_type, is_verified, created) " +
                "VALUES(?, ?, ?, ?, 1, 1, 0, 0, CURRENT_TIMESTAMP)";
        boolean check = false;
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, newUser.getUsername());
            statement.bindString(2, newUser.getPassword());
            statement.bindString(3, newUser.getEmail());
            statement.bindString(4, newUser.getPhone());
            statement.executeInsert();
            check = true;
        } catch (SQLiteException ex){
            ex.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            dbHelper.closeDatabase();
            return check;
        }

    }

    public User getUser(String username) {
        String sql = String.format("select * from user_profiles where username='%s'", username);
        SQLiteDatabase db = dbHelper.openDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Log.d("getUser", sql);
        User user = null;
        if(cursor != null){
            if(cursor.moveToFirst()) {
                boolean is_verfified = cursor.getInt(9) > 0;
                user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),
                        cursor.getInt(7), cursor.getString(8), is_verfified, cursor.getString(10),
                        cursor.getString(11), cursor.getString(12), cursor.getString(13));
            }
        }
        return user;
    }

    public User getUserByUserId(int user_id) {
        String sql = String.format("select * from user_profiles where user_id=%d", user_id);
        SQLiteDatabase db = dbHelper.openDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        Log.d("getUser", sql);
        User user = null;
        if(cursor != null){
            if(cursor.moveToFirst()) {
                boolean is_verfified = cursor.getInt(9) > 0;
                user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6),
                        cursor.getInt(7), cursor.getString(8), is_verfified, cursor.getString(10),
                        cursor.getString(11), cursor.getString(12), cursor.getString(13));
            }
        }
        return user;
    }

    public void updateUser(int id,String username,String password, String email, String phone) {
        SQLiteDatabase db = dbHelper.openDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put("username", username);
        newValues.put("password",password);
        newValues.put("email", email);
        newValues.put("phone_number",phone);
        db.update("user_profiles", newValues, "user_id="+id, null);
    }
}
