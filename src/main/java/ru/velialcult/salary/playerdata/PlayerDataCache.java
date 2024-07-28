package ru.velialcult.salary.playerdata;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDataCache {

    private final List<PlayerData> playerDataList;

    public PlayerDataCache() {
        this.playerDataList = new ArrayList<>();
    }

    public void cache(PlayerData playerData) {
        this.playerDataList.add(playerData);
    }

    public List<PlayerData> getPlayerDataList() {
        return playerDataList;
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataList.stream()
                .filter(playerData -> playerData.getUuid().equals(uuid))
                .findAny()
                .orElse(null);
    }
}
