package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.ScanData;

/**
 * Created by weizh on 2016/12/6.
 */
public class JobRunningTime extends AnalyseDisplayer {
    JobRunningTime() {
        super.itemInstr = "作业运行时间";
        super.unit = "Sec";
    }
    public Double Analyse(String jobid) {
        ScanData sd = new ScanData();
        Double runningTime = sd.getValFromDb(jobid, "plain_job.job","runningTime");
        return runningTime;
    }
}
