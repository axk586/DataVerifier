package com.equifax.ews;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApacheCSVTest {

    public static void main(String[] args) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get( "C:/Dev/EWS-Project/readfile.txt"));
        List<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter('|').parse(reader).getRecords();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("C:/Dev/EWS-Project/writefile.txt"));

        List<List<String>> recordData = new ArrayList<>();
        for (CSVRecord record : records) {
            List<String> recordDatas = new ArrayList<>();
            Iterator var2 = record.iterator();
            while(var2.hasNext()) {
                recordDatas.add((String) var2.next());
            /* System.out.print((String) var2.next());
                System.out.print(",");*/
            }
            recordData.add(recordDatas);
//            System.out.println();
        }
        for(List<String> rec : recordData) {
            for(int i=0;i<rec.size();i++) {
                System.out.println(rec.get(0));
                writer.write(rec.get(0));
                writer.newLine();
            }
            writer.flush();
        }

    }
}
