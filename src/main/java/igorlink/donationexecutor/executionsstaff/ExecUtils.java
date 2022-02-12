package igorlink.donationexecutor.executionsstaff;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExecUtils {
    public static void giveToPlayer(
            @NotNull Player player,
            @NotNull Material material,
            int amount,
            @NotNull String donationUsername
    ) {
        giveToPlayer(player, material, amount, donationUsername, null);
    }

    public static void giveToPlayer(
            @NotNull Player player,
            @NotNull Material material,
            int amount,
            @NotNull String donationUsername,
            @Nullable String itemName
    ) {
        ItemStack stack = createItemStack(material, amount, donationUsername, itemName);
        stack.getItemMeta().setDisplayName(itemName);

        giveItemStackToPlayer(player, stack);
    }

    private static void giveItemStackToPlayer(@NotNull Player player, @NotNull ItemStack stack) {
        Location playerLocation = player.getLocation();
        Location dropLocation = playerLocation.add(playerLocation.getDirection().setY(0).normalize());

        player.getWorld().dropItemNaturally(dropLocation, stack);
    }

    @NotNull
    private static ItemStack createItemStack(
            @NotNull Material material,
            int amount,
            @NotNull String donationUsername,
            @Nullable String itemName
    ) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta meta = itemStack.getItemMeta();
        meta.lore(List.of(Component.text(createLore(donationUsername))));

        if(itemName != null) {
            meta.displayName(Component.text(itemName));
        }

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    @NotNull
    private static String createLore(@NotNull String donationUsername) {
        return "§7Подарочек от §e" + donationUsername;
    }
}
