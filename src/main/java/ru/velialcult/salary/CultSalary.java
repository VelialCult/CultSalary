package ru.velialcult.salary;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.velialcult.library.bukkit.file.FileRepository;
import ru.velialcult.library.bukkit.utils.ConfigurationUtil;
import ru.velialcult.library.java.database.DataBase;
import ru.velialcult.library.java.database.DataBaseType;
import ru.velialcult.library.update.UpdateChecker;
import ru.velialcult.salary.command.SalaryCommand;
import ru.velialcult.salary.database.SalaryDataBase;
import ru.velialcult.salary.file.InventoriesFile;
import ru.velialcult.salary.file.MessagesFile;
import ru.velialcult.salary.manager.GroupManager;
import ru.velialcult.salary.providers.ProvidersManager;
import ru.velialcult.salary.service.SalaryService;

public class CultSalary extends JavaPlugin {
    
    private static CultSalary instance;
    private DataBase dataBase;
    
    private ProvidersManager providersManager;
    private GroupManager groupManager;
    private SalaryService service;
    
    private MessagesFile messagesFile;
    private InventoriesFile inventoriesFile;
    
    @Override
    public void onEnable() {
        instance = this;
        long mills = System.currentTimeMillis();
        
        try {
            
            providersManager = new ProvidersManager(this);
            providersManager.load();
            loadFiles();
            
            UpdateChecker updater = new UpdateChecker(this, "CultSalary");
            updater.check();
            
            String dataBaseType = getConfig().getString("settings.database.type");
            if (dataBaseType.equalsIgnoreCase("mysql")) {
                this.dataBase = new DataBase(this, DataBaseType.MySQL);
                dataBase.connect(getConfig().getString("settings.database.mysql.user"),
                                 getConfig().getString("settings.database.mysql.password"),
                                 getConfig().getString("settings.database.mysql.url"));
            } else {
                this.dataBase  = new DataBase(this, DataBaseType.SQLite);
                dataBase.connect();
            }
            
            SalaryDataBase salaryDataBase = new SalaryDataBase(dataBase);
            
            groupManager = new GroupManager(this, providersManager);
            
            this.service = new SalaryService(this,
                                             salaryDataBase,
                                             messagesFile);
            
            Bukkit.getPluginCommand("salary").setExecutor(new SalaryCommand(messagesFile));
            
            getLogger().info("Плагин был запущен за " + ChatColor.YELLOW + (System.currentTimeMillis() - mills) + "ms");
        } catch (Exception e) {
            getLogger().severe("Произошла ошибка при инициализации плагина: " + e.getMessage());
        }
    }
    
    private void loadFiles() {
        this.saveDefaultConfig();
        ConfigurationUtil.loadConfigurations(this, "messages.yml", "inventories.yml");
        FileRepository.load(this);
        messagesFile = new MessagesFile(this);
        messagesFile.load();
        inventoriesFile = new InventoriesFile(this);
        inventoriesFile.load();
    }
    
    public SalaryService getService() {
        return service;
    }
    
    public MessagesFile getMessagesFile() {
        return messagesFile;
    }
    
    public static CultSalary getInstance() {
        return instance;
    }
    
    public ProvidersManager getProvidersManager() {
        return providersManager;
    }
    
    public GroupManager getGroupManager() {
        return groupManager;
    }
    
    public InventoriesFile getInventoriesFile() {
        return inventoriesFile;
    }
}
