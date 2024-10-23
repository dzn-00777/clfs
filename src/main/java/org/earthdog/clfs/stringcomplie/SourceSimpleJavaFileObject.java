package org.earthdog.clfs.stringcomplie;

import org.earthdog.clfs.loader.SourceLoader;

import javax.tools.SimpleJavaFileObject;
import java.io.*;
import java.net.URI;

/**
 * @Date 2024/7/7 10:48
 * @Author DZN
 * @Desc SimpleJavaFileObjectOfString
 */
public class SourceSimpleJavaFileObject extends SimpleJavaFileObject {
    private ByteArrayOutputStream byteCode;
    private final CharSequence sourceCode;
    private final ClassLoader classLoader;
    private final String qualifiedName;
    private final String groupName;

    public SourceSimpleJavaFileObject(String qualifiedName, CharSequence sourceCode, ClassLoader classLoader, String groupName) {
        super(URI.create("string:///" + qualifiedName.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.sourceCode = sourceCode;
        this.classLoader = classLoader;
        this.qualifiedName = qualifiedName;
        this.groupName = groupName;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return sourceCode;
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(getByteCode());
    }

    // 注意这个方法是编译结果回调的OutputStream，回调成功后就能通过下面的getByteCode()方法获取目标类编译后的字节码字节数组
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public OutputStream openOutputStream() {
//        byteCode = new ByteArrayOutputStream();
//        return byteCode;
        try {
            String relativePath = groupName + "/" + qualifiedName.replace(".", "/") + Kind.CLASS.extension;
            File file = new File(SourceLoader.CLASS_OUTPUT_PATH + relativePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            byteCode = new FileByteArrayOutputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteCode;
    }

    public byte[] getByteCode() {
        return byteCode.toByteArray();
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public String getName() {
        return qualifiedName;
    }

    static class FileByteArrayOutputStream extends ByteArrayOutputStream {
        private final FileOutputStream fileOutputStream;

        public FileByteArrayOutputStream(File file) throws FileNotFoundException {
            this.fileOutputStream = new FileOutputStream(file);
        }

        @Override
        public void write(byte[] b, int off, int len) {
            try {
                fileOutputStream.write(b, off, len);
                super.write(b, off, len);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

/*
        @Override
        public void write(byte[] b) throws IOException {
            fileOutputStream.write(b);
            super.write(b);
        }
*/

        @Override
        public void write(int b) {
            try {
                fileOutputStream.write(b);
                super.write(b);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
