package com.ericlam.mc.eldgui.component.factory;

import com.ericlam.mc.eld.services.ItemStackService;
import com.ericlam.mc.eldgui.component.AttributeController;
import com.ericlam.mc.eldgui.component.Component;
import com.ericlam.mc.eldgui.component.PasswordInputField;

import java.util.regex.Pattern;

public final class ELDGPasswordInputFactory extends AbstractComponentFactory<PasswordInputFactory> implements PasswordInputFactory {

    public ELDGPasswordInputFactory(ItemStackService itemStackService, AttributeController attributeController) {
        super(itemStackService, attributeController);
    }

    private String showPasswordTxt;
    private String hidePasswordTxt;
    private String inputMessage;
    private String invalidMessage;
    private long maxWait;
    private Pattern regex;
    private HashType hashType;
    private boolean disabled;
    private char mask;

    @Override
    protected void defaultProperties() {
        this.showPasswordTxt = "&a顯示密碼";
        this.hidePasswordTxt = "&c隱藏密碼";
        this.inputMessage = "&a請在聊天欄輸入你的密碼。";
        this.invalidMessage = "&c無效的密碼格式。";
        this.maxWait = 200L;
        this.regex = Pattern.compile(".+");
        this.hashType = HashType.MD5;
        this.disabled = false;
        this.mask = '*';
    }

    @Override
    public Component build(ItemStackService.ItemFactory itemFactory) {
        return new PasswordInputField(
                attributeController,
                itemFactory,
                showPasswordTxt,
                hidePasswordTxt,
                inputMessage,
                invalidMessage,
                maxWait,
                regex,
                hashType,
                disabled,
                mask);
    }

    @Override
    public PasswordInputFactory bindInput(String field) {
        bind(AttributeController.FIELD_TAG, field);
        bind(AttributeController.VALUE_TAG, null);
        return this;
    }

    @Override
    public PasswordInputFactory showPasswordTxt(String show) {
        this.showPasswordTxt = show;
        return this;
    }

    @Override
    public PasswordInputFactory hidePasswordTxt(String hide) {
        this.hidePasswordTxt = hide;
        return this;
    }

    @Override
    public PasswordInputFactory hashType(HashType type) {
        this.hashType = type;
        return this;
    }

    @Override
    public PasswordInputFactory label(String label) {
        return editItemByFactory(f -> f.display(label));
    }

    @Override
    public PasswordInputFactory mask(char mask) {
        this.mask = mask;
        return this;
    }

    @Override
    public PasswordInputFactory inputMessage(String input) {
        this.inputMessage = input;
        return this;
    }

    @Override
    public PasswordInputFactory invalidMessage(String invalid) {
        this.invalidMessage = invalid;
        return this;
    }

    @Override
    public PasswordInputFactory regex(String regex) {
        this.regex = Pattern.compile(regex);
        return this;
    }

    @Override
    public PasswordInputFactory maxWait(long maxWait) {
        this.maxWait = maxWait;
        return this;
    }

    @Override
    public PasswordInputFactory disabled() {
        this.disabled = true;
        return this;
    }


}
