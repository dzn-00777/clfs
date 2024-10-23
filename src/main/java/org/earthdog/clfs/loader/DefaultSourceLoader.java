package org.earthdog.clfs.loader;

import org.earthdog.clfs.classloader.StringClassLoader;
import org.earthdog.clfs.conf.Config;
import org.earthdog.clfs.metadata.ClassMetadata;
import org.earthdog.clfs.metadata.ClassMetadataGroup;
import org.earthdog.clfs.stringcomplie.SourceJavaFileManager;
import org.earthdog.clfs.stringcomplie.SourceSimpleJavaFileObject;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @Date 2024/7/7 12:00
 * @Author DZN
 * @Desc StringSourceLoader 从字符串中加载类 JavaCompile.CompilationTask不支持使用线程池提交 javac内部已经支持多线程编译
 */
public class DefaultSourceLoader extends AbstractSourceLoader {
    private final JavaCompiler compiler;
    private final SourceJavaFileManager fileManager;
    private final StandardJavaFileManager manager;

    public DefaultSourceLoader() {
        // 获取Java编译器
        compiler = ToolProvider.getSystemJavaCompiler();
        // 获取标准的Java文件管理器实例
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, Locale.CHINA, StandardCharsets.UTF_8);
        this.manager = manager;
        // 添加SharedClass到classPath, classOutPath里
        Iterable<? extends File> files = manager.getLocation(StandardLocation.CLASS_PATH);
        ArrayList<File> classPath = new ArrayList<>();
        for (File file : files) {
            classPath.add(file);
        }
        File file = new File(CLASS_OUTPUT_PATH + "common");
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        classPath.add(file);
        try {
            manager.setLocation(StandardLocation.CLASS_PATH, classPath);
            manager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singletonList(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 初始化自定义Java文件管理器实例
        fileManager = new SourceJavaFileManager(manager);
    }

    @Override
    public Object loadClass(ClassMetadata classMetadata) {
        String code = classMetadata.getCode();
        String qualifiedName = classMetadata.getQualifiedName();

        ClassLoader classLoader = StringClassLoader.DefaultGroupClassLoader.getInstance(fileManager);

        classMetadata.setClassLoader(classLoader);
        fileManager.setClassLoader(classLoader);

        SourceSimpleJavaFileObject javaFileObject = new SourceSimpleJavaFileObject(qualifiedName, code, classLoader, DEFAULT_GROUP);
        // 添加Java源文件实例到自定义Java文件管理器实例中
        fileManager.addJavaFileObject(qualifiedName, javaFileObject);
        // 初始化一个编译任务实例并执行
        executeCompileTask(Collections.singletonList(javaFileObject), DEFAULT_GROUP);

        Object obj = loadClass(classLoader, qualifiedName);
        saveSingleObj(qualifiedName, obj);

        fileManager.removeClassLoader();
        return obj;
    }

    @Override
    public void loadClassBatch(ClassMetadataGroup classMetadataGroup) {
        String groupName = classMetadataGroup.getGroupName();
        ClassLoader classLoader = fileManager.getClassLoader(groupName);
        if (classLoader == null) {
            classLoader = new StringClassLoader(fileManager, groupName);
        }
        classMetadataGroup.setClassLoader(classLoader);
        loadClassBatch0(classMetadataGroup);
    }

    @Override
    public Config getConfig() {
        return null;
    }

    public byte[] getByteCode(String groupName, String qualifiedName) {
        SourceSimpleJavaFileObject javaFileObject = (SourceSimpleJavaFileObject) fileManager.getJavaFileObjByGroup(groupName, qualifiedName);
        return javaFileObject.getByteCode();
    }

    private void loadClassBatch0(ClassMetadataGroup classMetadataGroup) {
        ClassLoader classLoader = classMetadataGroup.getClassLoader();
        String groupName = classMetadataGroup.getGroupName();

        fileManager.setGroup(groupName, classLoader);
        ClassMetadata[] classMetadataArray = classMetadataGroup.getClassMetadataArray();

        Map<String, JavaFileObject> map = new HashMap<>(classMetadataArray.length);
        ArrayList<JavaFileObject> list = new ArrayList<>(classMetadataArray.length);
        for (ClassMetadata classMetadata : classMetadataArray) {
            SourceSimpleJavaFileObject javaFileObject = new SourceSimpleJavaFileObject(classMetadata.getQualifiedName(), classMetadata.getCode(), classLoader, groupName);
            map.put(classMetadata.getQualifiedName(), javaFileObject);
            list.add(javaFileObject);
        }
        fileManager.addJavaFileObjectByGroup(groupName, map);

        executeCompileTask(list, groupName);

        Map<String, Object> objectMap = new HashMap<>(classMetadataArray.length);
        for (ClassMetadata classMetadata : classMetadataArray) {
            objectMap.put(classMetadata.getQualifiedName(), loadClass(classLoader, classMetadata.getQualifiedName()));
        }
        saveObjs(groupName, objectMap);

        saveJavaFiles(list, groupName);

        fileManager.removeGroup();
    }

    @SuppressWarnings("unused")
    private void saveJavaFiles(ArrayList<JavaFileObject> list, String groupName) {
        for (JavaFileObject fileObject : list) {
            SourceSimpleJavaFileObject source = (SourceSimpleJavaFileObject) fileObject;
            String code = source.getCharContent(false).toString();
            String qualifiedName = source.getName();
            String filePath = SOURCE_PATH + groupName + "\\" + qualifiedName.replace(".", "\\") + JAVA_EXTENSION;
            saveJavaFile(code, filePath);
        }
    }

    private void saveJavaFile(String code, String filePath) {
        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            Files.writeString(path, code);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeCompileTask(List<JavaFileObject> list, String groupName) {
        try {
            File f = new File(SOURCE_PATH + groupName);
            if (!f.exists()) {
                //noinspection ResultOfMethodCallIgnored
                f.mkdirs();
            }
            manager.setLocation(StandardLocation.SOURCE_PATH, List.of(f));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JavaCompiler.CompilationTask compilationTask = compiler.getTask(
                null,
                fileManager,
                null,
                null,
                null,
                list
        );
        // 执行编译任务
        compilationTask.call();
    }
}
