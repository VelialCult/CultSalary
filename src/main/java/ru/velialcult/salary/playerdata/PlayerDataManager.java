package ru.velialcult.salary.playerdata;

import ru.velialcult.salary.database.SalaryDataBase;

import java.util.UUID;

public class PlayerDataManager {

    private final PlayerDataCache playerDataCache;
    private final SalaryDataBase salaryDataBase;

    public PlayerDataManager(SalaryDataBase salaryDataBase) {
        this.playerDataCache = new PlayerDataCache();
        this.salaryDataBase = salaryDataBase;
    }

    public PlayerData getPlayerData(UUID uuid) {
        PlayerData playerData = playerDataCache.getPlayerData(uuid);

        if (playerData == null) {
            playerData = new PlayerData(uuid, salaryDataBase.getTime(uuid));
            playerDataCache.cache(playerData);
        }
        return playerData;
    }
}
