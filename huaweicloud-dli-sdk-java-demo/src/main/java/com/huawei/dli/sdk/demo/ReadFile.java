package com.huawei.dli.sdk.demo;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFile {
  public static void main(String[] args) {
    try {
      FileInputStream inputStream = new FileInputStream("D:\\sdk\\ad445ee7-7f34-4a55-b662-5ddd4317efab");
      //FileInputStream inputStream = new FileInputStream("D:\\sdk\\test.txt");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String str = null;
      int line = 0;
      while ((str = bufferedReader.readLine()) != null) {
        int count = 0;
        Pattern pattern = Pattern.compile(",");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
          count++;
        }

        int count2 = 0;
        Pattern pattern2 = Pattern.compile("\\\\,");
        Matcher matcher2 = pattern2.matcher(str);
        while (matcher2.find()) {
          count2++;
        }

        line++;
        if ((count - count2) < 63) {
          System.out.println("Line " + line + " have " + (count - count2) + "： " + str);
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
