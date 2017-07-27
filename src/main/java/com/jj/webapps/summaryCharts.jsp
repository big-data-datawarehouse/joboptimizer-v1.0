<%@ page
        contentType="text/html; charset=utf-8"
        import="com.jj.job.optimizer.analyse.Analyse"
%>
<%@ page import="com.jj.job.optimizer.analyse.AnalyseResult" %>
<%@ page import="java.util.List" %>
<%@ page import="com.jj.job.optimizer.analyse.analysies.AnalyseFactory" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.ParseException" %>
<%
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String timeStr = "2017-03-02 00:00:00";
    String startTimeStr = "";
    try {
        long endStamp = df.parse(timeStr).getTime();
        long startStamp = endStamp - 7*24*3600*1000;
        startTimeStr = df.format(startStamp);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    //List<AnalyseResult> arList = new Analyse().summary(timeStr);
    List<AnalyseResult> arList = new Analyse().summaryFormDb(timeStr);

    int len = arList.size();
    String [] item = new String[len];
    String [] unit = new String[len];
    String [] gsonStr = new String[len];
    int [] allNum = new int[len];
    double [] avg = new double[len];
    double [] fractile = new double[len];
    String [] chartsStr = new String[len];
    String [] divstr = new String[len];

    for(int i=0; i<len; i++) {
        AnalyseResult ar = arList.get(i);
        item[i] = AnalyseFactory.createAnalyse(ar.AnalyseItemType).itemInstr;
        unit[i] = AnalyseFactory.createAnalyse(ar.AnalyseItemType).unit;


        gsonStr[i] = ar.gsonString;
        allNum[i] = ar.dataNum;
        avg[i] = ar.avg;
        fractile[i] = ar.fractile99;

        chartsStr[i] = "$('#container"+i+"').highcharts({chart:{type:'column'},title:{text:'"+item[i]+"'}," +
                "subtitle:{text:' 时间: " + startTimeStr + " To " + timeStr +" 作业个数:"+ allNum[i] + "   99%分位:"+ fractile[i] + "   均值:" + avg[i] +"'},xAxis:{type:'category',labels:{rotation:-45," +
                "style:{fontSize:'13px',fontFamily:'Verdana,sans-serif'}}},yAxis:{min:0,title:{text:'"+unit[i]+"'}}," +
                "legend:{enabled:false},tooltip:{pointFormat:'value:<b>{point.y:.1f}</b>'}," +
                "plotOptions:{column:{cursor:'pointer',events:{click:function(e){" +
                //"location.href=e.point.url;" +
                "window.open(e.point.url); ;" +
                "}},}}," +
                "series:[{name:'Population',data:"+gsonStr[i]+",dataLabels:{enabled:true,rotation:-90,color:'#FFFFFF',align:'right'," +
                "format:'{point.y:.1f}',y:10,style:{fontSize:'13px',fontFamily:'Verdana,sans-serif'}}}]});";

        divstr[i] = "<div id=\"container"+i+"\" style=\"min-width: 310px; height: 400px; margin: 0 auto\"></div>";



    }

%>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Highcharts Example</title>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <style type="text/css">
        ${demo.css}
    </style>
    <script type="text/javascript">
        $( function () {


            <%for(int i=0;i<len;i++) {
                out.println(chartsStr[i]);
            }
            %>
        });






    </script>
</head>
<body>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<%for(int i=0;i<len;i++) {
    out.println(divstr[i]);
}
%>

</body>
</html>
