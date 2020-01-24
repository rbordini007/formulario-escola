package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

	@Override
	public void insert(Funcionario obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Funcionario obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Funcionario findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM escola.funcionario where Id =?");
			
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

	@Override
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM escola.funcionario Order By Nome");
			
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
