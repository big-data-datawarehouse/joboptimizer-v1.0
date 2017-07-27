package com.jj.job.optimizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jj.dbClient.MysqlJdbc;
import com.jj.job.optimizer.analyse.AnalyseResult;
import com.jj.job.optimizer.analyse.analysies.AnalyseFactory.AnalyseType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weizh on 2016/11/19.
 */
public class ScanData {
    String url = "jdbc:mysql://ip_instead_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();
    Gson gson = new Gson();
    public  String TimeRangeOrderByOneKey(String startStamp, String endStamp, String key) {
        String sql = "select id, " + key + " from plain_job.job where submitTime between " +
                startStamp + " and " + endStamp + " order by ABS(`" + key + "`) desc limit 30";
         List<Object> res = mj.output(url, sql);

        /*GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy( LongSerializationPolicy.STRING );
        Gson gson = gsonBuilder.create();*/
        /*for(Object list:res){
            List<Object> sublist = (List<Object>) list;
            for(Object o:sublist){
                System.out.println(o.getClass().getName());
            }
        }*/

        return gson.toJson(res);
        //return String.valueOf(res);
    }

    public List<String> TimeRangeJobList(String startStamp, String endStamp) {
        List<String> joblist = new ArrayList<String>();
        String sql = "select id from plain_job.job where submitTime between " +
                startStamp + " and " + endStamp ;
        List<Object> res = mj.output(url, sql);
        for(Object jb:res){
            String jobid = ((List<String>) jb).get(0);
            joblist.add(jobid);
            //System.out.println(jobid);
        }

        return joblist;
    }

    public String TransToChartsForm(String startStamp, String endStamp, String key){
        String srcData = TimeRangeOrderByOneKey(startStamp, endStamp, key);
        List<List<Object>> srclist =  (List<List<Object>>) gson.fromJson(srcData, List.class);
        List<Map<String,Object>> chartsList = new ArrayList<Map<String,Object>>();
        for(List<Object> tmplist:srclist){
            Map<String, Object> chartsMap = new HashMap<String, Object>();
            String jobid = (String) tmplist.get(0);
            Double value = (Double)tmplist.get(1);
            String url = "http://ip_instead_tmp:8080/jobview.jsp?jobid="+ jobid;
            chartsMap.put("name", jobid);
            chartsMap.put("y",value);
            chartsMap.put("url", url);
            chartsList.add(chartsMap);
        }


        return new GsonBuilder()
                .disableHtmlEscaping()
                .create().toJson(chartsList);

    }

    public Double getValFromDb(String id, String tablename, String column){
        String sql = "select " + column + " from " + tablename  +" where id='"+ id + "'";
        List<Object> res = mj.output(url, sql);
        if(res.size()<1){
            return Double.NEGATIVE_INFINITY;
        }
        List<Object> vallist = (List<Object>) res.get(0);

        //System.out.println("id is " + id + " column is " + column );
        if(vallist == null || vallist.size()<1){
            return Double.NEGATIVE_INFINITY;
        }
        Long val = (Long)vallist.get(0);
        if(val == null){
            return Double.NEGATIVE_INFINITY;
        }
        return (double) val;
    }

    public Object getObjFromDb(String id, String tablename, String column){
        String sql = "select " + column + " from " + tablename  +" where id='"+ id + "'";
        List<Object> res = mj.output(url, sql);
        List<Object> vallist = (List<Object>) res.get(0);
        Object val = vallist.get(0);
        return  val;
    }

    public List<String> getTaskListFromDb(String jobid){
        String sql = "select tasks from plain_job.job where id ='" + jobid + "'";
        List<Object> res = mj.output(url, sql);
        List<String> tasklist = gson.fromJson((( List<String>)res.get(0)).get(0), List.class);
        return tasklist;
    }

    /*public AnalyseResult getARfromMyDb(String item, String startStamp, String endStamp, String key){
        AnalyseResult ar = new AnalyseResult();
        ar.AnalyseItem = item;
        ar.


    }*/
    public AnalyseResult GetExtractedOrderByType(long startStamp, long endStamp, AnalyseType AnalyseType, boolean desc) {
        String sql = "";
        if(desc){
            sql = "select id, " + AnalyseType.toString() + " from plain_job.extracted where submitTime between " +
                    startStamp + " and " + endStamp + " order by ABS(`" + AnalyseType + "`) desc ";
        }else{
            sql = "select id, " + AnalyseType.toString() + " from plain_job.extracted where submitTime between " +
                    startStamp + " and " + endStamp + " order by ABS(`" + AnalyseType + "`) ";
        }

        List<Object> res = mj.output(url, sql);
        AnalyseResult ar = new AnalyseResult();
        int length = res.size();
        Double sum = 0D;
        int index = Math.max(1, length/100);
        int count = 0;
        int listSize = 30;

        List<Map<String,Object>> chartList = new ArrayList<Map<String,Object>>();
        for(Object jobObj:res){
            Map<String, Object> entryMap = new HashMap<String, Object>();
            if((String.valueOf((  (List<Object>) jobObj  ).get(1))).equals("null")){
                continue;
            }
            count++;
            String jobid = (String)((List<Object>) jobObj).get(0);
            System.out.println(jobid);
            String val = String.valueOf(((List<Object>) jobObj).get(1)) ;
            Double value = Double.parseDouble(val);
            sum+=value;
            if(count == index){
                ar.fractile99 = value;
            }
            if(count <= listSize ) {
                //String url = "http://instead_tmp:8080/jobview.jsp?jobid="+ jobid;
                String url = "http://instead_tmp:19888/jobhistory/job/" + jobid;
                entryMap.put("name", jobid);
                entryMap.put("y", value);
                entryMap.put("url", url);
                chartList.add(entryMap);
            }
        }

        ar.gsonString = new GsonBuilder().disableHtmlEscaping().create().toJson(chartList);
        ar.avg = sum/length;
        ar.dataNum = length;
        ar.AnalyseItem = AnalyseType.toString();
        ar.AnalyseItemType = AnalyseType;
        return ar;

    }

    public static void main(String [] args ) {
        ScanData sd = new ScanData();
        //String res = sd.TimeRangeOrderByOneKey("1479355194000", "1479541594000", "HDFS_BYTES_WRITTEN_totalCounterValue");
        //String res = sd.TransToChartsForm("1479355194000", "1479541594000", "runningTime");
        //String res = sd.TransToChartsForm("1479355194000", "1479541594000", "HDFS_BYTES_WRITTEN_totalCounterValue");
        //ystem.out.println(sd.TimeRangeJobList("1479455194000", "1479541594000").size());
        //System.out.println(res);
       /* List<String> res = sd.getTaskListFromDb("job_1480409780677_1982");
        for( String id:res){
            System.out.println(id);
        }*/
        long endT = System.currentTimeMillis();
        long startT = endT - 12*3600*1000;
        sd.GetExtractedOrderByType(startT,endT,AnalyseType.JRuningTime, true);
    }
}
