package ru.sfedu.comicsShop.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static ru.sfedu.comicsShop.Constants.*;

public class SqlUtil {
    private static final Logger log = LogManager.getLogger(SqlUtil.class.getName());
    private  static Connection connection;

    public static Connection connectToMySql() {

        try {
            String url = ConfigurationUtil.getConfigurationEntry(URL_SQL_CONNECTION);
            String username = ConfigurationUtil.getConfigurationEntry(USER_SQL_CONNECTION);
            String password = ConfigurationUtil.getConfigurationEntry(PASSWORD_SQL_CONNECTION);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error(e);
        }

        Statement statement = null;
        try {statement = connection.createStatement();} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_DATABASE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_ITEM_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_USER_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_CART_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_ORDER_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_PROMO_CODE_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        try {statement.executeUpdate(CREATE_GIFT_CERTIFICATE_TABLE_SQL_CONNECTION);} catch (Exception ignored){}
        return connection;
    }
}

