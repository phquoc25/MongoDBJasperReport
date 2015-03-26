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
	<c:url value="/main/download?type=pdf" var="downloadPdf" />
	<a href="${downloadPdf}">Show PDF format</a>
	<br />
	<c:url value="/main/download?type=html" var="downloadHtml" />
	<a href="${downloadHtml}">show HTML format</a>
	<br />
</body>
</html>