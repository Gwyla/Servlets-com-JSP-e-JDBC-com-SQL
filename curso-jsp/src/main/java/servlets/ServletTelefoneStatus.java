package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;
import model.ModelTelefone;

import java.io.IOException;
import java.util.List;

import dao.DAOTelefoneRepository;
import dao.DAOUsuarioRepository;

@WebServlet("/ServletTelefoneStatus")
public class ServletTelefoneStatus extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	private DAOTelefoneRepository daoTelefoneRepository = new DAOTelefoneRepository();
	
	public ServletTelefoneStatus() {
		
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String acao = request.getParameter("acao");
//			String userPai = request.getParameter("userPai");
			
			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listaFone")) {
				
//				ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(Long.parseLong(userPai));
				List<ModelTelefone> telefones = daoTelefoneRepository.listaFoneTeste();
				
				request.setAttribute("telefones", telefones);
//				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/telefoneStatus.jsp").forward(request, response);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
