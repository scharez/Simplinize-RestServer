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
     * Register Student
     *
     * @param s dto of Ski-Teacher
     * @return Response with a json string
     */

    @Secure(Role.CONTACTPERSON)
    @Path("registerChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response registerChildren(StudentDTO s) {

        return Repository.getInstance().registerChildren(s.getFirstName(), s.getLastName(), s.getBirthday(), s.getPostCode(),
                s.getPlace(),s.getHouseNumber(), s.getStreet(), s.getGender());
    }

    /**
     * Update Student
     *
     * @param s dto of Student
     * @param id of Student
     * @return Response with a json string
     */

    @Secure(Role.CONTACTPERSON)
    @Path("updateChildren/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateChildren(StudentDTO s, @PathParam("id") long id) {

        return Repository.getInstance().updateChildren(s, id);
    }

    /**
     * Delete a Child
     *
     * @param id dto of Student
     * @return Response with a json string
     */

    @Secure(Role.CONTACTPERSON)
    @Path("deleteChildren/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @DELETE
    public Response deleteChildren(@PathParam("id") long id) {

        return Repository.getInstance().deleteChildren(id);
    }

    /**
     * Add Children to Course
     *
     * @param studentId of Student
     * @param courseId of Course
     * @return Response with a json string
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
     * Get all Students
     *
     * @return Response with a json string
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
     * Get Child of Contactperson
     *
     * @return Response with a json string
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getChild")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getChild(@QueryParam("studentId") long studentId) {

        return Repository.getInstance().getChildren(studentId); //getChild
    }

    /**
     * Get Children of Contactperson
     *
     * @return Response with a json string
     */

    @Secure(Role.CONTACTPERSON)
    @Path("getChildren")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getChildren() {

        //return Repository.getInstance().getChildren(); //Mit JWT Token get Children und dann alle Kinder von Person selektieren
        return null;
    }

    /*
------------------------------------------------------------------------------------------------------------------------
    */

    /**
     * Assign course
     *
     * @param course dto of Course
     * @return Response with a json string
     */

    @Secure(Role.ADMIN)
    @Path("assignCourse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response assignCourse(CourseDTO course) {

        return Repository.getInstance().assignCourse(course.getFrom(), course.getTo(), course.getPlace(), course.getInstructor().getId());
    }

    /**
     * Update course
     *
     * @param course dto of Course
     * @return Response with a json string
     */

    @Secure(Role.ADMIN)
    @Path("updateCourse/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PUT
    public Response updateCourse(@PathParam("id") long courseId, CourseDTO course) {

        return Repository.getInstance().updateCourse(courseId, course);
    }

    /**
     * Create Group
     *
     * @param g dto of Group
     * @return a json which can contain an error or a successfully login message
     */

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

    /**
     * Add teacher to group
     *
     * @param skiTeacherId of SkiTeacher
     * @param groupId of Group
     * @return Response with a json string
     */

    @Secure(Role.ADMIN)
    @Path("addTeacherToGroup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    public Response addTeacherToGroup(@QueryParam("skiTeacherId") long skiTeacherId, @QueryParam("groupId") long groupId) {

        return Repository.getInstance().addTeacherToGroup(groupId, skiTeacherId);
    }

    /**
     * Get all groups
     *
     * @param  courseId of Course
     * @return Response with a json string
     */

    @Secure(Role.ADMIN)
    @Path("getAllGroups")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllGroups(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllGroups(courseId);
    }

    /**
     * Get all course members
     *
     * @param courseId of Course
     * @return Response with a json string
     */

    @Secure(Role.ADMIN)
    @Path("getAllCourseMembers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getAllCourseMembers(@QueryParam("courseId") long courseId) {

        return Repository.getInstance().getAllCourseMembers(courseId);
    }

    /**
     * Get SkiTeachers
     *
     * @return Response with a json string
     */

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

    /**
     * Get group members
     *
     * @param  groupId of Group
     * @return Response with a json string
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

    /**
     * Get Group
     *
     * @param groupId of Group
     * @return Response with a json string
     */

    @Secure(Role.SKITEAM)
    @Path("getGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getGroup(@QueryParam("groupId") long groupId) {

        return null;
    }

    /**
     * Get Course Paticipants
     *
     * @param  proficiency
     * @return Response with a json string
     */

    @Secure(Role.SKITEAM)
    @Path("getCourseParticipants")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Response getCourseParticipants(@QueryParam("proficiency") String proficiency) {

        return Repository.getInstance().getCourseParticipants(proficiency);
    }

    /**
     * Add Children to Course
     *
     * @param  studentId of Student
     * @param groupId of Group
     * @return Response with a json string
     */

    @Secure(Role.SKITEAM)
    @Path("addChildrenToGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response addChildrenToGroup(@QueryParam("studentId") long studentId, @QueryParam("groupId") long groupId) {

        return Repository.getInstance().addChildrenToGroup(studentId, groupId);
    }

    /**
     * Set race time
     *
     * @param  groupId of Group
     * @param studentId of Student
     * @param time race time of Student
     * @return Response with a json string
     */

    @Secure(Role.RACE)
    @Path("setRaceTime/{groupId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response setRaceTime(@PathParam("groupId") long groupId, @QueryParam("studentId") long studentId, @QueryParam("time") String time) {

        return Repository.getInstance().setRaceTime(groupId, studentId, time);
    }

    /**
     * Set group race start
     *
     * @param  groupId of Group
     * @param time race start time of Group
     * @return Response with a json string
     */

    @Secure(Role.RACE)
    @Path("setGroupRaceStart")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    public Response setGroupRaceStart(@QueryParam("groupId") long groupId, @QueryParam("time") String time) {

        return Repository.getInstance().setGroupRaceStart(groupId, time);
    }


}
