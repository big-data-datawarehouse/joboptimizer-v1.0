package com.jj.job.optimizer.analyse;

import java.util.List;

/**
 * Created by weizh on 2016/11/29.
 */
public interface Operator {
    Double Calculate(List<Double> dataList);
}
