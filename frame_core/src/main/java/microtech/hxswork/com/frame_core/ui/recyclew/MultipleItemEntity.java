package microtech.hxswork.com.frame_core.ui.recyclew;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by microtech on 2017/11/20.
 */

public class MultipleItemEntity implements MultiItemEntity {
    private final ReferenceQueue<LinkedHashMap<Object,Object>> ITEM_QUENE = new ReferenceQueue<>();//处理当recycle数据庞大的时候防止内存泄露
    private final LinkedHashMap<Object,Object> MULTIPLE_FIRLDS= new LinkedHashMap<>();
    private final SoftReference<LinkedHashMap<Object,Object >> FIRLFS_REFERENCE=new SoftReference<LinkedHashMap<Object, Object>>(MULTIPLE_FIRLDS,ITEM_QUENE);
    public MultipleItemEntity(LinkedHashMap<Object,Object> fields){
        FIRLFS_REFERENCE.get().putAll(fields);
    }

    public static MultipleItemEntityBuilder builder(){
        return new MultipleItemEntityBuilder();
    }
    @Override
    public int getItemType() {
        return (int) FIRLFS_REFERENCE.get().get(MultipleFields.ITEM_TYPE);
    }
    @SuppressWarnings("unchecked")
    public final <T> T getField(Object key){
        return (T)FIRLFS_REFERENCE.get().get(key);
    }
    public final LinkedHashMap<?,?> getFields(){
        return FIRLFS_REFERENCE.get();
    }
    public final MultipleItemEntity setField(Object key,Object value){
        FIRLFS_REFERENCE.get().put(key,value);
        return this;
    }
}
