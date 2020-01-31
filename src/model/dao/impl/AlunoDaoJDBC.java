package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.AlunoDao;
import model.entities.Aluno;
import model.entities.Funcionario;

public class AlunoDaoJDBC implements AlunoDao {

	private Connection conn;

	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Aluno obj) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement(
							"Insert into formulario_escola.aluno "
									+ "(nome, rg, dataNascimento, telefone, funcionarioId) " + "values (?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getRg());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setString(4, obj.getTelefone());
			st.setInt(5, obj.getFuncionario().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Aluno obj) {
		PreparedStatement st = null;		
		try {
			st = conn.prepareStatement(
							"update formulario_escola.aluno "
									+ "set nome = ?, rg = ?, dataNascimento = ?, telefone = ?, funcionarioId = ? "
									+ "where id = ?"
							);

			st.setString(1, obj.getNome());
			st.setString(2, obj.getRg());
			st.setDate(3, new java.sql.Date(obj.getDataNascimento().getTime()));
			st.setString(4, obj.getTelefone());
			st.setInt(5, obj.getFuncionario().getId());
			st.setInt(6, obj.getId());

			st.executeUpdate();
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}	
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
						"Delete from formulario_escola.aluno where Id = ?"
					);
			
			st.setInt(1, id);
			st.executeUpdate();			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public Aluno findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select formulario_escola.aluno.*, "
							+ "funcionario.cpf as funCpf, funcionario.nome as funNome,"
							+ " funcionario.rg as funRg, funcionario.telefone as funTelefone  "+ 
							"FROM aluno INNER JOIN funcionario " + 
							"ON aluno.funcionarioId = funcionario.id " + 
							"WHERE aluno.Id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			if (rs.next()) {
				Funcionario funcionario = instantiateFuncionario(rs);
				Aluno obj = instantiateAluno(rs, funcionario);
				return obj;
			}
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}		
	}	

	@Override
	public List<Aluno> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select formulario_escola.aluno.*, "
					+ "funcionario.cpf as funCpf, funcionario.nome as funNome,"
					+ " funcionario.rg as funRg, funcionario.telefone as funTelefone  "
					+ "from formulario_escola.aluno inner join formulario_escola.funcionario " 
					+ "on formulario_escola.funcionario.Id = aluno.funcionarioId order by nome");
			
			rs = st.executeQuery();
			
			List<Aluno>list = new ArrayList<Aluno>();
			
			
			while (rs.next()) {
				Map<Integer, Funcionario> map = new HashMap<>();
				Funcionario funcio = map.get(rs.getInt("funcionarioId"));
				
				if (funcio == null) {
					funcio = instantiateFuncionario(rs);
					map.put(rs.getInt("funcionarioId"), funcio);
				}
				Aluno obj = instantiateAluno(rs, funcio);
				list.add(obj);
			}
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}		
	}

	@Override
	public List<Aluno> findByFuncionario(Funcionario funcionario) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select formulario_escola.aluno.*, " 
							+"funcionario.cpf as funCpf, funcionario.nome as funNome, "
							+ " funcionario.rg as funRg, funcionario.telefone as funTelefone "
							+"FROM aluno inner join funcionario "
							+"ON aluno.funcionarioId = funcionario.id " 
							+"WHERE funcionarioId = ? "
							+ "ORDER BY nome ");
			
			st.setInt(1, funcionario.getId());			
			rs = st.executeQuery();
			
			List<Aluno>list = new ArrayList<Aluno>();
			Map<Integer, Funcionario> map = new HashMap<>();
			
			while (rs.next()) {
				Funcionario funcio = map.get(rs.getInt("funcionarioId"));
				if (funcio == null) {
					funcio = instantiateFuncionario(rs);
					map.put(rs.getInt("funcionarioId"), funcio);
				}
				
				Aluno obj = instantiateAluno(rs, funcio);
				list.add(obj);
			}
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}		
	}	

	private Funcionario instantiateFuncionario(ResultSet rs)throws SQLException {
		Funcionario funcionario = new Funcionario();
		funcionario.setId(rs.getInt("funcionarioId"));
		funcionario.setNome(rs.getString("funNome"));
		funcionario.setRg(rs.getString("funRg"));		
		funcionario.setTelefone(rs.getString("funTelefone"));
		funcionario.setCpf(rs.getString("funCpf"));
		return funcionario;
	}
	
	private Aluno instantiateAluno(ResultSet rs, Funcionario funci)throws SQLException {
		Aluno obj = new Aluno();		
		
		obj.setId(rs.getInt("id"));
		obj.setNome(rs.getString("nome"));
		obj.setRg(rs.getString("rg"));
		obj.setTelefone(rs.getString("telefone"));
		obj.setDataNascimento(new java.util.Date(rs.getTimestamp("dataNascimento").getTime()));
		obj.setFuncionario(funci);
		return obj;
	}
}
