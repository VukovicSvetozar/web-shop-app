
<%@include file="/WEB-INF/pages/header.jsp"%>

<div class="container-fluid" style="margin-top: 30px">

	<div class="row">


		<div class="col-sm-4 col-xd-4  proba">

		<%@include file="/WEB-INF/pages/info.jsp"%>	

			<div id="filtriraj-naslov">
				<h2>Filtriraj</h2>
			</div>

			<div class="row">
				<div class="row justify-content-center">
					<div>
						<label for="datumOd">Datum od:</label> <input
							type="datetime-local" id="datumOd" name="datumOd">
					</div>
					<div>
						<label for="datumDo">Datum do:</label> <input
							type="datetime-local" id="datumDo" name="datumDo">
					</div>
				</div>
				<div class="row justify-content-center">
					<div id="alert-datum-danger" class="alert alert-danger"
						style="display: none;" role="alert">Odabrana vremena nisu
						pravilno uskladjena.</div>
					<div id="alert-datum-success" class="alert alert-success"
						style="display: none;" role="alert">Odabrana vremena su
						pravilno uskladjena.</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckInfo"> <label class="form-check-label"
							for="flexSwitchCheckDefault">INFO</label>
					</div>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckTrace"> <label class="form-check-label"
							for="flexSwitchCheckDefault">TRACE</label>
					</div>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckDebug"> <label class="form-check-label"
							for="flexSwitchCheckDefault">DEBUG</label>
					</div>
				</div>
				<div class="col-sm-6">
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckWarn"> <label class="form-check-label"
							for="flexSwitchCheckDefault">WARN</label>
					</div>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckError"> <label class="form-check-label"
							for="flexSwitchCheckDefault">ERROR</label>
					</div>
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox"
							id="switchCheckFatal"> <label class="form-check-label"
							for="flexSwitchCheckDefault">FATAL</label>
					</div>
				</div>
			</div>
			<div class="row">
				<button id="trazi-statistiku-button" type="button"
					class="btn btn-primary">Posalji</button>
			</div>
		
		</div>
		
		<div class="col-sm-8">
			<div class="row kontejner-lista-logovi">
				<ul id="listaLogova" class="list-group">
				</ul>
			</div>
		</div>
	</div>
</div>

<%@include file="/WEB-INF/pages/footer.jsp"%>
