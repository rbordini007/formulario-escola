package model.dao;

import java.util.List;

import model.entities.Instituicao;

public interface InstituicaoDao {
	void insert (Instituicao obj);
	void update(Instituicao obj);
	void deleteById(Integer id);
	Instituicao findById(Integer id);
	List<Instituicao> findAll();
}
