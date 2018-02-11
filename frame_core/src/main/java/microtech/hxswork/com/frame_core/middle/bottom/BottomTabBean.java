package microtech.hxswork.com.frame_core.middle.bottom;

/**
 * Created by microtech on 2017/11/17.
 */

public final class BottomTabBean {
    private final CharSequence ICON;
    private final CharSequence TITLE;

    public BottomTabBean(CharSequence icon,CharSequence title){
        this.ICON = icon;
        this.TITLE = title;
    }

    public CharSequence getICON(){
        return ICON;
    }
    public CharSequence getTITLE(){
        return TITLE;
    }
}
