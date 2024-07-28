package ru.velialcult.salary.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.salary.CultSalary;
import ru.velialcult.salary.providers.ProvidersManager;
import ru.velialcult.salary.providers.luckperms.LuckPermsProvider;

public class GroupManager {

    private final CultSalary cultSalary;
    private final ProvidersManager providersManager;

    public GroupManager(CultSalary cultSalary, ProvidersManager providersManager) {
        this.cultSalary = cultSalary;
        this.providersManager  = providersManager;
    }

    public String getPrimaryGroup(Player player) {
        if (providersManager.useLuckPerms()) {
            LuckPermsProvider luckPermsProvider = providersManager.getLuckPermsProvider();
            return luckPermsProvider.getGroup(player);
        }

        return "default";
    }

    public ItemStack getItemForGroup(String groupName) {
        FileConfiguration config = cultSalary.getConfig();
        if (config.contains("items." + groupName)) {
            return InventoryUtil.createItem(cultSalary.getConfig(), "items." + groupName);
        } else {
            return VersionAdapter.getSkullBuilder()
                    .setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTViOTA2MDYyNjIzYjFmNjM3YWY3ZTNmZTM0Y2Q5NDc3NzlmNWYyNGQyNzI3ZDY2M2JjMjY2MzI3YzVjYjdiOSJ9fX0=")
                    .build();
        }
    }
}
