package model.services;

import java.util.List;

import model.dao.AlunoDao;
import model.dao.DaoFactory;
import model.dao.FichaSaudeDao;
import model.entities.FichaSaude;

public class FichaSaudeService {
	
	private FichaSaudeDao dao = DaoFactory.createFichaSaudeDao();

	public List<FichaSaude> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
