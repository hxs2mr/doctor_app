package microtech.hxswork.com.frame_core.ui.recyclew;

import com.google.auto.value.AutoValue;

/**
 * Created by microtech on 2017/11/20.用于主界面Toolbar的颜色渐变
 */
@AutoValue
public abstract class Rgb {
    public abstract  int red();
    public abstract int green();
    public  abstract int blue();
   public static Rgb create(int red,int green,int blue){
       return new AutoValue_Rgb(red,green,blue);
    }
}
