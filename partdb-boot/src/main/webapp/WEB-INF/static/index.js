/**
 * 
 */
 
import { CommonJs } from './common/common.js';

const url = 'http://localhost:8765/partdb/executesql';

function executeSql(){
	let sql = document.getElementById('sql').value;
	const data = {sql: sql };
	const responseText = CommonJs.post(data,url);
	console.log(responseText);
}

