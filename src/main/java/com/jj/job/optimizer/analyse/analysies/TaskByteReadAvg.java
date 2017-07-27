package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.analyse.Analyser;
import com.jj.job.optimizer.analyse.OperatorFactory;

/**
 * Created by weizh on 2016/12/6.
 */
public class TaskByteReadAvg extends AnalyseDisplayer {
    TaskByteReadAvg() {
        super.itemInstr = "作业task处理数据量均值";
        super.unit = "Byte";
    }
    public Double Analyse(String jobid) {
        Analyser ar = OperatorFactory.CreateAnalyser(
                OperatorFactory.DataName.TaskReadBytes,
                OperatorFactory.DataOps.Avg);
        return ar.getAnalyser(jobid);
    }
}
