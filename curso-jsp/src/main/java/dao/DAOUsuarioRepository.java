package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {

	private Connection connection;

	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	public ModelLogin gravarUsuario(ModelLogin usuario, Long usuarioLogado) throws Exception {
		
		if (usuario.isNovo()) /*Grava novo usu�rio*/{
		
		String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo) VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());
		statement.setString(3, usuario.getNome());
		statement.setString(4, usuario.getEmail());
		statement.setLong(5, usuarioLogado);
		statement.setString(6, usuario.getPerfil());
		statement.setString(7, usuario.getSexo());

		statement.execute();
		connection.commit();
		
		if(usuario.getFotoUser() != null & !usuario.getFotoUser().isEmpty()) {
			sql = "update model_login set fotouser=?, extensaofotouser=? where login=?";
		
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, usuario.getFotoUser());
			statement.setString(2, usuario.getExtensaoFotoUser());
			statement.setString(3, usuario.getLogin());
			
			statement.execute();
			connection.commit();
		}
		
		} else { /*Atualiza usu�rio existente*/
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=? WHERE id = "+usuario.getId()+"";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			statement.setString(5, usuario.getPerfil());
			statement.setString(6, usuario.getSexo());
			
			statement.executeUpdate();
			connection.commit();
			
			if(usuario.getFotoUser() != null & !usuario.getFotoUser().isEmpty()) {
				sql = "update model_login set fotouser =?, extensaofotouser =? where id =?";
			
				statement = connection.prepareStatement(sql);
				
				statement.setString(1, usuario.getFotoUser());
				statement.setString(2, usuario.getExtensaoFotoUser());
				statement.setLong(3, usuario.getId());
				
				statement.execute();
				connection.commit();
			}
			
		}

		return this.consultarUsuario(usuario.getLogin(), usuarioLogado);
	}

	/*
	 * O id s� � gerado quando o usu�rio � gravado no DB. Pegamos o login como
	 * identificador porque ele � �nico para cada usu�rio (tem a constraint unique).
	 * Chamamos este m�todo no m�todo acima, ent�o al�m de gravar o usu�rio, ele j�
	 * vai exibir este usu�rio na tela.
	 */
	
	public List<ModelLogin> consultarUsuarioListJSTL(Long usuarioLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<>();
		/*Usamos like ao inv�s de = para que sejam retornados at� mesmo partes do nome passado como par�metro. Ex.: gu pode retornar guilherme ou gustavo.*/
		String sql = "select * from model_login where useradmin is false and usuario_id = " + usuarioLogado;
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha")); Por quest�o de seguran�a, n�o exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultarUsuarioList(String nome, Long usuarioLogado) throws Exception {

		List<ModelLogin> retorno = new ArrayList<>();
		/*Usamos like ao inv�s de = para que sejam retornados at� mesmo partes do nome passado como par�metro. Ex.: gu pode retornar guilherme ou gustavo.*/
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, usuarioLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha")); Por quest�o de seguran�a, n�o exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public ModelLogin consultarUsuarioLogado(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper � para que deixe de ser Case Sensitive no DB. Se for mai�sculo ou
		 * min�sculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usu�rio), ele far� o que est� abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
		}
		return modelLogin;
	}
	
	
	public ModelLogin consultarUsuario(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper � para que deixe de ser Case Sensitive no DB. Se for mai�sculo ou
		 * min�sculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usu�rio), ele far� o que est� abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
		}
		return modelLogin;
	}
	
	public ModelLogin consultarUsuario(String login, Long usuarioLogado) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper � para que deixe de ser Case Sensitive no DB. Se for mai�sculo ou
		 * min�sculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false and usuario_id = " + usuarioLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usu�rio), ele far� o que est� abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
		}
		return modelLogin;
	}
	
	public ModelLogin consultarUsuarioId(String id, Long usuarioLogado) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false and usuario_id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(id));
		statement.setLong(2, usuarioLogado);
		
		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usu�rio), ele far� o que est� abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
		}
		return modelLogin;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" +login+"')";

		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();/*Vai retornar os resultados encontrados*/
		return resultado.getBoolean("existe");
		/*existe, nesse caso, funciona como uma vari�vel booleana que guarda e retorna o valor desse SELECT.*/
		
	}
	
	public void deletarUsuario(String idUser) throws Exception {
		String sql = "DELETE FROM model_login	WHERE id = ? and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));
		
		statement.executeUpdate();
		connection.commit();
		
	}
}
