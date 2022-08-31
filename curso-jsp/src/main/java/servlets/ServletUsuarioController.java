package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuarioRepository;

@WebServlet("/ServletUsuarioController")
public class ServletUsuarioController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

	public ServletUsuarioController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
				String acao = request.getParameter("acao");
				
				if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
					String idUser = request.getParameter("id");
					daoUsuarioRepository.deletarUsuario(idUser);
					request.setAttribute("msg", "Usuário excluído com sucesso!");
					request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
						String idUser = request.getParameter("id");
						daoUsuarioRepository.deletarUsuario(idUser);
						response.getWriter().write("Usuário excluído com sucesso!");
					}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
					String nomeBusca = request.getParameter("nomeBusca");
					List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultarUsuarioList(nomeBusca);
					
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(dadosJsonUser);
					
					response.getWriter().write(json);
					
				}
				else {
					request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
				RequestDispatcher redireciona = request.getRequestDispatcher("erro.jsp");
				request.setAttribute("msg", e.getMessage());
				redireciona.forward(request, response);
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String msg = "Usuário cadastrado com sucesso em nossa base de dados!";
			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String email = request.getParameter("email");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			
			ModelLogin modelLogin = new ModelLogin();
			/*Se o id é diferente de null ou vazio, a String vai ser parseada para Long, senão, será null.*/
			modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			
			/*A verificação abaixo é:
			 * 1) Se já existe um usuário cadastrado com esse login, por meio do método validarLogin();
			 * 2) Se está sendo cadastrado um novo usuário. Isto porque, no momento do cadastro de um novo usuário, o id == null.
			 * Portanto, se as 2 expressões forem verdadeiras, percebe-se que estão tentando cadastrar um novo usuário com um login que já existe, o que não é permitido.*/		
			if (daoUsuarioRepository.validarLogin(modelLogin.getLogin()) && modelLogin.getId() == null) {
				msg = "Já existe um usuário com o mesmo login, por favor, informe outro login!";
			} else {
				if (modelLogin.isNovo()) {
					msg = "Usuário cadastrado com sucesso em nossa base de dados!";
				} else {
					msg = "Dados do usuário atualizados com sucesso!";
				}
				modelLogin =  daoUsuarioRepository.gravarUsuario(modelLogin);
			}
			
			request.setAttribute("msg", msg);
			request.setAttribute("modelLogin", modelLogin);		
			RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
			redireciona.forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			RequestDispatcher redireciona = request.getRequestDispatcher("erro.jsp");
			request.setAttribute("msg", e.getMessage());
			redireciona.forward(request, response);
		}
	}

}
