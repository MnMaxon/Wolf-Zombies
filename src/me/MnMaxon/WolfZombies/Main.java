package me.MnMaxon.WolfZombies;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static String dataFolder;
	public static Main plugin;
	public static YamlConfiguration config;

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();
		getServer().getPluginManager().registerEvents(new MainListener(), this);
		config = setupConfig();
		for (World world : getServer().getWorlds())
			for (Entity ent : world.getEntities())
				if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
						&& ((Wolf) ent).getCustomName().equals(ChatColor.GREEN + "Zombie"))
					DisguiseLib.disguise(ent);
	}

	public static YamlConfiguration setupConfig() {
		cfgSetter(Material.COOKED_CHICKEN);
		cfgSetter(Material.COOKED_FISH);
		cfgSetter(Material.COOKIE);
		cfgSetter(Material.EGG);
		cfgSetter(Material.GRILLED_PORK);
		cfgSetter(Material.MELON);
		cfgSetter(Material.PORK);
		cfgSetter(Material.PUMPKIN_PIE);
		cfgSetter(Material.RAW_BEEF);
		cfgSetter(Material.RAW_CHICKEN);
		cfgSetter(Material.RAW_FISH);
		cfgSetter(Material.ROTTEN_FLESH);
		cfgSetter(Material.SUGAR);
		cfgSetter(Material.APPLE);
		cfgSetter(Material.BAKED_POTATO);
		cfgSetter(Material.BREAD);
		cfgSetter(Material.CARROT);
		cfgSetter(Material.COCOA);
		cfgSetter(Material.COOKED_BEEF);
		return Config.Load(dataFolder + "/Config.yml");
	}

	public static void cfgSetter(Material material) {
		YamlConfiguration cfg = Config.Load(dataFolder + "/Config.yml");
		if (cfg.get("ZombieFood." + material.name()) == null) {
			cfg.set("ZombieFood." + material.name(), false);
			Config.Save(cfg, dataFolder + "/Config.yml");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			config = setupConfig();
			sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
		} else
			displayHelp(sender);
		return false;
	}

	private void displayHelp(CommandSender s) {
		s.sendMessage(ChatColor.AQUA + "========= HELP =========");
		s.sendMessage(ChatColor.DARK_PURPLE + "/TZ Reload");
	}
}