package ru.velialcult.salary.salary;
import java.util.List;

public class Salary {

    private final String groupName;

    private final List<String> commands;
    private final long replyTimeInSeconds;

    public Salary(String groupName,
                  List<String> commands,
                  long replyTimeInSeconds) {
        this.groupName = groupName;
        this.commands = commands;
        this.replyTimeInSeconds = replyTimeInSeconds;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getCommands() {
        return commands;
    }

    public long getReplyTimeInSeconds() {
        return replyTimeInSeconds;
    }
}
