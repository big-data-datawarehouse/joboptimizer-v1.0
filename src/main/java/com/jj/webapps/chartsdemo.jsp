<%@ page
        contentType="text/html; charset=utf-8"
        import="java.text.SimpleDateFormat"
        import="java.util.*"
        import="java.io.IOException"
%>
<%@ page import="com.jj.job.optimizer.ScanData" %>
<%
    String start = "1479355194000";
    String end = "1479541594000";
    String order = "runningTime";
    ScanData sd = new ScanData();
    //String res = sd.TimeRangeOrderByOneKey("1479355194000", "1479541594000", "runningTime");
    String res = sd.TransToChartsForm("1479355194000", "1479541594000", "runningTime");
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
                    text: 'World\'s largest cities per 2014'
                },
                subtitle: {
                    text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
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
                        text: 'Population (millions)'
                    }
                },
                legend: {
                    enabled: false
                },
                tooltip: {
                    pointFormat: 'Population in 2008: <b>{point.y:.1f} millions</b>'
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
