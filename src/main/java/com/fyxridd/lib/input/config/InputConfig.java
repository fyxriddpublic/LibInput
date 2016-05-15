package com.fyxridd.lib.input.config;

import com.fyxridd.lib.config.api.basic.Path;
import com.fyxridd.lib.config.api.convert.ConfigConvert;
import com.fyxridd.lib.lang.api.LangConverter;
import com.fyxridd.lib.lang.api.LangGetter;

public class InputConfig {
    @Path("cancel.interact")
    private boolean cancelInteract;

    @Path("cancel.animation")
    private boolean cancelAnimation;

    @Path("allow.chat")
    private boolean allowChat;

    @Path("allow.cmd")
    private boolean allowCmd;

    @Path("lang")
    @ConfigConvert(LangConverter.class)
    private LangGetter lang;

    public boolean isCancelInteract() {
        return cancelInteract;
    }

    public boolean isCancelAnimation() {
        return cancelAnimation;
    }

    public boolean isAllowChat() {
        return allowChat;
    }

    public boolean isAllowCmd() {
        return allowCmd;
    }

    public LangGetter getLang() {
        return lang;
    }
}
