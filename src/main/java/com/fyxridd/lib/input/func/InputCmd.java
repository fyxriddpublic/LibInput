package com.fyxridd.lib.input.func;

import com.fyxridd.lib.core.api.config.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.func.api.func.Extend;
import com.fyxridd.lib.func.api.func.Func;
import com.fyxridd.lib.func.api.func.FuncType;
import com.fyxridd.lib.input.InputPlugin;
import com.fyxridd.lib.input.config.InputConfig;

@FuncType("cmd")
public class InputCmd {
    private InputConfig config;
    
    public InputCmd() {
      //监听配置
        ConfigApi.addListener(InputPlugin.instance.pn, InputConfig.class, new Setter<InputConfig>() {
            @Override
            public void set(InputConfig value) {
                config = value;
            }
        });
    }
    
    @Func("input")
    public void onInput(CommandSender sender, @Extend String content) {
        if (config.isAllowCmd() && sender instanceof Player) InputPlugin.instance.getInputManager().checkOnInput((Player) sender, content);
    }
}
