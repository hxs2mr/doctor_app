package microtech.hxswork.com.frame_core.ui;

import android.content.Context;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

import java.util.WeakHashMap;

/**
 * Created by microtech on 2017/11/14.
 */

public class LoaderCreator {//已缓存方式Loading  提高性能
    private static  final WeakHashMap<String,Indicator> LOADING_MAP=new WeakHashMap<>();

    static AVLoadingIndicatorView create(String type, Context context){
        final AVLoadingIndicatorView avLoadingIndicatorView = new AVLoadingIndicatorView(context);
        if(LOADING_MAP.get(type) == null){
            final Indicator  indicator = getIndicator(type);
            LOADING_MAP.put(type,indicator);
        }
        avLoadingIndicatorView.setIndicator(LOADING_MAP.get(type));
        return avLoadingIndicatorView;
    }


    private static Indicator getIndicator(String name){
        if(name == null|| name.isEmpty()){
            return null;
        }
        final StringBuilder drawableClassName = new StringBuilder();
        if(!name.contains(".")) {
            final String defaulePackagName = AVLoadingIndicatorView.class.getPackage().getName();
            drawableClassName.append(defaulePackagName)
                            .append(".indicators")
                            .append(".");
        }
        drawableClassName.append(name);
        try {
            final Class<?> drawableClass = Class.forName(drawableClassName.toString());
            return (Indicator) drawableClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

