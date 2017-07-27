<%@ page
        contentType="text/html; charset=utf-8"
        import="com.jj.job.optimizer.ScanData"
        import="java.text.SimpleDateFormat"
%>
<%@ page import="java.util.Date" %>
<%

    String start = "1479355194000";
    String end = "1479541594000";
    String order = "runningTime";

    String begStr = request.getParameter("startTime");
    order = request.getParameter("orderby");
    String endStr = request.getParameter("endTime");

    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date1=simpleDateFormat.parse(begStr);
    Date date2=simpleDateFormat.parse(endStr);

    start = String.valueOf(date1.getTime());
    end = String.valueOf(date2.getTime());
    String log =  start + " " + end + " " + order ;
    ScanData sd = new ScanData();
    //String res = sd.TransToChartsForm("1479355194000", "1479541594000", "runningTime");
    String res = sd.TransToChartsForm(start, end, order);
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
        $(function () {
            $('#container').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: '<%=order%>'
                },
                subtitle: {
                    text: '<%=log%>'
                },
                xAxis: {
                    type: 'category',
                    labels: {
                        rotation: -45,
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif'
                        }
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                legend: {
                    enabled: false
                },
                tooltip: {
                    pointFormat: 'value : <b>{point.y:.1f} </b>'
                },
                plotOptions: {
                    column: {
                        cursor: 'pointer',
                        events: {
                            click: function(e) {
                                location.href = e.point.url; //上面是当前页跳转，如果是要跳出新页面，那就用
                                //window.open(e.point.url);
                                //这里的url要后面的data里给出
                            }
                        },
                    }
                },
                series: [{
                    name: 'Population',
                    data: <%=res%>,
                    dataLabels: {
                        enabled: true,
                        rotation: -90,
                        color: '#FFFFFF',
                        align: 'right',
                        format: '{point.y:.1f}', // one decimal
                        y: 10, // 10 pixels down from the top
                        style: {
                            fontSize: '13px',
                            fontFamily: 'Verdana, sans-serif'
                        }
                    }
                }]
            });
        });
    </script>
</head>
<body>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>

<div id="container" style="min-width: 300px; height: 400px; margin: 0 auto"></div>

</body>
</html>
