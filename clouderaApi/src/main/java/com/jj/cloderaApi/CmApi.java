package com.jj.cloderaApi;

import com.cloudera.api.ClouderaManagerClientBuilder;
import com.cloudera.api.model.*;
import com.cloudera.api.v11.TimeSeriesResourceV11;
import com.cloudera.api.v12.RootResourceV12;


/**
 * Created by weizh on 2016/11/28.
 */
public class CmApi {
    public void Api(){
    RootResourceV12 apiRoot = new ClouderaManagerClientBuilder()
            .withHost("ip_instead_tmp")
            .withPort(7180)
            .withUsernamePassword("username", "passwd")
            .build()
            .getRootV12();
    TimeSeriesResourceV11 tsr = apiRoot.getTimeSeriesResource();
    ApiTimeSeriesEntityTypeList etl = tsr.getEntityTypes();

       /* for(ApiTimeSeriesEntityType et:etl) {
            System.out.println(et.get);
        }*/
    String query = "SELECT allocated_memory_mb_cumulative WHERE" +
            " entityName=yarn:root.hdfs AND category=" +
            "YARN_POOL";
    String start = "2016-11-28T14:31:57.948Z";
    String end = "2016-11-29T15:01:57.948Z";

    ApiTimeSeriesResponseList atsrl = tsr.queryTimeSeries(query, start, end);
    //atsrl.getTimeSeriesRows();

    for(ApiTimeSeriesResponse ats:atsrl) {
        for(ApiTimeSeries apts:ats.getTimeSeries()){
            System.out.println(apts.getMetadata());
            for(ApiTimeSeriesData atsd:apts.getData()){
                System.out.println("type: "+atsd.getType() +
                        " time:  " + atsd.getTimestamp()
                        + " val: " + atsd.getValue() );

            }
        }
    }





}
    public static void main(String [] args ){
        CmApi ca = new CmApi();
        ca.Api();
    }
}
