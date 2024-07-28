package ru.velialcult.salary.providers;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import ru.velialcult.salary.providers.luckperms.LuckPermsProvider;

import java.util.HashMap;
import java.util.Map;

public class ProvidersManager {

    private final Map<String, Boolean> providers = new HashMap<>();
    private final Plugin plugin;

    public ProvidersManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        loadProvider("LuckPerms", "5.4.102");
    }

    private void loadProvider(String pluginName, String minVersion) {
        boolean isPluginLoaded = false;
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            String version = plugin.getDescription().getVersion();

            if (version.compareTo(minVersion) >= 0) {
                this.plugin.getLogger().info(pluginName + " найден, использую " + pluginName + " API");
                isPluginLoaded = true;
            } else {
                this.plugin.getLogger().warning("Версия " + pluginName + " < " + minVersion + " не поддерживается. Игнорирую данную зависимость");
            }
        }
        providers.put(pluginName, isPluginLoaded);
    }

    public boolean useLuckPerms() { return providers.getOrDefault("LuckPerms", false); }


    public LuckPermsProvider getLuckPermsProvider() {
        if (useLuckPerms()) {
            return new LuckPermsProvider();
        }
        return null;
    }
}
