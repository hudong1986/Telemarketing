<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线播放录音</title>
</head>
<body>
   <c:if test="${filepath.contains('.mp3') }">
	<audio controls>
		<source src="${filepath }" type="audio/mpeg">您的浏览器不支持 audio
			元素,无法播放当前音频，请使用Microsoft Edge或chrome6+浏览器。
	</audio>
	</c:if>
	<c:if test="${filepath.contains('.wav') }">
	<audio controls>
		<source src="${filepath }" type="audio/wav">您的浏览器不支持 audio
			元素,无法播放当前音频，请使用Microsoft Edge或chrome6+浏览器。
	</audio>
	</c:if>
</body>
</html>