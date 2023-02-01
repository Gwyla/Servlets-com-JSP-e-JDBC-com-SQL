package servlets;

import java.io.IOException;
import java.util.List;

import dao.DAOTelefoneRepository;
import dao.DAOUsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelLogin;
import model.ModelTelefone;

@WebServlet("/ServletTelefone")
public class ServletTelefone extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;

	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	private DAOTelefoneRepository daoTelefoneRepository = new DAOTelefoneRepository();

	public ServletTelefone() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			String acao = request.getParameter("acao");

			if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("excluir")) {

				String idFone = request.getParameter("id");

				daoTelefoneRepository.deletaTelefone(Long.parseLong(idFone));

				String userPai = request.getParameter("userPai");

				ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(Long.parseLong(userPai));

				List<ModelTelefone> telefones = daoTelefoneRepository.listaFone(modelLogin.getId());
				request.setAttribute("telefones", telefones);

				request.setAttribute("msg", "Telefone excluído com sucesso!");
				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);

				return;

			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("atualizar")) {
				String idFone = request.getParameter("id");
				ModelTelefone telefone = daoTelefoneRepository.consultaTelefone(Long.parseLong(idFone));

				String userPai = request.getParameter("userPai");
				ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(Long.parseLong(userPai));
				List<ModelTelefone> telefones = daoTelefoneRepository.listaFone(modelLogin.getId());

				request.setAttribute("telefones", telefones);
				request.setAttribute("telefone", telefone);
				request.setAttribute("msg", "Telefone em edição...");
				request.setAttribute("modelLogin", modelLogin);
				//request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
			} else if (acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listaFone")) {
				
				List<Object> dados = daoTelefoneRepository.listaFoneComStatus();
				
				request.setAttribute("telefones", dados);

				request.setAttribute("modelLogin", dados);
			}

			//iduser
			String userPai = request.getParameter("userPai");
			
			if (userPai != null && !userPai.isEmpty()) {

				ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(Long.parseLong(userPai));

				List<ModelTelefone> telefones = daoTelefoneRepository.listaFone(Long.parseLong(userPai));
				
				request.setAttribute("telefones", telefones);

				request.setAttribute("modelLogin", modelLogin);
				request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);
				
			} else {
				List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));
				request.setAttribute("modelLogins", modelLogins);
				request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
				request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			String usuario_pai_id = request.getParameter("id");
			String numero = request.getParameter("numero");
			String idFone = request.getParameter("idFone");
			String status = request.getParameter("status");
			
			if (!daoTelefoneRepository.existeFone(numero, Long.valueOf(usuario_pai_id), status)) {
				/*Ou seja, se não existir um telefone com este número, só então ele deixa gravar o número. */
				ModelTelefone modelTelefone = new ModelTelefone();

				modelTelefone.setNumero(numero);
				modelTelefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(Long.parseLong(usuario_pai_id)));
				modelTelefone.setUsuario_cad_id(super.getUserLogadoObjt(request));
				modelTelefone.setStatus(status);

				if (idFone == null || idFone == "") {

					daoTelefoneRepository.gravaTelefone(modelTelefone);
					request.setAttribute("msg", "Telefone salvo com sucesso!");
				} else {
					
					daoTelefoneRepository.atualizaTelefone(modelTelefone, Long.parseLong(idFone));
					request.setAttribute("msg", "Telefone atualizado com sucesso!");
				}

			} else {
				request.setAttribute("msg", "Este telefone já existe!");
			}

			List<ModelTelefone> telefones = daoTelefoneRepository.listaFone(Long.parseLong(usuario_pai_id));

			ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(Long.parseLong(usuario_pai_id));
			request.setAttribute("modelLogin", modelLogin);
			request.setAttribute("telefones", telefones);
			request.getRequestDispatcher("principal/telefone.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
