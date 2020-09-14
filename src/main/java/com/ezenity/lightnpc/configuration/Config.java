package com.ezenity.lightnpc.configuration;

import com.ezenity.lightnpc.Main;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * LightNPC Main Config Class
 * <p>
 * Here you will find all the default configuration options that are available with YouGotMail
 *
 * @author Ezenity
 * @version 0.0.1
 * @since 0.0.1
 */
public class Config {
    /**
     * This is a header for the config file. This will be displayed as a comment above all of
     * the settings regardless what was inputted.
     */
    private static final String HEADER = "The main configuration file for LightNPC";

    public static boolean DEBUG_MODE;
    public static boolean COLOR_LOGS;
    public static boolean INFO_LOGGING;
    public static boolean WARN_LOGGING;
    public static boolean ERROR_LOGGING;
    public static String LANGUAGE_FILE;

    /**
     * This method is utilized from applying all the configuration settings. This allows us to grab any of these static variables and apply them
     * throughout the rest of the plugin.
     */
    private static void init() {
        DEBUG_MODE = config.getBoolean("debug-mode", true);
        COLOR_LOGS = config.getBoolean("color-logs", true);
        INFO_LOGGING = config.getBoolean("logging.info", true);
        WARN_LOGGING = config.getBoolean("logging.warn", true);
        ERROR_LOGGING = config.getBoolean("logging.error", true);
        LANGUAGE_FILE = config.getString("language-file", "lang-en.yml");
    }

    // ############################  DO NOT EDIT BELOW THIS LINE  ############################
    /**
     * Initialize plugin instance. We use this to initialize the config file.
     */
    private static final Main plugin = Main.getInstance();
    /**
     * Configuration File. Gets the default plugin folder and creates a new config file from the default provided config file with comments.
     */
    private static  final File configFile = new File(plugin.getDataFolder(), "config.yml");
    /**
     * Config file configuration. This is used for creating the files settings general speaking.
     */
    private static FileConfiguration config;

    /**
     * This method will reload the configuration file(s) and its contents. If there is no configuration,
     * then the default configuration file will be generated and then will load the configuration file.
     * Another thing to note is that this will not overwrite the comments that are set inside a config file.
     */
    public static void reload() {
        if (!configFile.exists())
            plugin.saveResource("config.yml", false);

        config = YamlConfiguration.loadConfiguration(configFile);
        config.options().header(HEADER);
        Config.init();
    }
}
