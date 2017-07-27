package com.jj.job.optimizer.analyse.computeFunc;

import com.jj.job.optimizer.analyse.Operator;

import java.util.List;

/**
 * Created by weizh on 2016/11/29.
 */
public class ComputeDeviation implements Operator {
    public Double Calculate(List<Double> dataList) {
        Double sum = 0D;
        for(Double data:dataList){
            sum += (double)data;
            //System.out.println(data);
        }
        Double avg = sum/dataList.size();
        sum = 0D;
        for(Double data:dataList){
            Double div = (double)data - avg;
            Double divSqr = div*div;
            sum += divSqr;
        }
        return sum/avg/avg;

    }
}
