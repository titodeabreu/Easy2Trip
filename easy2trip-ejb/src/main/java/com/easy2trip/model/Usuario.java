package com.easy2trip.model;

import java.util.*;

import javax.persistence.*;

@Entity
@NamedQueries({
	@NamedQuery(name=Usuario.NQ_BUSCAPORNOMEEMAILOULOGIN,
			query="select u from Usuario u where u.nome like :palavra or u.login like :palavra or u.email like :palavra"),
	@NamedQuery(name=Usuario.NQ_BUSCAPORLOGIN,
			query="select u from Usuario u where u.login like :login"),
	@NamedQuery(name=Usuario.NQ_BUSCAPOREMAIL, query="select u from Usuario u where u.email = :email")
})
@Table(name="usuario")
public class Usuario implements AbstractEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	public static final String NQ_BUSCAPOREMAIL = "Usuario.buscaPorEmail";
	public static final String NQ_BUSCAPORLOGIN = "Usuario.buscaPorLogin";
	public static final String NQ_BUSCAPORNOMEEMAILOULOGIN = "Usuario.buscaPorNomeEmailOuLogin";
	
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=50, unique=true)
	private String login;
	
	@Column(length =100, nullable = false, unique=true)
	private String email;
	
	@Column(length=255)
	private String nome;
	
	@Column(length=255)
	private String biografia;
	
	@Column(length = 20, nullable = false)
	private String password;
	
	@Temporal(TemporalType.DATE)
	private Date nascimento;
	
	@Column(length=10)
	private String idioma;
	
	@Column(length=50)
	private String role;
	
	private boolean ativo;
	
	@Column(length=100)
	private String religiao;
	
	@Column(length=1000)
	private String info;
	
	@Column(name="ts_criacao",updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tsCriacao;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date ts;
	
	@Column(name="tem_foto")
	private boolean temFoto;
	
	@Column(name="path_foto")
	private String pathFoto;

	
	
	/*@OneToMany(mappedBy="usuario")
	private List<Amigo> amigos;*/
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		
		this.login = login;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getTsCriacao() {
		return tsCriacao;
	}

	public void setTsCriacao(Date tsCriacao) {
		this.tsCriacao = tsCriacao;
	}

	public boolean isTemFoto() {
		return temFoto;
	}

	public void setTemFoto(boolean temFoto) {
		this.temFoto = temFoto;
	}

	public String getPathFoto() {
		return pathFoto;
	}

	public void setPathFoto(String pathFoto) {
		this.pathFoto = pathFoto;
	}

	public String getReligiao() {
		return religiao;
	}

	public void setReligiao(String religiao) {
		this.religiao = religiao;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", email=" + email
				+ ", nome=" + nome + ", biografia=" + biografia + ", password="
				+ password + ", nascimento=" + nascimento + ", idioma="
				+ idioma + ", role=" + role + ", ativo=" + ativo
				+ ", religiao=" + religiao + ", info=" + info + ", tsCriacao="
				+ tsCriacao + ", ts=" + ts + ", temFoto=" + temFoto
				+ ", pathFoto=" + pathFoto + "]";
	}


	
}
