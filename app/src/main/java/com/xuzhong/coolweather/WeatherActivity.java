package com.xuzhong.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xuzhong.coolweather.gson.Forecast;
import com.xuzhong.coolweather.gson.Weather;
import com.xuzhong.coolweather.util.HttpUtil;
import com.xuzhong.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化控件
//        weatherLayout=findViewById(R.id.weather_layout);
//        titleCity=findViewById(R.id.title_city);
//        degreeText=findViewById(R.id.degree_text);
//        weatherInfoText=findViewById(R.id.weather_info_text);
//        forecastLayout=findViewById(R.id.forecast_layout);
//        aqiText=findViewById(R.id.pm25_text);
//        comfortText=findViewById(R.id.comfort_text);
//        carWashText=findViewById(R.id.car_wash_text);
//        sportText=findViewById(R.id.sport_text);

        weatherLayout=findViewById(R.id.weather_layout);
        titleCity=findViewById(R.id.title_city);
        degreeText=findViewById(R.id.degree_text);
        weatherInfoText=findViewById(R.id.weather_info_text);
        forecastLayout=findViewById(R.id.forecast_layout);
        aqiText=findViewById(R.id.pm25_text);
        comfortText=findViewById(R.id.comfort_text);
        carWashText=findViewById(R.id.car_wash_text);
        sportText=findViewById(R.id.sport_text);
        titleUpdateTime=findViewById(R.id.title_update_time);
        pm25Text=findViewById(R.id.pm25_text);


        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if(weatherString!=null){
            //有缓存时直接解析天气数据
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            //无缓存时去服务器查询天气
            //获取上一个Activity的数据"weather_id"的值
            String weatherId=getIntent().getStringExtra("weather_id");
            //设置布局不可见，但占据空间
            weatherLayout.setVisibility(View.INVISIBLE);
            //从服务器请求天气数据
            requestWeather(weatherId);
        }
    }
    /**
     * 根据天气ID请求城市天气信息
     */
    public void requestWeather(final String weatherId){
        //申请好的API Key拼装出一个接口地址
        String weatherUrl="http://guolin.tech/api/weather?cityid="
                +weatherId+"&key=01fee00440024b9b8d2039258d14656a";
        //发出请求，服务器会将相应城市的天气信息以JSON格式返回
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                //将返回的JSON数据转换成Weather对象
                final Weather weather=Utility.handleWeatherResponse(responseText);
                //将当前线程切换到主线程。然后进行判断，如果服务器返回的status状态是ok，就说明请求天气成功了
                // ，此时将返回的数据缓存到SharedPreferences当中，并调用showWeatherInfo()方法来进行内容显示
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    /**
     * 处理并展示Weather实体类中的数据
     * 从Weather对象中获取数据，然后显示到相应的控件上。注意在未来几天天气预报的部分我们使用了
     * 一个for循环来处理每天的天气信息，在循环中动态加载forecast_item.xml布局并设置相应的数据，
     * 然后添加到父布局当中。设置完了所有数据之后，记得要将ScrollView重新变成可见
     */
    private void showWeatherInfo(Weather weather){
        String cityName=weather.basic.cityName;
        String updateTime=weather.basic.update.updateTime.split("")[1];
        String degree=weather.now.temperature+"°C";
        String weatherInfo=weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast : weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText=view.findViewById(R.id.date_text);
            TextView infoText=view.findViewById(R.id.info_text);
            TextView maxText=view.findViewById(R.id.max_text);
            TextView minText=view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temerature.max);
            minText.setText(forecast.temerature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comfort="舒适度："+weather.suggestion.comfort.info;
        String carWash="洗车指数："+weather.suggestion.carWash.info;
        String sport="运动指数："+weather.suggestion.sport.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}