package br.com.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "musicas")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único da música

    @Column(name = "titulo", length = 100, nullable = false)
    private String title; // Título da música

    @Column(name = "cantor", length = 60, nullable = false)
    private String artist; // Artista da música

    @Column(name = "duracao", nullable = false)
    private int duration; // Duração da música em segundos

    @ManyToOne()
    @JoinColumn(name = "generos_musicais_id", referencedColumnName = "id")
    private GeneroMusical generoMusical; // Gênero musical da música

    @ManyToMany(mappedBy = "songs")
    private Set<Playlist> playlists = new HashSet<>(); // Conjunto de playlist da música

    // Getters e Setters
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getArtist() {
        return artist;
    }
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setGeneroMusical(GeneroMusical generoMusical) {
        this.generoMusical = generoMusical;
    }
    // Representação em string do objeto
    @Override
    public String toString() {
        return "Música{" +
                "id=" + id +
                ", Título='" + title + '\'' +
                ", Artista='" + artist + '\'' +
                ", Duração=" + duration +
                ", Gênero Musical=" + (generoMusical != null ? generoMusical.toString() : "null") +
                '}';
    }
}
