package com.ezenity.heavynpc.util;

import com.ezenity.heavynpc.configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * LightNPC Logger Class
 * <p>
 * Custom utilities for console messages
 */
public class Logger {
    /**
     * Static log string message.
     * <p>
     * Logs messages and sends to console with colors. If {@link Config#COLOR_LOGS} is false, output with strip color from
     * text.
     *
     * @param msg send log message
     */
    public static void log(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&a[&7Log&a]&r " + msg);
        if (!Config.COLOR_LOGS) {
            msg = ChatColor.stripColor(msg);
        }
        Bukkit.getServer().getConsoleSender().sendMessage(msg);
    }

    /**
     * Static debug string message
     * <p>
     * Display message that are referenced to debugging. If {@link Config#DEBUG_MODE} is false, no output. If
     * {@link Config#COLOR_LOGS} is false, output with strip color from text.
     *
     * @param msg send debug message
     */
    public static void debug(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&2[&eDEBUG&2]&f " + msg);
        if (Config.DEBUG_MODE){
            if (!Config.COLOR_LOGS){
                msg = ChatColor.stripColor(msg);
            }
            log(msg);
        }
    }

    /**
     * Static info string message
     * <p>
     * Display info messages that are referenced to information outputting. If {@link Config#INFO_LOGGING} is false, no output.
     * If {@link Config#COLOR_LOGS} is false, output with strip color from text.
     *
     * @param msg send info message
     */
    public static void info(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&b[&7INFO&b]&f " + msg);
        if (Config.INFO_LOGGING) {
            if (!Config.COLOR_LOGS){
                msg = ChatColor.stripColor(msg);
            }
            log(msg);
        }
    }

    /**
     * Static warn string message
     * <P>
     * Display warn message that is associated with warning attributes within the plugin. If {@link Config#WARN_LOGGING} is false, no
     * output. If {@link Config#COLOR_LOGS} is false, output with strip color from text.
     *
     * @param msg send warn message
     */
    public static void warn(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&6[&eWARN&6]&a " + msg);
        if (Config.WARN_LOGGING){
            if (!Config.COLOR_LOGS){
                msg = ChatColor.stripColor(msg);
            }
            log(msg);
        }
    }

    /**
     * Static error string message
     * <p>
     * Display error message that are associated with stacktrace outputs. If {@link Config#ERROR_LOGGING} is false, no outputting if custom
     * error messages. If {@link Config#COLOR_LOGS} is false, output with strip color from text.
     *
     * @param msg send error message
     */
    public static void error(String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', "&4[&cERROR&4]&e " + msg);
        if (Config.ERROR_LOGGING){
            if (!Config.COLOR_LOGS){
                msg = ChatColor.stripColor(msg);
            }
            log(msg);
        }
    }
}
