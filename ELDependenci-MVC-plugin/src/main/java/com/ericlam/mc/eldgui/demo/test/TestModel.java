package com.ericlam.mc.eldgui.demo.test;

import org.bukkit.Color;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestModel {

    public Color testColor;

    public LocalDate testDate;

    public LocalTime testTime;

    // test null
    public String txt;

    @Override
    public String toString() {
        return "TestModel{" +
                "testColor=" + testColor +
                ", testDate=" + testDate +
                ", testTime=" + testTime +
                ", txt='" + txt + '\'' +
                '}';
    }
}
