'use strict';
 
function countdown(due) {
	const now = new Date();
	const month = now.getMonth() + 1;
	const rest = due.getTime() - now.getTime();
	const sec = Math.floor(rest / 1000) % 60;
	const min = Math.floor(rest / 1000 / 60) % 60;
	const hours = Math.floor(rest / 1000 / 60 / 60) % 24;
	const days = Math.floor(rest / (1000 * 60 * 60 * 24));
	const count = [month, days, hours, min, sec];
 
	return count;
}
 
let goal;
let product;
let flag = false;
 
if (end == null) {
	goal = new Date(localStorage.getItem('goal'));
	product = localStorage.getItem('discountproduct');
} else {
	goal = new Date();
	goal.setDate(Number(end[2]));
	goal.setHours(Number(end[3]));
	goal.setMinutes(Number(end[4]));
	goal.setSeconds(Number(end[5]));
	product = discountproduct;
	localStorage.setItem('goal', goal);
	localStorage.setItem('endmonth', end[1]);
	localStorage.setItem('discountproduct', product);
}
 
 
function recalc() {
	const counter = countdown(goal);
 
	if (counter[1] == 0 && counter[2] == 0 && counter[3] == 0 && (counter[4] == 0 || counter[4] == 1)) {
		var x = document.getElementById("timer");
		x.remove();
 
		var form = document.createElement('form');
		form.action = '/timesalefinish';
		form.method = 'POST';
 
		// body に追加
		document.body.append(form);
 
		// submit
		form.submit();
		flag = true;
		return;
 
	} else if (counter[1] >= 0 || counter[2] >= 0 || counter[3] >= 0 || counter[4] >= 0 && flag == false) {
		const time = `<タイムセール中:${counter[1]}日${counter[2]}時間${counter[3]}分${counter[4]}秒>対象商品:${product} `;
		document.getElementById('timer').textContent = time;
		refresh();
	}
 
}
 
function refresh() {
	if(flag == false){
		console.log(flag);
	setTimeout(recalc, 1000);
	}else{
		return;
	}
}
 
if (flag == false) {
	recalc();
}