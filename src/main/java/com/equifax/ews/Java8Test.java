package com.equifax.ews;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class Java8Test {

    public static void main(String[] args) throws IOException {
       // Path path = FileSystems.getDefault().getPath("C:\\Dev", "LenderIssue.gz");
    //        Path tempFile = Files.createTempFile("one", ".gz");
        Path tempFile = Files.createTempDirectory("test-dir");
        //method 1
        boolean is_regular = Files.isRegularFile(tempFile);
        System.out.println("Regular : " +is_regular);
        String name = "arun.txt.gpg";
        String fn =  name.substring(0, name.indexOf("gpg"));
        System.out.println("File Name is : " +fn);
    }

}
