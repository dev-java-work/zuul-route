package com.ssstsar.zuul.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.ssstsar.zuul.courses.CourseDetails;
import com.ssstsar.zuul.courses.DeleteCourseDetailsRequest;
import com.ssstsar.zuul.courses.DeleteCourseDetailsResponse;
import com.ssstsar.zuul.courses.GetAllCourseDetailsRequest;
import com.ssstsar.zuul.courses.GetAllCourseDetailsResponse;
import com.ssstsar.zuul.courses.GetCourseDetailsRequest;
import com.ssstsar.zuul.courses.GetCourseDetailsResponse;
import com.ssstsar.zuul.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.ssstsar.zuul.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.ssstsar.zuul.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.ssstsar.zuul.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

@Endpoint
public class CourseDetailsEndpoint {

	@Autowired
	CourseDetailsService service;

	// method
	// input - GetCourseDetailsRequest
	// output - GetCourseDetailsResponse

	// http://ssstsar.com/courses
	// GetCourseDetailsRequest
	@PayloadRoot(namespace = "http://ssstsar.com/courses", localPart = "GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request) {

		Course course = service.findById(request.getId());

		if (course == null)
			throw new CourseNotFoundException("Invalid Course Id " + request.getId());

		return mapCourseDetails(course);
	}

	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		response.setCourseDetails(mapCourse(course));
		return response;
	}

	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		for (Course course : courses) {
			CourseDetails mapCourse = mapCourse(course);
			response.getCourseDetails().add(mapCourse);
		}
		return response;
	}

	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();

		courseDetails.setId(course.getId());

		courseDetails.setName(course.getName());

		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}

	@PayloadRoot(namespace = "http://ssstsar.com/courses", localPart = "GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(
			@RequestPayload GetAllCourseDetailsRequest request) {

		List<Course> courses = service.findAll();

		return mapAllCourseDetails(courses);
	}

	@PayloadRoot(namespace = "http://ssstsar.com/courses", localPart = "DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request) {

		Status status = service.deleteById(request.getId());

		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		response.setStatus(mapStatus(status));

		return response;
	}

	private com.ssstsar.zuul.courses.Status mapStatus(Status status) {
		if (status == Status.FAILURE)
			return com.ssstsar.zuul.courses.Status.FAILURE;
		return com.ssstsar.zuul.courses.Status.SUCCESS;
	}
}
