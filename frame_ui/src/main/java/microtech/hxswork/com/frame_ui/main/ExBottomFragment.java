package microtech.hxswork.com.frame_ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.LinkedHashMap;

import microtech.hxswork.com.frame_core.middle.bottom.BaseBottomFragment;
import microtech.hxswork.com.frame_core.middle.bottom.BottomItemFragment;
import microtech.hxswork.com.frame_core.middle.bottom.BottomTabBean;
import microtech.hxswork.com.frame_core.middle.bottom.ItemBuilder;
import microtech.hxswork.com.frame_ui.R;
import microtech.hxswork.com.frame_ui.login.LoginFragment;
import microtech.hxswork.com.frame_ui.main.Patient.PatientFragment;
import microtech.hxswork.com.frame_ui.main.Person.PersonFragment;
import microtech.hxswork.com.frame_ui.main.home.HomeFragment;
import microtech.hxswork.com.frame_ui.nim.DemoCache;

import static microtech.hxswork.com.frame_ui.login.LoginHandler.userBean;


/**
 * Created by microtech on 2017/11/17.
 */

public class ExBottomFragment extends BaseBottomFragment {
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemFragment> setItems(ItemBuilder builder) {
        final  LinkedHashMap<BottomTabBean,BottomItemFragment> items = new LinkedHashMap<>();
        items.put(new BottomTabBean("{fa-home}","工作台"),new HomeFragment());
        items.put(new BottomTabBean("{fa-plus}","患者"),new PatientFragment());
        items.put(new BottomTabBean("{fa-user}","我的"),new PersonFragment());
        return builder.addItem(items).build();

    }

    @Override
    public int setIndexFragment() {
        return 0;
    }
    @Override
    public int setClickedColor() {
        return Color.parseColor("#37BBFB");
    }



}
