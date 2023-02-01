<%@page import="model.ModelTelefone"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
  
<style>
 .tabela tbody tr:nth-child(even){
	background-color: #bee5eb;
}

 .tabela tbody tr:nth-child(odd){
	background-color: #c3e6cb;
}

</style>

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

										 <!-- Data e Hora -->
											<div class="row align-items-end pb-3">
												<div class="col"><h5>Telefone</h5></div>
												<div class="col"></div>
												<div class="col"><h5 id="time"></h5></div>
											</div>
											
												<div style="height: 300px; overflow: scroll;">
													<table class="table tabela"
														id="tabelaResultadosview">
														<thead>
															<tr class="table-secondary">
																<th scope="col">Nome do Usuário</th>
																<th scope="col">Número do Telefone</th>
																<th scope="col">Status</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach items="${telefones}" var="f">
																<tr>
																	<td><c:out value="${modelLogin.nome}"></c:out></td>
																	<td><c:out value="${f.numero}"></c:out></td>
																	<td><c:out value="${f.status}"></c:out></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>

												<div class="py-3">
													<button class="btn btn-primary" type="button"
														data-toggle="collapse" data-target="#cadastroFone"
														aria-expanded="false" aria-controls="collapseExample">Cadastrar
														telefone</button>
												</div>
												
												<div class="collapse" id="cadastroFone">
													<div class="row">
														<div class="col-sm-12">
															<!-- Basic Form Inputs card start -->
															<div class="card">
																<div class="card-block">
																	<h4 class="sub-title">Cadastro de telefone</h4>
																	<form class="form-material"
																		action="<%= request.getContextPath() %>/ServletTelefoneStatus"
																		method="post" id="formFone">

																		<div class="form-group form-default form-static-label">
																			<input type="text" name="idFone" id="idFone"
																				class="form-control" readonly="readonly"
																				value="${telefone.id}"> <span
																				class="form-bar"></span> <label class="float-label">Id
																				Fone</label>
																		</div>

																		<div class="form-group form-default form-static-label">
																			<input type="text" name="id" id="id"
																				class="form-control" readonly="readonly"
																				value="${modelLogin.id}"> <span
																				class="form-bar"></span> <label class="float-label">Id
																				Usuário</label>
																		</div>

																		<div class="form-group form-default form-static-label">
																			<input readonly="readonly" type="text" name="nome"
																				id="nome" class="form-control" required="required"
																				value="${modelLogin.nome}"> <span
																				class="form-bar"></span> <label class="float-label">Nome</label>
																		</div>

																		<div class="form-group form-default form-static-label">
																			<!-- Adicionei esse pattern para impedir que telefones com espaço em branco sejam salvos-->
																			<input type="text" name="numero" id="numero"
																				class="form-control" required="required"
																				value="${telefone.numero}" pattern="[0-9]+$">
																			<span class="form-bar"></span> <label
																				class="float-label">Número</label>
																		</div>

																		<div class="form-group form-default form-static-label">
																			<select class="form-control"
																				aria-label="Default select example" name="status">
																				<option disabled="disabled">[Selecione o
																					Status]</option>

																				<option value="ATIVO"
																					<%
																	  
																	  ModelTelefone telefone = (ModelTelefone) request.getAttribute("telefone");
																	  
																	  if(telefone != null && telefone.getStatus().equals("ATIVO")) {
																		  out.print("");
																		  out.print("selected=\"selected\"");
																		  out.print("");
																	  } %>>Ativo</option>

																				<option value="INATIVO"
																					<%
																	  
																		  telefone = (ModelTelefone) request.getAttribute("telefone");
																	  
																	  if(telefone != null && telefone.getStatus().equals("INATIVO")) {
																		  out.print("");
																		  out.print("selected=\"selected\"");
																		  out.print("");
																	  } %>>Inativo</option>

																			</select> <span class="form-bar"></span> <label
																				class="float-label">Status:</label>
																		</div>

																		<button
																			class="btn btn-success waves-effect waves-light">Salvar</button>
																	</form>

																</div>

															</div>
														</div>
													</div>
												</div>

												<span id="msg">${msg}</span>

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
   
<script type="text/javascript">

var timeDisplay = document.getElementById("time");

function refreshTime() {
    var dateString = new Date();
    let data = dateString.toLocaleDateString("pt-BR", { timeZone: "America/Sao_Paulo" });
    let hora = dateString.toLocaleTimeString("pt-BR", { timeZone: "America/Sao_Paulo" });

    var formattedString = 'Data: ' + data + ', Hora: ' + hora;
    timeDisplay.innerHTML = formattedString;
}
setInterval(refreshTime, 1000);

$("#numero").keypress(function(event) {
	return /\d/.test(String.fromCharCode(event.keyCode));
});

</script> 
</body>

</html>
