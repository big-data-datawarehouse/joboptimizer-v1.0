package com.jj.job.optimizer.analyse;

import com.google.gson.GsonBuilder;
import com.jj.job.optimizer.ScanData;
import com.jj.job.optimizer.analyse.analysies.AnalyseDisplayer;
import com.jj.job.optimizer.analyse.analysies.AnalyseFactory;
import com.jj.job.optimizer.analyse.analysies.AnalyseFactory.AnalyseType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by weizh on 2016/11/25.
 */

public class Analyse {


    public AnalyseResult getAnalyseResult(final AnalyseType at, List<String> idList, boolean desc){
        final PriorityBlockingQueue<Map.Entry<String,Double>> pq;
        int length = idList.size();


        if( length == 0 ) {
            return null;
        }
        if(desc){
            pq = new PriorityBlockingQueue(length, new Comparator<Map.Entry<String,Double>>() {
                public int compare(Map.Entry<String,Double> o1, Map.Entry<String,Double> o2) {
                    if((o2.getValue()- o1.getValue())>0){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }else{
            pq = new PriorityBlockingQueue(length, new Comparator<Map.Entry<String,Double>>() {
                public int compare(Map.Entry<String,Double> o1, Map.Entry<String,Double> o2) {
                    if(o2.getValue() <= o1.getValue()){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
        }
        final AnalyseDisplayer ad = AnalyseFactory.createAnalyse(at);
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(500);
        for (final String id:idList) {
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    double value = Double.NEGATIVE_INFINITY;
                    Map<String, Double> tmpMap = new HashMap<String, Double>();
                    value = ad.Analyse(id);
                    if( value != Double.NEGATIVE_INFINITY){
                        tmpMap.put(id, value);

                        for(Map.Entry<String, Double> entry:tmpMap.entrySet()){
                            pq.offer(entry);
                        }
                    }



                }
            });
        }
        fixedThreadPool.shutdown();
        try {
            while (!fixedThreadPool.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Not yet. Still waiting for termination");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("come here " +pq.size());
        List<Map<String,Object>> chartList = new ArrayList<Map<String,Object>>();
        AnalyseResult ar = new AnalyseResult();

        double sum = 0;
        int index = Math.max(1, length/100);
        int count = 1;
        int listSize = 30;
        while(!pq.isEmpty()){
            Map<String, Object> entryMap = new HashMap<String, Object>();
            Map.Entry<String, Double> entry = pq.poll();
            String id = entry.getKey();
            double value = entry.getValue();
            if(count == index){
                ar.fractile99 = value;
            }
            sum += value;
            if(count <= listSize ) {
                String url = "http://instead_tmp:8080/jobview.jsp?jobid="+ id;
                entryMap.put("name", id);
                entryMap.put("y", value);
                entryMap.put("url", url);
                chartList.add(entryMap);
            }
            count++;
        }


        ar.gsonString = new GsonBuilder().disableHtmlEscaping().create().toJson(chartList);
        ar.avg = sum/length;
        ar.dataNum = length;
        ar.AnalyseItem = at.toString();
        ar.AnalyseItemType = at;


        return ar;

    }

    public List<AnalyseResult> summary(String timeStrOut){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = "2016-12-05 00:00:00";
        if(timeStrOut!=""){
            timeStr = timeStrOut;
        }


        List<AnalyseResult> arList = new ArrayList<AnalyseResult>();
        //long endT = System.currentTimeMillis() - (84*3600*1000) ;
        //long endT = 1480694400000L;
        long endT = 0L;
        try {
            endT = df.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startT = endT  - (24*3600*1000);
        List<String> joblist = new ScanData().TimeRangeJobList(String.valueOf(startT), String.valueOf(endT));
        String begTime = df.format(new Date());
        arList.add(new Analyse().getAnalyseResult(AnalyseType.JRuningTime, joblist, true));
        System.out.println(df.format(new Date()) + " JDealDataPs finished!");
        arList.add(new Analyse().getAnalyseResult(AnalyseType.JDelayTime, joblist, true));
        System.out.println(df.format(new Date()) + " JDelayTime finished!");
        arList.add(new Analyse().getAnalyseResult(AnalyseType.JShuffleBytes, joblist, true));
        System.out.println(df.format(new Date()) + " JShuffleByates finished!");
        arList.add(new Analyse().getAnalyseResult(AnalyseType.JDealDataPS, joblist, false));
        System.out.println(df.format(new Date()) + " JDealDataPs finished!");
        arList.add(new Analyse().getAnalyseResult(AnalyseType.TByteReadAvg, joblist, true));
        System.out.println(df.format(new Date()) + " TByteReadAvg finished!");
        arList.add(new Analyse().getAnalyseResult(AnalyseType.TRunTimeDev, joblist, true));
        System.out.println(df.format(new Date()) + " TRunTimeDev finished!");
        System.out.println("Begin at " + begTime);
        return arList;
    }

    public List<AnalyseResult> summaryFormDb(String timeStrOut){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = "2016-12-05 00:00:00";
        if(timeStrOut!=""){
            timeStr = timeStrOut;
        }
        List<AnalyseResult> arList = new ArrayList<AnalyseResult>();
        //long endT = System.currentTimeMillis() - (84*3600*1000) ;
        //long endT = 1480694400000L;
        long endT = 0L;
        try {
            endT = df.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String begTime = df.format(new Date());
        ScanData sd = new ScanData();
        long startT = endT  - (1*24*3600*1000);
        for(AnalyseType at:AnalyseType.values()) {
            boolean desc = true;
            if(at.equals(AnalyseType.JDealDataPS) || at.equals(AnalyseType.TByteReadAvg)){
                desc = false;
            }
            arList.add(sd.GetExtractedOrderByType(startT, endT, at, desc));
            System.out.println(df.format(new Date()) + " " + at.name() +" finished!");
        }
        System.out.println("Begin at " + begTime);
        return arList;
    }

    public static void main(String [] args ){
        /*String jobid="job_1480409780677_3103";
        Long res = new Analyse().TaskRunTimeDeviation(jobid);
        res = new Analyse().TaskByteReadDeviation(jobid);
        res = new Analyse().TaskByteReadAvg(jobid);
        System.out.println(res);*/
        long endT = System.currentTimeMillis() ;
        long startT = endT  - (24*3600*1000);
        /*List<String> joblist = new ScanData().TimeRangeJobList(String.valueOf(startT), String.valueOf(endT));
        System.out.println("joblist size "+joblist.size());
        AnalyseResult ar = new Analyse().getAnalyseResult(AnalyseType.JDealDataPS, joblist, true);
        System.out.println("res " + ar.gsonString);*/
        new Analyse().summaryFormDb("2016-12-03 00:00:00");





    }
}
