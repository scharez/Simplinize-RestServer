package repository;

import entity.*;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
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
     * get the JWT-Token into Repository from Authfilter
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

        user.setUsername(firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase());

        Token token = new Token(user);

        TypedQuery<Long> queryUniqueMail = em.createNamedQuery("SkiTeacher.countEmail", Long.class);
        queryUniqueMail.setParameter("email", user.getEmail());

        if(queryUniqueMail.getSingleResult() != 0) {
            return jb.generateResponse("error", "addSkiTeacher", "Email already exists");
        }

        executor.execute(() -> mailer.sendSetPasswordMail(token, user));

        em.getTransaction().begin();
        em.persist(user);
        em.persist(token);
        em.getTransaction().commit();

        return jb.generateResponse("success", "addSkiTeacher", "SkiTeacher added");
    }

    public String setPassword4SkiTeacher(String token, String password) {

        TypedQuery<Token> queryToken = em.createQuery("SELECT t FROM Token t WHERE t.token = :token", Token.class);
        queryToken.setParameter("token", token);

        List<Token> tokenList = queryToken.getResultList();

        if(tokenList.size() == 0) {
            //Error
            System.out.println("Error");
            return "Error";
        }

        SkiTeacher user = tokenList.get(0).getSkiTeacher();

        user.setPassword(password);

        em.getTransaction().begin();
        em.remove(token);
        em.getTransaction().commit();

        return "Password successfully set";
    }

    public String assignCourse(Course course) {



        return "";
    }

    public String addTeacherToGroup(long groupId, SkiTeacher skiTeacher) {



        return "";
    }

    public String getAllGroups() {

        TypedQuery<CourseGroup> query = em.createNamedQuery("CourseGroup.getAllGroups", CourseGroup.class);

        List<CourseGroup> groupList = query.getResultList();

        if(groupList.size() == 0) {
            return jb.generateResponse("error", "getAllGroups","There are no Groups");
        }

        JSONArray lol = new JSONArray(groupList);

        return jb.generateDataResponse("success","getAllGroups",lol);
    }

    public String getAllMembers() {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if(studentList.size() == 0) {
            return jb.generateResponse("error", "getAllMembers", "There are no members");
        }
        return "";
    }

}
