package com.ezenity.heavynpc;

import com.ezenity.heavynpc.configuration.Config;
import com.ezenity.heavynpc.configuration.Lang;
import com.ezenity.heavynpc.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * LightNPC Main Plugin Class
 *
 * @author Ezenity
 * @version v0.0.1
 */
public class Main extends JavaPlugin {
    /**
     * Main instance. Create an instance of this plugin.
     */
    private static Main instance;

    /**
     * Constructor Instance of the main class.
     */
    private Main() {
        instance = this;
    }

    /**
     * This method is invoked when the plugin is enabled. When plugin is enabled, the config is reload,
     * lang is reload, events are registered, commands are registered and the plugin version is outputted.
     */
    @Override
    public void onEnable() {
        Config.reload();
        Lang.reload();

        Logger.info(getName() + " v" + getInstance().getDescription().getVersion() + " enabled!");
    }

    /**
     * This method is invoked when the plugin is disabled. When the plugin is disabled, the plugin will output
     * to console that it has been disabled.
     */
    @Override
    public void onDisable() {
        Logger.info(getName() + " disabled!");
    }

    /**
     * Gets the plugin instance of the main class.
     *
     * @return main plugin instance.
     */
    public static Main getInstance() {
        return instance;
    }
}
