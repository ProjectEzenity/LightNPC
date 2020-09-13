package com.ezenity.lightnpc.command;

import com.ezenity.lightnpc.Main;
import com.ezenity.lightnpc.configuration.Config;
import com.ezenity.lightnpc.configuration.Lang;
import com.ezenity.lightnpc.util.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

/**
 * LightNPC Reload Class
 *
 * This will allow you to reload the follow classes:
 * <ul>
 *     <li>Config</li>
 *     <li>Lang</li>
 * </ul>
 */
public class CmdReload implements TabExecutor {
    /**
     * Plugin instance. Initialize the plugin instance.
     */
    private final Main plugin;
    /**
     * String message. Used for outputting the current state of the reloaded files.
     */
    private String msg;

    /**
     * Constructor. Initializing the plugin object.
     *
     * @param plugin load instance of plugin
     */
    public CmdReload(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Tab Autocompletion
     * <p>
     * Will auto populate the reload option in the text box to allow the user to tab in the remainder of the option "reload".
     *
     * @param sender get sender
     * @param command get command object info
     * @param label get typed input from sender
     * @param args get arguments after inputted command
     * @return command options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if ( args.length == 1 && "reload".startsWith(args[0].toLowerCase()) && sender.hasPermission("command.lightnpc.reload") ) {
            return Collections.singletonList("reload");
        }

        return Collections.emptyList();
    }

    /**
     * Reload command.
     * <p>
     * This method will reload the Config and Lang yml files. If the sender does not have permission an error message will output and
     * notify that sender
     *
     * @param sender get sender
     * @param command get command object info
     * @param label get typed input from sender
     * @param args get arguments after inputted command
     * @return true if the yml files are load, if not return error message to sender.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("command.lightnpc.reload")) {
            Lang.send(sender, Lang.COMMAND_NO_PERMISSION
                    .replace("{getCommand}", "Reload"));
            Logger.error(sender.getName() + " tried to reload " + plugin.getName() + " v" + plugin.getDescription().getVersion() + " but had no permissions to.");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            Config.reload();
            Lang.reload();
            msg = plugin.getName() + " v" + plugin.getDescription().getVersion();
            msg += " reload";
            Logger.info(plugin.getName() + " v" + plugin.getDescription().getVersion() + " was reloaded by " + sender.getName());
        }

        Lang.send(sender, msg);

        return true;
    }
}
