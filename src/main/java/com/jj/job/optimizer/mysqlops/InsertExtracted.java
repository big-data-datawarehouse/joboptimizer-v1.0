package com.jj.job.optimizer.mysqlops;

import com.jj.dbClient.MysqlJdbc;
import com.jj.job.optimizer.ScanData;
import com.jj.job.optimizer.analyse.analysies.AnalyseFactory;
import com.jj.job.optimizer.analyse.analysies.AnalyseFactory.AnalyseType;

import java.util.*;

/**
 * Created by weizh on 2016/12/8.
 */
public class InsertExtracted {
    String url = "jdbc:mysql://instead_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();


    public void Insert(){

        long endT = System.currentTimeMillis();
        long startT = endT - 20*24*3600*1000;

        ScanData sd = new ScanData();
        List<String> joblist = sd.TimeRangeJobList(String.valueOf(startT), String.valueOf(endT));
        System.out.println(" joblist length : " + joblist.size());
        for(String jobid:joblist){
            //System.out.println("jobid is : " + jobid ) ;
            String probeSql = "select * from plain_job.extracted where id='" + jobid +"'";
            List<Object> haveRes = mj.output(url, probeSql);
            if(!haveRes.isEmpty()){
                //System.out.println("Job " + jobid + " already exist in DB!");
                continue;
            }
            Map<String, Object> mysqlColumn = new HashMap<String, Object>();
            String submitTime = (String) sd.getObjFromDb(jobid, "plain_job.job", "submitTime");
            if(submitTime == null || submitTime.equals("")){
                continue;
            }


            for(AnalyseType at: AnalyseType.values()){
                //System.out.println(at.name());
                Double value = AnalyseFactory.createAnalyse(at).Analyse(jobid);
                if( value == Double.NEGATIVE_INFINITY){
                    value = null;
                }
                mysqlColumn.put(at.name(), value);

            }
            if(mysqlColumn.size()<(AnalyseType.values().length)){
                continue;
            }
            StringBuffer coloumSb = new StringBuffer();
            StringBuffer valueSb = new StringBuffer();
            for(Map.Entry<String, Object> entry:mysqlColumn.entrySet()) {
                String key = entry.getKey();
                String val = String.valueOf(entry.getValue());
                coloumSb.append(key + "," );
                if( val.equals("null")){
                    valueSb.append( val + ","  );
                }else{
                    valueSb.append("'" + val + "',"  );
                }

            }
            coloumSb.append("id" + ", submitTime");
            valueSb.append("'" + jobid + "', " + "'" + submitTime + "'");
            String insertJobStr = "insert into plain_job.extracted (" + coloumSb.toString() + ") values(" + valueSb +");";
            mj.output(url, insertJobStr);
            haveRes = new ArrayList<Object>();
            haveRes = mj.output(url, probeSql);
            if(haveRes.isEmpty()){
                System.out.println(insertJobStr);
                System.out.println("Job " + jobid + " insert failed ! ");
            }else{
                System.out.println("insert " + jobid + " successfully!");
            }



        }

    }
    public static void main(String [] args ) {
        /*System.out.println(AnalyseType.values().length);
        for(AnalyseType at: AnalyseType.values()){
            System.out.println(at.name());
        }*/
        new InsertExtracted().Insert();
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date());
    }
}
