package com.cecel.wfwpp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileOptions {

    /**
     * 向Android创建的文件中写入用户名和密码，两者之间用"#"分隔
     * @param out 用openFileOutput()方法生成的输出流
     * @param username 学号
     * @param password 密码
     */
    public static void saveToFile(FileOutputStream out, String username, String password){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(out));
            if (!username.isEmpty())
                writer.write(MD5Util.convertMD5(username+"#"+password));
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (writer !=null)
                    writer.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param in 用openFileInput()方法生成的输入流
     * @return 返回"学号#密码"字符串
     */
    public static String loadFromFile(FileInputStream in){
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
