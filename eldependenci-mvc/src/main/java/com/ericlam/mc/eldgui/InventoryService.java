package com.ericlam.mc.eldgui;

/**
 * 界面服務類，用於獲取界面調度器
 */
public interface InventoryService {

    /**
     * 獲取界面調度器
     * @param controller controlelr id
     * @return 界面調度器
     * @throws UINotFoundException 找不到該 controller
     */
    UIDispatcher getUIDispatcher(String controller) throws UINotFoundException;
}
