package com.fyxridd.lib.input;

import com.fyxridd.lib.config.api.ConfigApi;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.input.config.InputConfig;
import com.fyxridd.lib.input.manager.InputManager;

public class InputPlugin extends SimplePlugin{
    public static InputPlugin instance;

    private InputManager inputManager;

    @Override
    public void onEnable() {
        instance = this;

        //注册配置
        ConfigApi.register(pn, InputConfig.class);

        inputManager = new InputManager();

        super.onEnable();
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}