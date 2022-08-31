package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import connection.SingleConnectionBanco;

@WebFilter(urlPatterns = {"/principal/*"})
/*Intercepta as requisições que vierem da pasta principal, de qualquer página jsp ou html.*/
public class FilterAutenticacao extends HttpFilter implements Filter {
	
	private static Connection connection;
    
    public FilterAutenticacao() {

    }

    /*Encerra os processos quando o servidor é parado*/
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*Intercepta as requisições e dá as respostas no sistema
	 * Serve para vários tipos de tarefas, como validações,
	 * commits e rollbacks em transações de DBs,
	 * Validações e redirecionamentos de páginas. */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			/*HttpServletRequest foi instanciado para que pudéssemos instanciar uma Session deste elemento.
			 * Assim, a autenticação que foi feita dentro de ServletLogin passa a ser feita por aqui.*/
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath();
			
			/*Aqui se verifica se o usuário = null, e se a url contém algo diferente do caminho
			 /principal/ServletLogin.
			 Deste modo, ele irá redirecionar para ServleLogin, que fará a verificação de login.*/		
			if(usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {
				
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url="
				+ urlParaAutenticar);
				request.setAttribute("msg", "Por favor, realize o Login!");
				redireciona.forward(request, response);
				return; /*Para a execução e redireciona para o login*/
			} else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); /*Se deu tudo certo, commit nas alterações no DB.*/
			
		} catch (Exception e) {
			e.printStackTrace();
			
			RequestDispatcher redirecionar = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redirecionar.forward(request, response);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/*Inicia os processos ou recursos quando o servidor sobe o projeto*/
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}

}
