package model.entities;

import java.io.Serializable;
import java.util.Date;

public class FichaSaude implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date dataDoDia;
	private Date dataAdmissao;
	private String nome;
	private Date dataNascimento;
	private String sexo;
	private Double estatura;
	private Double peso;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDataDoDia() {
		return dataDoDia;
	}
	public void setDataDoDia(Date dataDoDia) {
		this.dataDoDia = dataDoDia;
	}
	public Date getDataAdmissao() {
		return dataAdmissao;
	}
	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public Double getEstatura() {
		return estatura;
	}
	public void setEstatura(Double estatura) {
		this.estatura = estatura;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
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
		FichaSaude other = (FichaSaude) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FichaSaude [id=" + id + ", dataDoDia=" + dataDoDia + ", dataAdmissao=" + dataAdmissao + ", nome=" + nome
				+ ", dataNascimento=" + dataNascimento + ", sexo=" + sexo + ", estatura=" + estatura + ", peso=" + peso
				+ "]";
	}
}
