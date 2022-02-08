package me.bestem0r.villagermarket.command.subcommand;

import me.bestem0r.villagermarket.ConfigManager;
import me.bestem0r.villagermarket.VMPlugin;
import me.bestem0r.villagermarket.command.ISubCommand;
import me.bestem0r.villagermarket.menu.StorageHolder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ExpiredStorageCommand implements ISubCommand {

    private final VMPlugin plugin;

    public ExpiredStorageCommand(VMPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> getCompletion(int index, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (plugin.getShopManager().getExpiredStorages().containsKey(player.getUniqueId())) {
                final StorageHolder holder = new StorageHolder(plugin, 0);
                holder.loadItems(plugin.getShopManager().getExpiredStorages().get(player.getUniqueId()));

                holder.setCloseEvent(() -> {
                    List<ItemStack> items = holder.getItems();
                    if (items.isEmpty()) {
                        plugin.getShopManager().getExpiredStorages().remove(player.getUniqueId());
                    } else {
                        plugin.getShopManager().getExpiredStorages().put(player.getUniqueId(), items);
                    }
                });

                holder.open(player);

            } else {
                player.sendMessage(ConfigManager.getMessage("messages.no_expired_storage"));
            }
        }
    }

    @Override
    public String getDescription() {
        return "Open expired shop storage: /vm expiredstorage";
    }

    @Override
    public boolean requirePermission() {
        return false;
    }
}
