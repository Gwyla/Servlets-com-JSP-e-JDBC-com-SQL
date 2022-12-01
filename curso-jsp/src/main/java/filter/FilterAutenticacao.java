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

@SuppressWarnings("serial")
@WebFilter(urlPatterns = {"/principal/*"})
/*Intercepta as requisi��es que vierem da pasta principal, de qualquer p�gina jsp ou html.*/
public class FilterAutenticacao extends HttpFilter implements Filter {
	
	private static Connection connection;
    
    public FilterAutenticacao() {

    }

    /*Inicia os processos ou recursos quando o servidor sobe o projeto*/
	public void init(FilterConfig fConfig) throws ServletException {
		connection = SingleConnectionBanco.getConnection();
	}

	/*Intercepta as requisi��es e d� as respostas no sistema. Serve para v�rios tipos de tarefas, como valida��es, commits e rollbacks em transa��es de DBs, outros tipos
	 * de valida��es e redirecionamentos de p�ginas. */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			/*HttpServletRequest foi instanciado para que pud�ssemos instanciar uma Session deste elemento.
			 * Assim, a autentica��o que foi feita dentro de ServletLogin passa a ser feita por aqui.*/
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();
			
			String usuarioLogado = (String) session.getAttribute("usuario");
			String urlParaAutenticar = req.getServletPath(); //se passarmos principal/principal.jsp direto na url, este ser� o valor de urlParaAutenticar.
			
			/*Aqui se verifica se o usu�rio = null, e se a url cont�m algo diferente do caminho
			 /principal/ServletLogin.
			 Deste modo, ele ir� redirecionar para ServleLogin, que far� a verifica��o de login.
			 
			 *Update: se passarmos /principal/ServletLogin na url, n�o entraremos no if, porque precisamos que os dois valores sejam true para que o c�digo
			 abaixo seja executado. N�o entrando no if, ca�mos no else, e ele tenta realizar chain.doFilter(). Por�m, existe erros entre a request e a
			 response, e por causa disso, n�o h� um commit na conex�o com o DB. Portanto, ca�mos no catch, e somos redirecionados para a p�gina erro.jsp.
			 Outra observa��o � que se passarmos qualquer p�gina jsp existente em nossa aplica��o na url, no formato principal/pagina.jsp, entraremos no if
			 e receberemos a mensagem abaixo. Se retirarmos /principal da url, seremos redirecionados para a p�gina de erro.jsp.*/		
			if(usuarioLogado == null && !urlParaAutenticar.equalsIgnoreCase("/principal/ServletLogin")) {
				
				RequestDispatcher redireciona = request.getRequestDispatcher("/index.jsp?url="
				+ urlParaAutenticar);
				request.setAttribute("msg", "Por favor, realize o Login!");
				redireciona.forward(request, response);
				return; /*Para a execu��o e redireciona para o login*/
			} else {
				chain.doFilter(request, response);
			}
			
			connection.commit(); /*Se deu tudo certo, commit nas altera��es no DB.*/
			
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
	
	/*Encerra os processos quando o servidor � parado*/
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
