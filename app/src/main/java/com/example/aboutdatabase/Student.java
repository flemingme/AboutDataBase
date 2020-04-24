package com.example.aboutdatabase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity(generateConstructors = false)
public class Student {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private int age;

    public Student() {
    }

    @Keep
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }


    public Student(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Keep
    public Long getId() {
        return id;
    }

    @Keep
    public void setId(Long id) {
        this.id = id;
    }

    @Keep
    public String getName() {
        return name;
    }

    @Keep
    public void setName(String name) {
        this.name = name;
    }

    @Keep
    public int getAge() {
        return age;
    }

    @Keep
    public void setAge(int age) {
        this.age = age;
    }

    @Keep
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return name.equals(student.name);

    }

    @Keep
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Keep
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}