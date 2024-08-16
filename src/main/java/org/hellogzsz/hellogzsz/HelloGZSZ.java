package org.hellogzsz.hellogzsz;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class HelloGZSZ extends JavaPlugin implements Listener {

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
                // 如果没有提供子命令，可以显示默认消息或帮助信息
                sendMessage(sender, "欢迎使用 /gzsz 命令！请输入子命令来获取帮助或执行操作。");
            } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                // 处理 /gzsz help 命令
                sendMessage(sender, "-------------------------");
                sendMessage(sender, "/gzsz reload - 重新加载插件配置文件。");
                // 如果有更多子命令，可以在这里继续添加
                sendMessage(sender, "-------------------------");
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
                // 重新加载配置文件
                config = YamlConfiguration.loadConfiguration(configFile);
                getLogger().info(PLUGIN_PREFIX + "配置文件已重新加载。");
                sendMessage(sender, "配置文件已重新加载！");
                return true;
            } else {
                // 如果输入了未知的子命令
                sendMessage(sender, "未知的 /gzsz 子命令: " + args[0]);
            }
            return true; // 表示命令已被处理
        }
        return false; // 如果不是/gzsz命令，返回false让其他命令有机会被处理
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