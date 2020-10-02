package com.xuzhong.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.xuzhong.coolweather.db.City;
import com.xuzhong.coolweather.db.County;
import com.xuzhong.coolweather.db.Province;
import com.xuzhong.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析和处理JSON格式数据
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     * TextUtils类是系统自带的一个工具类,JSONArray由JSONObject构成的数组
     */
    public static boolean handleProvinceResponse(String response) {
        //如果为空直接返回false
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                //循环取得JSONNbject对象
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    //获取省份名称和ID，保存到Province类
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //保存数据到数据库
                    province.save();
                }
                return true;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 将返回的JSON数据解析成Weather实体类
     * 通过JSONObject和JSONArray将天气数据中的主体内容解析出来
     * 由于我们之前已经按照上面的数据格式定义过相应的GSON实体类，
     * 因此只需要通过调用fromJson()方法就能直接将JSON数据转换成Weather对象了
     */
    public static Weather handleWeatherResponse(String response){

        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}