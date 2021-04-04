package net.forgottenmc.equilibrium.database;

import java.util.List;

public interface DatabaseManager {
    DatabaseManager INSTANCE = new SQLiteDataSource();

    String getPrefix(long guildId);
    void setPrefix(long guildId, String newPrefix);
    void setPingRole(long guildId, long pingRoleId);
    void getPingRole(long guildId);
}
