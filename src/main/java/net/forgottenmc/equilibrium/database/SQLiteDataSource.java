package net.forgottenmc.equilibrium.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.forgottenmc.equilibrium.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLiteDataSource implements DatabaseManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
    private final HikariDataSource ds;

    public SQLiteDataSource() {
        try {
            final File dbFile = new File("database.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    LOGGER.info("Created database file");
                } else {
                    LOGGER.info("Could not create database file");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Config.get("db_url"));
        config.setConnectionTestQuery("SELECT 1");
        config.setUsername(Config.get("db_username"));
        config.setPassword(Config.get("db_password"));
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);

        try (final Connection connection = getConnection()){
            final Statement statement = connection.createStatement();
            final String defaultPrefix = Config.get("prefix");


            // language=MySQL
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (" +
                    "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "guild_id VARCHAR(20) NOT NULL," +
                    "prefix VARCHAR(255) NOT NULL DEFAULT '" + defaultPrefix + "'" +
                    ");");



            System.out.println("Table initialised");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPrefix(long guildId) {
        try (Connection connection = getConnection()) {
            final PreparedStatement preparedStatement = connection
                // language=MySQL
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?");

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = getConnection()
                    // language=MySQL
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }


    @Override
    public void setPrefix(long guildId, String newPrefix) {
        try (Connection connection = getConnection()) {
            final PreparedStatement preparedStatement = connection
                // language=MySQL
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?");

            //
            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


}