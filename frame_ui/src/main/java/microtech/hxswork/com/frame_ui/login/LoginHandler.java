package microtech.hxswork.com.frame_ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.style.UnderlineSpan;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.common.ui.recyclerview.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

import microtech.hxswork.com.frame_core.bean.FristBean;
import microtech.hxswork.com.frame_core.bean.PersonBean;
import microtech.hxswork.com.frame_core.bean.UserBean;
import microtech.hxswork.com.frame_core.init.AccountManager;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleFields;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;
import microtech.hxswork.com.frame_core.util.SAToast;
import microtech.hxswork.com.frame_ui.main.Person.team.TeamDataConvert;
import microtech.hxswork.com.frame_ui.main.Person.team.TeamItem;

import static android.content.Context.MODE_PRIVATE;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_REGION_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_DOCTOR_ID;
import static microtech.hxswork.com.frame_core.net.RestClent.TIGE_GOV_ID;
/**
 * Created by microtech on 2017/11/15.
 */

public class LoginHandler {
    public static  UserBean userBean;
    public static FristBean  fristBean;
    public static PersonBean  personBean;
    public static void onSignIn(Context context,String response, ILoginListener loginListener) {
        final JSONObject object = com.alibaba.fastjson.JSON.parseObject(response).getJSONObject("obj");//解析返回的json数据  xxx：表示是json中相应的字段
        int code = JSON.parseObject(response).getInteger("code");//获取token
        if(code != 200)
        {
            SAToast.makeText(context,"登录失败 请检查账号和密码").show();
            return;
        }
        final String userId = object.getString("_id");//获取json数据中的userId字段
        final String name = object.getString("name");
        final String avatar = object.getString("avatar");
        final int gender = object.getInteger("sex");
        final String address = object.getString("address");
        final String birthday = object.getString("birthday");
        final String phone = object.getString("phone");

        final String qr_code = object.getString("qr_code");
        final String team_name = object.getString("team_name");
        final String team_doctor_name = object.getString("team_doctor_name");
        final String times = object.getString("times");
        final JSONObject im  =  object.getJSONObject("im_account");
        final JSONObject setting  =  object.getJSONObject("settings");

        final JSONObject gov  =  object.getJSONObject("gov");
        final String version_name = object.getString("versionname");
        final String size = object.getString("size");
        final String upgradeinfo = object.getString("upgradeinfo");
        final String updateurl = object.getString("updateurl");
         String accid = null;
         String token = null;
         int speak =0;
         int waring =0;
         if(setting!=null)
         {
             speak = setting.getInteger("speak");
             waring = setting.getInteger("warning");
             Save_Setting(context,waring,speak);
         }
        if(im!=null) {
              accid = im.getString("accid");
              token = im.getString("token");
        }
         String id_card= object.getString("id_card");
         int sign_number = object.getInteger("sign_number");;//签约数
         int visits = object.getInteger("visits");//随访数
         String job_number = object.getString("job_number");//工号
         String peoples =object.getString("peoples");//民族
         String desc =object.getString("desc");//简介
         String dept_name =object.getString("dept_name");//职位
         String region_id = "";
        if(object.getString("region_id")!=null) {
            region_id = object.getString("region_id");//地区ID
        }
        String gov_id = "";
        String dep_id="";
        String dep_name="";
        String dep_title="";
        if(gov!=null) {
            gov_id = gov.getString("_id");//民族
            dep_id = gov.getString("dep_id");
            dep_name = gov.getString("name");
            dep_title = gov.getString("title");
        }
        List<MultipleItemEntity> entities = init_team_linst(response);
        userBean = new UserBean(userId,name,avatar,gender+"",
                address,birthday+"",
                phone,accid,token,
                id_card,sign_number,visits,
                job_number,peoples,qr_code,entities,team_doctor_name,team_name,times);   //保存用户的状态 已经注册并登录成功
//String gov_id, String dep_id, String dep_name, String dep_title, String dept_name, String desc

        TIGE_REGION_ID = region_id;
        TIGE_GOV_ID = gov_id;
        TIGE_DOCTOR_ID = userId;
        personBean = new PersonBean(gov_id,dep_id,dep_name,dep_title,dept_name,desc);
        fristBean = new FristBean(region_id,gov_id,version_name,size,upgradeinfo,updateurl);
        if (code == 200) {
            AccountManager.setLoginState(true);
        /*  final UserProfile profile = new UserProfile(userId,name,avatar,gender+"",address,birthday+"",phone);
          DataBaseManager.getInstance().getDao().insert(profile);//插入数据*/

            //保存用户的状态 已经注册并登录成功
            //AccountManager.setLoginState(true);
            loginListener.onLoginSuccess();
        }else if(code == 203){
            SAToast.makeText(context,"登录授权失败").show();
        }else if(code == 202)
        {
            SAToast.makeText(context,"用户名不存在").show();
        }else if(code == 201)
        {
            SAToast.makeText(context,"用户名或密码错误").show();
        }else if(code == 404)
        {
            SAToast.makeText(context,"请求参数错误").show();
        }else if(code == 500)
        {
            SAToast.makeText(context,"服务器异常").show();
        }
    }

    private static List<MultipleItemEntity> init_team_linst(String response) {
        final JSONObject obj = JSON.parseObject(response).getJSONObject("obj");
        final JSONArray array;
        List<MultipleItemEntity> entity = new ArrayList<>();
        if(obj.getJSONArray("list")!=null)
        {
             array = obj.getJSONArray("list");
            entity = new TeamDataConvert().setJsonData(response).CONVERT();
            return  entity;
        }else {
            MultipleItemEntity entity1 = MultipleItemEntity.builder()
                    .setItemType(TeamItem.MY_TEAM)
                    .setField(MultipleFields.NAME,"")
                    .setField(MultipleFields.STATUE,"0")
                    .setField(MultipleFields.USER_ID,"1")
                    .build();
            entity.add(entity1);
            return  entity;
        }
    }

    public static void onSignUp(String response,ILoginListener loginListener){
        final JSONObject JSON = com.alibaba.fastjson.JSON.parseObject(response).getJSONObject("data");//解析返回的json数据  xxx：表示是json中相应的字段
        final long userId = JSON.getLong("userId");//获取json数据中的userId字段
        final String name = JSON.getString("name");
        final String avatar = JSON.getString("avatar");
        final String gender = JSON.getString("sex");
        final String address = JSON.getString("address");
        final String birthday = JSON.getString("birthday");
        final String phone = JSON.getString("phone");

        /*final UserProfile profile = new UserProfile(userId,name,avatar,gender,address,birthday,phone);
        DataBaseManager.getInstance().getDao().insert(profile);//插入数据*/

        //保存用户的状态 已经注册并登录成功
        AccountManager.setLoginState(true);
        loginListener.OnResgisterSuuecc();
    }


    public static void onSignIn1(Context context,String response) {
        final JSONObject object = com.alibaba.fastjson.JSON.parseObject(response).getJSONObject("obj");//解析返回的json数据  xxx：表示是json中相应的字段
        int code = JSON.parseObject(response).getInteger("code");//获取token
        if (code != 200) {
            SAToast.makeText(context, "登录失败").show();
            return;
        }
        final String userId = object.getString("_id");//获取json数据中的userId字段
        final String name = object.getString("name");
        final String avatar = object.getString("avatar");
        final int gender = object.getInteger("sex");
        final String address = object.getString("address");
        final String birthday = object.getString("birthday");
        final String phone = object.getString("phone");
        String desc =object.getString("desc");//简介
        String dept_name =object.getString("dept_name");//职位
        final String qr_code = object.getString("qr_code");
        final String team_name = object.getString("team_name");
        final String team_doctor_name = object.getString("team_doctor_name");
        final String times = object.getString("times");
        final JSONObject im = object.getJSONObject("im_account");
        final JSONObject setting  =  object.getJSONObject("settings");
        final JSONObject gov  =  object.getJSONObject("gov");

        final String version_name = object.getString("versionname");
        String accid = "";
        String token = "";
        if (im != null) {
            accid = im.getString("accid");
            token = im.getString("token");
        }

        int speak =0;
        int waring =0;

        if(setting!=null)
        {
            speak = setting.getInteger("speak");
            waring = setting.getInteger("warning");
            Save_Setting(context,waring,speak);
        }
        String id_card = object.getString("id_card");
        int sign_number = object.getInteger("sign_number");
        //签约数
        int visits = object.getInteger("visits");//随访数
        String job_number = object.getString("job_number");//工号
        String peoples = object.getString("peoples");//民族
        final String size = object.getString("size");
        final String upgradeinfo = object.getString("upgradeinfo");
        final String updateurl = object.getString("updateurl");
        String region_id = "";
        if(object.getString("region_id")!=null) {
            region_id = object.getString("region_id");//地区ID
        }
        String gov_id = "";
        String dep_id="";
        String dep_name="";
        String dep_title="";
        if(gov!=null) {
            gov_id = gov.getString("_id");//民族
            dep_id = gov.getString("dep_id");
            dep_name = gov.getString("name");
            dep_title = gov.getString("title");
        }
        List<MultipleItemEntity> entities = init_team_linst(response);
        TIGE_REGION_ID = region_id;
        TIGE_GOV_ID = gov_id;
        TIGE_DOCTOR_ID = userId;
        fristBean = new FristBean(region_id,gov_id,version_name,size,upgradeinfo,updateurl);
        personBean = new PersonBean(gov_id,dep_id,dep_name,dep_title,dept_name,desc);
        userBean = new UserBean(userId,name,avatar,gender+"",
                address,birthday+"",
                phone,accid,token,
                id_card,sign_number,visits,
                job_number,peoples,qr_code,entities,team_doctor_name,team_name,times);   //保存用户的状态 已经注册并登录成功
    }

    public static void Save_Setting(Context context, int warning, int speak) {//保存设置推送的值
        //指定操作的文件名称
        //指定操作的文件名称
        SharedPreferences share = context.getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit(); //编辑文件
        edit.putString("warning", warning+"");
        edit.putString("speak", speak+"");
        edit.commit();
    }

}
