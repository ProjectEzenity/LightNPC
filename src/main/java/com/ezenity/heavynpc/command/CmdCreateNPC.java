package com.ezenity.heavynpc.command;

import com.ezenity.heavynpc.Main;
import com.ezenity.heavynpc.configuration.Config;
import com.ezenity.heavynpc.configuration.Lang;
import com.ezenity.heavynpc.util.Logger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Create NPC.
 *
 * This class will hold the core for creating an Light NPC.
 *
 * @author Ezenity
 * @version 0.0.1
 */
public class CmdCreateNPC implements TabExecutor {
    /**
     * Plugin instance. Initialize the plugin instance.
     */
    private Main plugin;

    /**
     * Constructor. Initializing the plugin object.
     *
     * @param plugin load instance of plugin
     */
    public CmdCreateNPC(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Tab Autocompletion
     * <p>
     * Will auto populate the create option for the npc command to allow the user to tab in the rest of the argument.
     *
     * @param sender get sender
     * @param command get command oject info
     * @param label get typed input from sender
     * @param args get arguments after inputted command
     * @return command options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if ( args.length == 1 && "create".startsWith(args[0].toLowerCase()) && sender.hasPermission("command.lightnpc.create") ) {
            return Collections.singletonList("create");
        }

        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if sender is a player
        if (!(sender instanceof Player)) {
            Lang.send(sender, Lang.PLAYER_COMMAND);
            Logger.error(sender + " is not a player. Cannot create Light NPC.");
            return true;
        }
        // Make sender player
        Player target = (Player) sender;

        // Check player permissions
        if (!target.hasPermission("command.lightnpc.create")) {
            Lang.send(target, Lang.COMMAND_NO_PERMISSION
            .replace("{getCommand}", "/npc create"));
            Logger.error(target + " does not have permission to use /npc create, cancelling.");
            return true;
        }

        // Change for "create" argument
        if (args.length < 2) {
            Lang.send(target, "Please specify a Light NPC Name."); // TODO: create Lang
            Lang.send(target, "/npc create (name)"); // TODO: Create Lang
            Logger.error(target + " did not provide a light npc name, cancelling.");
            return true;
        }

        final Location location = target.getLocation();
        final String name = (args[1].length() > 16) ? args[1].substring(0, 16) : args[1];
        final Integer npcID = 0; // TODO: Make npcManager

        if (npcID == null) { // Statement will change once NPCManage is created
            Lang.send(target, "Did not create Light NPC, Light NPC ID is invalid."); // TODO: Create Lang
            Logger.error("Light NPC Id is null, cancelling.");
            return true;
        }

        plugin.getConfig().set("npc.light." + npcID + ".name", name);
        plugin.getConfig().set("npc.light." + npcID + ".world", location.getWorld().getName());
        plugin.getConfig().set("npc.light." + npcID + ".x", location.getX()); // Try getBlockX() is getX() does not work.
        plugin.getConfig().set("npc.light." + npcID + ".y", location.getY());
        plugin.getConfig().set("npc.light." + npcID + ".z", location.getZ());

        // Needs testing
        Config.reload();


    }

}
