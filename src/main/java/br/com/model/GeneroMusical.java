package br.com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "generos_musicais")
public class GeneroMusical {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único do gênero musical

    @Column(name="genero_musical", length = 40, nullable = false)
    private String nome; // Nome do gênero musical

    // Construtor com nome
    public GeneroMusical(String music_genre) {
        this.nome = music_genre;
    }
    // Getters e Setters
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Long getId() {
        return id;
    }
    // Representação em string do objeto
    @Override
    public String toString() {
        return "GeneroMusical{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
