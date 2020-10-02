package com.xuzhong.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 总实体类
 * 在Weather类中，我们对Basic、AQI、Now、Suggestion和Forecast类进行了引用。
 * 其中，由于daily_forecast中包含的是一个数组，因此这里使用了List集合来引用Forecast类。
 * 另外，返回的天气数据中还会包含一项status数据，成功返回ok，失败则会返回具体的原因，
 * 那么这里也需要添加一个对应的status字段
 */
public class Weather {

    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
