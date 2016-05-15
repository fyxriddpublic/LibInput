package com.fyxridd.lib.input.config;

import com.fyxridd.lib.config.api.basic.Path;

public class InputConfig {
    @Path("cancel.interact")
    private boolean cancelInteract;

    @Path("cancel.animation")
    private boolean cancelAnimation;

    @Path("allow.chat")
    private boolean allowChat;

    @Path("allow.cmd")
    private boolean allowCmd;

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
}
