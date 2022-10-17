<%@page import="model.ModelLogin"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!-- TODAS AS P�GINAS DO SISTEMA PODEM SEGUIR ESSA ESTRUTURA -->

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
														<h4 class="sub-title">Cadastro de usu�rio</h4>
														<form class="form-material" enctype="multipart/form-data" action="<%= request.getContextPath() %>/ServletUsuarioController" method="post" id="formUser">
															
															<input type="hidden" name="acao" id="acao" value="">
															
                                                            <div class="form-group form-default form-static-label">
                                                                <input type="text" name="id" id="id" class="form-control" readonly="readonly" value="${modelLogin.id}">
                                                                <span class="form-bar"></span>
                                                                <label class="float-label">Id</label>
                                                            </div>
                                                            
                                                            <div class="form-group form-default input-group mb-4">
                                                            	<div class="input-group-prepend">
	                                                            	<c:if test="${modelLogin.fotoUser != '' && modelLogin.fotoUser != null}">
	                                                            		<img alt="Imagem usu�rio" id="fotoembase64" src="${modelLogin.fotoUser}" width="70px">
	                                                            	</c:if>
	                                                            	<c:if test="${modelLogin.fotoUser == '' || modelLogin.fotoUser == null}">
	                                                            		<img alt="Imagem usu�rio" id="fotoembase64" src="assets/images/avatar-blank.jpg" width="70px">
	                                                            	</c:if>
                                                            	</div>
                                                            	<input type="file" id="fileFoto" name="fileFoto" accept="image/*" onchange="visualizarImg('fotoembase64', 'fileFoto');" class="form-control-file" style="margin-top: 15px; margin-left: 5px">
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
	                                                            <select class="form-control" aria-label="Default select example" name="perfil">
																  <option disabled="disabled">[Selecione o perfil]</option>
																  
																  <option value="ADMIN" <%
																	  
																	  ModelLogin modelLogin = (ModelLogin) request.getAttribute("modelLogin");
																	  
																	  if(modelLogin != null && modelLogin.getPerfil().equals("ADMIN")) {
																		  out.print("");
																		  out.print("selected=\"selected\"");
																		  out.print("");
																	  } %>>Admin</option>
																  
																  <option value="SECRETARIA" <%
																	  
																	  modelLogin = (ModelLogin) request.getAttribute("modelLogin");
																	  
																	  if(modelLogin != null && modelLogin.getPerfil().equals("SECRETARIA")) {
																		  out.print("");
																		  out.print("selected=\"selected\"");
																		  out.print("");
																	  } %>>Secret�ria</option>
																	  
																  <option value="AUXILIAR" <%
																	  
																	  modelLogin = (ModelLogin) request.getAttribute("modelLogin");
																	  
																	  if(modelLogin != null && modelLogin.getPerfil().equals("AUXILIAR")) {
																		  out.print("");
																		  out.print("selected=\"selected\"");
																		  out.print("");
																	  } %>>Auxiliar</option>
																</select>
																<span class="form-bar"></span>
                                                                <label class="float-label">Perfil:</label>
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
                                                            
                                                            <div class="form-group form-default form-static-label">
                                                            	<input type="radio" name="sexo" checked="checked" value="MASCULINO" <%
                                                            			modelLogin = (ModelLogin) request.getAttribute("modelLogin");
                                                            	
			                                                    			if(modelLogin != null && modelLogin.getSexo().equals("MASCULINO")) {
																			  out.print("");
																			  out.print("checked=\"checked\"");
																			  out.print("");
			                                                			}
                                                    			
                                                            	%>>Masculino</>
                                                            	<input type="radio" name="sexo" value="FEMININO" <%
                                                            			modelLogin = (ModelLogin) request.getAttribute("modelLogin");
                                                            	
	                                                            			if(modelLogin != null && modelLogin.getSexo().equals("FEMININO")) {
																			  out.print("");
																			  out.print("checked=\"checked\"");
																			  out.print("");
                                                            			}
	                                                            			
                                                            	%>>Feminino</>
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
										<div style="height: 300px; overflow: scroll;">
											<table class="table" id="tabelaResultadosview">
												<thead>
													<tr>
														<th scope="col">Id</th>
														<th scope="col">Nome</th>
														<th scope="col">Ver</th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${modelLogins}" var="ml">
														<tr>
															<td><c:out value="${ml.id}"></c:out></td>
															<td><c:out value="${ml.nome}"></c:out></td>
															<td><a class="btn btn-success" href="<%= request.getContextPath() %>/ServletUsuarioController?acao=buscarEditar&id=${ml.id}">Ver</a></td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
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
        <h5 class="modal-title" id="exampleModalLabel">Pesquisa de Usu�rio</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
		<div class="input-group mb-3">
		  <input type="text" class="form-control" placeholder="Nome do usu�rio" aria-label="nome" id="nomeBusca" aria-describedby="basic-addon2">
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


	function visualizarImg(fotoBase64, fileFoto) {
		
		var preview = document.getElementById(fotoBase64);
		var fileUser = document.getElementById(fileFoto).files[0];
		
		var reader = new FileReader();
		
		reader.onloadend = function (){
			preview.src = reader.result; //carrega a foto na tela
		};
		
		if (fileUser) {
			reader.readAsDataURL(fileUser); //preview da img
		} else {
			preview.src = '';
		}
	}
	
	function verEEditar(id) {
		var urlAction = document.getElementById("formUser").action;
		
		window.location.href = urlAction + '?acao=buscarEditar&id=' + id;
	}
	
	function buscarUsuario() {
		var nomeBusca = document.getElementById('nomeBusca').value;
		
		if (nomeBusca != null && nomeBusca != '' && nomeBusca.trim() != '') { /*Validando que o campo estar� preenchido para buscar no DB.*/
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
								'</td> <td><button onclick="verEEditar('+ json[pos].id + ')" type="button" class="btn btn-info">Ver</button></td> </tr>');
					}
					
					document.getElementById('totalResultados').textContent = "Resultado: " + json.length + " usu�rios encontrados!"
				}
				
			}).fail(function(xhr, status, errorThrown){
				alert("Erro ao buscar usu�rio por nome: " + xhr.responseText);
			});
			
		}
	}
	
	function criarDeleteComAjax() {
		if (confirm("AVISO: Esta a��o n�o pode ser desfeita!\nDeseja realmente excluir seus dados da nossa base de dados?")) {
			
			var urlAction = document.getElementById("formUser").action;
			var idUser = document.getElementById("id").value;
			
	//o fail() � chamado em caso de erros na execu��o, xhr traz os detalhes do erro, status tras o status do erro, e errorThrown, identificando qual foi a exce��o lan�ada.
			$.ajax({
				
				method: "get",
				url: urlAction,
				data: 'id=' + idUser + '&acao=deletarajax',
				success: function(response) {
					limparForm();
					document.getElementById('msg').textContent = response;
				}
				
			}).fail(function(xhr, status, errorThrown){
				alert("Erro ao deletar usu�rio por id: " + xhr.responseText);
			});
			
		}
	}
	
	function criarDelete() {
		
		if (confirm("AVISO: Esta a��o n�o pode ser desfeita!\nDeseja realmente excluir seus dados da nossa base de dados?")) {
			
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
