package com.jeiker.service;

import com.jeiker.model.City;

import java.util.List;
import java.util.Map;

/**
 * @author : xiao
 * @date : 17/12/1 下午3:49
 * @description :
 */
public interface CityService {

    /**
     * 获取城市列表
     */
    List<City> getCityList();

    /**
     * 获取城市
     */
    City getCity(long id);

    /**
     * 创建城市
     */
    boolean createCity(Map<String, Object> fieldMap);

    /**
     * 更新城市
     */
    boolean updateCity(long id, Map<String, Object> fieldMap);
    /**
     * 删除城市
     */
    boolean deleteCity(long id);
}
