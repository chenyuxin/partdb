/**
 * 
 */
 
import { CommonJs } from './common/common.js';

const url = CommonJs.localUrl + '/partdb/executesql';


function executeSql(){
	const sql = document.getElementById('sql').value;
	const data = {sql: sql };
	const responseText = CommonJs.post(data,url);
	console.log(responseText);
}


document.getElementById("button").onclick = function() {
 	executeSql();
}