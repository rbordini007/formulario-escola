package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.FuncionarioDao;
import model.entities.Funcionario;

public class Testeaplicação {
	public static void main(String[] args) {
		
		FuncionarioDao funcionarioDao = DaoFactory.createFuncionarioDao();
		Funcionario funcionario = funcionarioDao.findById(1);
		
		System.out.println(funcionario);
		
		//======================================================
		System.out.println("============================================");
		List<Funcionario>list = funcionarioDao.findAll();
		list.forEach((obj)-> {
			System.out.println(obj);
		});
		
	}
}
