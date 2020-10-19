package bestem0r.villagermarket.events.chat;

import bestem0r.villagermarket.VMPlugin;
import bestem0r.villagermarket.items.ShopItem;
import bestem0r.villagermarket.utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetAmount implements Listener {

    private Player player;
    private ShopItem.Builder builder;

    public SetAmount(Player player, ShopItem.Builder builder) {
        this.player = player;
        this.builder = builder;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer() != player) return;
        String message = event.getMessage();
        String cancel = VMPlugin.getInstance().getConfig().getString("cancel");

        event.setCancelled(true);
        if (message.equalsIgnoreCase(cancel)) {
            player.sendMessage(new Color.Builder().path("messages.cancelled").addPrefix().build());
            HandlerList.unregisterAll(this);
            return;
        }
        if (!canConvert(message)) {
            player.sendMessage(new Color.Builder().path("messages.not_number").addPrefix().build());
            return;
        } else if (Integer.parseInt(message) > 64 || Integer.parseInt(message) < 1) {
            player.sendMessage(new Color.Builder().path("messages_not_valid_range").addPrefix().build());
            return;
        }
        builder.amount(Integer.parseInt(event.getMessage()));

        player.sendMessage(new Color.Builder().path("messages.amount_successful").addPrefix().build());
        player.sendMessage(new Color.Builder().path("messages.type_price").addPrefix().build());
        player.sendMessage(new Color.Builder().path("messages.type_cancel").replace("%cancel%", cancel).addPrefix().build());

        Bukkit.getServer().getPluginManager().registerEvents(new SetPrice(player, builder), VMPlugin.getInstance());
        HandlerList.unregisterAll(this);
    }

    private Boolean canConvert(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
