package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.ScanData;

/**
 * Created by weizh on 2016/12/7.
 */
public class JobShuffleBytes extends AnalyseDisplayer {

    JobShuffleBytes() {
        super.itemInstr = "作业shuffle大小";
        super.unit = "Byte";
    }
    public Double Analyse(String jobid) {
        ScanData sd = new ScanData();
        Double runningTime = sd.getValFromDb(jobid, "plain_job.job","REDUCE_SHUFFLE_BYTES_reduceCounterValue");
        return runningTime;
    }
}
