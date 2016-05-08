package fs.tandat.soccernetwork.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

/**
 * Created by dracu on 28/04/2016.
 */
public class SlotHelper {
    DatabaseHelper dbHelper;
    private Context context;

    public SlotHelper(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addSlots(int match_id, int user_id, int quantity){
        String sql = "insert into slots(match_id, user_id, quantity, is_verified, created) values(?,?,?, 0, CURRENT_TIMESTAMP);";
        try{
            SQLiteDatabase db = dbHelper.openDatabase();
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, match_id);
            statement.bindLong(2, user_id);
            statement.bindLong(3, quantity);
            statement.executeInsert();
            dbHelper.closeDatabase();
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    public int getReservedSlots(int match_id) {
        int slots = 0;
        String sql = "select sum(quantity) from slots where match_id = " + match_id;
        try{
            SQLiteDatabase db = dbHelper.openDatabase();
            Cursor cursor = db.rawQuery(sql, null);
            if(cursor != null && cursor.moveToFirst()){
                slots = cursor.getInt(0);
            }
            dbHelper.closeDatabase();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return slots;
    }
}
