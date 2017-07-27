<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<head runat="server">
    <title>setTimeout</title>
</head>
<body>



<%
    out.println("<script type=\"text/javascript\">\n" +
            "    var alertObj = new Object();\n" +
            "    var generalStyle = {\n" +
            "        zIndex: 0,\n" +
            "        width: \"500px\",\n" +
            "        height: \"100px\",\n" +
            "        border: \"thick solid #CCCCCC\",\n" +
            "        background: \"#FFFFFF\",\n" +
            "        position: \"absolute\",\n" +
            "        top: \"15%\",\n" +
            "        left: \"20%\"\n" +
            "    }\n" +
            "    var txtStyle = {\n" +
            "        textAlign: \"center\"\n" +
            "    }\n" +
            "    var btnStyle = {\n" +
            "        position: \"absolute\",\n" +
            "        left: \"40%\",\n" +
            "        top: \"70%\",\n" +
            "        color: \"#333333\",\n" +
            "        fontWeight: \"bold\",\n" +
            "        outlineColor:\"#3366FF\",\n" +
            "        outlineStyle:\"ridge\",\n" +
            "        outlineWidth:\"thin\",\n" +
            "//outline: \"thin ridge #3366FF\",\n" +
            "        innerHTML: \"OK\"\n" +
            "    }\n" +
            "    alertObj = {\n" +
            "        generalSet: generalStyle,\n" +
            "        txtSet: txtStyle,\n" +
            "        btnSet: btnStyle,\n" +
            "        isExist: false\n" +
            "    }\n" +
            "    alertObj.createComponent = function() {\n" +
            "        var component = document.createElement(arguments[0]);\n" +
            "        var styles = arguments[1];\n" +
            "        for (var property in styles) {\n" +
            "            if (styles[property] != null) {\n" +
            "                try{\n" +
            "                    component.style[property] = styles[property];\n" +
            "                }catch(err){\n" +
            "                    document.write(err.name+\":\"+property+\"<br/>\");//set property error!\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        return component;\n" +
            "    }\n" +
            "    alertObj.show = function() {\n" +
            "        if(!this.isExist){\n" +
            "            this.isExist = true;\n" +
            "            var bodyObj = document.body;\n" +
            "            bodyObj.style.zIndex = -1;\n" +
            "            bodyObj.style.background = \"#999999\";\n" +
            "            var divObj = this.createComponent(\"div\", this.generalSet);\n" +
            "\n" +
            "            var txtObj = this.createComponent(\"p\", this.txtSet);\n" +
            "            txtObj.innerHTML = arguments[0];\n" +
            "            var btnObj = this.createComponent(\"button\", this.btnSet);\n" +
            "            btnObj.innerHTML = this.btnSet.innerHTML;\n" +
            "            btnObj.onclick = function() {\n" +
            "                bodyObj.style.zIndex=0;\n" +
            "                bodyObj.style.background=\"\";\n" +
            "                bodyObj.removeChild(divObj);\n" +
            "                if(alertObj.isExist){\n" +
            "                    alertObj.isExist = false;\n" +
            "                }\n" +
            "                window.location.href=\"/\"; \n" +
            "            }\n" +
            "            divObj.appendChild(txtObj);\n" +
            "            divObj.appendChild(btnObj);\n" +
            "            bodyObj.appendChild(divObj);\n" +
            "        }\n" +
            "    }\n" +
            "    function show(s) {\n" +
            "        alertObj.show(s);\n" +
            "    }\n" +
            "</script>");
    String message = "注意忘记选metric或者忘记选host或者忘记选时间了,请返回重新选!!!!!!因为没选不会出图，记得选..";

    out.println("<SCRIPT LANGUAGE='JavaScript'>");
    out.println("<!--");
    out.println("show('" + message + "')");
    out.println("//-->");
    out.println("</SCRIPT></body>");
    out.flush();
    Thread.sleep(3000);
    response.sendRedirect("");
    out.println("1234");
    out.flush();

%>
</body>
</html>
