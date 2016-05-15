package com.fyxridd.lib.input.manager;

import com.fyxridd.lib.config.api.ConfigApi;
import com.fyxridd.lib.config.manager.ConfigManager;
import com.fyxridd.lib.core.CorePlugin;
import com.fyxridd.lib.core.api.CoreApi;
import com.fyxridd.lib.core.api.event.PlayerChatEvent;
import com.fyxridd.lib.input.InputPlugin;
import com.fyxridd.lib.input.api.InputHandler;
import com.fyxridd.lib.input.config.InputConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class InputManager implements CommandExecutor {
	private static final String INPUT = "input";

    //配置

    private InputConfig config;

    //缓存

    //在此缓存中说明玩家在输入状态中
    //玩家 输入处理器
    private HashMap<Player, InputHandler> inputHash = new HashMap<>();

	public InputManager() {
        //监听配置
        ConfigApi.addListener(InputPlugin.instance.pn, InputConfig.class, new ConfigManager.Setter<InputConfig>() {
            @Override
            public void set(InputConfig value) {
                config = value;
            }
        });
	}

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (config.isAllowCmd() && sender instanceof Player && args.length > 0) {
            Player p = (Player) sender;
            //玩家在输入事件中
            InputHandler inputHandler = inputHash.get(p);
            if (inputHandler != null) {
                String content = CoreApi.combine(args, " ", 0, args.length);
                if (inputHandler.onInput(content)) inputHash.remove(p);
            }
        }
        return true;
    }

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
        inputHash.remove(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
        inputHash.remove(e.getPlayer());
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (config.isCancelInteract()) del(e.getPlayer());
	}

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerAnimation(PlayerAnimationEvent e) {
        if (config.isCancelAnimation()) del(e.getPlayer());
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerChat(PlayerChatEvent e) {
        //事件已取消检测
        if (e.isCancelled()) return;

        if (config.isAllowChat()) {
            //玩家在输入事件中
            InputHandler inputHandler = inputHash.get(e.getP());
            if (inputHandler != null) {
                e.setCancelled(true);
                if (inputHandler.onInput(e.getMsg())) inputHash.remove(e.getP());
            }
        }
    }

    /**
     * @see CoreApi#registerInput(Player, com.fyxridd.lib.core.api.inter.InputHandler, boolean)
     */
    public static boolean register(Player p, InputHandler inputHandler, boolean tip) {
        return register(p, inputHandler, false, tip);
    }

    /**
     * @see CoreApi#registerInput(Player, com.fyxridd.lib.core.api.inter.InputHandler, boolean)
     */
    public static boolean register(Player p, InputHandler inputHandler, boolean ignoreSpeed, boolean tip) {
        //速度检测
        if (!ignoreSpeed && !SpeedApi.checkShort(p, CorePlugin.pn, INPUT, 2)) return false;
        //取消先前的
        if (inputHash.remove(p) != null) {
            if (tip) ShowManager.tip(p, get(800), false);
        }

        //注册新的
        inputHash.put(p, inputHandler);
        return true;
    }

    /**
     * @see #del(Player, boolean)
     */
    public static void del(Player p) {
        del(p, true);
    }

    /**
     * 删除玩家的输入注册
     * @param p 玩家,不为null
     * @param tip 成功删除是否提示玩家
     */
    public static void del(Player p, boolean tip) {
        if (inputHash.remove(p) != null) {
            if (tip) ShowManager.tip(p, get(800), false);
        }
    }
}
