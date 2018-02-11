package microtech.hxswork.com.frame_core.dialogpicker.id_cityname;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.CityBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.DistrictBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.bean.ProvinceBean;
import microtech.hxswork.com.frame_core.dialogpicker.city_20170724.utils;


/**
 * Created by microtech on 2017/10/31.
 */

public class IdToCityName {
    //省份数据
   public ArrayList<ProvinceBean> mProvinceBeanArrayList = new ArrayList<>();

    //城市数据
    public  ArrayList<ArrayList<CityBean>> mCityBeanArrayList;

    //地区数据
    public ArrayList<ArrayList<ArrayList<DistrictBean>>> mDistrictBeanArrayList;

    private ProvinceBean[] mProvinceBeenArray;

    private ProvinceBean mProvinceBean;

    private CityBean mCityBean;

    private DistrictBean mDistrictBean;

    /**
     * key - 省 value - 市
     */
    protected Map<String, CityBean[]> mPro_CityMap = new HashMap<String, CityBean[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, DistrictBean[]> mCity_DisMap = new HashMap<String, DistrictBean[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, DistrictBean> mDisMap = new HashMap<String, DistrictBean>();

    /**
     * 解析省市区的XML数据
     */

    public IdToCityName(Context context)
    {
        initProvinceDatas(context);
    }
    protected void initProvinceDatas(Context context) {
        String cityJson = utils.getJson(context, "city_20170724.json");
        Type type = new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType();

        mProvinceBeanArrayList = new Gson().fromJson(cityJson, type);
        mCityBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());
        mDistrictBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());

        //*/ 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
        if (mProvinceBeanArrayList != null && !mProvinceBeanArrayList.isEmpty()) {
            mProvinceBean = mProvinceBeanArrayList.get(0);
            List<CityBean> cityList = mProvinceBean.getCityList();
            if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                mCityBean = cityList.get(0);
                List<DistrictBean> districtList = mCityBean.getCityList();
                if (districtList != null && !districtList.isEmpty() && districtList.size() > 0) {
                    mDistrictBean = districtList.get(0);
                }
            }
        }

        //省份数据
        mProvinceBeenArray = new ProvinceBean[mProvinceBeanArrayList.size()];

        for (int p = 0; p < mProvinceBeanArrayList.size(); p++) {

            //遍历每个省份
            ProvinceBean itemProvince = mProvinceBeanArrayList.get(p);

            //每个省份对应下面的市
            ArrayList<CityBean> cityList = itemProvince.getCityList();

            //当前省份下面的所有城市
            CityBean[] cityNames = new CityBean[cityList.size()];

            //遍历当前省份下面城市的所有数据
            for (int j = 0; j < cityList.size(); j++) {
                cityNames[j] = cityList.get(j);

                //当前省份下面每个城市下面再次对应的区或者县
                List<DistrictBean> districtList = cityList.get(j).getCityList();

                DistrictBean[] distrinctArray = new DistrictBean[districtList.size()];

                for (int k = 0; k < districtList.size(); k++) {

                    // 遍历市下面所有区/县的数据
                    DistrictBean districtModel = districtList.get(k);
                    //存放 省市区-区 数据
                    mDisMap.put(itemProvince.getName() + cityNames[j].getName() + districtList.get(k).getName(), districtModel);
                    distrinctArray[k] = districtModel;
                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mCity_DisMap.put(itemProvince.getName() + cityNames[j].getName(), distrinctArray);
            }

            // 省-市的数据，保存到mCitisDatasMap
            mPro_CityMap.put(itemProvince.getName(), cityNames);

            mCityBeanArrayList.add(cityList);

            ArrayList<ArrayList<DistrictBean>> array2DistrictLists = new ArrayList<>(cityList.size());

            for (int c = 0; c < cityList.size(); c++) {
                CityBean cityBean = cityList.get(c);
                array2DistrictLists.add(cityBean.getCityList());
            }
            mDistrictBeanArrayList.add(array2DistrictLists);

            //赋值所有省份的名称
            mProvinceBeenArray[p] = itemProvince;

        }
    }
}
