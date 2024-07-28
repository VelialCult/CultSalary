package ru.velialcult.salary.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.velialcult.library.bukkit.utils.PlayerUtil;
import ru.velialcult.salary.file.MessagesFile;
import ru.velialcult.salary.menu.SalaryMenu;

public class SalaryCommand implements CommandExecutor {

    private final MessagesFile messagesFile;

    public SalaryCommand(MessagesFile messagesFile) {
        this.messagesFile = messagesFile;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (PlayerUtil.senderIsPlayer(commandSender)) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                SalaryMenu.generateInventory(player);
            } else {
                messagesFile.getFileOperations().getString("messages.commands.salary.usage");
            }
        }
        return false;
    }
}
