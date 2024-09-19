package br.com.dao;

import br.com.model.Song;
import jakarta.persistence.EntityManager;
import java.util.List;

public class SongDAO {

    private EntityManager em; // Gerenciador de Entidades
    // Construtor para inicializar o EntityManager
    public SongDAO(EntityManager em) {
        this.em = em;
    }
    // Metodo para cadastrar uma nova música
    public void cadastrar(Song song){
        this.em.persist(song);
    }
    // Metodo para atualizar uma música existente
    public void atualizar(Song song){
        this.em.merge(song);
    }
    // Metodo para remover uma música
    public void remover(Song song){
        Song managed = this.em.find(Song.class, song.getId());
        if (managed != null) {
            this.em.remove(managed);
        }
    }
    // Metodo para buscar todas as músicas
    public List<Song> buscarTodos(){
        String jpql = "Select s from Song s"; //GeneroMusical não é o nome da tabela, é o nome da entidade
        return em.createQuery(jpql, Song.class).getResultList();
    }
    // Metodo para buscar músicas por títul
    public List<Song> buscarPorNome(String nome){
        String jpql = "Select s from Song s where s.title = :nome";
        return em.createQuery(jpql, Song.class)
                .setParameter("nome", nome)
                .getResultList();
    }
    // Metodo para buscar músicas por nome de gênero
    public List<Song> buscarPorNomeDeGenero(String nome){
        String jpql = "Select s from Song s where s.generoMusical.nome = :nome";
        return em.createQuery(jpql, Song.class)
                .setParameter("nome", nome)
                .getResultList();
    }
    // Metodo para buscar a duração de uma música pelo título
    public int buscarDuracaoPorNome(String nome){
        String jpql = "Select s.duration from Song s where s.title = :nome";
        return em.createQuery(jpql, Integer.class)
                .setParameter("nome", nome)
                .getSingleResult();
    }
}
