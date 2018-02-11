package microtech.hxswork.com.frame_ui.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by microtech on 2017/11/15.
 */

public class ReleaseOpenHelper extends DaoMaster.OpenHelper{

    public ReleaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    public ReleaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

}
