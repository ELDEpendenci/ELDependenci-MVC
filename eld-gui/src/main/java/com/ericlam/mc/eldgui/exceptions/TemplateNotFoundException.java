package com.ericlam.mc.eldgui.exceptions;

public class TemplateNotFoundException extends Exception{

    public final String template;

    public TemplateNotFoundException(String template) {
        this.template = template;
    }

}
