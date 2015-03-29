<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Utt Report page</title>
</head>
<body>

	<h1>Report Page</h1>
	<p>Click the links below:</p>
	<c:url value="/main/showpiechart?type=pdf" var="showPiePdf" />
	<a href="${showPiePdf}">Show Pie Chart as PDF format</a>
	<br />
	<c:url value="/main/showpiechart?type=html" var="showPieHtml" />
	<a href="${showPieHtml}">show Pie Chart as HTML format</a>
	<br />
	<c:url value="/main/showbarchart?type=pdf" var="showBarPdf" />
	<a href="${showBarPdf}">Show Bar Chart as PDF format</a>
	<br />
	<c:url value="/main/showbarchart?type=html" var="showBarHtml" />
	<a href="${showBarHtml}">show Bar Chart as HTML format</a>
	<br />
	${reportBody}
</body>
</html>