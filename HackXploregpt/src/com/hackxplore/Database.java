package com.hackxplore;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connect(String dbPath) throws Exception {
    	Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
}

