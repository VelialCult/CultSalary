package ru.velialcult.salary.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import ru.velialcult.library.bukkit.utils.InventoryUtil;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.library.java.utils.TimeUtil;
import ru.velialcult.salary.CultSalary;
import ru.velialcult.salary.file.InventoriesFile;
import ru.velialcult.salary.manager.GroupManager;
import ru.velialcult.salary.manager.SalaryManager;
import ru.velialcult.salary.providers.ProvidersManager;
import ru.velialcult.salary.providers.luckperms.LuckPermsProvider;
import ru.velialcult.salary.salary.Salary;
import ru.velialcult.salary.service.SalaryService;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;
import xyz.xenondevs.invui.item.impl.SuppliedItem;
import xyz.xenondevs.invui.window.Window;

import java.util.List;
import java.util.Map;

public class SalaryMenu {

    public static void generateInventory(Player player) {

        GroupManager groupManager = CultSalary.getInstance().getGroupManager();
        InventoriesFile config = CultSalary.getInstance().getInventoriesFile();
        ProvidersManager providersManager = CultSalary.getInstance().getProvidersManager();
        SalaryService service = CultSalary.getInstance().getService();
        SalaryManager salaryManager = service.getSalaryManager();
        String group = groupManager.getPrimaryGroup(player);
        Salary salary = salaryManager.getSalaryByGroup(group);
        String groupName;
        if (providersManager.useLuckPerms()) {
            LuckPermsProvider luckPermsProvider = providersManager.getLuckPermsProvider();
            groupName = luckPermsProvider.getGroupPrefix(player);
        } else {
            groupName = group;
        }

        String[] structure = config.getFileOperations().getList("inventories.main-menu.structure").toArray(new String[0]);

        char itemChar = config.getFileOperations().getString("inventories.main-menu.item.symbol").charAt(0);

        SuppliedItem suppliedItem = new AutoUpdateItem(20, () ->
                s -> {
                    if (salaryManager.timeIsExpire(player.getUniqueId(), group)) {
                        return VersionAdapter.getItemBuilder().setItem(groupManager.getItemForGroup(group))
                                .setDisplayName(config.getFileOperations().getString(
                                        "inventories.main-menu.items.available.displayName",
                                        new ReplaceData("{group}", groupName),
                                        new ReplaceData("{cooldown}", TimeUtil.getTime(salary.getReplyTimeInSeconds()))
                                ))
                                .setLore(config.getFileOperations().getList("inventories.main-menu.items.available.lore",
                                                                            new ReplaceData("{group}", groupName),
                                                                            new ReplaceData("{cooldown}", TimeUtil.getTime(salary.getReplyTimeInSeconds()))))
                                .build();
                    } else {
                        return VersionAdapter.getItemBuilder().setItem(groupManager.getItemForGroup(group))
                                .setDisplayName(config.getFileOperations().getString(
                                        "inventories.main-menu.items.wait.displayName",
                                        new ReplaceData("{time}", salaryManager.getTimeLeft(player.getUniqueId(), group)),
                                        new ReplaceData("{group}", groupName),
                                        new ReplaceData("{cooldown}", TimeUtil.getTime(salary.getReplyTimeInSeconds()))
                                ))
                                .setLore(config.getFileOperations().getList(
                                        "inventories.main-menu.items.wait.lore",
                                        new ReplaceData("{time}", TimeUtil.getTime(salaryManager.getTimeLeft(player.getUniqueId(), group))),
                                        new ReplaceData("{group}", groupName),
                                        new ReplaceData("{cooldown}", TimeUtil.getTime(salary.getReplyTimeInSeconds()))
                                ))
                                .build();
                    }
                }) {
            @Override
            public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
                player.closeInventory();
                service.giveSalary(player, group);
            }
        };

        Gui.Builder.Normal builder = Gui.normal()
                .setStructure(structure)
                .addIngredient(itemChar, suppliedItem);

        Map<Character, SuppliedItem> customItemList = InventoryUtil.createItems(config.getConfig(),
                                                                     "inventories.main-menu.items",
                                                                     (event, path) -> {
            List<String> commands = config.getConfig().getStringList(path + ".actionOnClick");
            for (String command : commands) {
                        if (command.startsWith("[message]")) {
                            String message = command.replace("[message]", "");
                            VersionAdapter.MessageUtils().sendMessage(player, message);
                        }

                        if (command.startsWith("[execute]")) {
                            String executeCommand = command.replace("[execute]", "");
                            Bukkit.dispatchCommand(player, executeCommand);
                        }

                        if (command.equals("[close]")) {
                            player.closeInventory();
                        }
                    }
                });

        InventoryUtil.setItems(builder, customItemList);

        Window window = Window.single()
                .setViewer(player)
                .setTitle(config.getFileOperations().getString("inventories.main-menu.title"))
                .setGui(builder.build())
                .build();
        window.open();
    }
}
