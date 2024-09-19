import br.com.dao.GeneroMusicalDAO;
import br.com.model.GeneroMusical;
import br.com.util.JPAUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GeneroMusicalDAOTest {

    private EntityManager em;
    private GeneroMusicalDAO generoDao;

    @BeforeEach
    public void setUp() {
        this.em = JPAUtil.getEntityManager();

        this.generoDao = new GeneroMusicalDAO(em);

        //Limpa o banco de dados, removendo todos os registros de todas as tabelas
        //Criar uma transação
        this.em.getTransaction().begin();
        // JPQL para deletar todas as músicas associadas a uma playlist específica
        String jpql = "DELETE FROM Song s WHERE s.id IN (SELECT sp.id FROM Playlist p JOIN p.songs sp)";
        this.em.createQuery(jpql).executeUpdate();
        this.em.createQuery("DELETE FROM Playlist p").executeUpdate();
        this.em.createQuery("DELETE FROM Song s").executeUpdate();
        // Limpa a tabela antes de cada teste
        this.em.createQuery("DELETE FROM GeneroMusical").executeUpdate();
        //Finaliza a transação
        this.em.getTransaction().commit();
    }

    @Test
    public void testCadastrar(){
        //Criar uma transação
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Samba");
        //Chama o metodo a ser testado
        this.generoDao.cadastrar(gm);
        //Finaliza a transação
        em.getTransaction().commit();

        // Verifica se o ID foi gerado, o que indica que o objeto foi persistido
        assertNotNull(gm.getId());
        em.close();
    }

    @Test
    public void testAtualizar() {
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Sertanejo");
        em.persist(gm);
        em.getTransaction().commit();
        em.getTransaction().begin();
        // Atualiza o nome do gênero musical
        gm.setNome("Pop Rock");
        this.generoDao.atualizar(gm);
        em.getTransaction().commit();
        // Recupera o objeto atualizado
        GeneroMusical atualizado = em.find(GeneroMusical.class, gm.getId());
        //Testa o resultado da atualização
        assertEquals("Pop Rock", atualizado.getNome());
        em.close();
    }

    @Test
    public void testRemover(){
        em.getTransaction().begin();
        GeneroMusical gm = new GeneroMusical("Blues");
        em.persist(gm);
        em.getTransaction().commit();
        em.getTransaction().begin();
        this.generoDao.remover(gm); // Remover o gênero musical
        em.getTransaction().commit();
        // Verifica se o objeto foi removido corretamente
        GeneroMusical removido = em.find(GeneroMusical.class, gm.getId());
        assertNull(removido);
        em.close();
    }

    @Test
    public void testBuscarTodos(){
        em.getTransaction().begin();
        // Cria e persiste alguns objetos GeneroMusical
        GeneroMusical gm1 = new GeneroMusical("Pagode");
        em.persist(gm1);
        GeneroMusical gm2 = new GeneroMusical("MBP");
        em.persist(gm2);
        GeneroMusical gm3 = new GeneroMusical("Sertanejo");
        em.persist(gm3);
        //Finaliza a transação
        em.getTransaction().commit();

        // Busca todos os objetos persistidos
        List<GeneroMusical> generos = this.generoDao.buscarTodos();

        // Verifica se todos os objetos foram retornados corretamente
        assertEquals(3, generos.size());
        assertTrue(generos.contains(gm1));
        assertTrue(generos.contains(gm2));
        assertTrue(generos.contains(gm3));
        em.close();
    }


    @Test
    public void testBuscarPorNome(){
        em.getTransaction().begin();
        // Cria e persiste alguns objetos GeneroMusical
        GeneroMusical gm1 = new GeneroMusical("Gospel");
        em.persist(gm1);
        GeneroMusical gm2 = new GeneroMusical("Rap");
        em.persist(gm2);
        GeneroMusical gm3 = new GeneroMusical("Gospel");
        em.persist(gm3);
        //Finaliza a transação
        em.getTransaction().commit();
        // Busca todos os objetos persistidos
        List<GeneroMusical> generosGospel = this.generoDao.buscarPorNome("Gospel");

        // Verifica se apenas os objetos com nome "Gospel" foram retornados
        assertEquals(2, generosGospel.size());
        assertTrue(generosGospel.contains(gm1));
        assertTrue(generosGospel.contains(gm3));
        // Verifica se o objeto com nome "Rap" não foi retornado
        assertFalse(generosGospel.contains(gm2));
        // Fecha o EntityManager
        em.close();
    }
}
