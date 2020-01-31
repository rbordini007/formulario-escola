package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.InstituicaoDao;
import model.entities.Instituicao;

public class InstituicaoService {
	
	private InstituicaoDao dao = DaoFactory.createInstituicaoDao();
	
	public List<Instituicao> findAll() {
		return dao.findAll();
	}
	
	public void saveOrUpdate(Instituicao obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Instituicao obj) {
		dao.deleteById(obj.getId());
	}

}
