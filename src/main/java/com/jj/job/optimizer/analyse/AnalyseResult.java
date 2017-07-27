package com.jj.job.optimizer.analyse;

import com.jj.job.optimizer.analyse.analysies.AnalyseFactory.AnalyseType;

/**
 * Created by weizh on 2016/12/3.
 */
public class AnalyseResult {
    public String AnalyseItem;
    public AnalyseType AnalyseItemType;
    public String gsonString;
    public int dataNum;
    public Double avg;
    public Double fractile99;
}
