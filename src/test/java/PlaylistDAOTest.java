import br.com.dao.PlaylistDAO;
import br.com.model.GeneroMusical;
import br.com.model.Playlist;
import br.com.model.Song;
import br.com.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Set;

public class PlaylistDAOTest {
    private EntityManager em;
    private PlaylistDAO playlistDAO;
    @BeforeEach
    public void setUp() {
        em = JPAUtil.getEntityManager();
        playlistDAO = new PlaylistDAO(em);
//Criar uma transação
        em.getTransaction().begin();
// JPQL para deletar todas as músicas associadas a uma playlist específica
        String jpql = "DELETE FROM Song s WHERE s.id IN (SELECT sp.id FROM Playlist p JOIN p.songs sp)";
        em.createQuery(jpql).executeUpdate();
        em.createQuery("DELETE FROM Playlist p").executeUpdate();
        em.createQuery("DELETE FROM Song s").executeUpdate();
//Finaliza a transação
        em.getTransaction().commit();
    }
    @Test
    public void testCadastrarPlaylist() {
//Definir as músicas da playlist
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setDuration(354); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Imagine");
        song2.setArtist("John Lennon");
        song2.setDuration(183);
        song2.setGeneroMusical(gm);
        em.persist(song2);
        Song song3 = new Song();
        song3.setTitle("Hotel California");
        song3.setArtist("Eagles");
        song3.setDuration(391);
        song3.setGeneroMusical(gm);
        em.persist(song3);
        Playlist playlist = new Playlist("My Playlist 1");
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        playlistDAO.cadastrar(playlist);
        em.getTransaction().commit();
        Playlist found = em.find(Playlist.class, playlist.getId());
        assertNotNull(found);
        assertEquals("My Playlist 1", found.getName());
        assertEquals(3, found.getSongs().size());
        em.close();
    }
    @Test
    public void testAtualizarPlaylist() {
        em.getTransaction().begin();
        Playlist playlist = new Playlist("My Playlist 2");
        em.persist(playlist);
        em.getTransaction().commit();
        em.getTransaction().begin();
        playlist.setName("Minha lista 2");
        playlistDAO.atualizar(playlist);
        em.getTransaction().commit();
        Playlist found = em.find(Playlist.class, playlist.getId());
        assertEquals("Minha lista 2", found.getName());
        em.close();
    }
    @Test
    public void testRemoverPlaylist() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("MPB");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Aquarela");
        song1.setArtist("Toquinho");
        song1.setDuration(354); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Trem Bala");
        song2.setArtist("Ana Vilela");
        song2.setDuration(183);
        song2.setGeneroMusical(gm);
        em.persist(song2);
        Playlist playlist = new Playlist("Músicas para relaxar");
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlistDAO.cadastrar(playlist);
        em.getTransaction().commit();
        em.getTransaction().begin();
        playlistDAO.remover(playlist);
        em.getTransaction().commit();
        Playlist found = em.find(Playlist.class, playlist.getId());
        assertNull(found);
        em.close();
    }
    @Test
    public void testBuscarTodas() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setDuration(354); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Imagine");
        song2.setArtist("John Lennon");
        song2.setDuration(183);
        song2.setGeneroMusical(gm);
        em.persist(song2);
        Song song3 = new Song();
        song3.setTitle("Hotel California");
        song3.setArtist("Eagles");
        song3.setDuration(391);
        song3.setGeneroMusical(gm);
        em.persist(song3);
        Playlist playlist1 = new Playlist("My Playlist 1");
        playlist1.addSong(song1);
        playlist1.addSong(song2);
        playlist1.addSong(song3);
        playlistDAO.cadastrar(playlist1);
        em.getTransaction().commit();
        em.getTransaction().begin();
        GeneroMusical gm1 = new GeneroMusical("MPB");
        em.persist(gm1);
        Song song4 = new Song();
        song4.setTitle("Aquarela");
        song4.setArtist("Toquinho");
        song4.setDuration(354); // duração em segundos
        song4.setGeneroMusical(gm1);
        em.persist(song4);
        Song song5 = new Song();
        song5.setTitle("Trem Bala");
        song5.setArtist("Ana Vilela");
        song5.setDuration(183);
        song5.setGeneroMusical(gm1);
        em.persist(song5);
        Playlist playlist2 = new Playlist("Favoritas");
        playlist2.addSong(song4);
        playlist2.addSong(song5);
        playlistDAO.cadastrar(playlist2);
        em.getTransaction().commit();
        List<Playlist> playlists = playlistDAO.buscarTodas();
        System.out.println(playlists.toString());
        assertEquals(2, playlists.size());
        em.close();
    }
    @Test
    public void testBuscarPorNome() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("MPB");
        em.persist(gm);
        Song song4 = new Song();
        song4.setTitle("Aquarela");
        song4.setArtist("Toquinho");
        song4.setDuration(354); // duração em segundos
        song4.setGeneroMusical(gm);
        em.persist(song4);
        Song song5 = new Song();
        song5.setTitle("Trem Bala");
        song5.setArtist("Ana Vilela");
        song5.setDuration(183);
        song5.setGeneroMusical(gm);
        em.persist(song5);
        Playlist playlist2 = new Playlist("Favoritas");
        playlist2.addSong(song4);
        playlist2.addSong(song5);
        playlistDAO.cadastrar(playlist2);
        em.getTransaction().commit();
        List<Playlist> playlists = playlistDAO.buscarPorNome("Favoritas");
        assertEquals(1, playlists.size());
        assertEquals("Favoritas", playlists.get(0).getName());
        em.close();
    }
    @Test
    public void testBuscarPorNomeDeMusica() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        GeneroMusical gm1 = new GeneroMusical("MPB");
        em.persist(gm1);
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setDuration(354); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Trem Bala");
        song2.setArtist("Ana Vilela");
        song2.setDuration(183);
        em.persist(song2);
        Song song3 = new Song();
        song3.setTitle("Hotel California");
        song3.setArtist("Eagles");
        song3.setDuration(391);
        song3.setGeneroMusical(gm);
        em.persist(song3);
        Playlist playlist1 = new Playlist("My Playlist 1");
        playlist1.addSong(song1);
        playlist1.addSong(song2);
        playlist1.addSong(song3);
        playlistDAO.cadastrar(playlist1);
        Song song4 = new Song();
        song4.setTitle("Aquarela");
        song4.setArtist("Toquinho");
        song4.setDuration(354); // duração em segundos
        song4.setGeneroMusical(gm1);
        em.persist(song4);
        Playlist playlist2 = new Playlist("Favoritas");
        playlist2.addSong(song4);
        playlist2.addSong(song2);
        playlistDAO.cadastrar(playlist2);
        em.getTransaction().commit();
        List<Playlist> playlists = playlistDAO.buscarPorNomeDeMusica("Trem Bala");
        System.out.println(playlists.toString());
        assertEquals(2, playlists.size());
        em.close();
    }
    @Test
    public void testTotalizarDuracaoDaPlaylist() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setDuration(3); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Imagine");
        song2.setArtist("John Lennon");
        song2.setDuration(1);
        song2.setGeneroMusical(gm);
        em.persist(song2);
        Song song3 = new Song();
        song3.setTitle("Hotel California");
        song3.setArtist("Eagles");
        song3.setDuration(3);
        song3.setGeneroMusical(gm);
        em.persist(song3);
        Song song4 = new Song();
        song4.setTitle("Aquarela");
        song4.setArtist("Toquinho");
        song4.setDuration(3); // duração em segundos
        song4.setGeneroMusical(gm);
        em.persist(song4);
        Song song5 = new Song();
        song5.setTitle("Trem Bala");
        song5.setArtist("Ana Vilela");
        song5.setDuration(1);
        song5.setGeneroMusical(gm);
        em.persist(song5);
        Playlist playlist = new Playlist("Aniversário");
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        playlist.addSong(song4);
        playlist.addSong(song5);
        playlistDAO.cadastrar(playlist);
        em.getTransaction().commit();
        Long totalDuration = playlistDAO.totalizarDuracaoDaPlaylist(playlist.getId());
        assertEquals(11, totalDuration);
        em.close();
    }
    @Test
    public void testBuscarMusicasPorPlaylist() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Bohemian Rhapsody");
        song1.setArtist("Queen");
        song1.setDuration(3); // duração em segundos
        song1.setGeneroMusical(gm);
        em.persist(song1);
        Song song2 = new Song();
        song2.setTitle("Imagine");
        song2.setArtist("John Lennon");
        song2.setDuration(1);
        song2.setGeneroMusical(gm);
        em.persist(song2);
        Song song3 = new Song();
        song3.setTitle("Hotel California");
        song3.setArtist("Eagles");
        song3.setDuration(3);
        song3.setGeneroMusical(gm);
        em.persist(song3);
        Song song4 = new Song();
        song4.setTitle("Aquarela");
        song4.setArtist("Toquinho");
        song4.setDuration(3); // duração em segundos
        song4.setGeneroMusical(gm);
        em.persist(song4);
        Song song5 = new Song();
        song5.setTitle("Trem Bala");
        song5.setArtist("Ana Vilela");
        song5.setDuration(1);
        song5.setGeneroMusical(gm);
        em.persist(song5);
        Playlist playlist = new Playlist("Aniversário");
        playlist.addSong(song1);
        playlist.addSong(song2);
        playlist.addSong(song3);
        playlist.addSong(song4);
        playlist.addSong(song5);
        playlistDAO.cadastrar(playlist);
        em.getTransaction().commit();
        Set<Song> songs = playlistDAO.buscarMusicasPorPlaylist(playlist.getId());
        assertEquals("Aniversário", playlist.getName());
        assertEquals(5, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        assertTrue(songs.contains(song3));
        assertTrue(songs.contains(song4));
        assertTrue(songs.contains(song5));
        em.close();
    }
}
