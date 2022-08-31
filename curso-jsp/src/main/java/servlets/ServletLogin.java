package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;

import java.io.IOException;

import dao.DAOLoginRepository;

@WebServlet(urlPatterns = {"/principal/ServletLogin","/ServletLogin"}) /*Mapeamento de URL que vem da tela.*/
public class ServletLogin extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private DAOLoginRepository daoLoginRepository = new DAOLoginRepository();

	public ServletLogin() {
	}

	/*Recebe os dados pela URL em parâmetros*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String acao = request.getParameter("acao");
		
		if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("logout")) {
			request.getSession().invalidate(); //invalida a sessão e apaga todos os atributos salvos de um usuário logado;
			RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
			redirecionar.forward(request, response);
		} else {		
			doPost(request, response);
		}
	}

	/*Recebe os dados enviados por um formulário*/
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String url = request.getParameter("url");
		
		/*Aqui verificamos se o login e senha foram informados. Se tudo abaixo for falso, então ele cria um
		 * ModelLogin e setta os valores. Senão, ele usa um objeto RequestDispatcher para redirecionar para a
		 * tela de login (index.jsp), além de enviar a mensagem de informar o login e senha*/
		try {
			if(login != null && !login.isEmpty() && senha!= null && !senha.isEmpty()) {
				ModelLogin modelLogin = new ModelLogin(); //null;
				modelLogin.setLogin(login);
				modelLogin.setSenha(senha);
				
				/*O if abaixo faz uma verificação se o Login e Senha são iguais aos informados abaixo. Ele ignora
				 * letras maiúsculas. Se forem iguais, ele irá redirecionar para a página principal.jsp.*/
				/*SIMULAÇÃO DE LOGIN*/
				if (daoLoginRepository.validarAutenticacao(modelLogin)) {
					
					request.getSession().setAttribute("usuario", modelLogin.getLogin());
					/*Ele pega a sessão do request e setta o objeto modelLogin para dentro da String usuario.*/
					
					/*Em index.jsp, eu não estava entendendo o input referente a url. Porém, aqui
					 fizemos uma verificação que faz sentido: no momento em que fazemos login,
					 nossa url = null. Neste caso, seremos redirecionados para a tela principal
					 após o login.*/
					if(url == null || url.equals("null")) {
						url = "principal/principal.jsp";
					}
					
					RequestDispatcher redirecionar = request.getRequestDispatcher(url);
					redirecionar.forward(request, response);
				} else {
					RequestDispatcher redirecionar = request.getRequestDispatcher("/index.jsp");
					request.setAttribute("msg", "Informe o login e senha corretamente!");
					redirecionar.forward(request, response);
				}
			} else {
				/* RequestDispatcher define um objeto que recebe requests do cliente e as envia para qualquer
				recurso que queira usa-la (como uma servlet, um HTMLfile, ou JSP file) no servidor. O servlet
				container cria o objeto RequestDispatcher, o qual é usado como um wrapper ao redor do recurso
				do server localizado no path que foi passado. */
				RequestDispatcher redirecionar = request.getRequestDispatcher("index.jsp");
				request.setAttribute("msg", "Informe o login e senha corretamente!");
				redirecionar.forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
		}
	}
}
