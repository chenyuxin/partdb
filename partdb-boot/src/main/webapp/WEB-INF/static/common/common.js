/**
 * 通用js
 */
class CommonJs {
	
	
	
	
	
	
	/**
	 * @param reqParam 请求参数
	 * @param url 请求url
	 * @return 返回响应
	 */
	static post(reqParam,url) {
		const HTTP = new XMLHttpRequest();
		HTTP.open('POST',url,false);//传入false《同步》请求
		HTTP.setRequestHeader('Content-Type', 'application/json');
		let responseData = null; 
		HTTP.send(JSON.stringify(reqParam));
		if (HTTP.readyState === 4 && HTTP.status === 200) {
			if( HTTP.responseText ){
				responseData = JSON.parse(HTTP.responseText);
			}
		}
		return responseData;
	}	
	
	
	
}

export { CommonJs }