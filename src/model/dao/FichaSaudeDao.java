package model.dao;

import java.util.List;

import model.entities.Aluno;
import model.entities.FichaSaude;

public interface FichaSaudeDao {
	
	void insert(FichaSaude obj);
	void update(FichaSaude obj);
	void deleteById(Integer id);
	FichaSaude findById(Integer id);
	List<FichaSaude> findAll();
	List<FichaSaude> findById(Aluno aluno);
}
