<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>


<!-- TODAS AS PÁGINAS DO SISTEMA PODEM SEGUIR ESSA ESTRUTURA -->
    
<!DOCTYPE html>
<html lang="en">

<jsp:include page="head.jsp"></jsp:include>

  <body>
  <!-- Pre-loader start -->
  
  <jsp:include page="theme-loader.jsp"></jsp:include>
  
  <!-- Pre-loader end -->
  <div id="pcoded" class="pcoded">
      <div class="pcoded-overlay-box"></div>
      <div class="pcoded-container navbar-wrapper">
          
          <jsp:include page="navbar.jsp"></jsp:include>

          <div class="pcoded-main-container">
              <div class="pcoded-wrapper">
                  
                  <jsp:include page="navbarmainmenu.jsp"></jsp:include>
                  
                  <div class="pcoded-content">
                      <!-- Page-header start -->
                      <jsp:include page="page-header.jsp"></jsp:include>
                      <!-- Page-header end -->
                        <div class="pcoded-inner-content">
                            <!-- Main-body start -->
                            <div class="main-body">
                                <div class="page-wrapper">
                                    <!-- Page-body start -->
                                    <div class="page-body">
										<div class="row">
											<div class="col-sm-12">
												<!-- Basic Form Inputs card start -->
												<div class="card">
													<div class="card-block">
														<h4 class="sub-title">Relatório de usuário</h4>														
														<form class="form-material" action="<%= request.getContextPath() %>/ServletUsuarioController" method="get" id="formUser">
															
															<input type="hidden" id="acaoImprimirRelatorioTipo" name="acao" value="imprimirRelatorioUser">
															
															<div class="form-row align-items-center">
																
																<div class="col-auto">
																	<label class="sr-only" for="dataInicial">Data Inicial</label>
																	<input type="text" class="form-control mb-2"
																		id="dataInicial" name="dataInicial" placeholder="Data Inicial" value="${dataInicial}">
																</div>
																
																<div class="col-auto">
																	<label class="sr-only" for="dataFinal">Data Final</label>
																	<input type="text" class="form-control mb-2"
																		id="dataFinal" name="dataFinal" placeholder="Data Final" value="${dataFinal}">
																</div>
																
																<div class="col-auto">
																	<button type="button" onclick="gerarGrafico();" class="btn btn-primary mb-2">Gerar gráfico</button>
																</div>
															</div>
														</form>

														<div style="height: 600px; overflow: scroll;">
															<div>
																<canvas id="myChart"></canvas>
															</div>
														</div>

													</div>
												</div>
											</div>
										</div>
									</div>
                                    <!-- Page-body end -->
                                </div>
                                <div id="styleSelector"> </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
        
    <jsp:include page="javascriptfile.jsp"></jsp:include>
    
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javascript">

function gerarGrafico() {
	
	var myChart = new Chart(
		document.getElementById('myChart'),
		{
			type: 'line',
		    data: {
		      labels: [
		    	  'Red',
		    	  'Blue',
		    	  'Yellow',
		    	  'Green',
		    	  'Purple',
		    	  'Orange',
		    	  ],
		      datasets: [{
		        label: 'Gráfico de média salarial',
		        backgroundColor: 'rgb(255, 99, 132)',
		        borderColor: 'rgb(255, 99, 132)',
		        data: [12, 19, 3, 5, 2, 3, 7],
		        borderWidth: 1,
		      }]
		    },
		    options: {}
	  	}
	);
}

$( function() {
	  
	  $("#dataInicial").datepicker({
		    dateFormat: 'dd/mm/yy',
		    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
		    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
		    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
		    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		    nextText: 'Próximo',
		    prevText: 'Anterior'
		});
} );

$( function() {
	  
	  $("#dataFinal").datepicker({
		    dateFormat: 'dd/mm/yy',
		    dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
		    dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
		    dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
		    monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		    monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set','Out','Nov','Dez'],
		    nextText: 'Próximo',
		    prevText: 'Anterior'
		});
} );


</script>
</body>

</html>
