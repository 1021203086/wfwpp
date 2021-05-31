package com.cecel.wfwpp;

import java.util.StringTokenizer;
import java.util.Vector;

public class CourseInfo {

    public static Vector<Course> transferToCourse(String str){
        Course tempCourse;
        Vector<Course> courseVector = new Vector<>();
        String courseData = str.replace("\\n日历 \\n\\t\\n大纲 \\n\\t","");
        String[] courses = courseData.split("\\\\n");
        for(String course: courses) {
            if ((tempCourse = transferSingleCourse(course)) != null) {
                courseVector.add(tempCourse);
            }
        }
        return courseVector;
    }

    public static Course transferSingleCourse(String course){
        Course course1;
        String[] courseDetails = course.split("\\\\t",-1);
        if (courseDetails.length!=2) {
            int courseId = Integer.parseInt(courseDetails[0]);
            String name = courseDetails[1];
            int courseNum = Integer.parseInt(courseDetails[2]);
            int credit = Integer.parseInt(courseDetails[3]);
            String courseProperty = courseDetails[4];
            String courseTeacher = courseDetails[7];
            String courseTime,courseAddress;
            if ((courseTime = courseDetails[12]).equals(""))
                courseTime = null;
            if ((courseAddress = courseDetails[13]).equals(""))
                courseAddress = null;
            course1 = new Course(name, courseId, courseNum, credit,
                    courseProperty, courseTeacher, courseTime, courseAddress);
        }else {
            course1 = new Course(courseDetails[0],courseDetails[1]);
        }
        return course1;
    }

}
