package com.ericlam.mc.eldgui.demo.test;

import org.bukkit.Color;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestModel {

    public Color testColor;

    public LocalDate testDate;

    public LocalTime testTime;

    @Override
    public String toString() {
        return "TestModel{" +
                "testColor=" + testColor.toString() +
                ", testDate=" + testDate.toString() +
                ", testTime=" + testTime.toString() +
                '}';
    }
}
