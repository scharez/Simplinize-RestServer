package service;

import annotation.Secure;
import dto.CourseDTO;
import dto.GroupDTO;
import dto.StudentDTO;
import entity.Role;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("app")
public class AppService {

    @Secure(Role.ADMIN)
    @Path("init")
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String inti() {

        Repository.getInstance().init();

        return "DB wurde initialisiert";
    }

    /**
     * Register a Child
     *
     * @param s the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.CONTACTPERSON)
    @Path("registerChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String registerChildren(StudentDTO s) {

        return Repository.getInstance().registerChildren(s.getFirstName(), s.getLastName(), s.getBirthday(), s.getPostCode(),
                s.getPlace(),s.getHouseNumber(), s.getStreet());
    }

    /**
     * Add Children to Course
     *
     * @param studentId
     * @param courseId
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.CONTACTPERSON)
    @Path("addChildrenToCourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String addChildrenToCourse(@QueryParam("studentId") long studentId, @QueryParam("courseId") long courseId) {

        return Repository.getInstance().addChildrenToCourse(studentId, courseId);
    }

    /**
     * Get all Children of Contactperson
     *
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getAllChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getAllChildren() {

        return Repository.getInstance().getAllChildren();
    }

    /**
     * Get Children of Contactperson
     *
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getChildren(@QueryParam("studentId") long studentId) {

        return Repository.getInstance().getChildren(studentId);
    }

    /*
------------------------------------------------------------------------------------------------------------------------
    */

    /**
     * Assign a course
     *
     * @param  course the Transfer Object the Ski-Teacher Entity
     * @return a json which can contain an error or a successfully login message
     */

    @Secure(Role.ADMIN)
    @Path("assignCourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String assignCourse(CourseDTO course) {

        return Repository.getInstance().assignCourse(course.getFrom(), course.getTo(), course.getPlace(), course.getInstructor());
    }

    // Wird eventuell Automatisch erstellt, anhand der Teilnehmeranzahl, Anzahl der Gruppen mit der Proficiency GRÜN
    // Wird automatisch zum akutellen Course hinzugefügt
    @Secure(Role.ADMIN)
    @Path("createGroup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createGroup(GroupDTO g) {

        return Repository.getInstance().createGroup(g.getProficiency(), g.getAmount());
    }

    @Secure(Role.ADMIN)
    @Path("addTeacherToGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public String addTeacherToGroup(@QueryParam("skiTeacherId") long skiTeacherId, @QueryParam("groupId") long groupId) {

        return Repository.getInstance().addTeacherToGroup(groupId, skiTeacherId);
    }

    @Secure(Role.ADMIN)
    @Path("getAllGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getAllGroups(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllGroups(courseId);
    }

    @Secure(Role.ADMIN)
    @Path("getAllCourseMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getAllCourseMembers(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllCourseMembers(courseId);
    }

    @Secure(Role.ADMIN)
    @Path("getSkiTeachers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getSkiTeachers() {

        return Repository.getInstance().getSkiTeachers();
    }

    /*
------------------------------------------------------------------------------------------------------------------------
    */

    @Secure(Role.SKITEAM)
    @Path("getGroupMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getGroupMembers(@QueryParam("groupId") long groupId) {

        return Repository.getInstance().getGroupMembers(groupId);
    }

    //Programmtechnisch dann so programmieren, dass der Skiteacher immer vom akutellen Course selektiert von der DB!!

    @Secure(Role.SKITEAM)
    @Path("getGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getGroup(@QueryParam("groupId") long groupId) {

        return "";
    }

    @Secure(Role.SKITEAM)
    @Path("getCourseParticipants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public String getCourseParticipants(@QueryParam("proficiency") String proficiency) {

        return Repository.getInstance().getCourseParticipants(proficiency);
    }

}
