package com.ych.base;

public class OracleDBO extends DBOperator {
    public OracleDBO() {
        super(Config.getOracle_driver(), Config.getOracle_url(), Config
                .getOracle_user(), Config.getOracle_pwd());
    }
}
