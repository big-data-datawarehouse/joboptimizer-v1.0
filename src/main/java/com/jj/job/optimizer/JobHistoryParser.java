package com.jj.job.optimizer;


import com.google.gson.Gson;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weizh on 2016/11/14.
 */
public class JobHistoryParser {


    public static String RestFulGet(String requestTxt){
        String prefix = "ws/v1/history/mapreduce/jobs";
        ClientConfig config = new ClientConfig();


        //Client client ClientFactory.newClient(config);

        Client client = ClientBuilder.newClient(config);

        //URI uri = UriBuilder.fromUri("http://ip_instead_tmp:19888/").build();
        URI uri = null;
        try {
            uri = new URI("http://tk-dat-hadoop184:19888/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        WebTarget target = client.target(uri);
        /*try {
            requestTxt = URLEncoder.encode(requestTxt, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        String path = prefix + "/" + requestTxt;
        //System.out.println(path);
        return target.path(path).
                request().
                accept(MediaType.APPLICATION_JSON_TYPE).get(String.class).toString();



    }

    public static String GetJobTasks(String jobId){
        String taskRequstTxt = jobId + "/tasks" ;
        return RestFulGet(taskRequstTxt);
    }

    public static List<String> ExtractTasksList(String jsonString){
        List<String> res = new ArrayList<String>();
        //System.out.println(jsonString.length());
        Gson gson = new Gson();
       /* Map<String,Map<String,List<Map<String, String>>>> tasksMap = gson.fromJson(jsonString,
                new TypeToken<Map<String,Map<String,List<Map<String, String>>>>>(){}.getType());*/
        Map<String,Map<String,List<Map<String, String>>>> tasksMap = gson.fromJson(jsonString,Map.class);
        //System.out.println("map size is " + tasksMap);
        List<Map<String,String>> tasksList = tasksMap.get("tasks").get("task");

        //System.out.println("taskslist size is : " + tasksList);
        if(tasksList != null ) {
            for(Map<String, String> taskMap:tasksList){
                //System.out.println(taskMap);
                res.add(taskMap.get("id"));
            }
        }
        return res;
    }

    public static List<String> GetAllJobs(){
        List<String> res = new ArrayList<String>();
        Gson gson = new Gson();
        Map<String,Map<String,List<Map<String,String>>>> jobJsonMap = gson.fromJson(RestFulGet(""), Map.class);

        List<Map<String,String>> jobJsonList = jobJsonMap.get("jobs").get("job");

        for(Map<String,String> jobmap:jobJsonList){
            //System.out.println(jobmap.get("id"));
            res.add(jobmap.get("id"));
        }
        return res;
    }

    public static String JobToJson(String jobid) {
        Gson gson = new Gson();
        Map<String, Object> jobMap = new HashMap<String, Object>();
        List<String> tasklist = ExtractTasksList(GetJobTasks(jobid));
        List<Object> taskJsonList = new ArrayList<Object>();
        for(String task:tasklist){
            Map<String, Object> taskMap = new HashMap<String, Object>();
            String taskRequst = jobid + "/tasks/" + task;
            String taskCountersReq = taskRequst + "/counters";
            Object taskSummary =  gson.fromJson(RestFulGet( taskRequst ),Map.class).get("task");
            Object taskCounters = gson.fromJson(RestFulGet(taskCountersReq),Map.class).get("jobTaskCounters");
            taskMap.put("TaskSum", taskSummary);
            taskMap.put("TaskCounters", taskCounters);
            taskJsonList.add(taskMap);
        }
        Object jobSummary = gson.fromJson(RestFulGet(jobid), Map.class).get("job");
        Object jobCounters = gson.fromJson(RestFulGet(jobid+"/counters"), Map.class).get("jobCounters");
        jobMap.put("JobSum", jobSummary);
        jobMap.put("JobCounters", jobCounters);
        jobMap.put("Tasks", taskJsonList);

        return gson.toJson(jobMap);
    }



    public static void main(String [] args ) {
        //System.out.println(RestFulGet("job_1476341534026_51313/tasks/"));
        //System.out.println(RestFulGet("job_1476341534026_41016/tasks"));
        /*List<String> res = ExtractTasksList(GetJobTasks("job_1476341534026_41016"));
        for(String task:res){
            System.out.println(task);
        }*/
        //System.out.println(RestFulGet("job_1476341534026_56541/tasks/task_1476341534026_56541_m_000005/counters"));
        //System.out.println(JobToJson("job_1476341534026_56541"));
        //System.out.println(RestFulGet(""));
        System.out.println(GetAllJobs());

        //System.out.println(RestFulGet("job_local1314151179_0001"));
    }
}
