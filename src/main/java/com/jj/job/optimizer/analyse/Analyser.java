package com.jj.job.optimizer.analyse;

import com.jj.dbClient.MysqlJdbc;
import com.jj.job.optimizer.ScanData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weizh on 2016/11/29.
 */
public class Analyser {
    String url = "jdbc:mysql://instead_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();
    String tablename;
    String column;
    List<Double> dataList;
    Operator oper;

    public Analyser(String tn, String cn, Operator opr){
        tablename = tn;
        column = cn;
        oper = opr;
    }


    public List<Double> getDataList(String jobid) {
        ScanData sd = new ScanData();
        List<String> tasklist =
                sd.getTaskListFromDb(jobid);
                //JobHistoryParser.ExtractTasksList(JobHistoryParser.GetJobTasks(jobid));

        List<Double> dataList = new ArrayList<Double>();
        for(String taskid:tasklist) {

            /*String sql = "select " + column + " from " + tablename  +" where id='"+ taskid + "'";
            List<Object> res = mj.output(url, sql);
            List<Long> vallist = (List<Long>) res.get(0);
            long val = vallist.get(0);*/

            Double val = sd.getValFromDb(taskid, tablename, column);
            if(val != Double.NEGATIVE_INFINITY){
                dataList.add(val);
            }

        }
        return dataList;
    }



    public Double getAnalyser(String jobid){
        List<Double> datalist = this.getDataList(jobid);
        return oper.Calculate(datalist);
    }
    public static void main(String [] args ){

    }
}
