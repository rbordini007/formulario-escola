package model.dao;

import db.DB;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.FichaSaudeJDBC;
import model.dao.impl.FuncionarioDaoJDBC;
import model.dao.impl.InstituicaoDaoJDBC;

public class DaoFactory {
	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());		
	}
	
	public static InstituicaoDao createInstituicaoDao() {
		return new InstituicaoDaoJDBC(DB.getConnection());
	}
	
	public static AlunoDao createAlunoDao() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
	
	public static FichaSaudeDao createFichaSaudeDao() {
		return new FichaSaudeJDBC();
	}
}
