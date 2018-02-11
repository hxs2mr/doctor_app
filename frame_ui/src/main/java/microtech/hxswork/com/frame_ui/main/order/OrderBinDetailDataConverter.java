package microtech.hxswork.com.frame_ui.main.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import microtech.hxswork.com.frame_core.ui.OrderQianFields;
import microtech.hxswork.com.frame_core.ui.recyclew.DataConVerter;
import microtech.hxswork.com.frame_core.ui.recyclew.MultipleItemEntity;

/**
 * Created by microtech on 2017/12/1.患者详情的数据解析
 */

public class OrderBinDetailDataConverter extends DataConVerter{
    @Override
    public ArrayList<MultipleItemEntity> CONVERT() {
              int code = JSON.parseObject(getJsonData()).getInteger("code");
              if(code == 200) {

                  final JSONObject array = JSON.parseObject(getJsonData()).getJSONObject("obj");
                  String name = array.getString("name");
                  String sex = array.getString("sex");
                  String age = array.getString("age");
                  String birthday = array.getString("birthday");
                  String thumb="";
                  if( array.getString("avatar")!= null) {
                      thumb = array.getString("avatar");
                  }
                  String statue = array.getString("effect");
                  String time = array.getString("timeu");
                  String time_stop = array.getString("time_stop");
                  String zhu ="";
                  if( array.getString("peoples")!=null)
                  {
                      zhu = array.getString("peoples");
                  }
                  String number = array.getString("number_no");
                  String bin = array.getString("marking");
                  String userid = array.getString("user_id");
                  String qian_ren = array.getString("doctor_name");
                  String qian_time = array.getString("timeu");
                  String phone = array.getString("phone");
                  String address = array.getString("address");
                  String region_id = array.getString("region_id");
                  String gov_id = array.getString("gov_id");
                  String doctor_id = array.getString("doctor_id");

                  String id_card="";
                  if(array.getString("id_card")!=null)
                  {
                      id_card=array.getString("id_card");
                  }

                  String _id = array.getString("_id");
                  ArrayList<String> selectedPhotos = new ArrayList<>();
                  final JSONArray array_image = array.getJSONArray("upload_img");
                  int size = array_image.size();
                  if(array_image!=null)
                  {
                      for(int i =0 ; i < size ; i ++)
                      {
                          selectedPhotos.add(array_image.getString(i));
                      }
                  }

                  MultipleItemEntity entity = MultipleItemEntity.builder()
                          .setField(OrderQianFields.NAME, name)
                          .setField(OrderQianFields._ID, _id)
                          .setField(OrderQianFields.SEX, sex)
                          .setField(OrderQianFields.BIN, bin)
                          .setField(OrderQianFields.BIRTHDAY, birthday)
                          .setField(OrderQianFields.AGE, age)
                          .setField(OrderQianFields.THUMB, thumb)
                          .setField(OrderQianFields.STATUE, statue)
                          .setField(OrderQianFields.TIME, time)
                          .setField(OrderQianFields.ZHU, zhu)
                          .setField(OrderQianFields.ID_CARD, id_card)
                          .setField(OrderQianFields.NUMBER, number)
                          .setField(OrderQianFields.USER_ID, userid)
                          .setField(OrderQianFields.PERSON_QIAN, qian_ren)
                          .setField(OrderQianFields.TIME, qian_time)
                          .setField(OrderQianFields.PHONE, phone)
                          .setField(OrderQianFields.ADDRESS, address)
                          .setField(OrderQianFields.CODE, code)
                          .setField(OrderQianFields.IMAGE_ARRAY, selectedPhotos)
                          .setField(OrderQianFields.GOV_ID, gov_id)
                          .setField(OrderQianFields.REGON_ID, region_id)
                          .setField(OrderQianFields.DOCTOR_ID, doctor_id)
                          .setField(OrderQianFields.TIME_STOP, time_stop)
                          .build();
                  ENTITYES.add(entity);
              }else {
                  MultipleItemEntity entity = MultipleItemEntity.builder()
                          .setField(OrderQianFields.CODE, code)
                          .build();
                  ENTITYES.add(entity);
              }
        return ENTITYES;
    }
}
