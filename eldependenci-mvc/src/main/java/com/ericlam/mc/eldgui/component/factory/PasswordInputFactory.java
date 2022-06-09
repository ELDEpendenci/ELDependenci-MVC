package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eldgui.component.ComponentFactory;

// 密碼輸入組件
public interface PasswordInputFactory extends ComponentFactory<PasswordInputFactory> { // 繼承 ComponentFactory

    /**
     * 綁定屬性。由於是密碼，所以沒有初始數值。
     * @param field 屬性名稱
     * @return this
     */
    PasswordInputFactory bindInput(String field);

    /**
     * 顯示密碼文字
     * @param show 顯示密碼文字
     * @return this
     */
    PasswordInputFactory showPasswordTxt(String show);

    /**
     * 隱藏密碼文字
     * @param hide 隱藏密碼文字
     * @return this
     */
    PasswordInputFactory hidePasswordTxt(String hide);

    /**
     * 設置密碼混淆類型
     * @param type 密碼混淆類型
     * @return this
     */
    PasswordInputFactory hashType(HashType type);

    /**
     * 設置標題顯示
     * @param label 標題顯示
     * @return this
     */
    PasswordInputFactory label(String label);


    /**
     * 設置遮罩文字
     * @param mask 遮罩文字
     * @return this
     */
    PasswordInputFactory mask(char mask);

    /**
     * 設置輸入提示訊息
     * @param input 提示訊息
     * @return this
     */
    PasswordInputFactory inputMessage(String input);

    /**
     * 設置無效提示訊息
     * @param invalid 無效提示訊息
     * @return this
     */
    PasswordInputFactory invalidMessage(String invalid);

    /**
     * 設置 regex 來規限密碼格式
     * @param regex 規限密碼格式
     * @return this
     */
    PasswordInputFactory regex(String regex);

    /**
     * 設置等待最大輸入時間
     * @param maxWait 等待最大輸入時間
     * @return this
     */
    PasswordInputFactory maxWait(long maxWait);

    /**
     * 設置禁用組件
     * @return this
     */
    PasswordInputFactory disabled();

    /**
     * hash類型
     */
    enum HashType {
        SHA_256, MD5
    }

}
