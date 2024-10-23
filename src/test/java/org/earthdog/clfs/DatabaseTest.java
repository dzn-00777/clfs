package org.earthdog.clfs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.earthdog.clfs.loader.SourceLoader;
import org.earthdog.clfs.loader.SourceLoaders;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Date 2024/10/23 17:39
 * @Author DZN
 * @Desc DatabaseTest
 */
public class DatabaseTest {
    static String shareClass = """
            package com.dzn;
                            
            /**
             * @Date 2024/10/18 18:00
             * @Author DZN
             * @Desc Test
             */
            public class ShareClass {
                            
                public void println() {
                    System.out.println("ShareClass class println");
                    System.out.println();
                }
                            
            }
                            
            """;

    static String useShareClass = """
            package com.dzn;
            import com.dzn.ShareClass;
            /**
             * @Date 2024/10/18 18:00
             * @Author DZN
             * @Desc Test
             */
            public class UseShareClass {
                            
                public void println() {
                    System.out.println(ShareClass.class);
                    System.out.println("UseShareClass class println");
                    System.out.println();
                }
                            
            }
                            
            """;

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        config.setJdbcUrl("jdbc:mysql://localhost:3306/clfs?useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("dzn");
        config.setPassword("156377");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }

    static SourceLoader sourceLoader = SourceLoaders.newDatabaseSourceLoader(ds);

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        shareScopeClassTest();
        System.out.println(System.currentTimeMillis() - start);
    }

    private static void shareScopeClassTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassMetadata share = new ClassMetadata("com.dzn.ShareClass", shareClass);
        ClassMetadata useShare = new ClassMetadata("com.dzn.UseShareClass", useShareClass);

        Object o = sourceLoader.loadClass(share);
        Method method = o.getClass().getMethod("println");
        method.invoke(o);

        ClassMetadata[] classMetadata = new ClassMetadata[]{useShare};
        ClassMetadataGroup classMetadataGroup = new ClassMetadataGroup("com.dzn", classMetadata);

        sourceLoader.loadClassBatch(classMetadataGroup);
//        Object obj = sourceLoader.getObjByGroup("com.dzn", "com.dzn.UseShareClass");
//        Method method1 = obj.getClass().getMethod("println");
//        method1.invoke(obj);
    }
}
