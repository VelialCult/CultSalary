package ru.velialcult.salary.manager;

import org.bukkit.entity.Player;
import ru.velialcult.salary.playerdata.PlayerData;
import ru.velialcult.salary.playerdata.PlayerDataManager;
import ru.velialcult.salary.salary.Salary;
import ru.velialcult.salary.storage.SalaryStorage;

import java.time.*;
import java.util.UUID;

public class SalaryManager {
    
    private final SalaryStorage storage;
    private final PlayerDataManager playerDataManager;
    
    public SalaryManager(SalaryStorage storage,
                         PlayerDataManager playerDataManager) {
        this.storage = storage;
        this.playerDataManager = playerDataManager;
    }
    
    public Salary getSalaryByGroup(String groupName) {
        return storage.getSalaryList()
                .stream()
                .filter(salary -> salary.getGroupName().equalsIgnoreCase(groupName))
                .findAny()
                .orElse(null);
    }
    
    public boolean timeIsExpire(UUID uuid, String groupName) {
        PlayerData playerData = playerDataManager.getPlayerData(uuid);
        Salary salary = getSalaryByGroup(groupName);
        Instant instant = Instant.ofEpochSecond(playerData.getTime(groupName)).plusSeconds(salary.getReplyTimeInSeconds());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Europe/Moscow"));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        return Duration.between(now, zonedDateTime).isNegative();
    }
    
    public long getTimeLeft(UUID uuid, String groupName) {
        PlayerData playerData = playerDataManager.getPlayerData(uuid);
        Salary salary = getSalaryByGroup(groupName);
        Instant instant = Instant.ofEpochSecond(playerData.getTime(groupName)).plusSeconds(salary.getReplyTimeInSeconds());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Europe/Moscow"));
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        return Duration.between(now, zonedDateTime).toSeconds();
    }
}
