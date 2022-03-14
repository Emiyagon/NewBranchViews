package com.illyasr.mydempviews.view.dialog;

import java.util.List;

public class CityDataModel {
    public String areaId;
    public String areaName;
    public List<CitiesBean> cities;

    @Override
    public String toString() {
        return "CityDataModel{" +
                "areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", cities=" + cities +
                '}';
    }

    public static class CitiesBean {

        public String areaId;
        public String areaName;
        public List<CountiesBean> counties;

        @Override
        public String toString() {
            return "CitiesBean{" +
                    "areaId='" + areaId + '\'' +
                    ", areaName='" + areaName + '\'' +
                    ", counties=" + counties +
                    '}';
        }

        public static class CountiesBean {
            public String areaId;
            public String areaName;

            @Override
            public String toString() {
                return "CountiesBean{" +
                        "areaId='" + areaId + '\'' +
                        ", areaName='" + areaName + '\'' +
                        '}';
            }
        }
    }
}

