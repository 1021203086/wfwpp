package com.cecel.wfwpp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;
import java.util.Vector;

public class CourseList extends AppCompatActivity {
    private static int index = 0;

    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        gridLayout = findViewById(R.id.course_list);
        Vector<Course> courseVector = readCourseVector();
        if ((courseVector = readCourseVector())!=null){
            loadCourse(courseVector);
        }else {
            Toast.makeText(CourseList.this,"课程信息为空，请登录教务处导入！",Toast.LENGTH_SHORT).show();
        }
    }

    private void addCourseList(Course course){
        ++index;
        Drawable drawable = getDrawable(R.drawable.border);
        for (int i=0;i<8;i++) {
            TextView textView = new TextView(this);
            //textView.setTextIsSelectable(true);
            textView.setBackground(drawable);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            String text = "";
            switch (i){
                case 0:text+=course.getCourseId();break;
                case 1:text=course.getCourseName();break;
                case 2:text+=course.getCourseNum();break;
                case 3:text+=course.getCredit();break;
                case 4:text=course.getCourseProperty();break;
                case 5:text=course.getCourseTeacher();break;
                case 6:text=course.getCourseTime();break;
                case 7:text=course.getCourseAddress();break;
                default:break;
            }
            textView.setText(text);
            GridLayout.Spec row = GridLayout.spec(index, 2.0f);
            GridLayout.Spec column = GridLayout.spec(i, 3.0f);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, column);
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.width = 0;
            gridLayout.addView(textView, params);
        }

    }

    private void addCourseList(String subCourseTime, String subCourseAddress, Course mainCourse){
        Course course = new Course(
                mainCourse.getCourseName(),
                mainCourse.getCourseId(),
                mainCourse.getCourseNum(),
                mainCourse.getCredit(),
                mainCourse.getCourseProperty(),
                mainCourse.getCourseTeacher(),
                mainCourse.getCourseTime()+"\n"+subCourseTime,
                mainCourse.getCourseAddress()+"\n"+subCourseAddress
        );
        addCourseList(course);
    }

    private void loadCourse(Vector<Course> courseVector){
        Iterator<Course> iterator = courseVector.iterator();
        while (iterator.hasNext()){
            Course tempCourse = iterator.next();
            int index = courseVector.indexOf(tempCourse);
            int size = courseVector.size();
            Course subCourse;
            if (index+1<size && courseVector.get(index+1).getCourseId()==-1){
                StringBuilder subCourseTime = new StringBuilder();
                StringBuilder subCourseAddress = new StringBuilder();
                while (++index < size &&
                        (subCourse = courseVector.get(index)).getCourseId() == -1){
                    iterator.next();
                    subCourseTime.append("\n").append(subCourse.getCourseTime());
                    subCourseAddress.append("\n").append(subCourse.getCourseAddress());
                }
                addCourseList(subCourseTime.toString(), subCourseAddress.toString(),tempCourse);
            }else {
                addCourseList(tempCourse);
            }
        }
    }

    public Vector<Course> readCourseVector(){
        Vector<Course> courseVector = null;
        FileInputStream input = null;
        ObjectInputStream oInput = null;
        try {
            input = openFileInput("CourseInfo");
            oInput = new ObjectInputStream(input);
            courseVector = (Vector<Course>) oInput.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (oInput != null)
                    oInput.close();
                if (input != null)
                    input.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return courseVector;
    }
}