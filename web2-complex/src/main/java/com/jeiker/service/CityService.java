package com.jeiker.service;

import com.jeiker.helper.DatabaseHelper;
import com.jeiker.model.City;

import java.util.List;
import java.util.Map;

/**
 * @author : xiao
 * @date : 17/12/1 下午3:49
 * @description :
 */
public class CityService {

    /**
     * 获取城市列表
     */
    public List<City> getCityList() {
        String sql = "SELECT * FROM city";
        return DatabaseHelper.queryEntityList(City.class, sql);
    }

    /**
     * 获取城市
     */
    public City getCity(long id) {
        String sql = "SELECT * FROM city WHERE id = ?";
        return DatabaseHelper.queryEntity(City.class, sql, id);
    }

    /**
     * 创建城市
     */
    public boolean createCity(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(City.class, fieldMap);
    }

    /**
     * 更新城市
     */
    public boolean updateCity(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(City.class, id, fieldMap);
    }

    /**
     * 删除城市
     */
    public boolean deleteCity(long id) {
        return DatabaseHelper.deleteEntity(City.class, id);
    }
}
