package com.jj.job.optimizer.analyse;

import com.jj.job.optimizer.analyse.computeFunc.ComputeAvg;
import com.jj.job.optimizer.analyse.computeFunc.ComputeDeviation;

/**
 * Created by weizh on 2016/11/29.
 */
public class OperatorFactory {

    public enum DataName {
        TaskRunningTime, TaskReadBytes
    }
    public enum DataOps {
        Deviation, Avg
    }
    public static Analyser CreateAnalyser(DataName dn, DataOps dops ){
        String tn = null;
        String column = null;
        Operator oper = null;
        switch(dn){
            case TaskRunningTime:
                tn = "plain_job.task";
                column = "runningTime";
                break;
            case TaskReadBytes:
                tn = "plain_job.task";
                column = "HDFS_BYTES_READ";
                break;
        }
        switch(dops){
            case Deviation:
                oper = new ComputeDeviation();
                break;
            case Avg:
                oper = new ComputeAvg();
                break;
        }
        return new Analyser(tn, column, oper);
    }


}
