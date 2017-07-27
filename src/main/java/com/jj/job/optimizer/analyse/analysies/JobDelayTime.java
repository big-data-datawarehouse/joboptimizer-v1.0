package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.ScanData;

/**
 * Created by weizh on 2016/12/6.
 */
public class JobDelayTime extends AnalyseDisplayer {
    JobDelayTime() {
        super.itemInstr = "作业调度延迟时间";
        super.unit = "Sec";
    }

    public Double Analyse(String jobid) {
        ScanData sd = new ScanData();
        Double delayTime = sd.getValFromDb(jobid, "plain_job.job","delayTime");
        return delayTime;
    }
}
