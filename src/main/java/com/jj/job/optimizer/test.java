package com.jj.job.optimizer;

import com.jj.job.optimizer.analyse.analysies.AnalyseFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.jj.job.optimizer.analyse.analysies.AnalyseFactory.AnalyseType.JDealDataPS;

/**
 * Created by weizh on 2016/11/10.
 */
public class test {
    String a;
    test(){
        this.a="runbyself";
    }
    public static void main(String [] args ) {
        //String a = "asdfasdf";
        //System.out.println(a.substring(a.length()-1,a.length()));
        String a = "ASDFASD=ab = dsf ='ab";
        String [] arr = a.split("=");
        int b = 111;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String timeStr = "2016-12-06 00:00:00";

        try {
            long endStamp = df.parse(timeStr).getTime();
            long startStamp = endStamp - 24*3600*1000;
            System.out.println(df.format(startStamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(df.format(new Date()));


        System.out.println(b/100);
        test tt = new test();
        System.out.println(tt.a);
        String iI = AnalyseFactory.createAnalyse(JDealDataPS).itemInstr;
        System.out.println(iI);
    }
}
