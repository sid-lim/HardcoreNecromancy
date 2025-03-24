package mr.marl.HardcoreNecromancy;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import org.bukkit.potion.PotionEffect;

public class HardcoreNecromancy extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("HardcoreNecromancy by MrMarL");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0)
			return true;
		if (!cmd.getName().equalsIgnoreCase("necro"))
			return true;

		if (!(sender instanceof Player)) {
			getLogger().info("You have to be a Player to revive someone.");
			return true;
		}

		Player player = (Player) sender;

		if (player.getGameMode() == GameMode.SPECTATOR) {
			player.sendMessage(ChatColor.RED + "You can't revive people when you're dead.");
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);

		if (!(target instanceof Player)) {
			player.sendMessage(ChatColor.RED + "Couldn't find the player " + args[0]);
			return true;
		}

		if (target == player) {
			player.sendMessage(ChatColor.RED + "You can't revive yourself.");
			return true;
		}
		if (target.getGameMode() != GameMode.SPECTATOR) {
			player.sendMessage(ChatColor.RED + "This player isn't dead.");
			return true;
		}

		PlayerInventory inv = player.getInventory();
		if (inv.getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING
				|| inv.getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
			player.sendMessage(ChatColor.RED + "You must hold totems in both hands");
			return true;
		}

		if (player.getHealth() < 8d) {
			player.sendMessage(ChatColor.RED + "You need to have 4 health units.");
			return true;
		}

		inv.getItemInOffHand().setAmount(inv.getItemInOffHand().getAmount() - 1);
		player.damage(player.getHealth());//inv.getItemInMainHand().setAmount(inv.getItemInMainHand().getAmount() - 1);

		target.setGameMode(GameMode.SURVIVAL);
		target.teleport(player);
		target.getInventory().setItemInMainHand(new ItemStack(Material.TOTEM_OF_UNDYING));
		target.damage(target.getHealth());

		// player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
				//.setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 6);
		// target.getAttribute(Attribute.GENERIC_MAX_HEALTH)
				//.setBaseValue(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 6);

		target.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 1));

		player.sendMessage(ChatColor.GREEN + "You've revived " + args[0]);
		target.sendMessage(ChatColor.GREEN + "You've been revived by " + player.getDisplayName());
		return true;
	}
}
