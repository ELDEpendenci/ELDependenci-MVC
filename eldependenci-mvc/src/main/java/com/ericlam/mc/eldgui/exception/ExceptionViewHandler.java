package com.ericlam.mc.eldgui.exception;

import com.ericlam.mc.eldgui.UISession;
import com.ericlam.mc.eldgui.view.BukkitView;
import org.bukkit.entity.Player;

/**
 * 異常界面處理器。當界面執行時出現異常，將會調用這個類去渲染異常界面。
 * <br>
 * 你可以透過新增自定義方法並標注 {@link HandleException}
 * 來特別處理指定的異常，但其方法參數必須與
 * {@link ExceptionViewHandler#createErrorView(Exception, String, UISession, Player)}
 * 一致
 */
public interface ExceptionViewHandler {

    /**
     * 全局異常處理。如無指定，則調用此方法處理異常
     * @param exception 異常(報錯)
     * @param fromController 獲取抛出異常的 controller id
     * @param session Session
     * @param player 獲取抛出異常的所屬界面使用者
     * @return 報錯時出現的界面
     */
    BukkitView<?, ?> createErrorView(Exception exception, String fromController, UISession session, Player player);

}
