package com.zy.lib_arithmetic.huffman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class CommonUtils {


    private static String  readFile(String path){

        String result = "";
        File file = new File(path);
        if(!file.exists()){
            return result;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fis != null){
            int len;
            byte[] buffer = new byte[1024];
            try {
                while ((len = fis.read(buffer)) != -1){
                    bos.write(buffer,0,len);
                }
                bos.flush();
                result = new String(bos.toByteArray(), Charset.forName("UTF-8"));
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void writeFile(String path,String content){

        File file = new File(path);
        FileOutputStream fos = null;
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();

            fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void formatJson(String  str){



    }



}
