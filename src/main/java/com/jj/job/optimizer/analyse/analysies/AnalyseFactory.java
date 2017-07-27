package com.jj.job.optimizer.analyse.analysies;

/**
 * Created by weizh on 2016/12/6.
 */
public class AnalyseFactory {
    public enum AnalyseType{
        TRunTimeDev, TByteReadDev, TByteReadAvg, JDealDataPS, JRuningTime, JDelayTime, JShuffleBytes
    }
    public static AnalyseDisplayer createAnalyse( AnalyseType at){
        AnalyseDisplayer ad = null;
        switch(at) {
            case TRunTimeDev:
                ad = new TaskRunTimeDeviation();
                break;
            case TByteReadDev:
                ad = new TaskByteReadDeviation();
                break;
            case TByteReadAvg:
                ad = new TaskByteReadAvg();
                break;
            case JDealDataPS:
                ad = new JobDealDataPerSec();
                break;
            case JRuningTime:
                ad = new JobRunningTime();
                break;
            case JDelayTime:
                ad = new JobDelayTime();
                break;
            case JShuffleBytes:
                ad = new JobShuffleBytes();
                break;

        }

        return ad;


    }
}
