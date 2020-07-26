package com.equifax.ews;

public class DateTimeFunctions {

    public static void main(String[] args) {
        //dateTimeSplit();
        stringTest();
    }

    public static void dateTimeSplit() {
        String query = "SELECT * FROM ews-ss-de-npe-1c88.de_dw_qa.TWN_BATCHONCLOUD where TRANSACTION_ID is not null AND TRANSACTION_DATE >= cast('2020-01-16 06:00:00' as DATETIME) AND TRANSACTION_DATE <= cast('2020-01-17 16:58:51' as DATETIME)";
        String[] casts = query.split("cast\\('");

        for (String str : casts) {
            //int i = str.indexOf("'");
            String substring = str.substring(0, str.lastIndexOf("'"));
            System.out.println(substring);
        }
    }

    public static void stringTest() {

        String str = "This is a test";
        String s = str.toUpperCase();
        System.out.println("After upper case :" +s);
        System.out.println("Original str :" +str);
    }
}
