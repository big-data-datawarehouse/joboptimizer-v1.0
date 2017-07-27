package com.jj.job.optimizer.analyse.analysies;

import com.jj.job.optimizer.ScanData;

/**
 * Created by weizh on 2016/12/6.
 */
public class JobDealDataPerSec extends AnalyseDisplayer{
    JobDealDataPerSec() {
        super.itemInstr = "作业每秒处理数据量";
        super.unit = "Byte/Sec";
    }


    public Double Analyse(String jobid) {
        ScanData sd = new ScanData();
        Double runningTime = sd.getValFromDb(jobid, "plain_job.job","runningTime");
        Double bytesRead = sd.getValFromDb(jobid, "plain_job.job","FILE_BYTES_READ_mapCounterValue");
        if( runningTime == Double.NEGATIVE_INFINITY || bytesRead == Double.NEGATIVE_INFINITY ) {
            return Double.NEGATIVE_INFINITY;
        }
        return bytesRead/runningTime;
    }
}
