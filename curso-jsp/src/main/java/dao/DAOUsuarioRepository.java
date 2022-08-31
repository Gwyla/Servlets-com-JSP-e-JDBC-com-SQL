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

	public ModelLogin gravarUsuario(ModelLogin usuario) throws Exception {
		
		if (usuario.isNovo()) /*Grava novo usuário*/{
		
		String sql = "INSERT INTO model_login(login, senha, nome, email) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());
		statement.setString(3, usuario.getNome());
		statement.setString(4, usuario.getEmail());

		statement.execute();
		connection.commit();
		
		} else { /*Atualiza usuário existente*/
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=? WHERE id = "+usuario.getId()+"";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			
			statement.executeUpdate();
			connection.commit();
			
		}

		return this.consultarUsuario(usuario.getLogin());
	}

	/*
	 * O id só é gerado quando o usuário é gravado no DB. Pegamos o login como
	 * identificador porque ele é único para cada usuário (tem a constraint unique).
	 * Chamamos este método no método acima, então além de gravar o usuário, ele já
	 * vai exibir este usuário na tela.
	 */
	
	public List<ModelLogin> consultarUsuarioList(String nome) throws Exception {

		List<ModelLogin> retorno = new ArrayList<>();
		/*Usamos like ao invés de = para que sejam retornados até mesmo partes do nome passado como parâmetro. Ex.: gu pode retornar guilherme ou gustavo.*/
		String sql = "select * from model_login where upper(nome) like upper(?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha")); Por questão de segurança, não exibiremos a senha.
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	public ModelLogin consultarUsuario(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper é para que deixe de ser Case Sensitive no DB. Se for maiúsculo ou
		 * minúsculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "')";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usuário), ele fará o que está abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
		}
		return modelLogin;
	}

	public boolean validarLogin(String login) throws Exception {
		String sql = "select count(1) > 0 as existe from model_login where upper(login) = upper('" +login+"')";

		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();/*Vai retornar os resultados encontrados*/
		return resultado.getBoolean("existe");
		/*existe, nesse caso, funciona como uma variável booleana que guarda e retorna o valor desse SELECT.*/
		
	}
	
	public void deletarUsuario(String idUser) throws Exception {
		String sql = "DELETE FROM model_login	WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));
		
		statement.executeUpdate();
		connection.commit();
		
	}
}
