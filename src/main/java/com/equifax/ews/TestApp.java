package com.equifax.ews;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TestApp {

    public static void main(String[] args) {
        String s = "{\n" +
                "\"query\": \"--insert into `bq_test.transaction` values(1, \\\"Success\\\"),(2, \\\"Success\\\"),(" +
                "3, \\\"Failure\\\"),(4, \\\"Failure\\\");\\n\\nselect IFNULL(CAST(transactionId AS STRING), NULL) AS TID, " +
                "IFNULL(CAST(status AS STRING), '') as TSTATUS\\nfrom" +
                " `bq_test.transaction` where status='Success';\\n\\n--select IFNULL(CAST(transactionId AS STRING), NULL) AS TID, " +
                "IFNULL(CAST(status AS STRING), '') as TSTATUS\\n--from `bq_test.transaction` where status='Failure'" +
                "AND tboc.TRANSACTION_DATE >= '01/16/2020 06:00:00' AND tboc.TRANSACTION_DATE <= '01/16/2020 16:58:35';\"\n" +
                "}";

        JSONObject jsonObject = new JSONObject(s);
        String query = (String) jsonObject.get("query");
        String substring = query.substring(query.indexOf("AND tboc.TRANSACTION_DATE"));
        String[] split = substring.split("AND tboc.TRANSACTION_DATE");

        for(String str : split) {
            System.out.println("After splitting :"+str.replaceAll("[><=']",""));
        }

        System.out.println("After substring :"+substring);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        String gmtDateStr = sdf.format(instance.getTime());

        System.out.println("Zone :" +instance.getTimeZone());
        System.out.println("Before Zone change " +gmtDateStr);

        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
        System.out.println("after Zone change " +sdf.format(instance.getTime()));


    }
}
