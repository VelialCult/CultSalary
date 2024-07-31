package ru.velialcult.salary.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.velialcult.library.core.VersionAdapter;
import ru.velialcult.library.java.text.ReplaceData;
import ru.velialcult.library.java.utils.TimeUtil;
import ru.velialcult.salary.CultSalary;
import ru.velialcult.salary.database.SalaryDataBase;
import ru.velialcult.salary.file.MessagesFile;
import ru.velialcult.salary.loader.SalaryLoader;
import ru.velialcult.salary.manager.SalaryManager;
import ru.velialcult.salary.playerdata.PlayerData;
import ru.velialcult.salary.playerdata.PlayerDataManager;
import ru.velialcult.salary.salary.Salary;
import ru.velialcult.salary.storage.SalaryStorage;

public class SalaryService {
    
    private final SalaryStorage storage;
    private final SalaryManager salaryManager;
    private final PlayerDataManager playerDataManager;
    private final MessagesFile messagesFile;
    private final SalaryDataBase salaryDataBase;
    
    public SalaryService(CultSalary cultSalary,
                         SalaryDataBase salaryDataBase,
                         MessagesFile messagesFile) {
        this.storage = new SalaryStorage();
        SalaryLoader salaryLoader = new SalaryLoader(cultSalary.getConfig());
        this.storage.getSalaryList().addAll(salaryLoader.loadSalaries());
        this.salaryDataBase = salaryDataBase;
        this.playerDataManager = new PlayerDataManager(salaryDataBase);
        this.salaryManager = new SalaryManager(storage, playerDataManager);
        this.messagesFile = messagesFile;
    }
    
    public void giveSalary(Player player, String groupName) {
        Salary salary = salaryManager.getSalaryByGroup(groupName);
        PlayerData playerData = playerDataManager.getPlayerData(player.getUniqueId());
        if (salaryManager.timeIsExpire(player.getUniqueId(), groupName)) {
            VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getList("messages.salary.take"));
            salary.getCommands().forEach(string -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replace("{player}", player.getName()));
            });
            playerData.setTime(groupName, System.currentTimeMillis());
            salaryDataBase.setTime(player.getUniqueId(), groupName, System.currentTimeMillis());
            
        } else {
            VersionAdapter.MessageUtils().sendMessage(player, messagesFile.getFileOperations().getList("messages.salary.cooldown",
                                                                                                       new ReplaceData("{cooldown}", TimeUtil.getTime(salaryManager.getTimeLeft(player.getUniqueId(), groupName)))
            ));
        }
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    
    public SalaryManager getSalaryManager() {
        return salaryManager;
    }
    
    public SalaryStorage getStorage() {
        return storage;
    }
}
