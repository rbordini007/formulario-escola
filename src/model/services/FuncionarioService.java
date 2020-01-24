package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;

public class FuncionarioService {
	
	private FuncionarioDao dao = DaoFactory.createFuncionarioDao();
	
	public List<Funcionario> findAll(){
		/*
		 * List<Funcionario> list = new ArrayList<Funcionario>(); list.add(new
		 * Funcionario(1, "Daiane", "458998985-1", "123456789-87", "94567899"));
		 * list.add(new Funcionario(1, "Barbara", "458998985-1", "123456789-87",
		 * "897845656")); list.add(new Funcionario(1, "Vanilda", "458998985-1",
		 * "123456789-87", "94567899")); list.add(new Funcionario(1, "Ricardo",
		 * "458998985-1", "123456789-87", "94567899")); list.add(new Funcionario(1,
		 * "Melissa", "458998985-1", "123456789-87", "94567899")); 
		 * return list;
		 */
		
		return dao.findAll();
	}

}
