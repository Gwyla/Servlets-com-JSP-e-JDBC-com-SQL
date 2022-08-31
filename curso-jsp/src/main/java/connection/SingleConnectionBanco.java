package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionBanco {
	/*Tem o padr�o Singleton porque s� pode existir uma �nica comnex�o com o DB, o que abre e fecham
	 * in�meras vezes s�o as sess�es e transa��es.
	 * 
	 * autoReconnect = true faz com que se a conex�o cair, ele conecte automaticamente de novo.*/
	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp?autoReconnect=true";
	private static String usuario = "postgres";
	private static String senha = "admin";	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		return connection;
	}

	/*O static � para o caso de chamarem a Classe, ao inv�s de instanciarem um objeto dela. Assim, o
	 * m�todo conectar() se torna acess�vel tanto de forma static (por meio da Classe), quanto de forma
	 * Orientada a Objetos (por meio da refer�ncia do objeto). */
	static {
		conectar();
	}

	public SingleConnectionBanco() { /*Quando ela for instanciada, ir� conectar.*/
		conectar();
	}	
	
	private static void conectar() {
		
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");/*Carrega o Driver de conex�o do banco.*/
				connection = DriverManager.getConnection(banco, usuario, senha);
				connection.setAutoCommit(false);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
