package com.cecel.wfwpp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Vector;

public class CourseTable extends AppCompatActivity {
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_table);
        gridLayout = findViewById(R.id.course_table_main);
        Vector<Course> courseVector = null;
        //读取课程向量文件
        if ((courseVector = readCourseVector())!=null){
            loadCourse(courseVector);
        }else {
            Toast.makeText(CourseTable.this,"课程信息为空，请登录教务处导入！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 用于把单个课程添加到课表视图中
     * @param course
     */
    private void addCourse(Course course){
        if (course.getNumberOfStartCourse() != -1) {
            TextView textView = new TextView(this);
            Drawable drawable = getDrawable(R.drawable.border);
            textView.setBackground(drawable);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER);
            String result;
            String[] times = course.getCourseTime().split(">>");
            String[] address = course.getCourseAddress().split(">>");
            result = "<strong>"+ course.getCourseName() +"</strong>"+ "<br>" +
                     times[0] + times[2] +"<br>" +
                    "<b>"+address[1] +address[2] +"</b>"+ "<br>" +
                    course.getCourseTeacher();
            textView.setText(Html.fromHtml(result,Html.FROM_HTML_MODE_LEGACY));
            int rowN;
            if ((rowN = course.getNumberOfStartCourse()) > 9)
                rowN+=2;
            else if ((rowN = course.getNumberOfStartCourse()) > 4)
                rowN+=1;
            GridLayout.Spec row = GridLayout.spec(rowN, course.getNumberOfCourses(),
                    2.0f * course.getNumberOfCourses());
            GridLayout.Spec column = GridLayout.spec(course.getDayOfCourse(), 3.0f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(row, column);

            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = 0;
            layoutParams.setMargins(1,1,1,1);
            gridLayout.addView(textView, layoutParams);
        }
    }

    /**
     * 重载函数，对应一个课程有多个时间段的情况
     */
    private void addCourse(Course subCourse, Course mainCourse){
        Course course = new Course(
                mainCourse.getCourseName(),
                mainCourse.getCourseId(),
                mainCourse.getCourseNum(),
                mainCourse.getCredit(),
                mainCourse.getCourseProperty(),
                mainCourse.getCourseTeacher(),
                subCourse.getCourseTime(),
                subCourse.getCourseAddress()
        );
        addCourse(course);
    }

    /**
     * 给定一个课程向量集，调用transferToCourse(courseData)将其解析成课程向量
     * 再调用addCourse(tempCourse)和addCourse(tempCourse, mainCourse)
     * 将课程一一输出到表格中
     * @param courseVector
     */
    private void loadCourse(Vector<Course> courseVector){
        Iterator<Course> iterator = courseVector.iterator();
        while (iterator.hasNext()){
            Course tempCourse = iterator.next();
            if(tempCourse.getCourseId()!=-1){
                addCourse(tempCourse);
            }else {
                Course mainCourse;
                int index = courseVector.indexOf(tempCourse);
                while (true){
                    //向上查询，直到查询到包含完整信息的主课程
                    if ((mainCourse = courseVector.get(--index)).getCourseId()!=-1) {
                        addCourse(tempCourse, mainCourse);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.course_import:{
                Intent intent0 = getIntent();
                Intent intent = new Intent(CourseTable.this,CourseWebview.class);
                intent.putExtra("username",intent0.getStringExtra("username"));
                intent.putExtra("password",intent0.getStringExtra("password"));
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.course_list:{
                Intent intent1 = new Intent(CourseTable.this,CourseList.class);
                startActivity(intent1);
                break;
            }
            default:break;
        }
        return true;
    }

    //用于接收webview中传过来的信息
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1){
            if(resultCode == RESULT_OK){
                String courseData = data.getStringExtra("courseData");
                Vector<Course> courseVector = CourseInfo.transferToCourse(courseData);
                loadCourse(courseVector);     //解析并输出课表
                saveCourseVector(courseVector);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 创建名为CourseInfo的文件储存课程向量
     * 模式为MODE_PRIVATE，当新的用户导入课程信息时，原文件将被覆盖
     * @param courseVector
     */
    private void saveCourseVector(Vector<Course> courseVector){
        FileOutputStream out = null;
        ObjectOutputStream oOut = null;
        try {
            out = openFileOutput("CourseInfo",MODE_PRIVATE);
            oOut = new ObjectOutputStream(out);
            oOut.writeObject(courseVector);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (oOut!=null)
                    oOut.close();
                if (out!=null)
                    out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取课程文件
     * @return 包含Course类的向量
     */
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