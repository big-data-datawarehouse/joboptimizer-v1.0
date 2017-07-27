package com.jj.jspserver;

import com.jj.job.optimizer.HiveQueryLogParser;

import java.util.List;

/**
 * Created by weizh on 2016/11/11.
 */
public class test {
    public static void main(String [] args ){
        HiveQueryLogParser hqlp = new HiveQueryLogParser();
        List<String> log = hqlp.FileToList("D:\\hivelog.txt");
        String contents = hqlp.LogParser(log);
    }
}
