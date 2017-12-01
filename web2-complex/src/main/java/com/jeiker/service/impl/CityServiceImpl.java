package com.jeiker.service.impl;

import com.jeiker.helper.DatabaseHelper;
import com.jeiker.model.City;
import com.jeiker.service.CityService;

import java.util.List;
import java.util.Map;

/**
 * @author : xiao
 * @date : 17/12/1 下午3:50
 * @description : 城市服务
 */
public class CityServiceImpl implements CityService {

    /**
     * 获取城市列表
     */
    @Override
    public List<City> getCityList() {
        String sql = "SELECT * FROM city";
        return DatabaseHelper.queryEntityList(City.class, sql);
    }

    /**
     * 获取城市
     */
    @Override
    public City getCity(long id) {
        String sql = "SELECT * FROM city WHERE id = ?";
        return DatabaseHelper.queryEntity(City.class, sql, id);
    }

    /**
     * 创建城市
     */
    @Override
    public boolean createCity(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(City.class, fieldMap);
    }

    /**
     * 更新城市
     */
    @Override
    public boolean updateCity(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(City.class, id, fieldMap);
    }

    /**
     * 删除城市
     */
    @Override
    public boolean deleteCity(long id) {
        return DatabaseHelper.deleteEntity(City.class, id);
    }

}
