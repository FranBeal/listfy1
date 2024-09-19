package br.com;

import br.com.dao.GeneroMusicalDAO;
import br.com.dao.PlaylistDAO;
import br.com.dao.SongDAO;
import br.com.model.GeneroMusical;
import br.com.model.Playlist;
import br.com.model.Song;
import br.com.util.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Obtém uma instância de EntityManager
        EntityManager em = JPAUtil.getEntityManager();
        int option;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("*===== Menu de Opções =====*");
            System.out.println("1. Cadastrar Gênero Musical");
            System.out.println("2. Listar Gêneros Musicais");
            System.out.println("3. Cadastrar Música");
            System.out.println("4. Listar Músicas");
            System.out.println("5. Cadastrar Playlist");
            System.out.println("6. Listar Playlists");
            System.out.println("7. Buscar Gênero Musical por Nome");
            System.out.println("8. Buscar Música por Nome");
            System.out.println("9. Buscar Músicas por Gênero Musical");
            System.out.println("10. Buscar Playlists por Nome");
            System.out.println("11. Buscar Playlists por Nome de Música");
            System.out.println("12. Totalizar Duração da Playlist");
            System.out.println("13. Sair");
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            switch (option) {
                case 1:// Cadastrar Gênero Musical
                    System.out.println("Digite o nome do gênero musical:");
                    String genreName = scanner.nextLine();

                    GeneroMusical genero = new GeneroMusical(genreName);

                    em.getTransaction().begin();
                    //criando uma instância de GeneroMusicalDAO de forma anônima (sem atribur a uma variável)
                    //e imediatamente chamando o metodo cadastrar().
                    //Após a chamada do metodo, a instância do DAO é descartada (não pode ser reutilizada).
                    new GeneroMusicalDAO(em).cadastrar(genero);
                    em.getTransaction().commit();
                    System.out.println("Gênero musical cadastrado com sucesso!");
                    break;
                case 2: // Listar Gêneros Musicais
                    List<GeneroMusical> generos = new GeneroMusicalDAO(em).buscarTodos();
                    //O metodo forEach() é usado para iterar sobre elementos da lista generos e
                    // e para cada elemento , ele aplica a operação de impressão no console
                    // usando System.out::println.
                    generos.forEach(System.out::println);
                    //Outra forma de usar o forEach é usando uma expressão lambda em vez de
                    //uma referência de metodo como no código acima, e seria:
                    //generos.forEach(genero -> System.out.println(genero));
                    break;
                case 3: // Cadastrar Música
                    System.out.println("Digite o título da música:");
                    String title = scanner.nextLine();
                    System.out.println("Digite o nome do artista:");
                    String artist = scanner.nextLine();
                    System.out.println("Digite a duração da música (em segundos):");
                    int duration = scanner.nextInt();
                    scanner.nextLine(); // fazer a quebra de linha
                    System.out.println("Digite o ID do gênero musical da música:");
                    Long generoId = scanner.nextLong();
                    scanner.nextLine(); // fazer a quebra de linha
//Buscar no banco o gênero musical do id informado
                    GeneroMusical generoMusical = em.find(GeneroMusical.class, generoId);
//Se encontrou o gênero musical
                    if (generoMusical != null) {
//cadastra a música
                        em.getTransaction().begin();
                        Song song = new Song();
                        song.setTitle(title);
                        song.setArtist(artist);
                        song.setDuration(duration);
                        song.setGeneroMusical(generoMusical);
                        new SongDAO(em).cadastrar(song);
                        em.getTransaction().commit();
                        System.out.println("Música cadastrada com sucesso!");
                    } else {
                        System.out.println("Gênero musical não encontrado.");
                    }
                    break;
                case 4: // Listar Músicas
                    List<Song> songs = new SongDAO(em).buscarTodos();
                    songs.forEach(System.out::println);
                    break;
                case 5: //Cadastrar playlist
                    System.out.println("Digite o nome da playlist:");
                    String playlistName = scanner.nextLine();
                    em.getTransaction().begin();
                    Playlist playlist = new Playlist(playlistName);
                    new PlaylistDAO(em).cadastrar(playlist);
                    List<Long> songIds = new ArrayList<>();
                    boolean adicionarMaisMusicas;
// Loop para adicionar músicas à lista de IDs
                    do {
                        System.out.println("Digite o ID da música que deseja adicionar à playlist:");
                        Long songId = scanner.nextLong();
                        scanner.nextLine(); // fazer a quebra de linha
                        songIds.add(songId);
                        System.out.println("Deseja adicionar outra música à playlist? (s/n)");
                        String resposta = scanner.nextLine();
//compara o valor informado na variável resposta com a letra "s" (ignora maiúsculas e minúsculas)
//retorna, true ou false
                        adicionarMaisMusicas = resposta.equalsIgnoreCase("s");
                    } while (adicionarMaisMusicas);
// Chama o metodo cadastrar no PlaylistDAO passando a playlist e os IDs das músicas
                    PlaylistDAO playlistDAO = new PlaylistDAO(em);
                    playlistDAO.addSongs(playlist, songIds);
                    em.getTransaction().commit();
                    System.out.println("Playlist cadastrada com sucesso!");
                    break;
                case 6: // Listar Playlists
                    List<Playlist> playlists = new PlaylistDAO(em).buscarTodas();
                    playlists.forEach(System.out::println);
                    break;
                case 7: //Buscar o gênero musical por nome
                    System.out.println("Digite o nome do gênero musical:");
                    String genreSearchName = scanner.nextLine();
                    List<GeneroMusical> generosBuscados = new GeneroMusicalDAO(em).buscarPorNome(genreSearchName);
                    generosBuscados.forEach(System.out::println);
                    break;
                case 8: // Buscar Música por Nome
                    System.out.println("Digite o nome da música:");
                    String musicSearchName = scanner.nextLine();
                    List<Song> musicasBuscadas = new SongDAO(em).buscarPorNome(musicSearchName);
                    musicasBuscadas.forEach(System.out::println);
                    break;
                case 9:// Buscar Músicas por Gênero Musical
                    System.out.println("Digite o nome do gênero musical:");
                    String musicGenreSearchName = scanner.nextLine();
                    List<Song> musicasPorGenero = new SongDAO(em).buscarPorNomeDeGenero(musicGenreSearchName);
                    musicasPorGenero.forEach(System.out::println);
                    break;
                case 10: // Buscar Playlist por Nome
                    System.out.println("Digite o nome da playlist:");
                    String playlistSearchName = scanner.nextLine();
                    List<Playlist> playlistsBuscadas = new PlaylistDAO(em).buscarPorNome(playlistSearchName);
                    playlistsBuscadas.forEach(System.out::println);
                    break;
                case 11:// Buscar Playlist por Nome de Música
                    System.out.println("Digite o nome da música:");
                    String musicNameInPlaylist = scanner.nextLine();
                    List<Playlist> playlistsPorMusica = new PlaylistDAO(em).buscarPorNomeDeMusica(musicNameInPlaylist);
                    playlistsPorMusica.forEach(System.out::println);
                    break;
                case 12: // Totalizar Duração da Playlist
                    System.out.println("Digite o ID da playlist:");
                    Long playlistId = scanner.nextLong();
                    scanner.nextLine(); // Consumir a quebra de linha
                    Long duracaoTotal = new PlaylistDAO(em).totalizarDuracaoDaPlaylist(playlistId);
                    System.out.println("Duração total da playlist: " + duracaoTotal + " segundos");
                    break;
                case 13: // Sair
                    System.out.println("Saindo...");
                    break;
                default: System.out.println("Opção inválida. Tente novamente.");
            }
        } while (option != 13);
        scanner.close();
        em.close();
        JPAUtil.closeEntityManagerFactory();
    }
}