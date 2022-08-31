package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;

/*Essa classe terá toda a parte de persistência e manipulação de dados no DB, além de ter
 * um método de validação do Login.*/

public class DAOLoginRepository {

	private Connection connection;

	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	/* Como o Filter já trata a exceção, podemos usar o throws Exception aqui, e
	 * qualquer exceção que acontecer, o Filter nos mostrará o stacktrace().
	 */
	public boolean validarAutenticacao(ModelLogin modelLogin) throws Exception {

		String sql = "select * from model_login where login = ? and senha = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, modelLogin.getLogin());
		statement.setString(2, modelLogin.getSenha());

		ResultSet resultado = statement.executeQuery();

		if (resultado.next()) { /*Aqui o user foi autenticado*/
			return true;
		}
		return false; /*aqui não foi autenticado*/
	}

}
