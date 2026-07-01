
<%@page import="beans.NalogPodrska"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="col-sm-3">
	<h2>Informacije:</h2>
	<ul class="nav nav-pills flex-column">
		<li class="nav-item">Ime: &nbsp; <%=((NalogPodrska) (session.getAttribute("nalogPodrskaBean"))).getIme()%><br />
		</li>
		<li class="nav-item">Prezime: &nbsp; <%=((NalogPodrska) (session.getAttribute("nalogPodrskaBean"))).getPrezime()%><br />
		</li>
		<li class="nav-item">Vrijeme: &nbsp;<%=new java.util.Date()%><br />
		</li>

	</ul>
	<hr class="d-sm-none">
</div>




