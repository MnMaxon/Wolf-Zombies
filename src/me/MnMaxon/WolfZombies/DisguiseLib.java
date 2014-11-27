package me.MnMaxon.WolfZombies;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.ZombieWatcher;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;

public class DisguiseLib {
	public static void disguise(Entity ent, EntityEquipment equipment) {
		MobDisguise disguise = new MobDisguise(DisguiseType.ZOMBIE);
		ZombieWatcher watcher = ((ZombieWatcher) disguise.getWatcher());
		watcher.setCustomName(null);
		if (equipment != null) {
			watcher.setArmor(equipment.getArmorContents());
			watcher.setItemInHand(equipment.getItemInHand());
		}
		DisguiseAPI.disguiseToAll(ent, disguise);
	}

	public static void disguise(Entity ent) {
		disguise(ent, null);
	}
}
