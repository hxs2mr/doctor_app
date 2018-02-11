package microtech.hxswork.com.frame_core.ui.recyclew;

import java.util.LinkedHashMap;
import java.util.WeakHashMap;

/**
 * Created by microtech on 2017/11/20.
 */

public class MultipleItemEntityBuilder {
    private static final LinkedHashMap<Object,Object> FIELDS = new LinkedHashMap<>();
    public MultipleItemEntityBuilder(){
        //先清楚之前的数据
        FIELDS.clear();
    }
    public final MultipleItemEntityBuilder setItemType(int itemType){
        FIELDS.put(MultipleFields.ITEM_TYPE,itemType);
        return this;
    }
    public final MultipleItemEntityBuilder setField(Object key,Object value){
        FIELDS.put(key,value);
        return this;
    }
    public  final MultipleItemEntityBuilder setFields(WeakHashMap<?,?>map)
    {
        FIELDS.putAll(map);
        return this;
    }
    public final MultipleItemEntity build(){
       return new MultipleItemEntity(FIELDS);
    }

}
