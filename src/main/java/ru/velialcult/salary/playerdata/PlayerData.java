package ru.velialcult.salary.playerdata;

import java.util.Map;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final Map<String, Long> map;

    public PlayerData(UUID uuid, Map<String , Long> map) {
        this.uuid = uuid;
        this.map = map;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<String, Long> getMap() {
        return map;
    }

    public long getTime(String groupName) {
        return map.getOrDefault(groupName, 0L);
    }

    public void setTime(String groupName, long timeInMills) {
        map.putIfAbsent(groupName, timeInMills / 1000);
    }
}
