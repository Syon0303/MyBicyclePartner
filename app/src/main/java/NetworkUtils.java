package com.example.myDataWeather;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NetworkUtils {

    private static final String INFO_BASE_URL =
            "http://mysafeinfo.com/api/data?list=states&format=json";

    static String getMyInfo(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyMMdd");
        String strDate = formatter.format(date);
        System.out.println(strDate);

        //formatter 에는 현재 시간이 들어있음 (시간만)
        formatter = new SimpleDateFormat("HH00");
        String strTime=formatter.format(date);

        String baseTimes[]={"0200", "0500", "0800", "1100", "1400", "1700", "2000", "2300"};

        //가장 가까운시간을 찾아냄
        for(int i=0; i<baseTimes.length; i++){
            if(baseTimes[i].compareTo(strTime)<0) continue;

            //넘어가는 시간 바로 이전 baseTime 을 strTime 에 지정함
            strTime = baseTimes[i-1];
            break;
        }
        String myJSONString = null;

        try{
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst");
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + "BHHqztBHjQQDyBxjy1XO4gCu1bDDyT13vR6PN9W%2BmSyTINtKvg828xJ0MrK7katOhB9uTarEmbmE6JTz%2B9CZHw%3D%3D");
//            urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-","UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10","UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1","UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON","UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(strDate,"UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(strTime,"UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("86","UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("96","UTF-8"));
            Log.e("**********",urlBuilder.toString());
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode()>= 200 && conn.getResponseCode()<=300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else{
                rd=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while((line=rd.readLine())!=null){
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            myJSONString=sb.toString();
        }   catch(Exception e){
            e.printStackTrace();
        }
        //Write the final response
        return myJSONString;
    }
}
