
function get_value(name) {
	if (document.getElementsByName(name).length > 0) {
		return document.getElementsByName(name)[0].value;
	}
	else {
		return "";
	}
}

function set_value(name, value) {
	if (document.getElementsByName(name).length > 0) {
		document.getElementsByName(name)[0].value = value;
	}
}

//convertTime("2008-08-07 12:09:06");
function convertTime(string) {
	//alert("convertTime(string)");
	
	var pattern = /[-\s:]/;
	var result = string.split(pattern);
	var date = new Date();
	date.setFullYear(result[0]);
	//alert(result[0]);
	date.setMonth(result[1] - 1);
	//alert(result[1]);
	date.setDate(result[2]);
	//alert(result[2]);
	date.setHours(result[3]);
	//alert(result[3]);
	date.setMinutes(result[4]);
	//alert(result[4]);
	date.setSeconds(result[5]);
	//alert(result[5]);
	
	//alert(date.getTime());
	return date.getTime();
}

function convertString(time) {
	var date = new Date(time);
	
	var yyyy = date.getFullYear();
	var MM = date.getMonth() + 1;
	if (MM < 10)
		MM = "0" + MM;
	var dd = date.getDate();
	if (dd < 10)
		dd = "0" + dd;
	var HH = date.getHours();
	if (HH < 10)
		HH = "0" + HH;
	var mm = date.getMinutes();
	if (mm < 10)
		mm = "0" + mm;
	var ss = date.getSeconds();
	if (ss < 10)
		ss = "0" + ss;
	
	// "yyyy-MM-dd HH:mm:ss"
	return (yyyy + "-" + MM + "-" + dd + " " + HH + ":" + mm + ":" + ss);
}

function time_check() {
	var startTime = convertTime(document.getElementById("startTime").value);
	var endTime = convertTime(document.getElementById("endTime").value);
	if (startTime == NaN || endTime == NaN || endTime < startTime) {
		alert("时间设定错误 :(");
		return;
	}
}

function time_refresh() {
	var endTime = new Date().getTime();
	var deltaTime = 
		document.getElementById("deltaTime").value * 60 * 1000;
	
	document.getElementById("startTime").value = 
		convertString(endTime - deltaTime);
	document.getElementById("endTime").value = 
		convertString(endTime);
	
	time_check();
}
