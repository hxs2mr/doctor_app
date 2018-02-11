package microtech.hxswork.com.frame_core.ui.recyclew;

import java.util.ArrayList;

/**
 * Created by microtech on 2017/11/20.数据转换处理
 */

public abstract class DataConVerter {
    protected final ArrayList<MultipleItemEntity> ENTITYES = new ArrayList<>();
    private String mJsonData= null;
    public abstract ArrayList<MultipleItemEntity> CONVERT();

    public DataConVerter setJsonData(String json){
        this.mJsonData = json;
        return this;
    }
    protected  String getJsonData(){
        if(mJsonData == null||mJsonData.isEmpty()){
            throw  new NullPointerException("DATA IS NULL");
        }
        return mJsonData;
    }
}
