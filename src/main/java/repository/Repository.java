package repository;

import com.sun.jndi.toolkit.ctx.AtomicDirContext;
import dto.*;
import entity.*;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.JsonBuilder;
import utils.JwtHelper;
import utils.Mail;
import utils.ResponseBuilder;

import javax.persistence.*;
import javax.ws.rs.core.Response;
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
    private ResponseBuilder rb = new ResponseBuilder();
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

    /**
     * @return
     */

    private Response jwtError() {
        return rb.genErrorRes(jb.genRes("error", "jwt", "Server error"));
    }

    /**
     * @return
     */

    private Person getPerson() {

        String email = jwt.checkSubject(this.token);

        TypedQuery<Person> query = em.createNamedQuery("Person.getUser", Person.class);
        query.setParameter("email", email);

        List<Person> result = query.getResultList();

        // check if us er exists, but user should exist bc he has a token lol
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    /**
     * @return
     */

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

    /**
     * @param login
     * @return
     */

    public Response login(LoginDTO login) {

        switch (login.getType()) {
            case CONTACTPERSON:
                return loginContactPerson(login.getCredentials(), login.getPassword());

            case SKITEACHER:
                return loginTeacher(login.getCredentials(), login.getPassword());

            default:
                return rb.genErrorRes(jb.genRes("error", "login", "Invalid logintype!"));
        }
    }

    /**
     * @param username
     * @param password
     * @return
     */

    private Response loginTeacher(String username, String password) {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getUser", SkiTeacher.class);
        query.setParameter("username", username);

        if (query.getResultList().size() == 0) {
            return rb.genRes(jb.genRes("hint", "loginTeacher", "User does not exist"));
        }

        SkiTeacher user = query.getResultList().get(0);

        if (user.getPassword() == null) {
            return rb.genRes(jb.genRes("hint", "loginTeacher", "Password wurde noch nicht gesetzt"));
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(user.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(user.getPassword())) {
                return rb.genRes(jb.genRes("hint", "loginTeacher", "Wrong Password"));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(user.getEmail(), user.getRoles().toArray());

        JSONObject data = new JSONObject();
        data.put("id", user.getId())
                .put("credentials", user.getUsername())
                .put("firstName", user.getFirstName())
                .put("lastName", user.getLastName())
                .put("email", user.getEmail())
                .put("token", jwtToken);

        return rb.genRes(jb.genDataRes("loginTeacher", new JSONArray().put(data)));
    }

    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param roles
     * @return
     */

    public Response addSkiTeacher(String firstName, String lastName, String email, List<Role> roles) {

        SkiTeacher person = new SkiTeacher();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setRoles(roles);

        person.setUsername(firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase());

        Token token = new Token(person);

        TypedQuery<Long> queryUniqueUsername = em.createNamedQuery("SkiTeacher.uniqueUsername", Long.class);
        queryUniqueUsername.setParameter("username", person.getUsername());

        if (queryUniqueUsername.getSingleResult() != 0) {
            return rb.genRes(jb.genRes("hint", "addSkiTeacher", "Username already exists"));
        }

        // TODO: 16.01.20 When Username already assigned generate an other one!

        TypedQuery<Long> queryUniqueMail = em.createNamedQuery("Person.uniqueEmail", Long.class);
        queryUniqueMail.setParameter("email", person.getEmail());

        if (queryUniqueMail.getSingleResult() != 0) {
            return rb.genRes(jb.genRes("hint", "addSkiTeacher", "Email already exists"));
        }

        executor.execute(() -> mailer.sendSetPasswordMail(token, person));

        em.getTransaction().begin();
        em.persist(person);
        em.persist(token);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "addSkiTeacher", "SkiTeacher added"));
    }

    /**
     * @param token
     * @param password
     * @return
     */

    public String setPassword4SkiTeacher(String token, String password) {

        TypedQuery<Token> queryToken = em.createQuery("SELECT t FROM Token t WHERE t.token = :token", Token.class);
        queryToken.setParameter("token", token);

        List<Token> tokenList = queryToken.getResultList();

        if (tokenList.size() == 0) {
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

    /**
     * @param email
     * @param password
     * @return
     */

    private Response loginContactPerson(String email, String password) {

        TypedQuery<ContactPerson> query = em.createNamedQuery("ContactPerson.getUser", ContactPerson.class);
        query.setParameter("email", email);

        List<ContactPerson> result = query.getResultList();

        if (result.size() == 0) {
            return rb.genRes(jb.genRes("hint", "loginContactPerson", "Person does not exist"));
        }

        ContactPerson person = result.get(0);

        if (!person.isVerified()) {
            return rb.genRes(jb.genRes("hint", "loginContactPerson", "Please confirm your email first"));
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Hex.decode(person.getSalt()));

            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));

            if (!new String(Hex.encode(hash)).equals(person.getPassword())) {
                return rb.genRes(jb.genRes("hint", "loginContactPerson", "Wrong Password"));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String jwtToken = jwt.create(person.getEmail(), new Role[]{person.getRole()});

        JSONObject data = new JSONObject();
        data.put("id", person.getId())
                .put("credentials", "null")
                .put("firstName", person.getFirstName())
                .put("lastName", person.getLastName())
                .put("email", person.getEmail())
                .put("token", jwtToken);

        return rb.genRes(jb.genDataRes("loginContactPerson", new JSONArray().put(data)));
    }

    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param phoneNumber
     * @return
     */

    public Response registerContactPerson(String firstName, String lastName, String email, String password, String phoneNumber) {

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
            return rb.genRes(jb.genRes("hint", "registerContactPerson", "Email already exists"));
        }

        person.setToken(token);

        executor.execute(() -> mailer.sendConfirmation(token, person));

        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "registerContactPerson", "Please confirm your email now"));
    }

    /**
     * @param token
     * @return
     */

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

    /**
     * @param from
     * @param to
     * @param place
     * @param teacherId
     * @return
     */

    // TODO: 13.02.20 ÜberPrüfung dass man nicht 2 Course zum selben Datum erstellen kann!
    public Response assignCourse(Date from, Date to, String place, long teacherId) {

        SkiTeacher skiTeacher = getSkiTeacherById(teacherId);

        if (skiTeacher == null) {
            return rb.genRes(jb.genRes("hint", "assignCourse", "SkiTeacher does not exist"));
        }

        if (!skiTeacher.getRoles().contains(Role.INSTRUCTOR)) {
            return rb.genRes(jb.genRes("hint", "assignCourse", "This SkiTeacher has no Role Instuctor"));
        }

        Course course = new Course(from, to, place, skiTeacher);
        course.setFinished(false);

        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "assignCourse", "Course has been assigend successfully"));
    }

    /**
     * @param courseId
     * @param c
     * @return
     */

    public Response updateCourse(long courseId, CourseDTO c) {

        Course course = getCourseById(courseId);

        if (course == null) {
            return rb.genRes(jb.genRes("hint", "updateCourse", "This Course does not exist!"));
        }

        course.setFrom(c.getFrom());
        course.setTo(c.getTo());
        course.setPlace(c.getPlace());

        em.merge(course);


        return rb.genRes(jb.genRes("ok", "updateCourse", "Successfully updated Course!"));
    }

    /**
     * @param groupId
     * @param skiTeacherId
     * @return
     */

    public Response addTeacherToGroup(long groupId, long skiTeacherId) {

        Group group = getGroupById(groupId);

        if (group == null) {
            return rb.genRes(jb.genRes("hint", "addTeacherToGroup", "This Group does not exist"));
        }

        SkiTeacher skiTeacher = getSkiTeacherById(skiTeacherId);

        if (skiTeacher == null) {
            return rb.genRes(jb.genRes("hint", "addTeacherToGroup", "This SkiTeacher does not exist"));
        }

        group.setSkiTeacher(skiTeacher);

        em.getTransaction().begin();
        em.merge(group);
        em.getTransaction().commit();


        return rb.genRes(jb.genRes("ok", "addTeacherToGroup", "SkiTeacher successfully added to Group"));
    }

    /**
     * @param courseId
     * @return
     */

    public Response getAllGroups(long courseId) {

        TypedQuery<Group> query = em.createNamedQuery("Group.getGroupsByCourseID", Group.class);
        query.setParameter("id", courseId);

        List<Group> groupList = query.getResultList();

        if (groupList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getAllGroups", "There are no Groups"));
        }

        return rb.genRes(jb.genDataRes("getAllGroups", new JSONArray(groupList)));
    }

    /**
     * @param courseId
     * @return
     */

    public Response getAllCourseMembers(long courseId) {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if (studentList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getAllCourseMembers", "There are no members"));
        }

        TypedQuery<CourseParticipation> participationQuery = em.createNamedQuery("CourseParticipation.getFromCouresId", CourseParticipation.class);
        participationQuery.setParameter("courseId", courseId);

        List<CourseParticipation> courseParticipationList = participationQuery.getResultList();

        if (courseParticipationList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getAllCourseMembers", "No Teilnahmen lol"));
        }


        List<JSONObject> data = new ArrayList<>();

        courseParticipationList.forEach(courseParticipation ->
                data.add(new JSONObject()
                        .put("id", courseParticipation.getId())
                        .put("drivingCanFromRegistration", courseParticipation.getDrivingCanFromRegistration())
                        .put("proficiency", courseParticipation.getProficiency())
                        .put("student", courseParticipation.getStudent()))
        );

        return rb.genRes(jb.genDataRes("getAllCourseMembers", new JSONArray(data)));
    }

    /**
     * @param firstName
     * @param lastName
     * @param birthday
     * @param postCode
     * @param place
     * @param houseNumber
     * @param street
     * @param gender
     * @return
     */

    // TODO: 20.01.20 Student Überprüfung ob schon vorhanden! 
    public Response registerChildren(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street, String gender) {

        Student student = new Student(firstName, lastName, birthday, postCode, place, houseNumber, street, gender);

        Person person = getPerson();

        if (person == null) {
            return jwtError();

        }
        if (person.getStudents().contains(student)) {
            return rb.genRes(jb.genRes("hint", "registerChildren", "Child was already registered"));
        }

        person.getStudents().add(student);

        em.getTransaction().begin();
        em.persist(student);
        em.merge(person);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "registerChildren", "Child has been registerd"));
    }

    /**
     * @return
     */

    public Response getAllChildren() {

        TypedQuery<Student> query = em.createNamedQuery("Student.getAllMembers", Student.class);

        List<Student> studentList = query.getResultList();

        if (studentList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getAllChildren", "There are no members"));
        }

        return rb.genRes(jb.genDataRes("getAllChildren", new JSONArray(studentList)));
    }

    /**
     * @param studentId
     * @return
     */

    public Response getChildren(long studentId) {

        TypedQuery<Student> query = em.createNamedQuery("Student.getStudentById", Student.class);
        query.setParameter("id", studentId);

        List<Student> studentList = query.getResultList();

        if (studentList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getChildren", "Does not exist"));
        }

        return rb.genRes(jb.genDataRes("getChildren", new JSONArray(studentList.get(0))));
    }

    /**
     * @param studentId
     * @param courseId
     * @return
     */

    public Response addChildrenToCourse(long studentId, long courseId) {

        TypedQuery<Student> queryStudent = em.createNamedQuery("Student.getStudentById", Student.class);
        queryStudent.setParameter("id", studentId);

        List<Student> studentList = queryStudent.getResultList();

        if (studentList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "addChildrenToCourse", "Student does not exist"));
        }

        Student student = studentList.get(0);

        TypedQuery<Course> queryCourse = em.createNamedQuery("Course.getCourseById", Course.class);
        queryCourse.setParameter("id", courseId);

        List<Course> courseList = queryCourse.getResultList();

        if (courseList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "addChildrenToCourse", "Course error ka wos"));
        }

        Course course = courseList.get(0);

        CourseParticipation courseParticipation = new CourseParticipation();

        courseParticipation.setCourse(course);
        courseParticipation.setPerson(getPerson());
        courseParticipation.setStudent(student);

        em.getTransaction().begin();
        em.persist(courseParticipation);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "addChildrenToCourse", "Successfully added Children to Course"));
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

    public Response createGroup(String proficiency, int participants, int amount, long courseId) {

        Course course = getCourseById(courseId);

        if (course == null) {
            return rb.genRes(jb.genRes("hint", "createGroup", "Error"));
        }

        for (int i = 0; i < amount; i++) {
            Group group = new Group(Proficiency.valueOf(proficiency), participants);
            group.setCourse(course);
            em.getTransaction().begin();
            em.persist(group);
            em.getTransaction().commit();
        }

        String message = amount == 1 ? "Successfully created Group" : "Successfully created Groups";

        return rb.genRes(jb.genRes("ok", "createGroup", message));
    }

    /**
     * Gibt alle Teilnahmen einer Gruppe zurück
     *
     * @param groupId
     * @return
     */

    public Response getGroupParticipations(long groupId) {

        TypedQuery<GroupParticipation> query = em.createNamedQuery("GroupParticipation.getPartByGroupId", GroupParticipation.class);
        query.setParameter("id", groupId);

        List<GroupParticipation> groupParticipations = query.getResultList();

        if (groupParticipations.isEmpty()) {
            return rb.genRes(jb.genRes("hint", "getGroupMembers", "No Members in this group"));
        }

        List<JSONObject> getGroupParticipations = new ArrayList<>();

        groupParticipations.forEach(groupParticipation ->

                getGroupParticipations.add(new JSONObject()
                        .put("id", groupParticipation.getId())
                        .put("rank", groupParticipation.getRank())
                        .put("time", groupParticipation.getTime())
                        .put("drivingCan", groupParticipation.getDrivingCan())
                        .put("student", new JSONObject(groupParticipation.getStudent()))));

        return rb.genRes(jb.genDataRes("getGroupMembers", new JSONArray(getGroupParticipations)));
    }

    public Response getCourseParticipants(String proficiency, long courseId) {

        Course course = getCourseById(courseId);

        if (course == null) {
            return rb.genRes(jb.genRes("hint", "getCourseParticipants", "There is no Course"));
        }

        TypedQuery<CourseParticipation> query = em.createNamedQuery("CP.getFromCourseAndProficiency", CourseParticipation.class);
        query.setParameter("courseId", course.getId());
        query.setParameter("proficiency", Proficiency.valueOf(proficiency));

        List<CourseParticipation> list = query.getResultList();

        if (list.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getCourseParticipants", "There are no CourseParticipants with this proficiency"));
        }

        List<JSONObject> data = new ArrayList<>();

        list.forEach(courseParticipation ->
                data.add(new JSONObject()
                        .put("id", courseParticipation.getId())
                        .put("drivingCanFromRegistration", courseParticipation.getDrivingCanFromRegistration())
                        .put("proficiency", courseParticipation.getProficiency())
                        .put("student", courseParticipation.getStudent()))
        );

        return rb.genRes(jb.genDataRes("getCourseParticipants", new JSONArray(data)));
    }

    public Response addChildrenToGroup(long studentId, long groupId) {

        TypedQuery<Student> studentQuery = em.createNamedQuery("Student.getStudentById", Student.class);
        studentQuery.setParameter("id", studentId);

        if (studentQuery.getResultList().size() == 0) {
            return rb.genRes(jb.genRes("hint", "addChildrenToGroup", "This Student does not exist"));
        }

        TypedQuery<Group> groupQuery = em.createNamedQuery("Group.getGroupById", Group.class);
        groupQuery.setParameter("id", groupId);

        if (groupQuery.getResultList().size() == 0) {
            return rb.genRes(jb.genRes("hint", "addChildrenToGroup", "This Group does not exist"));
        }

        GroupParticipation gp = new GroupParticipation(groupQuery.getResultList().get(0), studentQuery.getResultList().get(0));

        em.getTransaction().begin();
        em.persist(gp);
        em.getTransaction().commit();

        return rb.genRes(jb.genRes("ok", "addChildrenToGroup", "Student added to Course"));
    }

    public Response getSkiTeachers() {

        TypedQuery<SkiTeacher> query = em.createNamedQuery("SkiTeacher.getAllTeachers", SkiTeacher.class);

        List<SkiTeacher> skiTeacherList = query.getResultList();

        if (skiTeacherList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getSkiTeachers", "There are no SkiTeachers"));
        }

        return rb.genRes(jb.genDataRes("getSkiTeachers", new JSONArray(skiTeacherList)));
    }

    public Response updateChildren(StudentDTO s, long id) {

        Student student = em.find(Student.class, id);
        student.setBirthday(s.getBirthday());
        student.setFirstName(s.getFirstName());
        student.setLastName(s.getLastName());
        student.setHouseNumber(s.getHouseNumber());
        student.setPlace(s.getPlace());
        student.setPostCode(s.getPostCode());
        student.setStreet(s.getStreet());
        student.setGender(s.getGender());

        em.merge(student);

        return rb.genRes("Updated Children");
    }

    public Response deleteChildren(long id) {

        Student student = em.find(Student.class, id);
        em.remove(student);

        return rb.genRes("Removed");
    }

    public Response deleteSkiTeacher(long id) {

        SkiTeacher teacher = em.find(SkiTeacher.class, id);
        em.remove(teacher);

        //Email senden, dass dieser SkiTeacher gelöscht wurde

        return rb.genRes("Removed SkiTeacher");
    }

    public Response updateSkiTeacher(long id, SkiTeacherDTO t) {

        SkiTeacher skiTeacher = em.find(SkiTeacher.class, id);
        skiTeacher.setBirthday(t.getBirthday());
        skiTeacher.setRoles(t.getRoles());
        skiTeacher.setUsername(t.getUsername());
        skiTeacher.setEmail(t.getEmail()); // wird dann noch per email bestätigt
        skiTeacher.setLastName(t.getLastName());
        skiTeacher.setFirstName(t.getFirstName());

        em.merge(skiTeacher);

        return rb.genRes("Updated SkiTeacher");
    }

    public Response deleteContactPerson(long id) {

        ContactPerson contactPerson = em.find(ContactPerson.class, id);
        em.remove(contactPerson);

        return rb.genRes("Removed ContactPerson");
    }

    public Response updateContactPerson(long id, ContactPersonDTO c) {

        ContactPerson contactPerson = em.find(ContactPerson.class, id);
        contactPerson.setPhone(c.getPhoneNumber());
        contactPerson.setEmail(c.getEmail()); // wird dann noch per email bestätigt
        contactPerson.setFirstName(c.getFirstName());
        contactPerson.setLastName(c.getLastName());

        em.merge(contactPerson);

        return rb.genRes("Updated SkiTeacher");
    }

    public Response setRaceTime(long groupiÍd, long studentId, String time) {

        TypedQuery<GroupParticipation> query = em.createNamedQuery("GroupParticipation.getPartByGroupIdAndStudentId", GroupParticipation.class);
        query.setParameter("groupId", groupiÍd);
        query.setParameter("studentId", studentId);

        List<GroupParticipation> groupParticipations = query.getResultList();

        if (groupParticipations.size() == 0) {
            return rb.genRes(jb.genRes("hint", "setRaceTime", "This Member does not exist in this group"));
        }

        groupParticipations.get(0).setTime(Double.parseDouble(time));


        return rb.genRes(jb.genRes("ok", "setRaceTime", "RaceTime successfully set for this Member"));
    }

    public Response setGroupRaceStart(long groupId, String time) {

        Group group = getGroupById(groupId);

        if (group == null) {
            return rb.genRes(jb.genRes("hint", "setGroupRaceStart", "This Group does not exist!"));
        }

        group.setStartTime(time);

        em.merge(group);

        return rb.genRes((jb.genRes("ok", "setGroupRaceStart", "RaceTime successfully set for this Group!")));
    }

    public Response getGroup(long courseId) {

        SkiTeacher skiTeacher = getSkiTeacher();

        if (skiTeacher == null) {
            return jwtError();
        }

        Course course = getCourseById(courseId);

        if (course == null) {
            return rb.genRes(jb.genRes("hint", "getGroup", "No Course found"));
        }

        TypedQuery<Group> query = em.createNamedQuery("Group.getGroupsByTeacherIdANDCourseId", Group.class);
        query.setParameter("sId", skiTeacher.getId());
        query.setParameter("cId", course.getId());

        List<Group> groupList = query.getResultList();

        if (groupList.size() == 0) {
            return rb.genRes(jb.genRes("hint", "getGroup", "Hab noch keine Gruppe!"));
        }

        Group group = groupList.get(0);

        JSONObject data = new JSONObject();

        data.put("id", group.getId())
                .put("participants", group.getParticipants())
                .put("startTime", group.getStartTime())
                .put("proficiency", group.getProficiency());

        return rb.genRes(jb.genDataRes("getGroup", new JSONArray().put(data)));
    }

    public Response getContactPerson(long studentId) {


        return null;
    }

    public Response getCourse(long id) {

        Course course = getCourseById(id);

        if (course == null) {
            return rb.genRes(jb.genRes("hint", "getCourse", "No Course found"));
        }

        JSONObject data = new JSONObject();

        data.put("id", course.getId())
                .put("from", course.getFrom())
                .put("to", course.getTo())
                .put("assigned", course.getAssigned())
                .put("place", course.getPlace())
                .put("instructor", new JSONObject()
                        .put("id", course.getInstructor().getId())
                        .put("firstName", course.getInstructor().getFirstName())
                        .put("lastName", course.getInstructor().getLastName())
                        .put("email", course.getInstructor().getEmail()));


        return rb.genRes(jb.genDataRes("getCourse", new JSONArray().put(data)));
    }

    public Response getCurrentCourse() {

        List<Course> courseList = em.createQuery("SELECT c FROM Course c where c.assigned = (select max(x.assigned) from Course x)", Course.class).getResultList();

        if (courseList == null) {
            return rb.genRes(jb.genRes("hint", "getCurrentCourse", "Error of getting current Course"));
        }

        Course course = courseList.get(0);

        JSONObject data = new JSONObject();

        data.put("id", course.getId())
                .put("from", course.getFrom())
                .put("to", course.getTo())
                .put("assigned", course.getAssigned())
                .put("place", course.getPlace())
                .put("instructor", new JSONObject()
                        .put("id", course.getInstructor().getId())
                        .put("firstName", course.getInstructor().getFirstName())
                        .put("lastName", course.getInstructor().getLastName())
                        .put("email", course.getInstructor().getEmail()));


        return rb.genRes(jb.genDataRes("getCurrentCourse", new JSONArray().put(data)));


    }
}
