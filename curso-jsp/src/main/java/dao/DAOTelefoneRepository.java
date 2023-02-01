package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
import model.ModelLogin;
import model.ModelTelefone;

public class DAOTelefoneRepository {

	private Connection connection;
	private DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();
	
	public DAOTelefoneRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public List<ModelTelefone> listaFone(Long idUserPai) throws Exception {
		List<ModelTelefone> listTelefones = new ArrayList<>();
		
		String sql = "select * from telefone where usuario_pai_id = ?";		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setLong(1, idUserPai);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			
			ModelTelefone telefone = new ModelTelefone();
			
			telefone.setId(resultado.getLong("id"));
			telefone.setNumero(resultado.getString("numero"));
			telefone.setUsuario_cad_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_cad_id")));
			telefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_pai_id")));
			telefone.setStatus(resultado.getString("status"));
			
			listTelefones.add(telefone);
		}
		
		return listTelefones;
	}
	
	public List<ModelTelefone> listaFoneTeste() throws Exception {
		List<ModelTelefone> listTelefones = new ArrayList<>();
		
		String sql = "select * from telefone";		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			
			ModelTelefone telefone = new ModelTelefone();
			
			telefone.setId(resultado.getLong("id"));
			telefone.setNumero(resultado.getString("numero"));
			telefone.setUsuario_cad_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_cad_id")));
			telefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_pai_id")));
			telefone.setStatus(resultado.getString("status"));
			
			listTelefones.add(telefone);
		}
		
		return listTelefones;
	}
	
	public void gravaTelefone(ModelTelefone modelTelefone) throws Exception {

		String sql = "insert into telefone(numero, usuario_pai_id, usuario_cad_id, status) values (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, modelTelefone.getNumero());
		statement.setLong(2, modelTelefone.getUsuario_pai_id().getId());
		statement.setLong(3, modelTelefone.getUsuario_cad_id().getId());
		statement.setString(4, modelTelefone.getStatus());

		statement.execute();
		connection.commit();

	}

	public void deletaTelefone(Long id) throws Exception {
		String sql = "delete from telefone where id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);		
		statement.setLong(1, id);
		
		statement.executeUpdate();
		connection.commit();
	}
	
	public boolean existeFone(String fone, Long idUser, String status) throws Exception {
		String sql = "select count(1) > 0 as existe from telefone where usuario_pai_id = ? and numero = ? and status = ?";
		/*Qualquer valor maior que 0 indica que já existe um telefone gravado com mesmo número para este usuário*/
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, idUser);
		statement.setString(2, fone);
		statement.setString(3, status);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		
		return resultado.getBoolean("existe");
	}
	
/*******************************************************************************************/	
	public List<Object> listaFoneComStatus() throws Exception {
		List<Object> listTelefones = new ArrayList<>();
		
		String sql = "select users.id as id, users.nome as nome, tel.id as telid, tel.numero as numero, users.perfil as perfil, tel.status as status\r\n"
				+ "from model_login as users, telefone as tel\r\n"
				+ "where users.id = tel.usuario_pai_id and perfil = 'SECRETARIA'\r\n"
				+ "order by nome ASC, status = 'INATIVO'";		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {

			ModelTelefone telefone = new ModelTelefone();
			ModelLogin usuario = new ModelLogin();

			usuario.setId(resultado.getLong("id"));
			usuario.setNome(resultado.getString("nome"));
//			usuario.setEmail(resultado.getString("email"));
//			usuario.setLogin(resultado.getString("login"));
			usuario.setPerfil(resultado.getString("perfil"));
//			usuario.setSexo(resultado.getString("sexo"));
			
			telefone.setId(resultado.getLong("telid"));
			telefone.setNumero(resultado.getString("numero"));
//			telefone.setUsuario_cad_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_cad_id")));
//			telefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_pai_id")));
			telefone.setStatus(resultado.getString("status"));

			listTelefones.add(telefone);
			listTelefones.add(usuario);
		}
		
		return listTelefones;
	}
	
	public void atualizaTelefone(ModelTelefone modelTelefone, Long idFone) throws Exception {
		
		String sql = "update telefone set numero=?, status=? where id = ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, modelTelefone.getNumero());
		statement.setString(2, modelTelefone.getStatus());
		statement.setLong(3, idFone);

		statement.executeUpdate();
		connection.commit();
	}
	
	public ModelTelefone consultaTelefone(Long id) throws Exception {
		ModelTelefone telefone = new ModelTelefone();
		
		String sql = "select * from telefone where id = ?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		
		ResultSet resultado = statement.executeQuery();
		
		while (resultado.next()) {
			telefone.setId(resultado.getLong("id"));
			telefone.setNumero(resultado.getString("numero"));
			telefone.setUsuario_cad_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_cad_id")));
			telefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_pai_id")));
			telefone.setStatus(resultado.getString("status"));
		}
		
		return telefone;
	}
}
