package repository;

import dto.LoginDTO;
import entity.*;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.JsonBuilder;
import utils.JwtHelper;
import utils.Mail;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
        return jb.genRes("error", "jwt", "Server error");
    }

    private Person getPerson() {

        String email = jwt.checkSubject(this.token);

        TypedQuery<Person> query = em.createNamedQuery("Person.getUser", Person.class);
        query.setParameter("email", email);

        List<Person> result = query.getResultList();

        // check if user exists, but user should exist bc he has a token lol
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    private SkiTeacher getSkiTeacher() {

        String email = jwt.checkSubject(this.token);

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getUserByEmail", SkiTeacher.class);
        query.setParameter("email", email);

        List<SkiTeacher> result = query.getResultList();

        // check if user exists, but user should exist bc he has a token lol
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    public String login(LoginDTO login) {

        switch (login.getType()) {
            case CONTACTPERSON:
                return loginContactPerson(login.getCredentials(), login.getPassword());

            case SKITEACHER:
                    return loginTeacher(login.getCredentials(), login.getPassword());

            default:
                return jb.genRes("error", "login", "Something went wrong");
        }
    }

    private String loginTeacher(String username, String password) {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getUser", SkiTeacher.class);
        query.setParameter("username", username);

        if (query.getResultList().size() == 0) {
            return jb.genRes("error", "loginTeacher", "User does not exist");
        }

        SkiTeacher user = query.getResultList().get(0);

        if(user.getPassword() == null) {
            return jb.genRes("error", "loginTeacher", "Password wurde noch nicht gesetzt");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(user.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(user.getPassword())) {
                return jb.genRes("error", "loginTeacher", "Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(user.getEmail(), user.getRoles().toArray());

        return jb.genDataRes( "loginTeacher", new JSONArray(jwtToken));
    }

    public String addSkiTeacher(String firstName, String lastName, String email, List<Role> roles) {

        SkiTeacher person = new SkiTeacher();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setRoles(roles);

        person.setUsername(firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase());

        Token token = new Token(person);

        TypedQuery<Long> queryUniqueMail = em.createNamedQuery("Person.uniqueEmail", Long.class);
        queryUniqueMail.setParameter("email", person.getEmail());

        if(queryUniqueMail.getSingleResult() != 0) {
            return jb.genRes("error", "addSkiTeacher", "Email already exists");
        }

        executor.execute(() -> mailer.sendSetPasswordMail(token, person));

        em.getTransaction().begin();
        em.persist(person);
        em.persist(token);
        em.getTransaction().commit();

        return jb.genRes("ok", "addSkiTeacher", "SkiTeacher added");
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

        Token verifyToken = tokenList.get(0);

        SkiTeacher user = verifyToken.getSkiTeacher();

        user.setPassword(password);

        em.getTransaction().begin();
        em.remove(verifyToken);
        em.getTransaction().commit();

        return "Password successfully set";
    }

    private String loginContactPerson(String email, String password) {

        TypedQuery<ContactPerson> query = em.createNamedQuery("ContactPerson.getUser", ContactPerson.class);
        query.setParameter("email", email);

        List<ContactPerson> result = query.getResultList();

        if (result.size() == 0) {
            return jb.genRes("error", "loginContactPerson", "Person does not exist"); // Error
        }

        ContactPerson person = result.get(0);

        if(!person.isVerified()) {
            return jb.genRes("error", "loginContactPerson", "Please confirm your email first");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(person.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(person.getPassword())) {
                return jb.genRes("error", "loginContactPerson", "Wrong Password");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(person.getEmail(), new Role[]{person.getRole()});

        return jb.genDataRes( "loginContactPerson", new JSONArray(jwtToken));
    }

    public String registerContactPerson(String firstName, String lastName, String email, String password, String phoneNumber) {

        ContactPerson person = new ContactPerson();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPassword(password);
        person.setPhone(phoneNumber);

        person.setRole(Role.CONTACTPERSON);

        Token token = new Token(person);

        TypedQuery<Long> queryUniqueEmail = em.createNamedQuery("Person.uniqueEmail", Long.class);
        queryUniqueEmail.setParameter("email", email);

        long numberOfEntriesEmail = queryUniqueEmail.getSingleResult();

        if (numberOfEntriesEmail != 0) {
            return jb.genRes("error", "registerContactPerson", "Email already exists");
        }

        person.setToken(token);

        executor.execute(() -> mailer.sendConfirmation(token, person));

        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();

        return jb.genRes("ok", "registerContactPerson", "Please confirm your email now");
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

        return jb.genRes("ok", "assignCourse", "Course has been assigend successfully");
    }

    public String addTeacherToGroup(long groupId, long skiTeacherId) {

        Group group = getGroupById(groupId);

        if(group == null) {
            return jb.genRes("error", "addTeacherToGroup", "CourseCroup error");
        }

        SkiTeacher skiTeacher = getSkiTeacherById(skiTeacherId);

        if(skiTeacher == null) {
            return jb.genRes("error", "addTeacherToGroup", "SkiTeacher error");
        }

        group.setSkiTeacher(skiTeacher);

        em.getTransaction().begin();
        em.merge(group);
        em.getTransaction().commit();


        return jb.genRes("ok", "addTeacherToGroup", "ok");
    }

    public String getAllGroups(long courseId) {

        TypedQuery<Group> query = em.createNamedQuery("Group.getGroupsByCourseID", Group.class);
        query.setParameter("id", courseId);

        List<Group> groupList = query.getResultList();

        if(groupList.size() == 0) {
            return jb.genRes("error", "getAllGroups","There are no Groups");
        }

        JSONArray groups = new JSONArray(groupList);

        return jb.genDataRes("getAllGroups", groups);
    }

    public String getAllCourseMembers(long courseId) {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if(studentList.size() == 0) {
            return jb.genRes("error", "getAllCourseMembers", "There are no members");
        }

        TypedQuery<CourseParticipation> participationQuery = em.createNamedQuery("CourseParticipation.getFromCouresId", CourseParticipation.class);
        participationQuery.setParameter("courseId", courseId);

        List<CourseParticipation> courseParticipationList = participationQuery.getResultList();

        if(courseParticipationList.size() == 0) {
            return jb.genRes("error", "getAllCourseMembers", "No Teilnahmen lol");
        }

        List<Student> list = new ArrayList<>();

        for (CourseParticipation courseParticipation : courseParticipationList) {
            list.add(courseParticipation.getStudent());
        }

        return jb.genDataRes("getAllCourseMembers", new JSONArray(list));
    }

    public String registerChildren(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street) {

        Student student = new Student(firstName, lastName, birthday, postCode, place, houseNumber, street);

        Person person = getPerson();

        if (person != null) {
            person.getStudents().add(student);
        } else {
            return jwtError();
        }

        em.getTransaction().commit();
        em.persist(student);
        em.merge(person);
        em.getTransaction().commit();

        return jb.genRes("ok", "registerChildren", "Child has been registerd");
    }

    public String getAllChildren() {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if(studentList.size() == 0) {
            return jb.genRes("error", "getAllChildren", "There are no members");
        }

        return jb.genDataRes("getAllChildren", new JSONArray(studentList));
    }

    public String getChildren(long studentId) {

        TypedQuery<Student> query = em.createNamedQuery("Student.getStudentById", Student.class);
        query.setParameter("id", studentId);

        List<Student> studentList = query.getResultList();

        if(studentList.size() == 0) {
            return jb.genRes("error", "getChildren", "Does not exist");
        }

        return jb.genDataRes("getChildren", new JSONArray(studentList.get(0)));
    }

    public String addChildrenToCourse(long studentId, long courseId) {

        TypedQuery<Student> queryStudent = em.createNamedQuery("Student.getStudentById", Student.class);
        queryStudent.setParameter("id", studentId);

        List<Student> studentList = queryStudent.getResultList();

        if(studentList.size() == 0) {
            return jb.genRes("error", "addChildrenToCourse", "Student does not exist");
        }

        Student student = studentList.get(0);

        TypedQuery<Course> queryCourse = em.createNamedQuery("Course.getCourseById", Course.class);
        queryCourse.setParameter("id", courseId);

        List<Course> courseList = queryCourse.getResultList();

        if(courseList.size() == 0) {
            return jb.genRes("error", "addChildrenToCourse", "Course error ka wos");
        }

        Course course = courseList.get(0);

        CourseParticipation courseParticipation = new CourseParticipation();

        courseParticipation.setCourse(course);
        courseParticipation.setPerson(getPerson());
        courseParticipation.setStudent(student);

        em.getTransaction().begin();
        em.persist(courseParticipation);
        em.getTransaction().commit();

        return jb.genRes("ok", "addChildrenToCourse", "Successfully added Children to Course");
    }

    private Course getCurrentCourse() {






        return null;
    }

    private Course getCourseById(long id) {
        return em.find(Course.class, id);
    }

    private Group getGroupById(long id) {
        return em.find(Group.class, id);
    }

    private SkiTeacher getSkiTeacherById(long id) {
        return em.find(SkiTeacher.class, id);
    }

    public String createGroup(String proficiency, int amount) {

        SkiTeacher skiTeacher = getSkiTeacher();
        Course course = getCurrentCourse();

        Group group = new Group();

        return "";
    }

    public String getGroupMembers(long groupId) {

        TypedQuery<GroupParticipation> query = em.createNamedQuery("GroupParticipation.getPartByGroupId", GroupParticipation.class);
        query.setParameter("id", groupId);

        List<GroupParticipation> groupParticipations = query.getResultList();

        if(groupParticipations.isEmpty()) {
            return jb.genRes("error", "getGroupMembers", "No Members in this group");
        }

        List<Student> studentList = new ArrayList<>();

        groupParticipations.forEach(groupParticipation -> studentList.add(groupParticipation.getStudent()));

        return jb.genDataRes("getGroupMembers", new JSONArray(studentList));
    }

    public String getCourseParticipants(String proficiency) {

        return "";
    }

    public String getSkiTeachers() {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getAllTeachers", SkiTeacher.class);

        List<SkiTeacher> skiTeacherList = query.getResultList();

        if(skiTeacherList.size() == 0) {
            return jb.genRes("error", "getSkiTeachers", "There are no SkiTeachers");
        }

        return jb.genDataRes("getSkiTeachers", new JSONArray(skiTeacherList));
    }

    public void init() {

    }


}
