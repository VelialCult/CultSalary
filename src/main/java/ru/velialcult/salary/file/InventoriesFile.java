package ru.velialcult.salary.file;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ru.velialcult.library.bukkit.file.FileOperations;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.salary.CultSalary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InventoriesFile {
    private FileOperations fileOperations;
    private final CultSalary plugin;
    private final Map<String, String> strings;
    private FileConfiguration config;

    public InventoriesFile(CultSalary plugin) {
        this.plugin = plugin;
        strings = new HashMap<>();
        config = FileRepository.getByName(plugin, "inventories.yml").getConfiguration();
    }

    public void load() {
        strings.clear();
        config = FileRepository.getByName(plugin, "inventories.yml").getConfiguration();
        ConfigurationSection section = config.getConfigurationSection("inventories");
        if (section != null) {
            Objects.requireNonNull(section).getKeys(true).forEach(path -> {
                String fullPath = "inventories." + path;
                if (config.isList(fullPath)) {
                    List<String> lines = config.getStringList(fullPath);
                    strings.put(fullPath, String.join("\n", lines));
                } else {
                    String value = config.getString(fullPath);
                    strings.put(fullPath, value);
                }
            });
        }
        this.fileOperations = new FileOperations(strings);
    }

    public FileOperations getFileOperations() {
        return fileOperations;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
