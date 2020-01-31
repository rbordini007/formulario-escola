package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;

public class FuncionarioDaoJDBC implements FuncionarioDao {
	
	private Connection conn;	

	public FuncionarioDaoJDBC(Connection conn) {		
		this.conn = conn;
	}
	
	//================= insert ==================
	@Override
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"Insert Into formulario_escola.funcionario "
					+ "(Nome, Rg, Cpf, Telefone) "
					+ "Values "
					+ "(?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getTelefone());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Unexpected error: No rows affected");
			}		
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	// ================ update =================
	@Override
	public void update(Funcionario obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"Update formulario_escola.funcionario Set "
					+ "Nome = ?, Rg = ?, Cpf = ?, Telefone = ? "
					+ "where id = ?");
			st.setString(1, obj.getNome());
			st.setString(2, obj.getRg());
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getTelefone());
			st.setInt(5, obj.getId());
			
			st.executeUpdate();			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}		
	}

	// ================= Delete By Id ===================
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from formulario_escola.funcionario where Id = ?");
			
			st.setInt(1, id);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}		
	}
	
	// =============== Find By Id =======================
	@Override
	public Funcionario findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM formulario_escola.funcionario where Id =?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Funcionario obj = new Funcionario();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setRg(rs.getString("Rg"));
				obj.setCpf(rs.getString("Cpf"));
				obj.setTelefone(rs.getString("Telefone"));
				
				return obj;
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	// =================== Find All =====================
	@Override
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM formulario_escola.funcionario Order By Nome");
			
			rs = st.executeQuery();
			
			List<Funcionario>list = new ArrayList<Funcionario>();
			
			while (rs.next()) {
				Funcionario obj = new Funcionario();
				obj.setId(rs.getInt("Id"));
				obj.setNome(rs.getString("Nome"));
				obj.setRg(rs.getString("Rg"));
				obj.setCpf(rs.getString("Cpf"));
				obj.setTelefone(rs.getString("Telefone"));
				list.add(obj);				
			}
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
}
