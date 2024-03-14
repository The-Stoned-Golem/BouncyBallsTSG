package com.huskydreaming.bouncyball.inventories.providers;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ProjectileData;
import com.huskydreaming.bouncyball.inventories.base.InventoryPageProvider;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.enumeration.Menu;
import com.huskydreaming.bouncyball.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MaterialInventory extends InventoryPageProvider<Material> {

    private final String key;
    private final BouncyBallPlugin plugin;
    private final InventoryService inventoryService;
    private final ProjectileService projectileService;

    public MaterialInventory(BouncyBallPlugin plugin, String key, int rows, Material[] array) {
        super(rows, array);
        this.key = key;
        this.plugin = plugin;

        this.inventoryService = plugin.provide(InventoryService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
        this.smartInventory = inventoryService.getEditInventory(plugin, key);
    }

    @Override
    public ItemStack construct(Player player, int index, Material material) {
        ProjectileData projectileData = projectileService.getDataFromKey(key);

        boolean isMaterial = projectileData.getMaterial() == material;

        Menu title = isMaterial ? Menu.EDIT_CURRENT_MATERIAL_TITLE : Menu.EDIT_SET_MATERIAL_TITLE;
        Menu lore = isMaterial ? Menu.EDIT_CURRENT_MATERIAL_LORE : Menu.EDIT_SET_MATERIAL_LORE;

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(material))
                .setLore(lore.parseList())
                .setMaterial(material)
                .setEnchanted(isMaterial)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Material material, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ProjectileData projectileData = projectileService.getDataFromKey(key);
            if (projectileData.getMaterial() == material) return;

            projectileData.setMaterial(material);
            inventoryService.getEditInventory(plugin, key).open(player);
        }
    }
}