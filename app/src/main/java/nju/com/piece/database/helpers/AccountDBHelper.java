package nju.com.piece.database.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import nju.com.piece.database.pos.AccountPO;

/**
 * Created by shen on 15/6/8.
 */
public class AccountDBHelper extends DatabaseHelper {

    private static final String KEY_ID = "_id";
    protected static final String TABLE_NAME = "account";
    private static final String COL_NAME = "username";
    private static final String COL_PSWD = "password";

    protected final static String DATABASE_NAME = "account.db";

    public static AccountDBHelper instance(Context context) {
        return new AccountDBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    protected AccountDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    protected String DatabaseCreate() {
        return "create table " + TABLE_NAME +
                " ("+KEY_ID+" integer primary key autoincrement, " +
                COL_NAME +" TEXT not null, " + COL_PSWD +" TEXT not null);";
    }

//set current account
    public void setAccount(AccountPO po){
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, po.getName());
        cv.put(COL_PSWD, po.getPswd());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME, "_id", cv);
        db.close();
    }

//    clear the account info
    public void delAccount(){
        String where = null;
        String[] whereArgs = null;

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, where, whereArgs);
        db.close();
    }

    public void updatePswd(String username, String pswd) {
        ContentValues cv = new ContentValues();
        cv.put(COL_PSWD , pswd);

        String where = COL_NAME + " = ?";
        String[] whereArgs = new String[]{username};

        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, cv, where, whereArgs);
        db.close();
    }

    public AccountPO getAccount() {
        String[] result_cols = new String[]{COL_NAME, COL_PSWD};
        String where = null;
        String[] whereArgs = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, result_cols, where, whereArgs, groupBy, having, order);

        if (cursor.moveToNext()) {
            AccountPO po = getPOByCursor(db, cursor);
            cursor.close();
            db.close();
            return po;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }


    private AccountPO getPOByCursor(SQLiteDatabase db, Cursor cursor){
        int index_name = cursor.getColumnIndex(COL_NAME);
        int index_pswd = cursor.getColumnIndex(COL_PSWD);

        String tag = cursor.getString(index_name);
        String pswd = cursor.getString(index_pswd);

        AccountPO po = new AccountPO(tag,pswd);

        return po;
    }
}
