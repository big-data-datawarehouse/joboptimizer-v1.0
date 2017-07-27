package com.jj.job.optimizer;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weizh on 2016/11/10.
 */
public class HiveQueryLogParser {
    public List<String> FileToList(String filename){
        File file = new File(filename);
        List<String> res = new ArrayList<String>();
        try {
            FileInputStream is=new FileInputStream(file);
            InputStreamReader isr= new InputStreamReader(is);
            BufferedReader in= new BufferedReader(isr);
            String line=null;
            while( (line=in.readLine())!=null ){
                res.add(line);
            }
            in.close();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    public String LogParser(List<String> log){
        String res = null;
        List<Object> allList = new ArrayList<Object>();

        int count = 0;
        Gson gson = new Gson();
        for(String line:log){
            String [] tmp = line.split(" ");
            Map<String,Map<String,Object>> lineMap = new HashMap<String,Map<String,Object>>();
            Map<String,Object> subMap = new HashMap<String,Object>();
            count++;
            System.out.println("line num is : " + count);
            for(int i=1; i<tmp.length; i++){
                String wholeString = tmp[i];
                while(! wholeString.substring(wholeString.length()-1,wholeString.length()).equals("\"")) {
                    i++;
                    wholeString= wholeString + " " + tmp[i];
                }
                //System.out.println(wholeString);
                String [] kv = wholeString.split("=");
                String key = kv[0];
                Object val = kv[1];
                if(kv.length > 2){
                    for(int j=2; j<kv.length;j++){
                        val = val + "=" + kv[j];
                    }
                }

                if(key.equals("plan")){
                    StringBuffer tmpSb = new StringBuffer((String)val);
                    tmpSb.replace(0,1,"");
                    tmpSb.replace(tmpSb.length()-1,tmpSb.length(),"");
                    //val = tmpSb;
                    //System.out.println(val);
                    val = gson.fromJson(tmpSb.toString(), Object.class);
                }

                subMap.put(key,val);
            }
            lineMap.put(tmp[0],subMap);
            allList.add(lineMap);


        }
        System.out.println("allList size " + allList.size());

        res = gson.toJson(allList);
        System.out.println("gson string size " + res.length());
        return res;
    }

    public static void WriteToFile(String filename, String contents) {


        File file=new File(filename);
        //if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
        byte bytes[]=new byte[512];
        bytes=contents.getBytes();
        int b=contents.length();
        FileOutputStream fos= null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes,0,b);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args ){
        System.out.println("from here");
        HiveQueryLogParser hqlp = new HiveQueryLogParser();
        List<String> log = hqlp.FileToList("D:\\hivelog.txt");
        String contents = hqlp.LogParser(log);
        WriteToFile("D:\\res.txt", contents);
    }

}
