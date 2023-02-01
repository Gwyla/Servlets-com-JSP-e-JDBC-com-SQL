package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.ModelLogin;
import util.ReportUitl;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.tomcat.jakartaee.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.DAOUsuarioRepository;

@MultipartConfig
@WebServlet(urlPatterns = {"/ServletUsuarioController"})
public class ServletUsuarioController extends ServletGenericUtil {
	private static final long serialVersionUID = 1L;
	
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

	public ServletUsuarioController() {
		super();
	}

	@SuppressWarnings("static-access")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
				String acao = request.getParameter("acao");
				
				if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletar")) {
					String idUser = request.getParameter("id");
					daoUsuarioRepository.deletarUsuario(idUser);
					
					List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));
					request.setAttribute("modelLogins", modelLogins);
					
					request.setAttribute("msg", "Usuário excluído com sucesso!");
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
					request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("deletarajax")) {
						String idUser = request.getParameter("id");
						daoUsuarioRepository.deletarUsuario(idUser);
						response.getWriter().write("Usuário excluído com sucesso!");
					}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjax")) {
					String nomeBusca = request.getParameter("nomeBusca");
					List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultarUsuarioList(nomeBusca, super.getUserLogado(request));
					
					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(dadosJsonUser);
					
					response.addHeader("totalPagina", ""+ daoUsuarioRepository.consultarUsuarioListTotalPagina(nomeBusca, super.getUserLogado(request)));
					response.getWriter().write(json);
					
				}
				
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarUserAjaxPage")) {
					String nomeBusca = request.getParameter("nomeBusca");
					String pagina = request.getParameter("pagina");
					
					List<ModelLogin> dadosJsonUser =  daoUsuarioRepository.consultarUsuarioListOffSet(nomeBusca, super.getUserLogado(request), Integer.parseInt(pagina));
					
					ObjectMapper mapper = new ObjectMapper();
					
					String json = mapper.writeValueAsString(dadosJsonUser);
					
					response.addHeader("totalPagina", ""+ daoUsuarioRepository.consultarUsuarioListTotalPagina(nomeBusca, super.getUserLogado(request)));
					response.getWriter().write(json);
					
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("buscarEditar")) {
					String id = request.getParameter("id");
					
					ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(id, super.getUserLogado(request));
					
					List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));
					request.setAttribute("modelLogins", modelLogins);
					
					request.setAttribute("msg", "Usuário em edição...");
					request.setAttribute("modelLogin", modelLogin);
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
					RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
					redireciona.forward(request, response);
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("listarUser")) {
					
					List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));

					request.setAttribute("msg", "Usuário carregados");
					request.setAttribute("modelLogins", modelLogins);
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
					RequestDispatcher redireciona = request.getRequestDispatcher("principal/usuario.jsp");
					redireciona.forward(request, response);
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("downloadFoto")) {
					
					String idUser = request.getParameter("id");
					ModelLogin modelLogin = daoUsuarioRepository.consultarUsuarioId(idUser, super.getUserLogado(request));
					
					if(modelLogin.getFotoUser() != null && !modelLogin.getFotoUser().isEmpty()) {
						response.setHeader("Content-Disposition", "attachment;filename=arquivo." + modelLogin.getExtensaoFotoUser());
						response.getOutputStream().write(new Base64().decodeBase64(modelLogin.getFotoUser().split("\\,")[1]));
						/*O conteúdo que está antes da vírgula é data:image/jpeg;base64 e depois da vírgula vem a String gigantesca que é a foto em base64. Ou seja, tudo
						 que veio antes da String é só pra identificação do Java. E depois da vírgula é a foto em base64. Passamos spli() com o regex e pegamos o que vem
						 depois da vírgula, que é a foto.*/
					}
					
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("paginar")) {
					
					Integer offset = Integer.parseInt(request.getParameter("pagina"));
					List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTLPaginado(super.getUserLogado(request), offset);
					request.setAttribute("modelLogins", modelLogins);
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
					request.getRequestDispatcher("principal/usuario.jsp").forward(request, response);
				}
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioUser")) {
					
					String dataInicial = request.getParameter("dataInicial");
					String dataFinal = request.getParameter("dataFinal");

					if(dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {
						request.setAttribute("listaUser", daoUsuarioRepository.consultarUsuarioListJSTLRel(super.getUserLogado(request)));
					} else {
						request.setAttribute("listaUser", daoUsuarioRepository.consultarUsuarioListJSTLRel(super.getUserLogado(request), dataInicial, dataFinal));
					}
					
					request.setAttribute("dataInicial", dataInicial);
					request.setAttribute("dataFinal", dataFinal);
					request.getRequestDispatcher("principal/reluser.jsp").forward(request, response);
				}
				
				else if(acao != null && !acao.isEmpty() && acao.equalsIgnoreCase("imprimirRelatorioPDF")) {
					String dataInicial = request.getParameter("dataInicial");
					String dataFinal = request.getParameter("dataFinal");
					
					List<ModelLogin> modelLogins = null;

					if(dataInicial == null || dataInicial.isEmpty() && dataFinal == null || dataFinal.isEmpty()) {
						modelLogins = daoUsuarioRepository.consultarUsuarioListJSTLRel(super.getUserLogado(request));
					} else {
						modelLogins = daoUsuarioRepository.consultarUsuarioListJSTLRel(super.getUserLogado(request), dataInicial, dataFinal);
						
					}
					
					byte [] relatorio = new ReportUitl().geraRelatorioPDF(modelLogins, "reluser-jsp", request.getServletContext());
					
					response.setHeader("Content-Disposition", "attachment;filename=arquivo.pdf");
					response.getOutputStream().write(relatorio);
					
				}
				
				else {
					List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));
					request.setAttribute("modelLogins", modelLogins);
					request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
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
			String perfil = request.getParameter("perfil");			
			String sexo = request.getParameter("sexo");
			String cep = request.getParameter("cep");
			String logradouro = request.getParameter("logradouro");
			String bairro = request.getParameter("bairro");
			String localidade = request.getParameter("localidade");
			String uf = request.getParameter("uf");
			String numero = request.getParameter("numero");
			String dataNascimento = request.getParameter("dataNascimento");
			String rendaMensal = request.getParameter("rendaMensal");
			
			rendaMensal = rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".");
			
			ModelLogin modelLogin = new ModelLogin();
			/*Se o id é diferente de null ou vazio, a String vai ser parseada para Long, senão, será null.*/
			modelLogin.setId(id != null && !id.isEmpty() ? Long.parseLong(id) : null);
			modelLogin.setNome(nome);
			modelLogin.setEmail(email);
			modelLogin.setLogin(login);
			modelLogin.setSenha(senha);
			modelLogin.setPerfil(perfil);			
			modelLogin.setSexo(sexo);
			
			modelLogin.setCep(cep);
			modelLogin.setLogradouro(logradouro);
			modelLogin.setBairro(bairro);
			modelLogin.setLocalidade(localidade);
			modelLogin.setUf(uf);
			modelLogin.setNumero(numero);			
			modelLogin.setDataNascimento(Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataNascimento))));
			/*Segundo o Alex, a leitura é feita da direita para a esquerda: ele pega a variável dataNascimento da tela, converte essa data no padrão com barras, converte
			 * em texto para o formato com traços, e por fim, converte em data novamente.*/
			modelLogin.setRendaMensal(Double.valueOf(rendaMensal));
			
			if (ServletFileUpload.isMultipartContent(request)) {
				Part part = request.getPart("fileFoto"); /*Pega a foto da tela*/
				
				if (part.getSize() > 0) {
					byte[] foto = IOUtils.toByteArray(part.getInputStream()); /*Converte a imagem para byte*/
					@SuppressWarnings("static-access")
					String imagemBase64 = "data:image/" +  part.getContentType().split("\\/")[1] + ";base64," + new Base64().encodeBase64String(foto);
					/*Observação sobre o método split(): Dentro de uma String, temos caracteres de escape. Quando vamos especificar um caminho no PC, não podemos simplesmente
					passar C:/... porque com uma barra só, o programa não compila. Para resolver, fazemos C://..., e aí o código compila com 2 barras. No método split acima,
					ele foi chamado porque o resultado de part.getContentType() é "image/jpeg". Porém, no começo da String, já colocamos "data:image/". Para consertar o caminho,
					fizemos um split nesse contentType, e pedimos para a String ser quebrada onde houver uma barra (/). Só que isso não ia compilar, por isso usamos \\ para
					permitir passar a /. Depois, pegamos o que está na posição [1] (que é de fato a extensão do arquivo) e concatenamos com o resto da String. Isso retorna
					data:image/jpeg... ou png, ou qualquer outro tipo de imagem.
					*/
					/*A String tem esse formato para que seja salva da maneira correta no DB. É um padrão para que o HTML possa entender essa imagem depois*/
					
					modelLogin.setFotoUser(imagemBase64);
					modelLogin.setExtensaoFotoUser(part.getContentType().split("\\/")[1]);
					/*Esse método retorna a extensão do objeto. Inicialmente veio como image/png, por isso fizemos o split para trazer só o png*/
				}
			}
			
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
				modelLogin =  daoUsuarioRepository.gravarUsuario(modelLogin, super.getUserLogado(request));
			}
			
			List<ModelLogin> modelLogins = daoUsuarioRepository.consultarUsuarioListJSTL(super.getUserLogado(request));
			request.setAttribute("modelLogins", modelLogins);
			
			request.setAttribute("msg", msg);
			request.setAttribute("modelLogin", modelLogin);
			request.setAttribute("totalPagina", daoUsuarioRepository.totalPagina(this.getUserLogado(request)));
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
