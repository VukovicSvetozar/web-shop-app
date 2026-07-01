<%@ page import="beans.NalogPodrska"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:useBean id="nalogPodrskaBean" class="beans.NalogPodrska"
	scope="session"></jsp:useBean>
<jsp:useBean id="nalogPodrskaService"
	class="service.NalogPodrskaService" scope="application"></jsp:useBean>
<jsp:setProperty property="korisnickoIme" name="nalogPodrskaBean"
	param="korisnickoIme" />
<jsp:setProperty property="lozinka" name="nalogPodrskaBean"
	param="lozinka" />

<!DOCTYPE html>

<html>

<head>

<title>Korisnicka podrska</title>

<meta http-equiv="content-type"
	content="text/html;charset=utf-8 width=device-width, initial-scale=1" />

<link rel="shortcut icon" href="favicon.ico" />
<link rel="stylesheet" type="text/css" href="css/prijava.css" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ"
	crossorigin="anonymous" />

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript" src="js/prijava.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js"
	integrity="sha384-zYPOMqeu1DAVkHiLqWBUTcbYfZ8osu1Nd6Z89ify25QV9guujx43ITvfi12/QExE"
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.min.js"
	integrity="sha384-Y4oOpwW3duJdCWv5ly8SCFYWqFDsfob/3GkgExXKV4idmbt98QcxXYs9UoXAB7BZ"
	crossorigin="anonymous"></script>

</head>

<body>

	<%
		if (request.getParameter("prijava") != null) {
			NalogPodrska nalog = nalogPodrskaService.provjeraKredencijala(nalogPodrskaBean.getKorisnickoIme(),
			nalogPodrskaBean.getLozinka());
			if (nalog != null) {
		nalogPodrskaBean.setId(nalog.getId());
		nalogPodrskaBean.setIme(nalog.getIme());
		nalogPodrskaBean.setPrezime(nalog.getPrezime());
		nalogPodrskaBean.setUloga(nalog.getUloga());
		nalogPodrskaBean.setPrijavljen(true);
	%>
	<script>
		var redirekcijaURL = "poruke.jsp";
		uspjesnaPrijava('<%=nalogPodrskaBean.getKorisnickoIme()%>', '<%=nalogPodrskaBean.getLozinka()%>');
		
	</script>
	<%
	} else {
	%>
	<script>
		neuspjesnaPrijava();
	</script>
	<%
	nalogPodrskaBean.setPrijavljen(false);
	}
	}
	%>

	<div id="mainContentArea">
		<div id="contentBox">
			<div id="blueBox">
				<div class="wrapper">
					<div class="text-center mt-4 name">Korisnicka podrska</div>
					<form class="p-3 mt-3" action="index.jsp" method="post">
						<div class="form-field d-flex align-items-center">
							<span class="far fa-user"></span> <input type="text"
								name="korisnickoIme" id="korisnickoIme"
								placeholder="korisnicko ime" />
						</div>
						<div class="form-field d-flex align-items-center">
							<span class="fas fa-key"></span> <input type="password"
								name="lozinka" id="lozinka" placeholder="lozinka" />
						</div>
						<button class="btn mt-3" id="prijava" name="prijava">Prijava</button>
						<br />
					</form>
					<div id="alert-prijava-neuspjesno" class="alert alert-danger"
						style="display: none;">Unijeli ste neispravno
						korisni&#269;ko ime ili lozinku.</div>
				</div>
			</div>
		</div>
	</div>

</body>

</html>
