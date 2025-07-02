//タイムセールの終了時間の制限をかける関数
window.onload = function() {
	var getToday = new Date();
	var date = new Date();
	var y = getToday.getFullYear();
	var m = getToday.getMonth() + 1;
	var d = getToday.getDate();
	var h = getToday.getHours();
	var min = getToday.getMinutes();
	var today = y + "-" + m.toString().padStart(2, '0') + "-" +
		d.toString().padStart(2, '0') + "T" + h.toString().padStart(2, '0') + ":" +
		min.toString().padStart(2, '0') + ":" + 00;
 
	date.setMonth(date.getMonth() + 1);
	date.setDate(0);
	let endOfMonth = new Date(date);
	
	var ey = endOfMonth.getFullYear();
	var em = endOfMonth.getMonth() + 1;
	var ed = endOfMonth.getDate();
	var eh = endOfMonth.getHours();
	var emin = endOfMonth.getMinutes();
	
	var max = ey + "-" + em.toString().padStart(2, '0') + "-" +
		ed.toString().padStart(2, '0') + "T" + eh.toString().padStart(2, '0') + ":" +
		emin.toString().padStart(2, '0') + ":" + 00;
	
	
	document.getElementById("datepicker2").setAttribute("min", today);
	document.getElementById("datepicker2").setAttribute("max", max);
}