/*
 * This file is part of the SwissKnife plugin distribution  (https://github.com/EgirlsNationDev/SwissKnife).
 * Copyright (c) 2022 Egirls Nation Development
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GPL-3.0 License.
 *
 * You should have received a copy of the GPL-3.0
 * License along with this program.  If not, see
 * <https://opensource.org/licenses/GPL-3.0>.
 */

package com.egirlsnation.swissknife.systems.sql;

import com.egirlsnation.swissknife.utils.OldConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);
    }

    public void connect() throws ClassNotFoundException, SQLException{
        if(!isConnected()){
            connection = DriverManager.getConnection("jdbc:mysql://" + OldConfig.instance.databaseHost + ":" + OldConfig.instance.databasePort + "/" + OldConfig.instance.databaseName + "?useSSL=false", OldConfig.instance.databaseUsername, OldConfig.instance.databasePassword);
        }
    }

    public Connection getConnection(){
        return connection;
    }


    public void disconnect(){
        if(!isConnected()) return;
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
