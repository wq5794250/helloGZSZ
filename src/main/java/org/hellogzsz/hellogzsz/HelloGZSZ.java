package org.hellogzsz.hellogzsz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class HelloGZSZ extends JavaPlugin implements Listener, TabCompleter {

    private static final String PLUGIN_PREFIX = "[ §a赣州师专 §f]";

    private FileConfiguration config;

    @Override
    public void onEnable() {
        // 加载配置文件
        saveDefaultConfig();
        config = getConfig();

        // 注册命令
        getCommand("gzsz").setExecutor(this);

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);

        // 插件启动日志
        getLogger().info(PLUGIN_PREFIX + "helloGZSZ 插件加载成功！");
    }

    @Override
    public void onDisable() {
        getLogger().info(PLUGIN_PREFIX + "helloGZSZ 插件卸载成功！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("gzsz")) {
            if (args.length == 0) {
                // 如果没有提供子命令，显示默认消息或帮助信息
                sender.sendMessage(PLUGIN_PREFIX + "欢迎使用 /gzsz 命令！请输入子命令来获取帮助或执行操作。");
                return true; // 表明命令已处理
            }

            String subCommand = args[0].toLowerCase(); // 将子命令转换为小写以统一处理

            switch (subCommand) {
                case "help":
                    // 处理 /gzsz help 命令
                    sender.sendMessage("-----Hello-GZSZ-帮助------");
                    sender.sendMessage("/gzsz reload - 重新加载插件配置文件。");
                    sender.sendMessage("/gzsz info   - 查看插件信息。");
                    sender.sendMessage("/gzsz help   - 打开HelloGZSZ插件帮助文件。");
                    sender.sendMessage("-------------------------");
                    return true; // 表明命令已处理

                case "reload":
                    // 重新加载配置文件
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(PLUGIN_PREFIX + "配置文件已重新加载！");
                    return true; // 表明命令已处理

                case "info":
                    // 插件信息
                    sender.sendMessage("-----Hello-GZSZ-信息------");
                    sender.sendMessage("此插件由魔大可编写，用于赣州师专周边服务器");
                    sender.sendMessage("适用服务端 paper/spigot 1.20.1 ");
                    sender.sendMessage("-------------------------");
                    return true; // 表明命令已处理

                default:
                    // 如果输入了未知的子命令
                    sender.sendMessage(PLUGIN_PREFIX + "未知的 /gzsz 子命令: " + args[0]);
                    return true; // 表明命令已处理，尽管是未知命令

            }
        }
        return false; // 如果不是/gzsz命令，返回false让其他命令有机会被处理
    }

    // Tab补全功能
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("gzsz")) {
            List<String> options = new ArrayList<>();
            if (args.length == 1) {
                // 为第一个参数提供补全选项
                options.add("help");
                options.add("reload");
                options.add("info");
                return options;
            }
        }
        return null;
    }

    // 事件监听器，用于处理玩家加入事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // 移除默认的加入消息
        event.setJoinMessage(null); // 或者 event.setJoinMessage("")

        // 获取玩家
        Player player = event.getPlayer();

        // 获取欢迎信息
        String welcomeMessage = config.getString("welcomeMessage", "默认欢迎信息：%player% 加入了游戏！");
        welcomeMessage = welcomeMessage.replace("%player%", player.getName());

        // 向所有在线玩家发送自定义的欢迎信息
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(welcomeMessage);
        }
    }
}