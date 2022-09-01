<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


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
										<!-- task, page, download counter  start -->

										<div class="row">
											<div class="col-sm-12">
												<!-- Basic Form Inputs card start -->
												<div class="card">
													<div class="card-block">
														<h4 class="sub-title">Cadastro de usuário</h4>
														<form class="form-material" action="<%= request.getContextPath() %>/ServletUsuarioController" method="post" id="formUser">
															
															<input type="hidden" name="acao" id="acao" value="">
															
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="text" name="id" id="id" class="form-control" readonly="readonly" value="${modelLogin.id}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Id</label>
                                                            </div>
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="text" name="nome" id="nome" class="form-control" required="required" value="${modelLogin.nome}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Nome</label>
                                                            </div>
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="email" name="email" id="email" class="form-control" required="required" autocomplete="off" value="${modelLogin.email}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">E-mail</label>
                                                            </div>
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="text" name="login" id="login" class="form-control" required="required" value="${modelLogin.login}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Login</label>
                                                            </div>
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="password" name="senha" id="senha" class="form-control" required="required" autocomplete="off" value="${modelLogin.senha}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Senha</label>
                                                            </div>
                                                            
                                                        <button type="button" class="btn btn-primary waves-effect waves-light" onclick="limparForm();">Novo</button>
                                                        <button class="btn btn-success waves-effect waves-light">Salvar</button>
											            <button type="button" class="btn btn-info waves-effect waves-light" onclick="criarDeleteComAjax();">Excluir</button>
														<button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#modalUsuario">Pesquisar</button>
                                                        </form>

													</div>
												</div>
											</div>
										</div>
										<span id="msg">${msg}</span>
										<!--  project and team member end -->
								</div>
								<!-- Page-body end -->
							</div>
							<div id="styleSelector"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

	<jsp:include page="javascriptfile.jsp"></jsp:include>
	
	<!-- Modal -->
<div class="modal fade" id="modalUsuario" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">Pesquisa de Usuário</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
		<div class="input-group mb-3">
		  <input type="text" class="form-control" placeholder="Nome do usuário" aria-label="nome" id="nomeBusca" aria-describedby="basic-addon2">
		  <div class="input-group-append">
		    <button class="btn btn-primary" type="button" onclick="buscarUsuario();">Buscar</button>
		  </div>
		</div>
		
		<div style="height: 300px; overflow: scroll;">
			<table class="table" id="tabelaResultados">
			  <thead>
			    <tr>
			      <th scope="col">Id</th>
			      <th scope="col">Nome</th>
			      <th scope="col">Ver</th>
			    </tr>
			  </thead>
			  <tbody>
			   
			  </tbody>
			</table>
		</div>
			<span id="totalResultados"></span>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
      </div>
    </div>
  </div>
</div>
	
<script type="text/javascript">
	
	function buscarUsuario() {
		var nomeBusca = document.getElementById('nomeBusca').value;
		
		if (nomeBusca != null && nomeBusca != '' && nomeBusca.trim() != '') { /*Validando que o campo estará preenchido para buscar no DB.*/
			var urlAction = document.getElementById("formUser").action;
			
			$.ajax({
				
				method: "get",
				url: urlAction,
				data: 'nomeBusca=' + nomeBusca + '&acao=buscarUserAjax',
				success: function(response) {
					var json = JSON.parse(response);
					
					$('#tabelaResultados > tbody > tr').remove();
					
					for (var pos = 0; pos < json.length; pos++) {
						$('#tabelaResultados > tbody').append('<tr> <td>' + json[pos].id +'</td> <td>'+ json[pos].nome +
								'</td> <td><button type="button" class="btn btn-info">Ver</button></td> </tr>');
					}
					
					document.getElementById('totalResultados').textContent = "Resultado: " + json.length + " usuários encontrados!"
				}
				
			}).fail(function(xhr, status, errorThrown){
				alert("Erro ao buscar usuário por nome: " + xhr.responseText);
			});
			
		}
	}
	
	function criarDeleteComAjax() {
		if (confirm("AVISO: Esta ação não pode ser desfeita!\nDeseja realmente excluir seus dados da nossa base de dados?")) {
			
			var urlAction = document.getElementById("formUser").action;
			var idUser = document.getElementById("id").value;
			
	//o fail() é chamado em caso de erros na execução, xhr traz os detalhes do erro, status tras o status do erro, e errorThrown, identificando qual foi a exceção lançada.
			$.ajax({
				
				method: "get",
				url: urlAction,
				data: 'id=' + idUser + '&acao=deletarajax',
				success: function(response) {
					limparForm();
					document.getElementById('msg').textContent = response;
				}
				
			}).fail(function(xhr, status, errorThrown){
				alert("Erro ao deletar usuário por id: " + xhr.responseText);
			});
			
		}
	}
	
	function criarDelete() {
		
		if (confirm("AVISO: Esta ação não pode ser desfeita!\nDeseja realmente excluir seus dados da nossa base de dados?")) {
			
		document.getElementById("formUser").method ='get';
		document.getElementById("acao").value = 'deletar';
		document.getElementById("formUser").submit();
		}
	}
	
	function limparForm() {

		var elementos = document.getElementById("formUser").elements; /*Retorna um array de elementos html dentro do form*/
		
		for (pos = 0; pos < elementos.length; pos++) {
			elementos[pos].value = '';
		}
	}
</script>
</body>

</html>
