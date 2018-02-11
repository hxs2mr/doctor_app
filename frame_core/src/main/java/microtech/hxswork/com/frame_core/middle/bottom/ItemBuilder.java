package microtech.hxswork.com.frame_core.middle.bottom;

import java.util.LinkedHashMap;

/**
 * Created by microtech on 2017/11/17.
 */

public final class ItemBuilder {
    private final LinkedHashMap<BottomTabBean,BottomItemFragment> ITEMS= new LinkedHashMap<>();
    static ItemBuilder builder(){
        return new ItemBuilder();
    }
    public final ItemBuilder addItem(BottomTabBean bean,BottomItemFragment fragment){
        ITEMS.put(bean,fragment);
        return this;
    }

    public final ItemBuilder addItem(LinkedHashMap<BottomTabBean,BottomItemFragment> items){
        ITEMS.putAll(items);
        return this;
    }

    public final LinkedHashMap<BottomTabBean,BottomItemFragment> build(){
        return ITEMS;
    }
}
