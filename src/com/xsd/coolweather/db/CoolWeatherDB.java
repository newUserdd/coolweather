package com.xsd.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.xsd.coolweather.model.City;
import com.xsd.coolweather.model.County;
import com.xsd.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	public static final String DB_NAME = "cool_weather";

	public static final int VERSION = 1;

	public static CoolWeatherDB coolWeatherDB;

	private SQLiteDatabase db;

	/**
	 * 
	 * 将构造方法私有化
	 */
	public CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION, null);
		db = dbHelper.getWritableDatabase();

	}

	/**
	 * 获取CoolWeatherDB实例
	 * 
	 * @param context
	 * @return
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		//单例类
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 存储省份数据到数据库
	 * 
	 * @param province
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * 获取所有省份
	 * 
	 * @return
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			Province province = new Province();

			province.setId(cursor.getColumnIndex("id"));
			province.setProvinceName(cursor.getString(cursor
					.getColumnIndex("province_name")));
			province.setProvinceName(cursor.getString(cursor
					.getColumnIndex("province_name")));

			list.add(province);
		}
		return list;
	}

	/**
	 * 存储城市数据到数据库
	 * 
	 * @param province
	 */
	public void saveCities(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * 获取所有城市
	 * 
	 * @return
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		while (cursor.moveToNext()) {
			City city = new City();

			city.setId(cursor.getInt(cursor.getColumnIndex("id")));
			city.setCityName(cursor.getString(cursor
					.getColumnIndex("city_name")));
			city.setCityCode(cursor.getString(cursor
					.getColumnIndex("city_code")));
			city.setProvinceId(provinceId);
			list.add(city);
		}
		return list;
	}

	/**
	 * 存储县数据到数据库
	 * 
	 * @param province
	 */
	public void saveCounties(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();

		Cursor cursor = db.query("County", null, "cityId=?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		while (cursor.moveToNext()) {
			County county = new County();
			county.setId(cursor.getInt(cursor.getColumnIndex("id")));
			county.setCountyName(cursor.getString(cursor
					.getColumnIndex("county_name")));
			county.setCountyCode(cursor.getString(cursor
					.getColumnIndex("county_code")));
			county.setCityId(cityId);
			list.add(county);
		}
		return list;
	}

}
