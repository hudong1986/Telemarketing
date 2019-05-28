<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.HashSet" %>

	   技术支持：胡工  邮箱：1171579337@qq.com 联系电话：18782404805 在线人数:<% 
HashSet sessions=(HashSet)application.getAttribute("sessions");
out.print(sessions.size());
%>
