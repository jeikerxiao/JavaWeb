package com.jeiker.model;

/**
 * @author : xiao
 * @date : 17/12/1 下午3:44
 * @description : 城市 model
 */
public class City {

    /**
     * ID
     */
    private Integer id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
