package com.jj.job.optimizer.mysqlops;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jj.dbClient.MysqlJdbc;
import com.jj.job.optimizer.JobHistoryParser;

import java.util.*;

/**
 * Created by weizh on 2016/11/17.
 */
public class InsertPlainJob {
    String url = "jdbc:mysql://instead_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();
    Set coloum = new HashSet<String>();

    InsertPlainJob(){
        String [] columStr = { "BAD_ID","BYTES_READ","BYTES_WRITTEN","COMBINE_INPUT_RECORDS","COMBINE_OUTPUT_RECORDS",
                "COMMITTED_HEAP_BYTES","CONNECTION","CPU_MILLISECONDS","CREATED_FILES","DATA_LOCAL_MAPS",
                "DESERIALIZE_ERRORS","FAILED_SHUFFLE","FILE_BYTES_READ","FILE_BYTES_WRITTEN","FILE_LARGE_READ_OPS",
                "FILE_READ_OPS","FILE_WRITE_OPS","GC_TIME_MILLIS","HDFS_BYTES_READ","HDFS_BYTES_WRITTEN",
                "HDFS_LARGE_READ_OPS","HDFS_READ_OPS","HDFS_WRITE_OPS","IO_ERROR","MAP_INPUT_RECORDS",
                "MAP_OUTPUT_BYTES","MAP_OUTPUT_MATERIALIZED_BYTES","MAP_OUTPUT_RECORDS","MB_MILLIS_MAPS",
                "MB_MILLIS_REDUCES","MERGED_MAP_OUTPUTS","MILLIS_MAPS","MILLIS_REDUCES","NUM_KILLED_REDUCES",
                "OTHER_LOCAL_MAPS","PHYSICAL_MEMORY_BYTES","RACK_LOCAL_MAPS","RECORDS_IN","RECORDS_OUT_0",
                "RECORDS_OUT_INTERMEDIATE","REDUCE_INPUT_GROUPS","REDUCE_INPUT_RECORDS","REDUCE_OUTPUT_RECORDS",
                "REDUCE_SHUFFLE_BYTES","SHUFFLED_MAPS","SLOTS_MILLIS_MAPS","SLOTS_MILLIS_REDUCES","SPILLED_RECORDS",
                "SPLIT_RAW_BYTES","TOTAL_LAUNCHED_MAPS","TOTAL_LAUNCHED_REDUCES","VCORES_MILLIS_MAPS",
                "VCORES_MILLIS_REDUCES","VIRTUAL_MEMORY_BYTES","WRONG_LENGTH","WRONG_MAP","WRONG_REDUCE" };

        for(String col:columStr){
            coloum.add(col);
        }
    }
    public void Insert(String jobid){

        String probeSql = "select * from plain_job.job where id='" + jobid +"'";
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
         * Insert job summary to plain_job.job
         */
        StringBuffer coloumSb = new StringBuffer();
        StringBuffer valueSb = new StringBuffer();
        Map<String, String> jobSummaryMap = jobSummary.get("job");
        for(Map.Entry<String,String> entry:jobSummaryMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(key.equals("name")){
                val = val.replaceAll("'","\\\\'");
            }
            coloumSb.append(key + ",");
            valueSb.append("'" + val + "'," );

        }
        String runningTime = String.valueOf(Long.parseLong( jobSummaryMap.get("finishTime"))
                - Long.parseLong(jobSummaryMap.get("startTime")) );
        String delayTime = String.valueOf(Long.parseLong( jobSummaryMap.get("startTime"))
                - Long.parseLong(jobSummaryMap.get("submitTime")) );


        Map<String,Map<String,Object>> jobCounters = gson.fromJson(JobHistoryParser.RestFulGet(jobid+"/counters"),
                Map.class);
        List<Map<String,Object>> jobCountersList =
                (List<Map<String,Object>>)jobCounters.get("jobCounters").get("counterGroup");
        if( jobCountersList == null ) {
            System.out.println("job " + jobid + " counter is null , skip...");
            return;
        }
        for(Map<String,Object> counterMap:jobCountersList) {
            //Object counterGroupName = counterMap.get("counterGroupName");
            List<Map<String,String>> counters = (List<Map<String,String>>) counterMap.get("counter");
            for(Map<String,String> cm:counters) {
                String colname =  cm.get("name");
                if(!coloum.contains(colname)){
                    System.out.println("job : "  + jobid + " " + colname + "Not exist in mysql columns , " +
                            "skip this column and insert others!");
                    continue;
                }
                String map_val = String.valueOf(cm.get("mapCounterValue"));
                String rdc_val = String.valueOf(cm.get("reduceCounterValue"));
                String total_val = String.valueOf(cm.get("totalCounterValue"));

                coloumSb.append( colname + "_mapCounterValue, " + colname
                        + "_reduceCounterValue," + colname + "_totalCounterValue,");
                valueSb.append("'" + map_val +"', '" + rdc_val + "', '" + total_val + "', ");
            }
        }

        coloumSb.append("runningTime, delayTime, tasks");
        valueSb.append("'" + runningTime +"', '" + delayTime +"', '" + tasksJson + "'");

        String insertJobStr = "insert into plain_job.job (" + coloumSb.toString() + ") values(" + valueSb +");";
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
         * insert tasks message to job.task & job.taskcounters
         */

        for(String taskid:tasklist) {
            coloumSb = new StringBuffer();
            valueSb = new StringBuffer();
            Map<String, Map<String, String>> taskSummary = gson.fromJson(
                    JobHistoryParser.RestFulGet(jobid + "/tasks/" + taskid),
                    new TypeToken<Map<String, Map<String, String>>>() {
                    }.getType());
            Map<String, String> taskSummaryMap = taskSummary.get("task");
            for (Map.Entry<String, String> entry : taskSummaryMap.entrySet()) {
                coloumSb.append(entry.getKey() + ",");
                valueSb.append("'" + entry.getValue() + "',");
            }
            runningTime = String.valueOf(Long.parseLong( taskSummaryMap.get("finishTime"))
                    - Long.parseLong(taskSummaryMap.get("startTime")) );

            Map<String,Map<String,Object>> taskCounters = gson.fromJson(
                    JobHistoryParser.RestFulGet(jobid + "/tasks/" + taskid + "/counters"),Map.class);
            List<Map<String,Object>> taskCountersList =
                    (List<Map<String,Object>>)taskCounters.get("jobTaskCounters").get("taskCounterGroup");
            if(taskCountersList == null){
                continue;
            }

            for(Map<String,Object> counterMap:taskCountersList) {
                //Object counterGroupName = counterMap.get("counterGroupName");
                List<Map<String,String>> counters = (List<Map<String,String>>)counterMap.get("counter");
                for(Map<String,String> cm:counters) {
                    String colname =  cm.get("name");
                    if(!coloum.contains(colname)){
                        System.out.println("task: "  + taskid + " " + colname + " Not exist in mysql columns , " +
                                "skip this column and insert others !");
                        continue;
                    }
                    String colmval = String.valueOf(cm.get("value"));

                    coloumSb.append(colname + ",");
                    valueSb.append("'" + colmval + "', ");
                }
            }

            coloumSb.append("runningTime");
            valueSb.append("'"+ runningTime +"'");

            String insertTaskStr = "insert into plain_job.task (" + coloumSb.toString() + ") values(" + valueSb +");";
            //System.out.println(insertTaskStr);
            mj.output(url, insertTaskStr);
        }
    }

    public static void main(String [] args){
        List<String> joblist = JobHistoryParser.GetAllJobs();
        InsertPlainJob ipj = new InsertPlainJob();
        System.out.println("jobs num is : " + joblist.size());
        for(String jobid:joblist){
            ipj.Insert(jobid);
            System.out.println("insert job " + jobid + " to mysql completed!");
        }
        //im.InsertJobToMysql("job_1476341534026_56541");
    }
}
