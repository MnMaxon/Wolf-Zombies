package me.MnMaxon.WolfZombies;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MainListener implements Listener {
	ArrayList<Zombie> targetCooldown = new ArrayList<Zombie>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteractWithEntity(PlayerInteractEntityEvent e) {
		if (e.getPlayer().hasPermission("TamableZombies.tame")
				&& e.getRightClicked().getType().equals(EntityType.ZOMBIE)) {
			ItemStack is = e.getPlayer().getItemInHand();
			if (Main.config.get("ZombieFood." + is.getType().name()) != null
					&& Main.config.getBoolean("ZombieFood." + is.getType().name())) {
				if (new Random().nextInt(3) != 0) {
					e.setCancelled(true);
					if (e.getPlayer().getItemInHand().getAmount() == 1)
						e.getPlayer().setItemInHand(null);
					else
						e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
					final Zombie zombie = (Zombie) e.getRightClicked();
					((Zombie) e.getRightClicked()).setTarget(null);
					e.getPlayer().updateInventory();
					if (!targetCooldown.contains(zombie)) {
						targetCooldown.add(zombie);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
							public void run() {
								targetCooldown.remove(zombie);
							}
						}, 20L);
					}
					return;
				}
				Location loc = e.getRightClicked().getLocation();
				e.getRightClicked().remove();
				Wolf wolf = (Wolf) loc.getWorld().spawnEntity(loc, EntityType.WOLF);
				DisguiseLib.disguise(wolf, ((Zombie) e.getRightClicked()).getEquipment());
				wolf.setAdult();
				wolf.setOwner(e.getPlayer());
				wolf.setTamed(true);
				wolf.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 0, Integer.MAX_VALUE));
				wolf.setCustomName(ChatColor.GREEN + "Zombie");
			}
		}
	}

	@EventHandler
	public void onEntityTp(EntityTeleportEvent e) {
		if (e.getEntity().isValid() && e.getEntity() instanceof LivingEntity
				&& ((LivingEntity) e.getEntity()).getCustomName() != null
				&& ((LivingEntity) e.getEntity()).getCustomName().equals(ChatColor.GREEN + "Zombie"))
			DisguiseLib.disguise(e.getEntity());
	}

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (targetCooldown.contains(e.getEntity()))
			e.setCancelled(true);
	}

	@EventHandler
	public void onChunkPopulate(ChunkPopulateEvent e) {
		for (Entity ent : e.getChunk().getEntities())
			if (ent.isValid() && ent instanceof LivingEntity && ((LivingEntity) ent).getCustomName() != null
					&& ((LivingEntity) ent).getCustomName().equals(ChatColor.GREEN + "Zombie"))
				DisguiseLib.disguise(ent);
	}

	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			for (Entity ent : p.getNearbyEntities(10, 10, 10))
				if (ent instanceof Wolf && ((Wolf) ent).getCustomName() != null
						&& ((Wolf) ent).getCustomName().equals(ChatColor.GREEN + "Zombie")
						&& e.getEntity() instanceof LivingEntity && ((Wolf) ent).getTarget() == null)
					((Wolf) ent).setTarget((LivingEntity) e.getEntity());
		}
	}

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent e) {
		int count = 0;
		if (e.getEntity() instanceof Wolf && e.getSpawnReason().equals(SpawnReason.BREEDING)) {
			for (Entity ent : e.getEntity().getNearbyEntities(3, 3, 3))
				if (ent instanceof Wolf)
					count++;
			if (count >= 2) {
				e.getEntity().setCustomName(ChatColor.GREEN + "Zombie");
				DisguiseLib.disguise(e.getEntity());
			}
		}
	}
}
