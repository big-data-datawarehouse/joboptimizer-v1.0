<%@ page
        contentType="text/html; charset=utf-8"
        import="java.text.SimpleDateFormat"
        import="java.util.*"
        import="java.io.IOException"
%>
<!DOCTYPE html>
<html lang="en" class=" js no-touch"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tentacles</title>
    <link href="download/appstart/assets/css/test.css" rel="stylesheet">
    <br>
    <br>
    <br>
<body class="">
<div id="preloader" style="display: none;">
    <div id="preloader-logo" style="display: none;">
        <img src="./Dashboard   AppStart - Admin Template_files/preloader-logo.png" alt="Logo">
    </div>
    <div id="preloader-icon" style="display: none;">
        <i class="im-spinner icon-spin"></i>
    </div>
</div>
<!-- Start #header -->
<div id="header" class="header-fixed">
    <div class="container-fluid">
        <div class="navbar">

            <nav id="top-nav" class="navbar-no-collapse" role="navigation">
                <ul class="dropdown"   style="width:900px; margin:0 auto;top:15px;" >
                    <div id="menu">
                        <ul>

                            <%
                                String[] clusters = {
                                        "submitTime","startTime","finishTime","mapsTotal",
                                        "mapsCompleted","reducesTotal","reducesCompleted",
                                        "avgMapTime","avgReduceTime","avgShuffleTime","avgMergeTime",
                                        "failedReduceAttempts","killedReduceAttempts","successfulReduceAttempts",
                                        "failedMapAttempts","killedMapAttempts","successfulMapAttempts","runningTime",
                                        "FILE_BYTES_READ_mapCounterValue","FILE_BYTES_READ_reduceCounterValue",
                                        "FILE_BYTES_READ_totalCounterValue","FILE_BYTES_WRITTEN_mapCounterValue",
                                        "FILE_BYTES_WRITTEN_reduceCounterValue","FILE_BYTES_WRITTEN_totalCounterValue",
                                        "FILE_READ_OPS_mapCounterValue","FILE_READ_OPS_reduceCounterValue","FILE_READ_OPS_totalCounterValue",
                                        "FILE_LARGE_READ_OPS_mapCounterValue","FILE_LARGE_READ_OPS_reduceCounterValue",
                                        "FILE_LARGE_READ_OPS_totalCounterValue","FILE_WRITE_OPS_mapCounterValue",
                                        "FILE_WRITE_OPS_reduceCounterValue","FILE_WRITE_OPS_totalCounterValue",
                                        "HDFS_BYTES_READ_mapCounterValue","HDFS_BYTES_READ_reduceCounterValue",
                                        "HDFS_BYTES_READ_totalCounterValue","HDFS_BYTES_WRITTEN_mapCounterValue",
                                        "HDFS_BYTES_WRITTEN_reduceCounterValue","HDFS_BYTES_WRITTEN_totalCounterValue",
                                        "HDFS_READ_OPS_mapCounterValue","HDFS_READ_OPS_reduceCounterValue","HDFS_READ_OPS_totalCounterValue",
                                        "HDFS_LARGE_READ_OPS_mapCounterValue","HDFS_LARGE_READ_OPS_reduceCounterValue",
                                        "HDFS_LARGE_READ_OPS_totalCounterValue","HDFS_WRITE_OPS_mapCounterValue",
                                        "HDFS_WRITE_OPS_reduceCounterValue","HDFS_WRITE_OPS_totalCounterValue",
                                        "NUM_KILLED_REDUCES_mapCounterValue","NUM_KILLED_REDUCES_reduceCounterValue",
                                        "NUM_KILLED_REDUCES_totalCounterValue","TOTAL_LAUNCHED_MAPS_mapCounterValue",
                                        "TOTAL_LAUNCHED_MAPS_reduceCounterValue","TOTAL_LAUNCHED_MAPS_totalCounterValue",
                                        "TOTAL_LAUNCHED_REDUCES_mapCounterValue","TOTAL_LAUNCHED_REDUCES_reduceCounterValue",
                                        "TOTAL_LAUNCHED_REDUCES_totalCounterValue","OTHER_LOCAL_MAPS_mapCounterValue",
                                        "OTHER_LOCAL_MAPS_reduceCounterValue","OTHER_LOCAL_MAPS_totalCounterValue",
                                        "DATA_LOCAL_MAPS_mapCounterValue","DATA_LOCAL_MAPS_reduceCounterValue",
                                        "DATA_LOCAL_MAPS_totalCounterValue","RACK_LOCAL_MAPS_mapCounterValue",
                                        "RACK_LOCAL_MAPS_reduceCounterValue","RACK_LOCAL_MAPS_totalCounterValue",
                                        "SLOTS_MILLIS_MAPS_mapCounterValue","SLOTS_MILLIS_MAPS_reduceCounterValue",
                                        "SLOTS_MILLIS_MAPS_totalCounterValue","SLOTS_MILLIS_REDUCES_mapCounterValue",
                                        "SLOTS_MILLIS_REDUCES_reduceCounterValue","SLOTS_MILLIS_REDUCES_totalCounterValue",
                                        "MILLIS_MAPS_mapCounterValue","MILLIS_MAPS_reduceCounterValue","MILLIS_MAPS_totalCounterValue",
                                        "MILLIS_REDUCES_mapCounterValue","MILLIS_REDUCES_reduceCounterValue","MILLIS_REDUCES_totalCounterValue",
                                        "VCORES_MILLIS_MAPS_mapCounterValue","VCORES_MILLIS_MAPS_reduceCounterValue",
                                        "VCORES_MILLIS_MAPS_totalCounterValue","VCORES_MILLIS_REDUCES_mapCounterValue",
                                        "VCORES_MILLIS_REDUCES_reduceCounterValue","VCORES_MILLIS_REDUCES_totalCounterValue",
                                        "MB_MILLIS_MAPS_mapCounterValue","MB_MILLIS_MAPS_reduceCounterValue",
                                        "MB_MILLIS_MAPS_totalCounterValue","MB_MILLIS_REDUCES_mapCounterValue",
                                        "MB_MILLIS_REDUCES_reduceCounterValue","MB_MILLIS_REDUCES_totalCounterValue",
                                        "MAP_INPUT_RECORDS_mapCounterValue","MAP_INPUT_RECORDS_reduceCounterValue",
                                        "MAP_INPUT_RECORDS_totalCounterValue","MAP_OUTPUT_RECORDS_mapCounterValue",
                                        "MAP_OUTPUT_RECORDS_reduceCounterValue","MAP_OUTPUT_RECORDS_totalCounterValue",
                                        "MAP_OUTPUT_BYTES_mapCounterValue","MAP_OUTPUT_BYTES_reduceCounterValue",
                                        "MAP_OUTPUT_BYTES_totalCounterValue","MAP_OUTPUT_MATERIALIZED_BYTES_mapCounterValue",
                                        "MAP_OUTPUT_MATERIALIZED_BYTES_reduceCounterValue","MAP_OUTPUT_MATERIALIZED_BYTES_totalCounterValue",
                                        "SPLIT_RAW_BYTES_mapCounterValue","SPLIT_RAW_BYTES_reduceCounterValue",
                                        "SPLIT_RAW_BYTES_totalCounterValue","COMBINE_INPUT_RECORDS_mapCounterValue",
                                        "COMBINE_INPUT_RECORDS_reduceCounterValue","COMBINE_INPUT_RECORDS_totalCounterValue",
                                        "COMBINE_OUTPUT_RECORDS_mapCounterValue","COMBINE_OUTPUT_RECORDS_reduceCounterValue",
                                        "COMBINE_OUTPUT_RECORDS_totalCounterValue","REDUCE_INPUT_GROUPS_mapCounterValue",
                                        "REDUCE_INPUT_GROUPS_reduceCounterValue","REDUCE_INPUT_GROUPS_totalCounterValue",
                                        "REDUCE_SHUFFLE_BYTES_mapCounterValue","REDUCE_SHUFFLE_BYTES_reduceCounterValue",
                                        "REDUCE_SHUFFLE_BYTES_totalCounterValue","REDUCE_INPUT_RECORDS_mapCounterValue",
                                        "REDUCE_INPUT_RECORDS_reduceCounterValue","REDUCE_INPUT_RECORDS_totalCounterValue",
                                        "REDUCE_OUTPUT_RECORDS_mapCounterValue","REDUCE_OUTPUT_RECORDS_reduceCounterValue",
                                        "REDUCE_OUTPUT_RECORDS_totalCounterValue","SPILLED_RECORDS_mapCounterValue",
                                        "SPILLED_RECORDS_reduceCounterValue","SPILLED_RECORDS_totalCounterValue",
                                        "SHUFFLED_MAPS_mapCounterValue","SHUFFLED_MAPS_reduceCounterValue",
                                        "SHUFFLED_MAPS_totalCounterValue","FAILED_SHUFFLE_mapCounterValue",
                                        "FAILED_SHUFFLE_reduceCounterValue","FAILED_SHUFFLE_totalCounterValue",
                                        "MERGED_MAP_OUTPUTS_mapCounterValue","MERGED_MAP_OUTPUTS_reduceCounterValue",
                                        "MERGED_MAP_OUTPUTS_totalCounterValue","GC_TIME_MILLIS_mapCounterValue",
                                        "GC_TIME_MILLIS_reduceCounterValue","GC_TIME_MILLIS_totalCounterValue",
                                        "CPU_MILLISECONDS_mapCounterValue","CPU_MILLISECONDS_reduceCounterValue",
                                        "CPU_MILLISECONDS_totalCounterValue","PHYSICAL_MEMORY_BYTES_mapCounterValue",
                                        "PHYSICAL_MEMORY_BYTES_reduceCounterValue","PHYSICAL_MEMORY_BYTES_totalCounterValue",
                                        "VIRTUAL_MEMORY_BYTES_mapCounterValue","VIRTUAL_MEMORY_BYTES_reduceCounterValue",
                                        "VIRTUAL_MEMORY_BYTES_totalCounterValue","COMMITTED_HEAP_BYTES_mapCounterValue",
                                        "COMMITTED_HEAP_BYTES_reduceCounterValue","COMMITTED_HEAP_BYTES_totalCounterValue",
                                        "CREATED_FILES_mapCounterValue","CREATED_FILES_reduceCounterValue",
                                        "CREATED_FILES_totalCounterValue","DESERIALIZE_ERRORS_mapCounterValue",
                                        "DESERIALIZE_ERRORS_reduceCounterValue","DESERIALIZE_ERRORS_totalCounterValue",
                                        "RECORDS_IN_mapCounterValue","RECORDS_IN_reduceCounterValue","RECORDS_IN_totalCounterValue",
                                        "RECORDS_OUT_0_mapCounterValue","RECORDS_OUT_0_reduceCounterValue","RECORDS_OUT_0_totalCounterValue",
                                        "RECORDS_OUT_INTERMEDIATE_mapCounterValue","RECORDS_OUT_INTERMEDIATE_reduceCounterValue",
                                        "RECORDS_OUT_INTERMEDIATE_totalCounterValue","BAD_ID_mapCounterValue","BAD_ID_reduceCounterValue",
                                        "BAD_ID_totalCounterValue","CONNECTION_mapCounterValue","CONNECTION_reduceCounterValue",
                                        "CONNECTION_totalCounterValue","IO_ERROR_mapCounterValue","IO_ERROR_reduceCounterValue",
                                        "IO_ERROR_totalCounterValue","WRONG_LENGTH_mapCounterValue","WRONG_LENGTH_reduceCounterValue",
                                        "WRONG_LENGTH_totalCounterValue","WRONG_MAP_mapCounterValue","WRONG_MAP_reduceCounterValue",
                                        "WRONG_MAP_totalCounterValue","WRONG_REDUCE_mapCounterValue","WRONG_REDUCE_reduceCounterValue",
                                        "WRONG_REDUCE_totalCounterValue","BYTES_READ_mapCounterValue","BYTES_READ_reduceCounterValue",
                                        "BYTES_READ_totalCounterValue", "BYTES_WRITTEN_mapCounterValue","BYTES_WRITTEN_reduceCounterValue",
                                        "BYTES_WRITTEN_totalCounterValue","delayTime"

                                };
                                SimpleDateFormat dateFormat =
                                        new SimpleDateFormat("yyyy-MM-dd" + " " + "HH:mm:ss");
                                Date reqTime = new Date();
                                String endStr = request.getParameter("endTime");
                                //String slaveHost = request.getParameter("hosts");
                                String begStr = request.getParameter("startTime");
                                //session.setAttribute("slave",slaveHost);
                                if (endStr == null || endStr.equals("")) {
                                    endStr = dateFormat.format(reqTime);
                                }
                                if (begStr == null || begStr.equals("")) {
                                    begStr = dateFormat.format(
                                            new Date(reqTime.getTime() - 3600 * 1000));
                                }
                            %>
                            <html>
                            <script language="javascript" type="text/javascript" src="js/Main.js"></script>
                            <script language="javascript" type="text/javascript" src="js/WebCalendar.js"></script>

                            <head>
                                <meta http-equiv="content-type" content="text/html; charset=utf-8">
                                <link href="resources/HomePageStyle.css" rel="stylesheet" media="screen" type="text/css" charset="utf-8"></link>
                            </head>

                            <body>

                            <div id="menu">
                                <ul>
                                    <li>
                                        <form action="charts.jsp" method="POST" >
                                            <label id="deltaTimeLabel" for="deltaTime"><B>Last: </B></label>
                                            <select name="deltaTime" id="deltaTime" onchange="time_refresh()" >
                                                <option value="10" >10 min</option>
                                                <option value="30" >30 min</option>
                                                <option value="60" SELECTED>hour</option>
                                                <option value="120" >2 hour</option>
                                                <option value="180" >3 hour</option>
                                                <option value="360" >6 hour</option>
                                                <option value="1440" >day</option>
                                                <option value="2880" >2 day</option>
                                                <option value="4320" >3 day</option>
                                                <!--option value="10080" >week</option-->
                                            </select>

                                            <label id="startTimeLabel" for="startTime"><B>From: </B></label>
                                            <input name="startTime" id="startTime" type="text" class="form_css"
                                                   value="<% out.print(begStr); %>"
                                                   onclick="SelectDate(this,'yyyy-MM-dd hh:mm:ss',0,0)"
                                                   onchange="time_check()" />

                                            <label id="endTimeLabel" for="endTime"><B>To: </B></label>
                                            <input name="endTime" id="endTime" type="text" class="form_css"
                                                   value="<% out.print(endStr); %>"
                                                   onclick="SelectDate(this,'yyyy-MM-dd hh:mm:ss',0,0)"
                                                   onchange="time_check()" />

                                            <!--label id="endTimeLabel" for="endTime"><B>To: </B></label-->
                                            <select name="orderby">
                                                <%
                                                    for (int i = 0; i < clusters.length; i++) {
                                                        out.println("<option value=\"" + clusters[i] + "\" selected>"
                                                                + clusters[i]+ "</option>");
                                                    }
                                                %>
                                            </select>
                                            <input id="sub_button" type="submit" value="submit" />

                                    </li>
                                    <style type="text/css">
                                        .ct{
                                            text-align:center;
                                        }
                                    </style>


                                    <body>




                                    </body>
                            </html>


                        </ul>

                    </div>
</body>
</html>
