package com.zhy.inject_compile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class FileUtils {

    public static void main(String[] args){
        print("111");
    }
    public static void print(String message)  {
        Objects.isNull(message);
        FileWriter outputStream;
        File file = new File("/Users/yedongqin/desktop/print.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream = new FileWriter(file.getAbsolutePath(),true);
            outputStream.write(message);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("  dfdfd " + "fddd");
    }


}
