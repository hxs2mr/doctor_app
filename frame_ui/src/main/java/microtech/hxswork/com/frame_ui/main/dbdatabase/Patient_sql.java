package microtech.hxswork.com.frame_ui.main.dbdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by microtech on 2017/11/8.数据库 用于保存本地的患者信息
 */

public  class Patient_sql extends SQLiteOpenHelper {

    private static final  String System = "create table patient("
            +"id integer primary key autoincrement,"
            +"name text,"
            +"number text,"
            +"user_id text)";
    public Patient_sql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(System);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
