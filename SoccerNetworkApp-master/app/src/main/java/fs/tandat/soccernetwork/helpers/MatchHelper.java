package fs.tandat.soccernetwork.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

import fs.tandat.soccernetwork.bean.Match;
import fs.tandat.soccernetwork.bean.User;

/**
 * Created by dracu on 24/04/2016.
 */
public class MatchHelper {
    DatabaseHelper dbHelper;
    private Context context;
    public MatchHelper(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }
    public ArrayList<Match> getYourMatches(String username){
        ArrayList<Match> matches = new ArrayList<>();
        UserHelper userHelper = new UserHelper(context);
        User user = userHelper.getUser(username);
        int user_id = user.getUser_id();
        String sql = "select match_id, field_name, username, maximum_players, price, start_time, end_time " +
                "from matches join user_profiles on matches.host_id = user_id " +
                "join fields on fields.field_id=matches.field_id " +
                " where user_id = " + user_id +
                " or match_id in (select match_id from slots where user_id =" + user_id +
                ") order by start_time";
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getYourMatches", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Match m = new Match();
                    m.setMatch_id(cursor.getInt(0));
                    m.setField_name(cursor.getString(1));
                    m.setUsername(cursor.getString(2));
                    m.setMaximum_players(cursor.getInt(3));
                    m.setPrice(cursor.getInt(4));
                    m.setStart_time(cursor.getString(5));
                    m.setEnd_time(cursor.getString(6));
                    matches.add(m);
                    cursor.moveToNext();
                }
                return matches;
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<Match> getUpcommingMatches(String username){
        ArrayList<Match> matches = new ArrayList<>();
        UserHelper userHelper = new UserHelper(context);
        User user = userHelper.getUser(username);
        String sql =  "select match_id, field_name, username, maximum_players, price, start_time, end_time " +
                "from matches join user_profiles on matches.host_id = user_id " +
                "join fields on fields.field_id=matches.field_id where start_time > date('now') order by start_time";
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getUpcommingMatches", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Match m = new Match();
                    m.setMatch_id(cursor.getInt(0));
                    m.setField_name(cursor.getString(1));
                    m.setUsername(cursor.getString(2));
                    m.setMaximum_players(cursor.getInt(3));
                    m.setPrice(cursor.getInt(4));
                    m.setStart_time(cursor.getString(5));
                    m.setEnd_time(cursor.getString(6));
                    matches.add(m);
                    cursor.moveToNext();
                }
                return matches;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Match getMatch(int match_id) {
        Match m = null;
        String sql =  "select match_id, field_id, host_id, status, maximum_players, price, start_time, end_time, " +
                "is_verified, verification_code, created, updated, deleted " +
                "from matches where match_id = " + match_id;
        try {
            SQLiteDatabase db = dbHelper.openDatabase();
            Log.d("getMatch", sql);
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                m = new Match();
                m.setMatch_id(cursor.getInt(0));
                m.setField_id(cursor.getInt(1));
                m.setHost_id(cursor.getInt(2));
                m.setStatus(cursor.getInt(3));
                m.setMaximum_players(cursor.getInt(4));
                m.setPrice(cursor.getInt(5));
                m.setStart_time(cursor.getString(6));
                m.setEnd_time(cursor.getString(7));
                m.setIs_verified(cursor.getInt(8)>0);
                m.setVerification_code(cursor.getString(9));
                m.setCreated(cursor.getString(10));
                m.setUpdated(cursor.getString(11));
                m.setDeleted(cursor.getString(12));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return m;

    }

    public int getRemainingSlots(int match_id){
        int reservedSlot = (new SlotHelper(context)).getReservedSlots(match_id);
        int max_players = this.getMaxPlayers(match_id);
        return max_players - reservedSlot;
    }

    private int getMaxPlayers(int match_id) {
        int max_players = 0;
        String sql = "select maximum_players from matches where match_id=" + match_id;
        try{
            SQLiteDatabase db = dbHelper.openDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor != null && cursor.moveToFirst()) {
                max_players = cursor.getInt(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return max_players;
    }

    //Hoai
    // - create a match
    public boolean createMatch(Match match){
        String qr = "insert into matches values("+match.getMatch_id()+", "+match.getField_id()+", "+match.getHost_id()+", " +
                ""+match.getStatus()+", "+match.getMaximum_players()+", "+match.getPrice()+", '"+match.getStart_time()+"', " +
                "'"+match.getEnd_time()+"', '"+match.is_verified()+"', '"+match.getVerification_code()+"','"+match.getCreated()+"'," +
                "'"+match.getUpdated()+"','"+match.getDeleted()+"') ";
        Log.d("sql",qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeInsert();
        return true;
    }

    //get finally match
    public int getFinallyMatch(){
        int maxMatch_id = 0;
        String qr = "select max(match_id) from matches";
        try{
            SQLiteDatabase db = dbHelper.openDatabase();
            Cursor cursor = db.rawQuery(qr, null);
            if (cursor != null && cursor.moveToFirst()) {
                maxMatch_id = cursor.getInt(0);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return maxMatch_id;
    }

    //update a match
    public boolean updateMatch(Match match){
        String qr = "update  matches set field_id = "+match.getField_id()+", " +
                "host_id = "+match.getHost_id()+", status = "+match.getStatus()+", maximum_players = "+match.getMaximum_players()+", " +
                "price = "+match.getPrice()+", start_time = '"+match.getStart_time()+"', end_time = '"+match.getEnd_time()+"', " +
                "is_verified = '"+match.is_verified()+"', verification_code = '"+match.getVerification_code()+"',created = '"+match.getCreated()+"'," +
                "updated = '"+match.getUpdated()+"',deleted = '"+match.getDeleted()+"' where match_id = "+match.getMatch_id()+" ";

        Log.d("qr",qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.execute();
        return true;
    }

    //delete match
    public boolean deleteMatch(Match match){
        String qr = "Delete from matches where match_id = "+match.getMatch_id()+" ";
        Log.d("qr", qr);
        SQLiteDatabase db = dbHelper.openDatabase();
        SQLiteStatement statement = db.compileStatement(qr);
        statement.executeUpdateDelete();
        return true;
    }
}
