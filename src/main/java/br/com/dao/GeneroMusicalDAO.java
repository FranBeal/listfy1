package br.com.dao;
import br.com.model.GeneroMusical;
import jakarta.persistence.EntityManager;
import java.util.List;

public class GeneroMusicalDAO {

    private EntityManager em; // Gerenciador de Entidades
    // Construtor para inicializar o EntityManager

    public GeneroMusicalDAO(EntityManager em) {
        this.em = em;
    }

    // Metodo para cadastrar um novo Gênero Musical
    public void cadastrar(GeneroMusical gm){
        this.em.persist(gm);
    }
    // Metodo para atualizar um Gênero Musical existente
    public void atualizar(GeneroMusical gm){
        this.em.merge(gm);
    }
    // Metodo para remover um Gênero Musical
    public void remover(GeneroMusical gm){
        GeneroMusical managed = this.em.find(GeneroMusical.class, gm.getId());
        if (managed != null) {
            this.em.remove(managed);
        }
    }
    // Metodo para buscar todos os Gêneros Musicais
    public List<GeneroMusical> buscarTodos(){
        String jpql = "Select gm from GeneroMusical gm"; //GeneroMusical não é o nome da tabela, é o nome da entidade
        return em.createQuery(jpql, GeneroMusical.class).getResultList();
    }
    // Metodo para buscar todos Gênero Musical pelo nome
    public List<GeneroMusical> buscarPorNome(String nome){
        String jpql = "Select gm from GeneroMusical gm where gm.nome = :nome";
        return em.createQuery(jpql, GeneroMusical.class)
                .setParameter("nome", nome)
                .getResultList();
    }
}
