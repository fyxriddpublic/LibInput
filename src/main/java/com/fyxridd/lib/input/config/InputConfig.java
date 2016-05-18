package com.fyxridd.lib.input.config;

import com.fyxridd.lib.core.api.config.basic.Path;
import com.fyxridd.lib.core.api.config.convert.ConfigConvert;
import com.fyxridd.lib.core.api.lang.LangConverter;
import com.fyxridd.lib.core.api.lang.LangGetter;

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
