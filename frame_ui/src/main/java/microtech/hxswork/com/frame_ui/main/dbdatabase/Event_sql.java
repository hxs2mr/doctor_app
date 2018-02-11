package microtech.hxswork.com.frame_ui.main.dbdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by microtech on 2017/12/27.
 */

public class Event_sql  extends SQLiteOpenHelper {

    private static final  String System = "create table event("
            +"id integer primary key autoincrement,"
            +"title text,"//标题
            +"content text,"//文本
            +"type text,"//类型
            +"user_id text,"//user_id
            +"accid_id text,"//聊天需要
            +"time text,"//时间
            +"du text,"//已读和未读的状态
            +"qita text)";//预留字段
    public Event_sql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
