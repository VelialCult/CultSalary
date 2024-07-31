package ru.velialcult.salary.database;

import ru.velialcult.library.java.database.Connector;
import ru.velialcult.library.java.database.DataBase;
import ru.velialcult.library.java.database.query.QuerySymbol;
import ru.velialcult.library.java.database.query.SQLQuery;
import ru.velialcult.library.java.database.table.ColumnType;
import ru.velialcult.library.java.database.table.TableColumn;
import ru.velialcult.library.java.database.table.TableConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SalaryDataBase {
    
    private final DataBase dataBase;
    private final Connector connector;
    
    public SalaryDataBase(DataBase dataBase) {
        this.dataBase = dataBase;
        this.connector = dataBase.getConnector();
        createTables();
    }
    
    public void setTime(UUID uuid, String groupName, long timeInMills) {
        long timeInSeconds = timeInMills / 1000;
        connector.execute(SQLQuery.insertOrUpdate(dataBase, "salary_users")
                                  .set("uuid", uuid.toString())
                                  .set("salary_group", groupName)
                                  .set("time", timeInSeconds)
                                  .where("uuid", QuerySymbol.EQUALLY, uuid.toString())
                                  .where("salary_group", QuerySymbol.EQUALLY, groupName),
                          true);
    }
    
    public Map<String, Long> getTime(UUID uuid) {
        Map<String, Long> groups = new HashMap<>();
        connector.executeQuery(SQLQuery.selectFrom("salary_users")
                                       .where("uuid", QuerySymbol.EQUALLY, uuid.toString()),
                               rs -> {
                                   while (rs.next()) {
                                       String groupName = rs.getString("salary_group");
                                       long time = rs.getLong("time");
                                       groups.put(groupName, time);
                                   }
                                   return Void.TYPE;
                               }, false);
        
        return groups;
    }
    
    private void createTables() {
        new TableConstructor("salary_users",
                             new TableColumn("uuid", ColumnType.VARCHAR_32),
                             new TableColumn("salary_group", ColumnType.VARCHAR_32),
                             new TableColumn("time", ColumnType.BIG_INT)
        ).create(dataBase);
    }
}