package com.jeiker.service;

import com.jeiker.framework.annotation.Inject;
import com.jeiker.model.City;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author : xiao
 * @date : 17/12/5 上午10:02
 * @description :
 */
public class CityServiceTest {

    @Inject
    private CityService cityService;

    @Test
    public void getCityList() throws Exception {
        List<City> cityList = cityService.getCityList();
        for (City city: cityList) {
            System.out.println(city.getName());
        }
    }

    @Test
    public void getCity() throws Exception {

    }

    @Test
    public void createCity() throws Exception {

    }

    @Test
    public void updateCity() throws Exception {

    }

    @Test
    public void deleteCity() throws Exception {

    }

}