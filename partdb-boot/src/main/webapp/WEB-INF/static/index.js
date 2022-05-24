/**
 * 
 */
 
import { CommonJs } from './common/common.js';

const url = window.location.protocol + '//' + window.location.host + '/partdb/executesql';



function executeSql(){
	let sql = document.getElementById('sql').value;
	const data = {sql: sql };
	const responseText = CommonJs.post(data,url);
	console.log(responseText);
}


document.getElementById("button").onclick = function() {
 	executeSql();
}