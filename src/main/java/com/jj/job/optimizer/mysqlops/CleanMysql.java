package com.jj.job.optimizer.mysqlops;

import com.jj.dbClient.MysqlJdbc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by weizh on 2016/12/8.
 */
public class CleanMysql {
    static String url = "jdbc:mysql://instead_tmp:3306";
    MysqlJdbc mj = new MysqlJdbc();
    Set coloum = new HashSet<String>();
    public void cleanMysql(){
        Long beforeDay = System.currentTimeMillis() - (40*24*3600*1000);
        List<String> sqlList = new ArrayList<String>();
        sqlList.add("delete from plain_job.job where submitTime < " + beforeDay);
        sqlList.add("delete from plain_job.task where startTime < " + beforeDay);
        for(String sql:sqlList){
            mj.output(url,sql);
        }

    }
    public static void main(String [] args ){
       /* Long beforeDay = System.currentTimeMillis() - (20*24*3600*1000);
        String sql = "select count(*) from plain_job.job where submitTime < " + beforeDay;
        //String sql = "select count(*) from plain_job.task where startTime < " + beforeDay;
        List<Object> list = new MysqlJdbc().output(url, sql);
        if(!list.isEmpty()){
            System.out.println("content is " + list);
        }
        System.out.println("content " + ToJson.toJson(list));*/
       new CleanMysql().cleanMysql();
    }
}
