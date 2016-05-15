package com.fyxridd.lib.input.api;

import com.fyxridd.lib.input.InputPlugin;
import org.bukkit.entity.Player;

public class InputApi {
    /**
     * @see #registerInput(Player, InputCallback, boolean, boolean)
     * 不忽略速度检测
     */
    public static boolean registerInput(Player p, InputCallback inputCallback, boolean tip) {
        return registerInput(p, inputCallback, false, tip);
    }

    /**
     * 注册玩家输入事件
     * @param p 玩家,不为null
     * @param inputCallback 处理器,不为null
     * @param ignoreSpeed 是否忽略速度检测,true时不进行速度检测
     * @param tip 成功删除旧的注册输入是否提示玩家
     * @return 是否注册成功
     */
    public static boolean registerInput(Player p, InputCallback inputCallback, boolean ignoreSpeed, boolean tip) {
        return InputPlugin.instance.getInputManager().register(p, inputCallback, ignoreSpeed, tip);
    }

    /**
     * @see #delInput(Player, boolean)
     * 成功删除提示玩家
     */
    public static void delInput(Player p) {
        delInput(p, true);
    }

    /**
     * 删除玩家的输入注册
     * @param p 玩家,不为null
     * @param tip 成功删除是否提示玩家
     */
    public static void delInput(Player p, boolean tip) {
        InputPlugin.instance.getInputManager().del(p, tip);
    }
}
