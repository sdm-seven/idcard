package com.ych.base;

public class MssqlDBO extends DBOperator {
    public MssqlDBO() {
        super(Config.getMssql_driver(), Config.getMssql_url(), Config
                .getMssql_user(), Config.getMssql_pwd());
    }
}
