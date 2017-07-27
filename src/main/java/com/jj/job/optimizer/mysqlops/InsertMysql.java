package com.jj.job.optimizer.mysqlops;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jj.dbClient.MysqlJdbc;
import com.jj.job.optimizer.JobHistoryParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weizh on 2016/11/15.
 */
public class InsertMysql {
    //String url = "jdbc:mysql://instead_ip_tmp:3306";
    String url = "jdbc:mysql://instead_ip_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();

    public  void InsertJobToMysql(String jobid) {

        String probeSql = "select * from job.job where id='" + jobid +"'";
        List<Object> haveRes = mj.output(url, probeSql);
        if(!haveRes.isEmpty()){
            System.out.println("Job " + jobid + " already exist in DB!");
            return;
        }
        Gson gson = new Gson();
        List<String> tasklist = JobHistoryParser.ExtractTasksList(JobHistoryParser.GetJobTasks(jobid));
        String tasksJson = gson.toJson(tasklist);
        Map<String,Map<String,String>> jobSummary = gson.fromJson(JobHistoryParser.RestFulGet(jobid),
                new TypeToken<Map<String,Map<String,String>>>(){}.getType());



        /*
         * Insert job summary to job.job
         */
        StringBuffer coloumSb = new StringBuffer();
        StringBuffer valueSb = new StringBuffer();
        for(Map.Entry<String,String> entry:jobSummary.get("job").entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(key.equals("name")){
                val = val.replaceAll("'","\\\\'");
            }
            coloumSb.append(key + ",");
            valueSb.append("'" + val + "'," );

        }
        coloumSb.append("tasks");
        valueSb.append("'" + tasksJson + "'");
        String insertJobStr = "insert into job.job (" + coloumSb.toString() + ") values(" + valueSb +");";
        //System.out.println(insertJobStr);

        mj.output(url, insertJobStr);
        haveRes = new ArrayList<Object>();
        haveRes = mj.output(url, probeSql);
        if(haveRes.isEmpty()){
            System.out.println(insertJobStr);
            System.out.println("Job " + jobid + " insert failed ! ");
            return;
        }

        /*
         *  intsert job counter to job.jobcounters
         */
        Map<String,Map<String,Object>> jobCounters = gson.fromJson(JobHistoryParser.RestFulGet(jobid+"/counters"),
                Map.class);
        List<Map<String,Object>> jobCountersList =
                (List<Map<String,Object>>)jobCounters.get("jobCounters").get("counterGroup");
        for(Map<String,Object> counterMap:jobCountersList) {
            Object counterGroupName = counterMap.get("counterGroupName");
            String counters = gson.toJson(counterMap.get("counter"));
            String insertJobCounterStr = "insert into job.jobcounters (jobid, counterGroupName, counter ) values('" +
                        jobid + "', '" + counterGroupName + "', '" + counters + "');";
            //System.out.println(insertJobCounterStr);
            mj.output(url, insertJobCounterStr);

        }

        /*
         * insert tasks message to job.task & job.taskcounters
         */

        for(String taskid:tasklist) {
            coloumSb = new StringBuffer();
            valueSb = new StringBuffer();
            Map<String,Map<String,String>> taskSummary = gson.fromJson(
                    JobHistoryParser.RestFulGet(jobid + "/tasks/" + taskid),
                    new TypeToken<Map<String,Map<String,String>>>(){}.getType());
            for(Map.Entry<String,String> entry:taskSummary.get("task").entrySet()) {
                coloumSb.append(entry.getKey() + ",");
                valueSb.append("'" + entry.getValue() + "'," );
            }
            coloumSb.replace(coloumSb.length()-1, coloumSb.length(), "");
            valueSb.replace(valueSb.length()-1, valueSb.length(), "");
            String insertTaskStr = "insert into job.task (" + coloumSb.toString() + ") values(" + valueSb +");";
            //System.out.println(insertTaskStr);
            mj.output(url, insertTaskStr);

            Map<String,Map<String,Object>> taskCounters = gson.fromJson(
                    JobHistoryParser.RestFulGet(jobid + "/tasks/" + taskid + "/counters"),Map.class);
            List<Map<String,Object>> taskCountersList =
                    (List<Map<String,Object>>)taskCounters.get("jobTaskCounters").get("taskCounterGroup");
            if(taskCountersList == null){
                continue;
            }
            for(Map<String,Object> counterMap:taskCountersList) {
                Object counterGroupName = counterMap.get("counterGroupName");
                String counters = gson.toJson(counterMap.get("counter"));
                String insertTaskCounterStr = "insert into job.taskcounters (taskid, counterGroupName, counter ) values('" +
                        taskid + "', '" + counterGroupName + "', '" + counters + "');";
                //System.out.println(insertTaskCounterStr);
                mj.output(url, insertTaskCounterStr);
            }


        }





    }


    public static void main(String [] args){
        List<String> joblist = JobHistoryParser.GetAllJobs();
        InsertMysql im = new InsertMysql();
        System.out.println("jobs num is : " + joblist.size());
        for(String jobid:joblist){
            im.InsertJobToMysql(jobid);
            System.out.println("insert job " + jobid + " to mysql completed!");
        }
        //im.InsertJobToMysql("job_1476341534026_56541");
    }
}
