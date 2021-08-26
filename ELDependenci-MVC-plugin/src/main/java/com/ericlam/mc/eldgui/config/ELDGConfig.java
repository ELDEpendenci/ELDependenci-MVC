package com.ericlam.mc.eldgui.config;

import com.ericlam.mc.eld.annotations.Resource;
import com.ericlam.mc.eld.components.Configuration;

@Resource(locate = "config.yml")
public class ELDGConfig extends Configuration {

    public boolean enableDemo = true;

}
