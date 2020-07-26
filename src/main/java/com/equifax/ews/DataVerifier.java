package com.equifax.ews;

public class DataVerifier {

    private static String lookAhead = "(?=\")";
    private static String lookBehind = "(?<=\")";
    private static String delimiter = ",";
    private static String rowdata =
            "\"27524\",\"999913210\",715,01/10/2020 16:53:16,01/10/2020 16:53:16,79,14,10046671,\"\",\"Tomy\",\"Night\",960090,\"Batch\",\"DEMO A\",\"\",\"\",\"999913210\",\"Y\",\"Batch\",\"A\",715,\"\",\"\",0,6,23,23,0,\"\",\"N\",\"\",\"test-27524\",716,,\"creditKarma@50005\",01/07/2020 17:19:07,01/07/2020 17:19:07,\"\",\"999913210\",1505755,1799929,\"\",\"CreditKarma-BATCH\",\"Per TRX Pricing\",\"\",\"Credit Karma\",\"\",\"PPCREDIT\",1,\"Twn\",\"\",\"\",\"\",\"B\",,\"\",\"\",,\"\",\"\",\"\",\"\"<?xml version=\"\"1.0\"\"?><flags><flag>7</flag><flag>21</flag><flag>44</flag><flag>62</flag><flag>74</flag><flag>105</flag><flag>124</flag><flag>145</flag><flag>220</flag><flag>205</flag><flag>206</flag></flags>\"\",,\"\",\"\"<?xml version=\"\"1.0\"\"?><subcodes><subcode>11</subcode><subcode>47001</subcode><subcode>401</subcode><subcode>48000</subcode><subcode>48100</subcode><subcode>48101</subcode><subcode>48102</subcode></subcodes>\"\",0,\"\",,1,1,3,\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",,\"\"";

    //String rowdata = "\"13943681\"|\"3085\"|\"999505206\"|\"00002202\"|\"ANTHONY\"|\"JOE1\"|\"\"|\"||||1850 BO|RMAN# COUR|\"|\"|  |185-0 BO|.....RMAN# COUR|\"|\"|MA RS|HALL||\"|\"19\"|\"32303\"|\"US\"|\"\"|\"1972-08-02 00:00:00\"|\"\"|\"\"|\"\"|\"\"|\"13325122\"|\"11372264\"|\"447\"|\"2017-09-25 23:06:30\"|\"\"|\"\"|\"\"|\"2010-07-20 16:04:16\"|\"\"|\"N\"|\"Y\"|\"5555551234\"|\"\"|\"2010-07-20 16:04:16\"|\"2010-06-11 00:00:00\"|\"\"|\"2017-09-25 23:06:30\"|\"2017-09-25 23:06:30.934389000\"|\"i999505206\"|\"2017-09-25 23:06:30\"|\"h999505206\"|\"2017-09-25 23:06:30\"|\"a999505206\"|\"2017-09-25 23:06:30\"|\"K\"|\"2017-09-25 23:06:30\"\u000B";
    //String rowdata = "\"abc\"|\"|\"|\"acx\"";
    //String rowdata = "\"abc|\"|\"|\"|\"|acx\"";
    //String rowdata = "\"abc|| |\"|\"|\"|\"| ||\"|acx\"";
    //String rowdata = "\"|  ||abc\"|||\"|\"|\"acx\"";
    //String rowdata =  "\"a\"\\|\"\\|\"|||\"\"|||\"|\"";
    //String rowdata =  "abc"|"|"|"acx"
    //String rowdata =  "abc|"|"|"|"|acx"
    //String rowdata =  "abc|| |"|"|""| ||"|acx"

    public static void main(String[] args) {
        testData();
    }

    private static void testData() {
        /*String newData = rowdata.replaceAll("\"\\|\"\\|\"\\|\"", "\"\\|\" \\|\"\\|\"");
        String data[] = newData.
                split(lookBehind + delimiter + lookAhead, -1);*/
        String data[] = rowdata.split(delimiter);
        System.out.println("*******************************************");
        System.out.println("There are "+data.length+" columns in this table");
        System.out.println("*******************************************");
        int counter = 0;
        for (String d : data) {
            counter++;
            System.out.println(counter + " : " + d);
        }

    }
}
