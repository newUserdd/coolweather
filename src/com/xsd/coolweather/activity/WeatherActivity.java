package com.xsd.coolweather.activity;


import com.xsd.coolweather.R;
import com.xsd.coolweather.util.HttpCallbackListener;
import com.xsd.coolweather.util.HttpUtil;
import com.xsd.coolweather.util.Utility;

import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private LinearLayout weather_info_layout;
	private TextView tv_city_name;
	private TextView tv_publish_text;
	private TextView tv_weather_desp;
	private TextView tv_temp1;
	private TextView tv_temp2;
	private TextView tv_current_date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		weather_info_layout = (LinearLayout) findViewById(R.id.weather_info_layout);
		tv_city_name = (TextView) findViewById(R.id.tv_city_name);
		tv_publish_text = (TextView) findViewById(R.id.tv_publish_text);
		tv_weather_desp = (TextView) findViewById(R.id.tv_weather_desp);
		tv_temp1 = (TextView) findViewById(R.id.tv_temp1);
		tv_temp2 = (TextView) findViewById(R.id.tv_temp2);
		tv_current_date = (TextView) findViewById(R.id.tv_current_date);
		
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			tv_publish_text.setText("同步中...");
			weather_info_layout.setVisibility(View.INVISIBLE);
			tv_city_name.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
		
		
	}
	private void queryWeatherCode(String countyCode){
		String address= "http://www.weather.com.cn/data/list3/city" +
				countyCode + ".xml";
		queryFromServer(address,"countyCode");
		
	}
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/" +
				weatherCode + ".html";
		System.out.println(address);
		
		queryFromServer(address,"weatherCode");
		
	}
	
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array!=null && array.length==2){
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
							
						}
					});
				}
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 从SharedPreferences文件读取存储的天气信息，并显示到界面上
	 */
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		tv_city_name.setText(prefs.getString("city_name", ""));
		tv_temp1.setText(prefs.getString("temp1", ""));
		tv_temp2.setText(prefs.getString("temp2", ""));
		tv_weather_desp.setText(prefs.getString("weather_desp", ""));
		tv_publish_text.setText("今天"+prefs.getString("publish_time", "")+"发布");
		tv_current_date.setText(prefs.getString("current_date", ""));
		weather_info_layout.setVisibility(View.VISIBLE);
		tv_city_name.setVisibility(View.VISIBLE);
		
		
	}

}

