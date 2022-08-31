package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;

/*Essa classe ter� toda a parte de persist�ncia e manipula��o de dados no DB, al�m de ter
 * um m�todo de valida��o do Login.*/

public class DAOLoginRepository {

	private Connection connection;

	public DAOLoginRepository() {
		connection = SingleConnectionBanco.getConnection();
	}

	/* Como o Filter j� trata a exce��o, podemos usar o throws Exception aqui, e
	 * qualquer exce��o que acontecer, o Filter nos mostrar� o stacktrace().
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
		return false; /*aqui n�o foi autenticado*/
	}

}
