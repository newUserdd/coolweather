package com.xsd.coolweather.util;

import android.text.TextUtils;

import com.xsd.coolweather.db.CoolWeatherDB;
import com.xsd.coolweather.model.City;
import com.xsd.coolweather.model.County;
import com.xsd.coolweather.model.Province;

public class Utility {

	/**
	 * 处理省份
	 * 
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * 处理城市
	 * 
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String p : allCities) {
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// coolWeatherDB.saveci(city);
					coolWeatherDB.saveCities(city);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * 处理县级数据
	 * 
	 * @param coolWeatherDB
	 * @param response
	 * @return
	 */
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String p : allCounties) {
					String[] array = p.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounties(county);
				}
				return true;
			}
		}

		return false;
	}
}
