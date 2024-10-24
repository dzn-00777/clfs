package org.earthdog.clfs;

import org.earthdog.clfs.loader.SourceLoaders;
import org.earthdog.clfs.loader.DefaultSourceLoader;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Date 2024/10/18 17:54
 * @Author DZN
 * @Desc Test
 */
public class Test {
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

    static DefaultSourceLoader sourceLoader = (DefaultSourceLoader) SourceLoaders.newStringSourceLoader();

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 测试共享类是否可以被其他group发现到
        System.out.println("测试共享类是否可以被其他group发现到");
        shareScopeClassTest();
        // 测试共享类加载是否冲突
        System.out.println("测试共享类加载是否冲突");
        shareClassTest();
        // 测试group之间是否有冲突
        System.out.println("测试group之间是否有冲突");
        groupClassTest();
        // 测试group内部分批load是否冲突
        System.out.println("测试group内部分批load是否冲突");
        groupInnerLoadClassTest();
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
        Object obj = sourceLoader.getObjByGroup("com.dzn", "com.dzn.UseShareClass");
        Method method1 = obj.getClass().getMethod("println");
        method1.invoke(obj);
    }

    private static void shareClassTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String shareClass1 = """
                package com.dzn;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class ShareClass1 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println("UseShareClass class println");
                        System.out.println();
                    }
                                
                }
                """;
        String shareClass2 = """
                package com.dzn;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class ShareClass2 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(ShareClass1.class);
                        System.out.println("UseShareClass class println");
                        System.out.println();
                    }
                                
                }
                """;

        ClassMetadata share1 = new ClassMetadata("com.dzn.ShareClass1", shareClass1);
        ClassMetadata share2 = new ClassMetadata("com.dzn.ShareClass2", shareClass2);

        Object o1 = sourceLoader.loadClass(share1);
        Method method1 = o1.getClass().getMethod("println");
        method1.invoke(o1);

        Object o2 = sourceLoader.loadClass(share2);
        Method method2 = o2.getClass().getMethod("println");
        method2.invoke(o2);

        Object o3 = sourceLoader.loadClass(share1);
        Method method3 = o3.getClass().getMethod("println");
        method3.invoke(o3);
    }

    private static void groupClassTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String group1Class1 = """
                package com.dzn.group1;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class Group1Test1 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println("Group1Test1 obj println...");
                        System.out.println();
                    }
                                
                }
                """;
        String group1Class2 = """
                package com.dzn.group1;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class Group1Test2 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(Group1Test1.class);
                        System.out.println("Group1Test2 obj println...");
                        System.out.println();
                    }
                                
                }
                """;

        String group2Class1 = """
                package com.dzn.group2;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class Group2Test1 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println("Group2Test1 obj println...");
                        System.out.println();
                    }
                                
                }
                """;
        String group2Class2 = """
                package com.dzn.group2;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class Group2Test2 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(Group2Test1.class);
                        System.out.println("Group2Test2 obj println...");
                        System.out.println();
                    }
                                
                }
                """;

        ClassMetadata group1ClassMetadata1 = new ClassMetadata("com.dzn.group1.Group1Test1", group1Class1);
        ClassMetadata group1ClassMetadata2 = new ClassMetadata("com.dzn.group1.Group1Test2", group1Class2);
        ClassMetadata group2ClassMetadata1 = new ClassMetadata("com.dzn.group2.Group2Test1", group2Class1);
        ClassMetadata group2ClassMetadata2 = new ClassMetadata("com.dzn.group2.Group2Test2", group2Class2);

        ClassMetadata[] classMetadata1s = new ClassMetadata[]{group1ClassMetadata1, group1ClassMetadata2};
        ClassMetadata[] classMetadata2s = new ClassMetadata[]{group2ClassMetadata1, group2ClassMetadata2};

        ClassMetadataGroup metadataGroup1 = new ClassMetadataGroup("com.dzn.group1", classMetadata1s);
        ClassMetadataGroup metadataGroup2 = new ClassMetadataGroup("com.dzn.group2", classMetadata2s);

        sourceLoader.loadClassBatch(metadataGroup1);
        Object o1 = sourceLoader.getObjByGroup("com.dzn.group1", "com.dzn.group1.Group1Test2");
        Method method1 = o1.getClass().getMethod("println");
        method1.invoke(o1);

        sourceLoader.loadClassBatch(metadataGroup2);
        Object o2 = sourceLoader.getObjByGroup("com.dzn.group2", "com.dzn.group2.Group2Test2");
        Method method2 = o2.getClass().getMethod("println");
        method2.invoke(o2);
    }

    private static void groupInnerLoadClassTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String groupClass1 = """
                package com.dzn.inner;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class GroupTest1 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println("GroupTest1 obj println...");
                        System.out.println();
                    }
                                
                }
                """;
        String groupClass2 = """
                package com.dzn.inner;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class GroupTest2 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(GroupTest1.class);
                        System.out.println("GroupTest2 obj println...");
                        System.out.println();
                    }
                                
                }
                """;
        String groupClass3 = """
                package com.dzn.inner;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class GroupTest3 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(GroupTest1.class);
                        System.out.println(GroupTest2.class);
                        System.out.println("GroupTest3 obj println...");
                        System.out.println();
                    }
                                
                }
                """;
        String groupClass4 = """
                package com.dzn.inner;
                import com.dzn.ShareClass;
                /**
                 * @Date 2024/10/18 18:00
                 * @Author DZN
                 * @Desc Test
                 */
                public class GroupTest4 {
                                
                    public void println() {
                        System.out.println(ShareClass.class);
                        System.out.println(GroupTest1.class);
                        System.out.println(GroupTest2.class);
                        System.out.println(GroupTest3.class);
                        System.out.println("GroupTest4 obj println...");
                        System.out.println();
                    }
                                
                }
                """;

        ClassMetadata groupClassMetadata1 = new ClassMetadata("com.dzn.inner.GroupTest1", groupClass1);
        ClassMetadata groupClassMetadata2 = new ClassMetadata("com.dzn.inner.GroupTest2", groupClass2);
        ClassMetadata groupClassMetadata3 = new ClassMetadata("com.dzn.inner.GroupTest3", groupClass3);
        ClassMetadata groupClassMetadata4 = new ClassMetadata("com.dzn.inner.GroupTest4", groupClass4);

        ClassMetadata[] classMetadata1s = new ClassMetadata[]{groupClassMetadata1, groupClassMetadata2};
        ClassMetadata[] classMetadata2s = new ClassMetadata[]{groupClassMetadata3, groupClassMetadata4};

        ClassMetadataGroup metadataGroup1 = new ClassMetadataGroup("com.dzn.inner", classMetadata1s);
        ClassMetadataGroup metadataGroup2 = new ClassMetadataGroup("com.dzn.inner", classMetadata2s);

        sourceLoader.loadClassBatch(metadataGroup1);
        Object o1 = sourceLoader.getObjByGroup("com.dzn.inner", "com.dzn.inner.GroupTest1");
        Method method1 = o1.getClass().getMethod("println");
        method1.invoke(o1);

        Object o2 = sourceLoader.getObjByGroup("com.dzn.inner", "com.dzn.inner.GroupTest2");
        Method method2 = o2.getClass().getMethod("println");
        method2.invoke(o2);

        sourceLoader.loadClassBatch(metadataGroup2);
        Object o3 = sourceLoader.getObjByGroup("com.dzn.inner", "com.dzn.inner.GroupTest3");
        Method method3 = o3.getClass().getMethod("println");
        method3.invoke(o3);

        Object o4 = sourceLoader.getObjByGroup("com.dzn.inner", "com.dzn.inner.GroupTest4");
        Method method4 = o4.getClass().getMethod("println");
        method4.invoke(o4);
    }
}
