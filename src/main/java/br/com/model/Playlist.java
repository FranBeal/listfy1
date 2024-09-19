package br.com.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único da playlist

    @Column(length = 100, nullable = false)
    private String name; // Nome da playlist

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "playlist_musicas", // Nome da tabela associativa
            joinColumns = @JoinColumn(name = "playlist_id"), // Coluna que referencia a playlist
            inverseJoinColumns = @JoinColumn(name = "musicas_id") // Coluna que referencia a música
    )
    private Set<Song> songs = new HashSet<>(); // Conjunto de músicas na playlist

    // Construtor com parâmetros
    public Playlist(String name) {
        this.name = name;
    }
    // Métodos para adicionar e remover músicas
    public void addSong(Song song) {
        this.songs.add(song);
    }
    // Getters e Setters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<Song> getSongs() {
        return songs;
    }
    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", nome='" + name + '\'' +
                ", músicas=" + songs.stream()
                .map(song -> song.getTitle() + " (" + song.getArtist() + ", " + song.getDuration() + "s)")
                .collect(Collectors.toList()) +
                '}';
    }
}