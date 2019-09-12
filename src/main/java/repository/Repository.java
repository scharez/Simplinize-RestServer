package repository;

import entity.Role;
import entity.SkiTeacher;
import org.bouncycastle.util.encoders.Hex;
import utils.JsonBuilder;
import utils.JwtHelper;
import utils.Mail;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static Repository instance = null;

    private JsonBuilder jb = new JsonBuilder();
    private JwtHelper jwt = new JwtHelper();
    private Mail mailer = new Mail();

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("SimplinizePU");
    private EntityManager em = emf.createEntityManager();

    private ExecutorService executor = Executors.newCachedThreadPool();

    private String token;

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    /**
     * get the JWT-Token into Repository from the Filter
     *
     * @param token
     */

    public void saveHeader(String token) {
        this.token = token;
    }

    private String jwtError() {
        return jb.generateResponse("error", "jwt", "Wrong Token!");
    }

    public String login(String username, String password) {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getUser", SkiTeacher.class);
        query.setParameter("username", username);

        if (query.getResultList().size() == 0) {
            return jb.generateResponse("error", "login", "User does not exist");
        }

        SkiTeacher user = query.getResultList().get(0);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(user.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(user.getPassword())) {
                return jb.generateResponse("error", "login", "Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(user.getUsername(), user.getRoles().toArray());

        return jb.generateResponse("success", "login", jwtToken);
    }

    public String addSkiTeacher(String firstName, String lastName, String email, List<Role> roles) {

        SkiTeacher user = new SkiTeacher(firstName, lastName, email, roles);

        TypedQuery<Long> queryUniqueMail = em.createNamedQuery("SkiTeacher.countEmail", Long.class);
        queryUniqueMail.setParameter("email", user.getEmail());

        if(queryUniqueMail.getSingleResult() != 0) {
            return jb.generateResponse("error", "addSkiTeacher", "Email already exists");
        }

        executor.execute(() -> mailer.sendRegisterEmail());

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        return jb.generateResponse("success", "addSkiTeacher", "SkiTeacher successfully added");
    }

    public String setPassword4SkiTeacher(String token, String password) {

        return "";
    }
}
