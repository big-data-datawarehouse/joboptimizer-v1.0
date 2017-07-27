package com.jj.job.optimizer.analyse.collector;

import com.google.gson.Gson;
import com.jj.restful.util.ClouderaClient;
import com.jj.restful.util.ClouderaClient.QueryType;

import java.util.List;
import java.util.Map;

import static com.jj.restful.util.ClouderaClient.QueryType.QueueMem;

/**
 * Created by weizh on 2016/12/1.
 */
public class QueueResourceStat {
    public Double QueueResAvg(QueryType queryType, String queueName, Long from, Long to ) {

        String queryRes = ClouderaClient.clouderaCli(queryType, queueName, from, to);
        //System.out.println(queryRes);
        Gson gson = new Gson();
        Map<String, List<Map<String,List<Map<String,Object>>>>> objectMap = gson.fromJson(queryRes, Map.class);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>)
                objectMap.get("items").get(0).get("timeSeries").get(0).get("data");

        double sum = 0;
        for(Map<String, Object> data:dataList ) {
            double val = (Double) data.get("value");
            sum+=val;
        }
        System.out.println(sum/dataList.size());


        return null;
    }

    public static void main(String [] args ){
        QueueResourceStat qrs = new QueueResourceStat();
        qrs.QueueResAvg(QueueMem, "yarn:root.hdfs", 1480494317000L, 1480577268000L);
    }
}
