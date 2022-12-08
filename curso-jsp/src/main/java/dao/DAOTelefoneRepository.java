package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import connection.SingleConnectionBanco;
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
			
			listTelefones.add(telefone);
		}
		
		return listTelefones;
	}
	
	public void gravaTelefone(ModelTelefone modelTelefone) throws Exception {
		
		String sql = "insert into telefone(numero, usuario_pai_id, usuario_cad_id) values (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, modelTelefone.getNumero());
		statement.setLong(2, modelTelefone.getUsuario_pai_id().getId());
		statement.setLong(3, modelTelefone.getUsuario_cad_id().getId());
		
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
	
	/***********************************************************************************/
	
	public ModelTelefone consultaTelefone(Long id) throws Exception {
		ModelTelefone telefone = new ModelTelefone();
		
		String sql = "select * from telefone where id=?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setLong(1, id);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			telefone.setId(resultado.getLong("id"));
			telefone.setNumero(resultado.getString("numero"));
			telefone.setUsuario_pai_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_pai_id")));
			telefone.setUsuario_cad_id(daoUsuarioRepository.consultarUsuarioId(resultado.getLong("usuario_cad_id")));
		}
		
		return telefone;
	}
	
	public ModelTelefone atualizaTelefone(Long id) throws Exception {
		String sql = "update telefone set numero=? where id=?";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, this.consultaTelefone(id).getNumero());
		statement.setLong(2, id);
		
		statement.executeUpdate();
		connection.commit();
		
		return this.consultaTelefone(id);
	}
}
