package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionBanco {
	/*Tem o padrão Singleton porque só pode existir uma única comnexão com o DB, o que abre e fecham
	 * inúmeras vezes são as sessões e transações.
	 * 
	 * autoReconnect = true faz com que se a conexão cair, ele conecte automaticamente de novo.*/
	private static String banco = "jdbc:postgresql://localhost:5432/curso-jsp?autoReconnect=true";
	private static String usuario = "postgres";
	private static String senha = "admin";	
	private static Connection connection = null;
	
	public static Connection getConnection() {
		return connection;
	}

	/*O static é para o caso de chamarem a Classe, ao invés de instanciarem um objeto dela. Assim, o
	 * método conectar() se torna acessível tanto de forma static (por meio da Classe), quanto de forma
	 * Orientada a Objetos (por meio da referência do objeto). */
	static {
		conectar();
	}

	public SingleConnectionBanco() { /*Quando ela for instanciada, irá conectar.*/
		conectar();
	}	
	
	private static void conectar() {
		
		if (connection == null) {
			try {
				Class.forName("org.postgresql.Driver");/*Carrega o Driver de conexão do banco.*/
				connection = DriverManager.getConnection(banco, usuario, senha);
				connection.setAutoCommit(false);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
