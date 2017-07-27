<%@ page
        import="java.util.*"
        import="java.text.SimpleDateFormat"
        import="org.apache.commons.lang.ObjectUtils"
%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    var alertObj = new Object();
    var generalStyle = {
        zIndex: 0,
        width: "500px",
        height: "100px",
        border: "thick solid #CCCCCC",
        background: "#FFFFFF",
        position: "absolute",
        top: "15%",
        left: "20%"
    }
    var txtStyle = {
        textAlign: "center"
    }
    var btnStyle = {
        position: "absolute",
        left: "40%",
        top: "70%",
        color: "#333333",
        fontWeight: "bold",
        outlineColor:"#3366FF",
        outlineStyle:"ridge",
        outlineWidth:"thin",
//outline: "thin ridge #3366FF",
        innerHTML: "OK"
    }
    alertObj = {
        generalSet: generalStyle,
        txtSet: txtStyle,
        btnSet: btnStyle,
        isExist: false
    }
    alertObj.createComponent = function() {
        var component = document.createElement(arguments[0]);
        var styles = arguments[1];
        for (var property in styles) {
            if (styles[property] != null) {
                try{
                    component.style[property] = styles[property];
                }catch(err){
                    document.write(err.name+":"+property+"<br/>");//set property error!
                }
            }
        }
        return component;
    }
    alertObj.show = function() {
        if(!this.isExist){
            this.isExist = true;
            var bodyObj = document.body;
            bodyObj.style.zIndex = -1;
            bodyObj.style.background = "#999999";
            var divObj = this.createComponent("div", this.generalSet);

            var txtObj = this.createComponent("p", this.txtSet);
            txtObj.innerHTML = arguments[0];
            var btnObj = this.createComponent("button", this.btnSet);
            btnObj.innerHTML = this.btnSet.innerHTML;
            btnObj.onclick = function() {
                bodyObj.style.zIndex=0;
                bodyObj.style.background="";
                bodyObj.removeChild(divObj);
                if(alertObj.isExist){
                    alertObj.isExist = false;
                }
            }
            divObj.appendChild(txtObj);
            divObj.appendChild(btnObj);
            bodyObj.appendChild(divObj);
        }
    }
    function show(s) {
        alertObj.show(s);
    }
</script>
</head>
<body>
<%

    String [] mId = request.getParameterValues("metric");
    String begStr = request.getParameter("startTime");
    String endStr = request.getParameter("endTime");
    if(session.getAttribute("cluster") == null || mId==null||mId.length==0 )
    {
        String message = "注意忘记选metric或者忘记选host或者忘记选时间了,请返回重新选!!!!!!因为没选不会出图，记得选..";
        out.println("<SCRIPT LANGUAGE='JavaScript'>");
        out.println("<!--");
        out.println("show('" + message + "')");
        //out.println("show(message)");
        //out.println("window.history.back(-1);");
        out.println("//-->");
        out.println("</SCRIPT>");
    }
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date1=simpleDateFormat.parse(begStr);
    Date date2=simpleDateFormat.parse(endStr);
    String begStamp = String.valueOf(date1.getTime()).substring(0,10);
    String endStamp = String.valueOf(date2.getTime()).substring(0,10);

    String clusterName=session.getAttribute("cluster").toString();
    String [] args = new String[5];
    args[1] = clusterName;
    args[2] = "0";
    args[3] = begStamp;
    args[4] = endStamp;
    String[] hstr=new String[300];
    String[] divstr=new String[300];
    int len = 0;
    if(session.getAttribute("cluster") == null || mId==null||mId.length==0 ||args[3]==null||args[4]==null)
    {
        String message = "注意忘记选metric或者忘记选host或者忘记选时间了,请返回重新选!!!!!!因为没选不会出图，记得选..";
        out.println("<SCRIPT LANGUAGE='JavaScript'>");
        out.println("<!--");
        out.println("show('" + message + "')");
        //out.println("show(message)");
        //out.println("window.history.back(-1);");
        out.println("//-->");
        out.println("</SCRIPT>");
    }
    else {
        String suffix = "";
        int end = Integer.valueOf(endStamp);
        int start = Integer.valueOf(begStamp);
        if (end - start <= 3600) {
            suffix = "ClusterOrig";
        } else if (end - start <= 86400) {
            suffix = "ClusterHour";
        } else if(end -start<=830121){
            suffix = "ClusterDay";
        }
        else if(end -start<=2678400)
        {

            suffix="ClusterWeek";
        }
        else
        {
            suffix="ClusterMonth";
        }

        String table_name = "ns_hadoopadmin:BigMonitorMetricData" + suffix;
        args[0] = table_name;
        //String [] res = { GetData.get(), GetData.get() };
        String title = "";
        String yAtext = "data";
        len = mId.length;
        hstr = new String[len];
        divstr = new String[len];
        for (int i = 0; i < len; i++) {
            String[] mInfo = mId[i].split(":");
            args[2] = mInfo[0];
            hstr[i] = "$('#container" + i + "').highcharts({chart:{zoomType:'x'},title:{text:'" + title + mInfo[1] + "'}," +
                    "subtitle:{text:document.ontouchstart===undefined?'" +
                    "Clickanddragintheplotareatozoomin':'Pinchthecharttozoomin'},xAxis:{type:'datetime'}," +
                    "yAxis:{title:{text:'" + yAtext + " '}},legend:{enabled:false},plotOptions:{area:{fillColor:" +
                    "{linearGradient:{x1:0,y1:0,x2:0,y2:1},stops:[[0,Highcharts.getOptions().colors[0]],[1," +
                    "Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]]}," +
                    "marker:{radius:2},lineWidth:1,states:{hover:{lineWidth:1}},threshold:null}}," +
                    "series:[{type:'area',name:'value',data: [1,2] }]});";
            divstr[i] = "<div id=\"container" + i + "\" style=\"min-width: 310px; height: 400px; margin: 0 auto\"></div>";
        }
    }

%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Highcharts Example</title>

<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<style type="text/css">
    ${demo.css}
</style>
<head>
    <script type="text/javascript">
        $( function () {
            Highcharts.setOptions({
                global: {
                    useUTC: false
                }
            });

            <%
    for(int i=0;i<len;i++) {
        out.println(hstr[i]);
    }
    %>


        });

    </script>
</head>
<script src="./Highcharts-4.2.6/js/highcharts.js"></script>
<script src="./Highcharts-4.2.6/modules/exporting.js"></script>

<%for(int i=0;i<len;i++) {
    out.println(divstr[i]);
}
%>
</body>
</html>