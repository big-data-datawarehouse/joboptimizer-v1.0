package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.analyse.Analyser;
import com.jj.job.optimizer.analyse.OperatorFactory;

/**
 * Created by weizh on 2016/12/6.
 */
public class TaskRunTimeDeviation extends AnalyseDisplayer {

    TaskRunTimeDeviation(){
        super.itemInstr="作业task运行时间均方差";
        super.unit="Sec^2";
    }
    public Double Analyse(String jobid) {
        Analyser ar = OperatorFactory.CreateAnalyser(
                OperatorFactory.DataName.TaskRunningTime,
                OperatorFactory.DataOps.Deviation);
        return ar.getAnalyser(jobid);
    }
}



