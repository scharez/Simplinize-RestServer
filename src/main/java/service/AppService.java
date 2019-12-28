package service;

import annotation.Secure;
import dto.CourseDTO;
import dto.GroupDTO;
import dto.StudentDTO;
import entity.Role;
import repository.Repository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("app")
public class AppService {


    @Path("test")
    @GET
    public Response test() {

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("SEAS OIDA")
                .build();
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
    public Response registerChildren(StudentDTO s) {

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
    public Response addChildrenToCourse(@QueryParam("studentId") long studentId, @QueryParam("courseId") long courseId) {

        return Repository.getInstance().addChildrenToCourse(studentId, courseId);
    }

    /**
     * Get all Children
     *
     * @return
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getAllChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllChildren() {

        return Repository.getInstance().getAllChildren();
    }

    /**
     * Get Children of Contactperson
     *
     * @return
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getChildren(@QueryParam("studentId") long studentId) {

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
    public Response assignCourse(CourseDTO course) {

        return Repository.getInstance().assignCourse(course.getFrom(), course.getTo(), course.getPlace(), course.getInstructor());
    }

    // Wird eventuell Automatisch erstellt, anhand der Teilnehmeranzahl, Anzahl der Gruppen mit der Proficiency GRÜN
    // Wird automatisch zum akutellen Course hinzugefügt
    @Secure(Role.ADMIN)
    @Path("createGroup")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(GroupDTO g) {

        return Repository.getInstance().createGroup(g.getProficiency(), g.getParticipants(), g.getAmount());
    }

    @Secure(Role.ADMIN)
    @Path("addTeacherToGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response addTeacherToGroup(@QueryParam("skiTeacherId") long skiTeacherId, @QueryParam("groupId") long groupId) {

        return Repository.getInstance().addTeacherToGroup(groupId, skiTeacherId);
    }

    @Secure(Role.ADMIN)
    @Path("getAllGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllGroups(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllGroups(courseId);
    }

    @Secure(Role.ADMIN)
    @Path("getAllCourseMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllCourseMembers(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllCourseMembers(courseId);
    }

    @Secure(Role.ADMIN)
    @Path("getSkiTeachers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getSkiTeachers() {

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
    public Response getGroupMembers(@QueryParam("groupId") long groupId) {

        return Repository.getInstance().getGroupMembers(groupId);
    }

    //Programmtechnisch dann so programmieren, dass der Skiteacher immer vom akutellen Course selektiert von der DB!!

    @Secure(Role.SKITEAM)
    @Path("getGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getGroup(@QueryParam("groupId") long groupId) {

        return null;
    }

    @Secure(Role.SKITEAM)
    @Path("getCourseParticipants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getCourseParticipants(@QueryParam("proficiency") String proficiency) {

        return Repository.getInstance().getCourseParticipants(proficiency);
    }

    @Secure(Role.SKITEAM)
    @Path("addChildrenToGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response addChildrenToGroup(@QueryParam("studentId") long studentId, @QueryParam("groupId") long groupId) {

        return Repository.getInstance().addChildrenToGroup(studentId, groupId);
    }

}
