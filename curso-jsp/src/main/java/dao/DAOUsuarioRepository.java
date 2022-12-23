package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		
		if (usuario.isNovo()) /*Grava novo usuário*/{
		
		String sql = "INSERT INTO model_login(login, senha, nome, email, usuario_id, perfil, sexo, cep, logradouro, bairro, localidade, uf, numero, datanascimento, rendamensal)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, usuario.getLogin());
		statement.setString(2, usuario.getSenha());
		statement.setString(3, usuario.getNome());
		statement.setString(4, usuario.getEmail());
		statement.setLong(5, usuarioLogado);
		statement.setString(6, usuario.getPerfil());
		statement.setString(7, usuario.getSexo());
		statement.setString(8, usuario.getCep());
		statement.setString(9, usuario.getLogradouro());
		statement.setString(10, usuario.getBairro());
		statement.setString(11, usuario.getLocalidade());
		statement.setString(12, usuario.getUf());
		statement.setString(13, usuario.getNumero());
		statement.setDate(14, usuario.getDataNascimento());
		statement.setDouble(15, usuario.getRendaMensal());

		statement.execute();
		connection.commit();
		
		if(usuario.getFotoUser() != null) {
			sql = "update model_login set fotouser=?, extensaofotouser=? where login=?";
		
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, usuario.getFotoUser());
			statement.setString(2, usuario.getExtensaoFotoUser());
			statement.setString(3, usuario.getLogin());
			
			statement.execute();
			connection.commit();
		}
		
		} else { /*Atualiza usuário existente*/
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=?, perfil=?, sexo=?, cep=?, logradouro=?, bairro=?, localidade=?, uf=?, numero=?, datanascimento=?, rendamensal=?"
					+ " WHERE id = "+usuario.getId()+"";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, usuario.getLogin());
			statement.setString(2, usuario.getSenha());
			statement.setString(3, usuario.getNome());
			statement.setString(4, usuario.getEmail());
			statement.setString(5, usuario.getPerfil());
			statement.setString(6, usuario.getSexo());			
			statement.setString(7, usuario.getCep());
			statement.setString(8, usuario.getLogradouro());
			statement.setString(9, usuario.getBairro());
			statement.setString(10, usuario.getLocalidade());
			statement.setString(11, usuario.getUf());
			statement.setString(12, usuario.getNumero());
			statement.setDate(13, usuario.getDataNascimento());
			statement.setDouble(14, usuario.getRendaMensal());
			
			statement.executeUpdate();
			connection.commit();
			
			if(usuario.getFotoUser() != null) {
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
	 * O id só é gerado quando o usuário é gravado no DB. Pegamos o login como
	 * identificador porque ele é único para cada usuário (tem a constraint unique).
	 * Chamamos este método no método acima, então além de gravar o usuário, ele já
	 * vai exibir este usuário na tela.
	 */
	
public List<ModelLogin> consultarUsuarioListJSTLPaginado(Long usuarioLogado, Integer offset) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<>();
		
		String sql = "select * from model_login where useradmin is false and usuario_id = " + usuarioLogado + " order by nome offset " + offset + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha")); Por questão de segurança, não exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}	

	public int totalPagina(Long userLogado) throws Exception {
		
		String sql = "select count(1) as total from model_login where usuario_id = " + userLogado;
		/* select count vai retornar o número de usuários cadastrado pelo usuário passado como parâmetro. Ex.: se for 1 (admin), vai retornar 9 usuáros, que até o momento
		 foram cadastrados por ele.*/
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");
		
		Double porPagina = 5.0;

		Double pagina = cadastros / porPagina;


		Double resto = pagina % 2;
		
		if (resto > 0) {
			pagina++;
		}
		
		return pagina.intValue();

	}
	
	public List<ModelLogin> consultarUsuarioListJSTL(Long usuarioLogado) throws Exception {
		
		List<ModelLogin> retorno = new ArrayList<>();
		String sql = "select * from model_login where useradmin is false and usuario_id = " + usuarioLogado + " limit 5";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			ModelLogin modelLogin = new ModelLogin();
			
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setLogin(resultado.getString("login"));
			//modelLogin.setSenha(resultado.getString("senha")); Por questão de segurança, não exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			
			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public int consultarUsuarioListTotalPagina(String nome, Long usuarioLogado) throws Exception {

		String sql = "select count(1) as total from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? ";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, "%" + nome + "%");
		statement.setLong(2, usuarioLogado);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		
		Double cadastros = resultado.getDouble("total");

		Double porPagina = 5.0;

		Double pagina = cadastros / porPagina;

		Double resto = pagina % 2;

		if (resto > 0) {
			pagina++;
		}

		return pagina.intValue();
	}
	
	public List<ModelLogin> consultarUsuarioListOffSet(String nome, Long usuarioLogado, int offset) throws Exception {

		List<ModelLogin> retorno = new ArrayList<>();
		
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? offset "+offset+" limit 5";
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
			//modelLogin.setSenha(resultado.getString("senha")); Por questão de segurança, não exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public List<ModelLogin> consultarUsuarioList(String nome, Long usuarioLogado) throws Exception {

		List<ModelLogin> retorno = new ArrayList<>();
		/*Usamos like ao invés de = para que sejam retornados até mesmo partes do nome passado como parâmetro. Ex.: gu pode retornar guilherme ou gustavo.*/
		String sql = "select * from model_login where upper(nome) like upper(?) and useradmin is false and usuario_id = ? limit 5";
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
			//modelLogin.setSenha(resultado.getString("senha")); Por questão de segurança, não exibiremos a senha.
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));

			retorno.add(modelLogin);
		}
		
		return retorno;
	}
	
	public ModelLogin consultarUsuarioLogado(String login) throws Exception {
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
			modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
			
		}
		return modelLogin;
	}
	
	
	public ModelLogin consultarUsuario(String login) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper é para que deixe de ser Case Sensitive no DB. Se for maiúsculo ou
		 * minúsculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usuário), ele fará o que está abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setUserAdmin(resultado.getBoolean("useradmin"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		return modelLogin;
	}
	
	public ModelLogin consultarUsuario(String login, Long usuarioLogado) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		/*
		 * upper é para que deixe de ser Case Sensitive no DB. Se for maiúsculo ou
		 * minúsculo, ele vai igualar no DB e fazer a consulta corretamente.
		 */
		String sql = "select * from model_login where upper(login) = upper('" + login + "') and useradmin is false and usuario_id = " + usuarioLogado;
		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usuário), ele fará o que está abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
		}
		return modelLogin;
	}
	
	public ModelLogin consultarUsuarioId(Long id) throws Exception {
		ModelLogin modelLogin = new ModelLogin();

		String sql = "select * from model_login where id = ? and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		
		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) /* Se tem resultado (retorno de usuário), ele fará o que está abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
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

		while (resultado.next()) /* Se tem resultado (retorno de usuário), ele fará o que está abaixo */ {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setPerfil(resultado.getString("perfil"));
			modelLogin.setSexo(resultado.getString("sexo"));
			modelLogin.setFotoUser(resultado.getString("fotouser"));
			modelLogin.setExtensaoFotoUser(resultado.getString("extensaofotouser"));
			modelLogin.setCep(resultado.getString("cep"));
			modelLogin.setLogradouro(resultado.getString("logradouro"));
			modelLogin.setBairro(resultado.getString("bairro"));
			modelLogin.setLocalidade(resultado.getString("localidade"));
			modelLogin.setUf(resultado.getString("uf"));
			modelLogin.setNumero(resultado.getString("numero"));
			modelLogin.setDataNascimento(resultado.getDate("datanascimento"));
			modelLogin.setRendaMensal(resultado.getDouble("rendamensal"));
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
		String sql = "DELETE FROM model_login	WHERE id = ? and useradmin is false";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, Long.parseLong(idUser));
		
		statement.executeUpdate();
		connection.commit();
		
	}
}
