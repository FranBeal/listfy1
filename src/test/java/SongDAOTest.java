import br.com.dao.SongDAO;
import br.com.model.GeneroMusical;
import br.com.model.Song;
import br.com.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SongDAOTest {
    private EntityManager em;
    private SongDAO songDAO;
    @BeforeEach
    public void setUp() {
        em = JPAUtil.getEntityManager();
        songDAO = new SongDAO(em);
        //Criar uma transação
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Song").executeUpdate();
        em.createQuery("DELETE FROM GeneroMusical").executeUpdate();
        //Finaliza a transação
        em.getTransaction().commit();
    }
    @Test
    public void testCadastrar() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song = new Song();
        song.setTitle("Bohemian Rhapsody");
        song.setArtist("Queen");
        song.setDuration(354); // duração em segundos
        song.setGeneroMusical(gm);
        songDAO.cadastrar(song);
        em.getTransaction().commit();
        Song foundSong = em.find(Song.class, song.getId());
        assertNotNull(foundSong);
        assertEquals("Bohemian Rhapsody", foundSong.getTitle());
        em.close();
    }
    @Test
    public void testAtualizar() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song = new Song();
        song.setTitle("Imagine");
        song.setArtist("John Lennon");
        song.setDuration(183);
        song.setGeneroMusical(gm);
        em.persist(song);
        em.getTransaction().commit();
// Atualiza a duração da música
        em.getTransaction().begin();
        song.setDuration(190);
        songDAO.atualizar(song);
        em.getTransaction().commit();
        Song updatedSong = em.find(Song.class, song.getId());
        assertEquals(190, updatedSong.getDuration());
        em.close();
    }
    @Test
    public void testRemover() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song = new Song();
        song.setTitle("Hotel California");
        song.setArtist("Eagles");
        song.setDuration(391);
        song.setGeneroMusical(gm);
        em.persist(song);
        em.getTransaction().commit();
// Remove a música
        em.getTransaction().begin();
        songDAO.remover(song);
        em.getTransaction().commit();
        Song removedSong = em.find(Song.class, song.getId());
        assertNull(removedSong);
        em.close();
    }
    @Test
    public void testBuscarTodos() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Stairway to Heaven");
        song1.setArtist("Led Zeppelin");
        song1.setDuration(482);
        song1.setGeneroMusical(gm);
        Song song2 = new Song();
        song2.setTitle("Smells Like Teen Spirit");
        song2.setArtist("Nirvana");
        song2.setDuration(301);
        song2.setGeneroMusical(gm);
        em.persist(song1);
        em.persist(song2);
        em.getTransaction().commit();
        List<Song> songs = songDAO.buscarTodos();
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        em.close();
    }
    @Test
    public void testBuscarPorNome() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Wonderwall");
        song1.setArtist("Oasis");
        song1.setDuration(258);
        song1.setGeneroMusical(gm);
        Song song2 = new Song();
        song2.setTitle("Hey Jude");
        song2.setArtist("The Beatles");
        song2.setDuration(431);
        song2.setGeneroMusical(gm);
        em.persist(song1);
        em.persist(song2);
        em.getTransaction().commit();
        List<Song> songs = songDAO.buscarPorNome("Wonderwall");
        assertEquals(1, songs.size());
        assertTrue(songs.contains(song1));
        em.close();
    }
    @Test
    public void testBuscarPorNomeDeGenero() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song1 = new Song();
        song1.setTitle("Thunderstruck");
        song1.setArtist("AC/DC");
        song1.setDuration(292);
        song1.setGeneroMusical(gm);
        Song song2 = new Song();
        song2.setTitle("Back in Black");
        song2.setArtist("AC/DC");
        song2.setDuration(255);
        song2.setGeneroMusical(gm);
        em.persist(song1);
        em.persist(song2);
        em.getTransaction().commit();
        List<Song> songs = songDAO.buscarPorNomeDeGenero("Rock");
        assertEquals(2, songs.size());
        assertTrue(songs.contains(song1));
        assertTrue(songs.contains(song2));
        em.close();
    }
    @Test
    public void testBuscarDuracaoPorNome() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Rock");
        em.persist(gm);
        Song song = new Song();
        song.setTitle("November Rain");
        song.setArtist("Guns N´Roses");
        song.setDuration(537);
        song.setGeneroMusical(gm);
        em.persist(song);
        em.getTransaction().commit();
        int duration = songDAO.buscarDuracaoPorNome("November Rain");
        assertEquals(537, duration);
        em.close();
    }
}
