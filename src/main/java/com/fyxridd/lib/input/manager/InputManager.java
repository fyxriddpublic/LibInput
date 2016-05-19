package com.fyxridd.lib.input.manager;

import com.fyxridd.lib.core.api.MessageApi;
import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.event.PlayerChatEvent;
import com.fyxridd.lib.core.api.fancymessage.FancyMessage;
import com.fyxridd.lib.core.config.ConfigManager;
import com.fyxridd.lib.func.api.FuncApi;
import com.fyxridd.lib.input.InputPlugin;
import com.fyxridd.lib.input.api.InputApi;
import com.fyxridd.lib.input.api.InputCallback;
import com.fyxridd.lib.input.cmd.InputCmd;
import com.fyxridd.lib.input.config.InputConfig;
import com.fyxridd.lib.speed.api.SpeedApi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;

import java.util.HashMap;
import java.util.Map;

public class InputManager {
	private static final String SHORT_SPEED_INPUT = "input";

    private InputConfig config;

    //在输入状态中的玩家列表
    //玩家 输入处理器
    private Map<Player, InputCallback> inputtingPlayers = new HashMap<>();

	public InputManager() {
        //监听配置
        ConfigApi.addListener(InputPlugin.instance.pn, InputConfig.class, new ConfigManager.Setter<InputConfig>() {
            @Override
            public void set(InputConfig value) {
                config = value;
            }
        });
        //注册功能
        FuncApi.register(InputPlugin.instance.pn, new InputCmd());
        //注册事件
        {
            //玩家加入
            Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, InputPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerJoinEvent event = (PlayerJoinEvent) e;
                    inputtingPlayers.remove(event.getPlayer());
                }
            }, InputPlugin.instance);
            //玩家退出
            Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, InputPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerQuitEvent event = (PlayerQuitEvent) e;
                    inputtingPlayers.remove(event.getPlayer());
                }
            }, InputPlugin.instance);
            //玩家交互
            Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, InputPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerInteractEvent event = (PlayerInteractEvent) e;
                    if (config.isCancelInteract()) InputApi.delInput(event.getPlayer());
                }
            }, InputPlugin.instance, true);
            //玩家挥手
            Bukkit.getPluginManager().registerEvent(PlayerAnimationEvent.class, InputPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerAnimationEvent event = (PlayerAnimationEvent) e;
                    if (config.isCancelAnimation()) InputApi.delInput(event.getPlayer());
                }
            }, InputPlugin.instance, true);
            //玩家聊天
            Bukkit.getPluginManager().registerEvent(PlayerChatEvent.class, InputPlugin.instance, EventPriority.LOWEST, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerChatEvent event = (PlayerChatEvent) e;

                    if (config.isAllowChat() && checkOnInput(event.getP(), event.getMsg())) event.setCancelled(true);
                }
            }, InputPlugin.instance, true);
        }
	}

    /**
     * @see com.fyxridd.lib.input.api.InputApi#registerInput(Player, InputCallback, boolean, boolean)
     */
    public boolean register(Player p, InputCallback inputCallback, boolean ignoreSpeed, boolean tip) {
        //速度检测
        if (!ignoreSpeed && !SpeedApi.checkShort(p, InputPlugin.instance.pn, SHORT_SPEED_INPUT, 2)) return false;
        //取消先前的
        if (inputtingPlayers.remove(p) != null && tip) MessageApi.send(p, get(p.getName(), 10), false);

        //注册新的
        inputtingPlayers.put(p, inputCallback);
        return true;
    }

    /**
     * 检测输入
     * @param p 玩家
     * @param content 内容
     * @return 玩家是否在输入事件中
     */
    public boolean checkOnInput(Player p, String content) {
        InputCallback inputCallback = inputtingPlayers.get(p);
        if (inputCallback != null) {
            if (inputCallback.onInput(content)) inputtingPlayers.remove(p);
            return true;
        }
        return false;
    }

    /**
     * @see com.fyxridd.lib.input.api.InputApi#delInput(Player, boolean)
     */
    public void del(Player p, boolean tip) {
        if (inputtingPlayers.remove(p) != null && tip) MessageApi.send(p, get(p.getName(), 10), false);
    }

    private FancyMessage get(String player, int id, Object... args) {
        return config.getLang().get(player, id, args);
    }
}
