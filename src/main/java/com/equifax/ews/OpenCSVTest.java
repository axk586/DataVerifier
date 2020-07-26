package com.equifax.ews;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvMalformedLineException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class OpenCSVTest {

    public static void main(String[] args) throws IOException, URISyntaxException, CsvException {

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .build();

        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            Reader reader = Files.newBufferedReader(Paths.get( "C:/Dev/EWS-Project/readfile.txt"));
            prop.load(input);
            Path path = Paths.get( prop.getProperty("writefile"));
            int i=0;
            String[] records = {};
            try ( CSVReader csvReader = new CSVReaderBuilder(reader)
                         .withCSVParser(parser)
                         .build();
                 CSVWriter writer = new CSVWriter(new FileWriter(path.toString())))
            {
                while ((records = csvReader.readNext()) != null) {
                    i++;
                    writer.writeNext(records);
                }
                if(records != null && records.length > 0) {
                    System.out.println("Find the next id : " + records[0]);
                } else {
                    System.out.println("**NEW**" + records);
                }
            } catch (CsvMalformedLineException e) {
                System.out.println("Number of records processed :"+i);
                if(records != null && records.length > 0) {
                    System.out.println("Find the next id : " + records[0]);
                }
                System.out.println("Print error" + e);
            }
        }
    }
}
