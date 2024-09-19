package br.com.dao;

import br.com.model.Playlist;
import br.com.model.Song;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlaylistDAO {
    private EntityManager em; // Gerenciador de Entidade
    // Construtor para inicializar o EntityManager

    public PlaylistDAO(EntityManager em) {
        this.em = em;
    }
    // Metodo para cadastrar uma nova playlist
    public void cadastrar(Playlist playlist){
        this.em.persist(playlist);
    }
    // Metodo para atualizar uma playlist existente
    public void atualizar(Playlist playlist){
        this.em.merge(playlist);
    }
    // Metodo para remover uma playlist
    public void remover(Playlist playlist){
        Playlist managed = this.em.find(Playlist.class, playlist.getId());
        if (managed != null) {
            this.em.remove(managed);
        }
    }
    // Metodo para adicionar as músicas à playlist
    public void addSongs(Playlist playlist, List<Long> songIds){
    // Adiciona as músicas à playlist antes de persistir
        for (Long songId : songIds) {
            Song song = em.find(Song.class, songId);
            if (song != null) {
                playlist.addSong(song);
            } else {
                System.out.println("Música com ID " + songId + " não encontrada.");
            }
        }
        this.em.merge(playlist);
    }
    // Metodo para buscar todas as playlists
    public List<Playlist> buscarTodas(){
        String jpql = "Select p from Playlist p JOIN p.songs";
        return em.createQuery(jpql, Playlist.class).getResultList();
    }
    // Metodo para buscar playlists por nome
    public List<Playlist> buscarPorNome(String nome){
        String jpql = "Select p from Playlist p where p.name = :nome";
        return em.createQuery(jpql, Playlist.class)
                .setParameter("nome", nome)
                .getResultList();
    }
    // Metodo para buscar playlists por nome de música
    public List<Playlist> buscarPorNomeDeMusica(String nome){
        String jpql = "SELECT p FROM Playlist p JOIN p.songs sp WHERE sp.title = :nome";
        return em.createQuery(jpql, Playlist.class)
                .setParameter("nome", nome)
                .getResultList();
    }
    // Metodo para calcular a duração total de uma playlist
    public Long totalizarDuracaoDaPlaylist(Long id){
        String jpql = "SELECT SUM(sp.duration) FROM Playlist p JOIN p.songs sp WHERE p.id = :id";
        return em.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
    }
    // Metodo para buscar músicas de uma playlist
    public Set<Song> buscarMusicasPorPlaylist(Long playlistId) {
        Playlist playlist = em.find(Playlist.class, playlistId);
        if (playlist != null) {
            return playlist.getSongs();
        } else {
            return new HashSet<>(); //retorna um conjunto vazio para evitar erros nulos
        }
    }
}
