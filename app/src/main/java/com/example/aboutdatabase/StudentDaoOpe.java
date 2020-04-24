package com.example.aboutdatabase;

import android.content.Context;

import com.example.aboutdatabase.greendao.StudentDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class StudentDaoOpe {
 
    /**
     * 添加数据至数据库
     *
     * @param context
     * @param stu
     */
    public static void insertData(Context context, Student stu) {
 
        DbManager.getDaoSession(context).getStudentDao().insert(stu);
    }
 
 
    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param context
     * @param list
     */
    public static void insertData(Context context, List<Student> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        DbManager.getDaoSession(context).getStudentDao().insertInTx(list);
    }
 
    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param student
     */
    public static void saveData(Context context, Student student) {
        DbManager.getDaoSession(context).getStudentDao().save(student);
    }
 
    /**
     * 删除数据至数据库
     *
     * @param context
     * @param student 删除具体内容
     */
    public static void deleteData(Context context, Student student) {
        DbManager.getDaoSession(context).getStudentDao().delete(student);
    }
 
    /**
     * 根据id删除数据至数据库
     *
     * @param context
     * @param id      删除具体内容
     */
    public static void deleteByKeyData(Context context, long id) {
        DbManager.getDaoSession(context).getStudentDao().deleteByKey(id);
    }
 
    /**
     * 删除全部数据
     *
     * @param context
     */
    public static void deleteAllData(Context context) {
        DbManager.getDaoSession(context).getStudentDao().deleteAll();
    }
 
    /**
     * 更新数据库
     *
     * @param context
     * @param student
     */
    public static void updateData(Context context, Student student) {
        DbManager.getDaoSession(context).getStudentDao().update(student);
    }
 
    /**
     * 查询所有数据
     *
     * @param context
     * @return
     */
    public static List<Student> queryAll(Context context) {
        QueryBuilder<Student> builder = DbManager.getDaoSession(context).getStudentDao().queryBuilder();
 
        return builder.build().list();
    }
 
/**
     * 根据id，其他的字段类似
     *
     * @param context
     * @param id
     * @return
     */
    public static Student queryForId(Context context, long id) {
        QueryBuilder<Student> builder = DbManager.getDaoSession(context).getStudentDao().queryBuilder();
        /**
         * 返回当前id的数据集合,当然where(这里面可以有多组，做为条件);
         * 这里build.list()；与where(StudentDao.Properties.Id.eq(id)).list()结果是一样的；
         * 在QueryBuilder类中list()方法return build().list();
         *
         */
        // Query<Student> build = builder.where(StudentDao.Properties.Id.eq(id)).build();
        // List<Student> list = build.list();
        return builder.where(StudentDao.Properties.Id.eq(id)).unique();
    }
}