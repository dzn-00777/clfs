package org.earthdog.clfs;

import org.earthdog.clfs.conf.Config;
import org.earthdog.clfs.enums.DataSourceType;
import org.earthdog.clfs.init.DatabaseInitialization;
import org.earthdog.clfs.init.FileInitialization;
import org.earthdog.clfs.init.Initialization;

/**
 * @Date 2024/10/23 13:09
 * @Author DZN
 * @Desc InitTest
 */
public class InitTest {

    public static void main(String[] args) {
        Initialization initializationFile = new FileInitialization();
        initializationFile.init();

        Initialization initializationDatabase = DatabaseInitialization.getInstance(DataSourceType.MYSQL, new Config());
        initializationDatabase.init();
    }

}
