package com.jeiker.controller;

import com.jeiker.model.City;
import com.jeiker.service.CityService;
import com.jeiker.framework.annotation.Action;
import com.jeiker.framework.annotation.Controller;
import com.jeiker.framework.annotation.Inject;
import com.jeiker.framework.bean.Data;
import com.jeiker.framework.bean.Param;
import com.jeiker.framework.bean.View;

import java.util.List;
import java.util.Map;

/**
 * 处理客户管理相关请求
 */
@Controller
public class CityController {

    @Inject
    private CityService cityService;

    /**
     * 进入 客户列表 界面
     */
    @Action("get:/city")
    public View index() {
        List<City> cityList = cityService.getCityList();
        return new View("city.jsp").addModel("cityList", cityList);
    }

    /**
     * 显示客户基本信息
     */
    @Action("get:/city_show")
    public View show(Param param) {
        long id = param.getLong("id");
        City city = cityService.getCity(id);
        return new View("city_show.jsp").addModel("city", city);
    }

    /**
     * 进入 创建客户 界面
     */
    @Action("get:/city_create")
    public View create(Param param) {
        return new View("city_create.jsp");
    }

    /**
     * 处理 创建客户 请求
     */
    @Action("post:/city_create")
    public Data createSubmit(Param param) {
        Map<String, Object> fieldMap = param.getFieldMap();
        boolean result = cityService.createCity(fieldMap);
        return new Data(result);
    }

    /**
     * 进入 编辑客户 界面
     */
    @Action("get:/city_edit")
    public View edit(Param param) {
        long id = param.getLong("id");
        City city = cityService.getCity(id);
        return new View("city_edit.jsp").addModel("city", city);
    }

    /**
     * 处理 编辑客户 请求
     */
    @Action("put:/city_edit")
    public Data editSubmit(Param param) {
        long id = param.getLong("id");
        Map<String, Object> fieldMap = param.getFieldMap();
        boolean result = cityService.updateCity(id, fieldMap);
        return new Data(result);
    }

    /**
     * 处理 删除客户 请求
     */
    @Action("delete:/city_edit")
    public Data delete(Param param) {
        long id = param.getLong("id");
        boolean result = cityService.deleteCity(id);
        return new Data(result);
    }
}