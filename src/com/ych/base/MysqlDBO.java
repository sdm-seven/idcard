package com.ych.base;

public class MysqlDBO extends DBOperator {
    public MysqlDBO() {
        super(Config.getMysql_driver(), Config.getMysql_url(), Config
                .getMysql_user(), Config.getMysql_pwd());
    }
}