package com.cecel.wfwpp;

import android.content.Intent;

import java.io.Serializable;

/**
 * 课程信息类
 * 加上Serializable是为了实现将对象储存到文件
 */
public class Course implements Serializable, Cloneable {
    private String courseName;
    private int courseId;
    private int courseNum;
    private int credit;
    private String courseProperty;
    private String courseTeacher;
    private String courseTime;      //
    private String courseAddress;   //



    private String[] timeDetail;

    public Course(String name, int courseId, int courseNum, int credit, String courseProperty, String courseTeacher, String courseTime, String courseAddress) {
        this.courseName = name;
        this.courseId = courseId;
        this.courseNum = courseNum;
        this.credit = credit;
        this.courseProperty = courseProperty;
        this.courseTeacher = courseTeacher;
        this.courseTime = courseTime;
        this.courseAddress = courseAddress;
        if (this.courseTime != null)
            timeDetail = courseTime.split(">>");
        else
            timeDetail = null;
    }

    public Course(String courseTime, String courseAddress){
        this.courseTime = courseTime;
        this.courseAddress = courseAddress;
        this.courseId = -1;
        timeDetail = courseTime.split(">>");
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getCourseNum() {
        return courseNum;
    }

    public int getCredit() {
        return credit;
    }

    public String getCourseProperty() {
        return courseProperty;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public String getCourseAddress() {
        return courseAddress;
    }

    public int getDayOfCourse(){
        if (timeDetail != null)
        {
            switch (timeDetail[1]) {
                case "星期一":
                    return 1;
                case "星期二":
                    return 2;
                case "星期三":
                    return 3;
                case "星期四":
                    return 4;
                case "星期五":
                    return 5;
                default:
                    return -1;
            }
        }else
            return -1;
    }

    public int getNumberOfCourses(){
        if (timeDetail!=null)
        {
            String[] result = timeDetail[2].split("-");
            if (result.length == 1)
                return 1;
            else
                return 1 + Integer.parseInt(result[1].substring(0, result[1].length() - 1))
                        - Integer.parseInt(result[0]);
        }else
            return -1;
    }

    public int getNumberOfStartCourse(){
        if (timeDetail != null)
        {
            String[] result = timeDetail[2].split("-");
            if (result.length == 1)
                return Integer.parseInt(result[0].substring(0, result[0].length() - 1));
            else
                return Integer.parseInt(result[0]);
        }
        else
            return -1;
    }

}
