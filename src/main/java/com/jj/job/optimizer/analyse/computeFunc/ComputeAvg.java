package com.jj.job.optimizer.analyse.computeFunc;

import com.jj.job.optimizer.analyse.Operator;

import java.util.List;

/**
 * Created by weizh on 2016/11/29.
 */
public class ComputeAvg implements Operator {
    public Double Calculate(List<Double> dataList) {
        Double sum = 0D;
        for(Double data:dataList){
            sum += data;
            //System.out.println(data);
        }
        Double avg = sum/dataList.size();
        return avg;
    }
}
