package org.hellogzsz.hellogzsz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class HelloGZSZ extends JavaPlugin implements Listener, TabCompleter {

    // 插件配置文件
    private FileConfiguration config;
    private File configFile;

    // 插件前缀
    private static final String PLUGIN_PREFIX = "[ §a赣州师专 §f]";

    @Override
    public void onEnable() {
        // 初始化配置文件路径
        configFile = new File(getDataFolder(), "config.yml");
        // 确保data文件夹存在
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        // 如果配置文件不存在，则保存默认配置
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        // 加载配置文件
        config = YamlConfiguration.loadConfiguration(configFile);

        // 注册命令
        Objects.requireNonNull(getCommand("gzsz")).setExecutor(this);

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(this, this);

        // 插件启动日志
        getLogger().info(PLUGIN_PREFIX + "helloGZSZ 插件加载成功！");
    }

    // 保存配置文件到磁盘
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            getLogger().severe(PLUGIN_PREFIX + "无法保存配置文件: " + e.getMessage());
        }
    }

    // 封装消息发送方法，自动添加插件前缀
    private void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(PLUGIN_PREFIX + message);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("gzsz")) {
            if (args.length == 0) {
                // 如果没有提供子命令，显示默认消息或帮助信息
                sendMessage(sender, "欢迎使用 /gzsz 命令！请输入子命令来获取帮助或执行操作。");
                return true; // 表明命令已处理
            }

            String subCommand = args[0].toLowerCase(); // 将子命令转换为小写以统一处理

            switch (subCommand) {
                case "help":
                    // 处理 /gzsz help 命令
                    sendMessage(sender, "-----Hello-GZSZ-帮助------");
                    sendMessage(sender, "/gzsz reload - 重新加载插件配置文件。");
                    sendMessage(sender, "/gzsz info   - 查看插件信息。");
                    sendMessage(sender, "/gzsz help   - 打开HelloGZSZ插件帮助文件。");
                    // 如果有更多子命令，可以在这里继续添加
                    sendMessage(sender, "-------------------------");
                    return true; // 表明命令已处理

                case "reload":
                    // 重新加载配置文件
                    config = YamlConfiguration.loadConfiguration(configFile);
                    getLogger().info(PLUGIN_PREFIX + "配置文件已重新加载。");
                    sendMessage(sender, "配置文件已重新加载！");
                    return true; // 表明命令已处理

                case "info":
                    // 插件信息
                    sendMessage(sender, "-----Hello-GZSZ-信息------");
                    sendMessage(sender, "此插件由魔大可编写，用于赣州师专周边服务器");
                    sendMessage(sender, "适用服务端 paper/spigot 1.20.1 ");
                    sendMessage(sender, "-------------------------");
                    return true; // 表明命令已处理

                default:
                    // 如果输入了未知的子命令
                    sendMessage(sender, "未知的 /gzsz 子命令: " + args[0]);
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
                return StringUtil.copyPartialMatches(args[0], options, new ArrayList<>());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void onDisable() {
        getLogger().info(PLUGIN_PREFIX + "helloGZSZ 插件卸载成功！");
        // 如果需要，可以在这里添加插件关闭时的清理逻辑
    }

    // 事件监听器，用于处理玩家加入事件
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String welcomeMessage = config.getString("welcomeMessage", "默认欢迎信息：%player% 加入了游戏！");
        welcomeMessage = welcomeMessage.replace("%player%", player.getName());
        sendMessage(player, welcomeMessage);
    }
}