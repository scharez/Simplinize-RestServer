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
import java.util.Date;
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

    private Person getPerson() {

        String subject = jwt.checkSubject(this.token);



        return new Person();
    }

    public String loginTeacher(String username, String password) {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getUserByUsername", SkiTeacher.class);
        query.setParameter("username", username);

        if (query.getResultList().size() == 0) {
            return jb.generateResponse("error", "loginTeacher", "User does not exist");
        }

        SkiTeacher user = query.getResultList().get(0);

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(user.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(user.getPassword())) {
                return jb.generateResponse("error", "loginTeacher", "Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(user.getEmail(), user.getRoles().toArray());

        return jb.generateResponse("success", "loginTeacher", jwtToken);
    }

    public String addSkiTeacher(String firstName, String lastName, String email, List<Role> roles) {

        roles.add(Role.SKITEAM);
        roles.add(Role.SKITEAM);

        SkiTeacher user = new SkiTeacher(firstName, lastName, email, roles);


        user.setUsername(firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase());

        Token token = new Token(user);

        TypedQuery<Long> queryUniqueMail = em.createNamedQuery("SkiTeacher.uniqueEmail", Long.class);
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
        Token dbToken = user.getToken();

        user.setPassword(password);

        em.getTransaction().begin();
        //em.remove(dbToken);
        em.getTransaction().commit();

        return "Password successfully set";
    }

    public String loginContactPerson(String email, String password) {

        TypedQuery<ContactPerson> query = em.createNamedQuery("ContactPerson.getPerson", ContactPerson.class);
        query.setParameter("email", email);

        List<ContactPerson> result = query.getResultList();

        if (result.size() == 0) {
            return jb.generateResponse("error", "loginContactPerson", "Person does not exist"); // Error
        }

        ContactPerson person = result.get(0);

        if(!person.isVerified()) {
            return jb.generateResponse("error", "loginContactPerson", "Please confirm your email first");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(person.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(person.getPassword())) {
                return jb.generateResponse("error", "loginContactPerson", "Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        person.setRole(Role.CONTACTPERSON);

        String jwtToken = jwt.create(person.getEmail(), new Role[]{person.getRole()});

        return jb.generateResponse("success", "loginContactPerson", jwtToken);
    }

    public String registerContactPerson(String firstName, String lastName, String email, String password, String phoneNumber) {

        ContactPerson person = new ContactPerson(firstName, lastName, email, password, phoneNumber);

        person.setRole(Role.CONTACTPERSON);

        Token token = new Token(person);

        TypedQuery<Long> queryUniqueEmail = em.createNamedQuery("ContactPerson.uniqueEmail", Long.class);
        queryUniqueEmail.setParameter("email", email);

        long numberOfEntriesEmail = queryUniqueEmail.getSingleResult();

        if (numberOfEntriesEmail != 0) {
            return jb.generateResponse("error", "registerContactPerson", "Email already exists");
        }

        person.setToken(token);

        executor.execute(() -> mailer.sendConfirmation(token, person));

        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();

        return jb.generateResponse("success", "registerContactPerson", "Please confirm your email now");
    }

    public String confirmMailContactPerson(String token) {

        TypedQuery<Token> queryToken = em.createNamedQuery("Token.getToken", Token.class);
        queryToken.setParameter("token", token);

        List<Token> tokenList = queryToken.getResultList();

        if (tokenList.size() == 0) {
            return "<h1> Error </h1>";
        }

        Token verifyToken = tokenList.get(0);

        verifyToken.getContactPerson().setVerified(true);

        em.getTransaction().begin();
        em.remove(verifyToken);
        em.getTransaction().commit();

        return "<h1> Verificated! </h1>";
    }

    public String assignCourse(Date from, Date to, String place, SkiTeacher instructor) {

        Course course = new Course(from, to, place, instructor);

        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();

        return jb.generateResponse("succes", "assignCourse", "Course has been assigend successfully");
    }

    public String addTeacherToGroup(long groupId, long skiTeacherId) {



        return "";
    }

    public String getAllGroups(long courseId) {

        TypedQuery<CourseGroup> query = em.createNamedQuery("CourseGroup.getCourseGroups", CourseGroup.class);
        query.setParameter("id", courseId);

        List<CourseGroup> groupList = query.getResultList();

        if(groupList.size() == 0) {
            return jb.generateResponse("error", "getAllGroups","There are no Groups");
        }

        JSONArray groups = new JSONArray(groupList);

        return jb.generateDataResponse("success","getAllGroups", groups);
    }

    public String getAllCourseMembers(long courseId) {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if(studentList.size() == 0) {
            return jb.generateResponse("error", "getAllCourseMembers", "There are no members");
        }
        return "";
    }

    public String registerChildren(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street) {

        Student student = new Student(firstName, lastName, birthday, postCode, place, houseNumber, street);

        em.getTransaction().commit();
        em.persist(student);
        em.getTransaction().commit();

        return jb.generateResponse("success", "registerChildren", "Child has been registerd");
    }

    public String getAllChildren() {




        return "";
    }

    public String getChildren(long studentId) {

        return "";
    }

    public String addChildrenToCourse(long studentId, long courseId) {

        TypedQuery<Student> queryStudent = em.createNamedQuery("Student.getStudentById", Student.class);
        queryStudent.setParameter("id", studentId);

        List<Student> studentList = queryStudent.getResultList();

        if(studentList.size() == 0) {
            return jb.generateResponse("error", "addChildrenToCourse", "Student does not exist");
        }

        Student student = studentList.get(0);

        TypedQuery<Course> queryCourse = em.createNamedQuery("Course.getCourseById", Course.class);
        queryCourse.setParameter("id", courseId);

        List<Course> courseList = queryCourse.getResultList();

        if(courseList.size() == 0) {
            return jb.generateResponse("error", "addChildrenToCourse", "Course error ka wos");
        }

        Course course = courseList.get(0);


        Participation participation = new Participation();


        return "";
    }

    public Course getCurrentCourse() {

        return null;
    }

    public String createGroup(String proficiency, int amount) {

        return "";
    }

    public String getGroupMembers(long groupId) {

        return "";
    }

    public String getCourseParticipants(String proficiency) {

        return "";
    }

    public String getSkiTeachers() {

        return "";
    }

    public void init() {

    }


}
