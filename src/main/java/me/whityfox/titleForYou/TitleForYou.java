package me.whityfox.titleForYou;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TitleForYou extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Automatically create config if it doesn't exist and set default values
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs(); // Create the plugin folder if it doesn't exist
        }

        // Load the default configuration if it doesn't exist yet
        FileConfiguration config = getConfig();
        if (!config.contains("title") || !config.contains("subtitle")) {
            // Set default title and subtitle
            config.set("title", "Welcome to the Server!");
            config.set("subtitle", "Enjoy your stay!");
            saveConfig();  // Save the configuration with default values
        }

        // Register event listener
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("JoinTitlePlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("JoinTitlePlugin disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = getConfig();

        // Get title and subtitle from the config
        String title = config.getString("title", "Welcome!");
        String subtitle = config.getString("subtitle", "Enjoy your stay!");

        // Send title to the player
        player.sendTitle(title, subtitle, 10, 200, 20);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("settitle")) {
            // Объединяем аргументы в строку
            String input = String.join(" ", args);

            // Используем регулярное выражение для разделения аргументов по кавычкам
            Pattern pattern = Pattern.compile("\"(.*?)\"");
            Matcher matcher = pattern.matcher(input);

            String title = null;
            String subtitle = null;

            if (matcher.find()) {
                title = matcher.group(1); // Первый аргумент (заголовок)
                if (matcher.find()) {
                    subtitle = matcher.group(1); // Второй аргумент (подзаголовок)
                }
            }

            // Если оба аргумента заданы, обновляем конфиг
            if (title != null && subtitle != null) {
                FileConfiguration config = getConfig();
                config.set("title", title);
                config.set("subtitle", subtitle);
                saveConfig();

                sender.sendMessage("Title and subtitle updated!");
                for (Player player: Bukkit.getServer().getOnlinePlayers()){
                    player.sendTitle(title, subtitle, 10, 200, 20);
                }
                return true;
            } else {
                sender.sendMessage("Usage: /settitle \"<title>\" \"<subtitle>\"");
                return false;
            }
        }
        return false;
    }
}
