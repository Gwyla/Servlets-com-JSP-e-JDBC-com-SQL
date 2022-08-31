<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">

<title>Curso JSP</title>

<style type="text/css">
	
	form {
	position: absolute;
	top: 40%;
	left: 33%;
	right: 33%;
	}
	
	h2 {
	position: absolute;
	top: 30%;
	left: 33%;
	}
	
</style>

</head>
<body>
	<h2>Bem vindo ao curso de JSP!</h2>

	<form action="<%=request.getContextPath() %>/ServletLogin" method="post" class="row g-3 needs-validation" novalidate>
		
		<!-- Acredito que a linha de baixo captura o valor da url (neste caso = null) e
		manda para a ServletLogin fazer as verificações e redirecionamentos necessários. -->
		<input type="hidden" value="<%= request.getParameter("url") %>" name="url">

		<div class="mb-3">
			<label class="form-label">Login</label>
			<input class="form-control" name="login" type="text" required="required">
			<div class="invalid-feedback">
		      Login inválido ou não informado!
		    </div>
		</div>
		
		<div class="mb-3">
			<label class="form-label">Senha</label>
			<input class="form-control" name="senha" type="password" required="required">
			<div class="invalid-feedback">
		      Senha inválida ou não informada!
		    </div>
		</div>
		
		<div class="col-12">
			<input type="submit" value="Acessar" class="btn btn-primary">
		</div>


	</form>
	
	<h5 class="msg">${msg}</h5>
	
	<!-- Todo o código abaixo, inclusive as divs, são para chamar a atenção para o caso de
	o login e senha não serem informados. -->
	<!-- 
	<svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
	  <symbol id="exclamation-triangle-fill" fill="currentColor" viewBox="0 0 16 16">
	    <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
	  </symbol>
	</svg>		
	<div class="alert alert-danger d-flex align-items-center" role="alert">
	  <svg class="bi flex-shrink-0 me-2" width="24" height="24" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>
		  <div>
		  <h5>${msg}</h5>
		  </div>
	</div>
	 -->

	<!-- Option 1: Bootstrap Bundle with Popper -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
		crossorigin="anonymous"></script>
		
	<script type="text/javascript">
	// Example starter JavaScript for disabling form submissions if there are invalid fields
	(function () {
	  'use strict'

	  // Fetch all the forms we want to apply custom Bootstrap validation styles to
	  var forms = document.querySelectorAll('.needs-validation')

	  // Loop over them and prevent submission
	  Array.prototype.slice.call(forms)
	    .forEach(function (form) {
	      form.addEventListener('submit', function (event) {
	        if (!form.checkValidity()) {
	          event.preventDefault()
	          event.stopPropagation()
	        }

	        form.classList.add('was-validated')
	      }, false)
	    })
	})()
	</script>
</body>
</html>