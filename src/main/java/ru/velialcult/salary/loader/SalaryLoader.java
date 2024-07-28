package ru.velialcult.salary.loader;

import org.bukkit.configuration.file.FileConfiguration;
import ru.velialcult.library.java.utils.TimeUtil;
import ru.velialcult.salary.salary.Salary;

import java.util.ArrayList;
import java.util.List;

public class SalaryLoader {

    private final FileConfiguration config;

    public SalaryLoader(FileConfiguration config) {
        this.config = config;
    }

    public List<Salary> loadSalaries() {
        List<Salary> salaries = new ArrayList<>();

        for (String groupName : config.getConfigurationSection("salary").getKeys(false)) {
            List<String> commands = config.getStringList("salary." + groupName + ".commands");
            long replyInSeconds = TimeUtil.parseStringToTime(config.getString("salary." + groupName + ".reply"));
            salaries.add(new Salary(groupName, commands, replyInSeconds));
        }

        return salaries;
    }
}
