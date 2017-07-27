package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.analyse.Analyser;
import com.jj.job.optimizer.analyse.OperatorFactory;

/**
 * Created by weizh on 2016/12/6.
 */
public class TaskByteReadDeviation extends AnalyseDisplayer {
    TaskByteReadDeviation() {
        super.itemInstr = "作业task处理数据量均方差";
        super.unit = "Byte^2";
    }
    public Double Analyse(String jobid) {
        Analyser ar = OperatorFactory.CreateAnalyser(
                OperatorFactory.DataName.TaskReadBytes,
                OperatorFactory.DataOps.Deviation);
        return ar.getAnalyser(jobid);
    }
}
