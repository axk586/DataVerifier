package com.equifax.ews;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuperCSVTest {

    public static void main(String[] args) {

        CsvPreference csvPreference = new CsvPreference.Builder('"', '|', "\r\n")
                .useQuoteMode(new AlwaysQuoteMode()).build();

        try(ICsvListReader iCsvListReader = new CsvListReader(new FileReader("C:/Dev/EWS-Project/readfile.txt"),
                csvPreference))
        {
            List<String> rowData;
            while ((rowData = iCsvListReader.read()) != null) {
                for(String data : rowData) {
                    System.out.println("Data : "+data);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
