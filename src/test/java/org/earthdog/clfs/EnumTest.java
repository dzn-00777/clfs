package org.earthdog.clfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @Date 2024/10/20 14:39
 * @Author DZN
 * @Desc EnumTest
 */
public class EnumTest {

    public static void main(String[] args) {
        URI uri = URI.create("string:///com/dzn/Test.java");
        File file = new File(uri);
        System.out.println(file.getAbsolutePath());
        try {
            FileOutputStream byteCode = new FileOutputStream(file);
            byteCode.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
