package com.jeiker.controller;

import com.jeiker.model.City;
import com.jeiker.service.impl.CityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 进入 城市列表 界面
 */
@WebServlet("/city")
public class CityServlet extends HttpServlet {

    private CityServiceImpl cityService;

    @Override
    public void init() throws ServletException {
        cityService = new CityServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<City> cityList = cityService.getCityList();
        req.setAttribute("cityList", cityList);
        req.getRequestDispatcher("/WEB-INF/view/city.jsp").forward(req, resp);
    }
}
