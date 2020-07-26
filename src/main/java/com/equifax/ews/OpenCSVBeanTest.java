package com.equifax.ews;

import com.equifax.ews.model.UserBean;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OpenCSVBeanTest {

    public static void main(String[] args) throws IOException {

        Reader reader = Files.newBufferedReader(Paths.get( "C:/Dev/EWS-Project/readfile.txt"));
        CsvToBeanBuilder<UserBean> userBeanCsvToBeanBuilder = new CsvToBeanBuilder<>(reader);

        CsvToBean<UserBean> build = userBeanCsvToBeanBuilder
                .withType(UserBean.class)
                .withSeparator(',').withIgnoreQuotations(true)
                .withThrowExceptions(false) //1
                .build();

        final List<UserBean> users = build.parse();//2
        users.stream().forEach((user) -> {
            System.out.println("Parsed data:" + user.toString());
        });

        build.getCapturedExceptions().stream().forEach((exception) -> { //3
            System.out.println("Inconsistent data:" + String.join("", exception.getLine()));
        });
    }
}

