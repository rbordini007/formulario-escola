package model.entities;

import java.io.Serializable;
import java.util.Date;

public class Aluno extends Funcionario implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private String rg;
	private Date dataNascimento;
	private String telefone;
	private Funcionario funcionario;
	
	public Aluno() {
			}	

	public Aluno(Integer id, String nome, String rg, Date dataNascimento, String telefone) {			
		this.id = id;
		this.nome = nome;
		this.rg = rg;
		this.dataNascimento = dataNascimento;
		this.telefone = telefone;
	}

	public Aluno(Integer id, String nome, String rg, Date dataNascimento, String telefone, Funcionario funcionario) {		
		this(id, nome, rg, dataNascimento, telefone);		
		this.funcionario = funcionario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Aluno [id=" + id + ", nome=" + nome + ", rg=" + rg + ", dataNascimento=" + dataNascimento
				+ ", telefone=" + telefone + ", funcionario=" + funcionario + "]";
	}
	
}