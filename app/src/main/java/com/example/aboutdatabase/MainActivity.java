package com.example.aboutdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmResults;

import android.content.ContentValues;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.squareup.okhttp.internal.spdy.Variant;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button add;
    private Button delete;
    private Button update;
    private Button check;
    private Button deleteAll;
    private Button checkId;
    private TextView textView;
    private RadioGroup radioGroup;

    private List<Student> studentList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();
    private RealmList<Dog> dogList = new RealmList<>();

    private DBType type;

    private enum DBType {
        GREENDAO, LITEPAL, REALM
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();

        type = DBType.GREENDAO;
    }

    private void initView() {
        add = findViewById(R.id.add);
        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);
        check = findViewById(R.id.check);
        deleteAll = findViewById(R.id.deleteAll);
        checkId = findViewById(R.id.check_id);
        textView = findViewById(R.id.textView);
        radioGroup = findViewById(R.id.radio_g);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_greenDao:
                    type = DBType.GREENDAO;
                    break;
                case R.id.rb_litePal:
                    type = DBType.LITEPAL;
                    break;
                case R.id.rb_realm:
                    type = DBType.REALM;
                    break;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        final int length = 10000;

        for (int i = 0; i < length; i++) {
            Student student = new Student((long) i, "Tom" + i, 4);
            studentList.add(student);
        }

        for (int i = 0; i < length; i++) {
            User user = new User("Garon" + i, 25);
            userList.add(user);
        }

        for (int i = 0; i < length; i++) {
            Dog dog = new Dog(i + 1, "Buddy" + i, 2);
            dogList.add(dog);
        }
    }

    private void initListener() {
        /**
         *增
         */
        add.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            switch (type) {
                case GREENDAO:
                    StudentDaoOpe.insertData(this, studentList);
                    break;
                case LITEPAL:
                    LitePal.saveAll(userList);
                    break;
                case REALM:
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(r -> r.copyToRealm(dogList));
                    break;
            }
            long end = System.currentTimeMillis();
            textView.setText(String.format("%s插入1w条数据耗时：%sms", type.toString(), end - start));
        });
        /**
         * 删
         */
        delete.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            switch (type) {
                case GREENDAO:
                    Student student = new Student((long) 5, "haung" + 5, 25);
                    /**
                     * 根据特定的对象删除
                     */
//                    StudentDaoOpe.deleteData(this, student);
                    /**
                     * 根据主键删除
                     */
                    StudentDaoOpe.deleteByKeyData(this, 7);
                    break;
                case LITEPAL:
                    LitePal.delete(User.class, 5);
//                    userList.get(7).delete();
                    break;
                case REALM:
                    Realm mRealm = Realm.getDefaultInstance();
                    RealmResults<Dog> dogs = mRealm.where(Dog.class).findAll();
                    mRealm.executeTransaction(realm -> {
                        Dog dog = dogs.get(5);
                        dog.deleteFromRealm();
                        //删除第一个数据
//                        dogs.deleteFirstFromRealm();
                        //删除最后一个数据
//                        dogs.deleteLastFromRealm();
                        //删除位置为1的数据
//                        dogs.deleteFromRealm(1);
                    });
                    break;
            }
            long end = System.currentTimeMillis();
            textView.setText(String.format("%s删除1条数据耗时：%sms", type.toString(), end - start));
        });
        /**
         *删除所有
         */
        deleteAll.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            switch (type) {
                case GREENDAO:
                    StudentDaoOpe.deleteAllData(this);
                    break;
                case LITEPAL:
                    LitePal.deleteAll(User.class);
                    break;
                case REALM:
                    Realm mRealm = Realm.getDefaultInstance();
                    RealmResults<Dog> dogs = mRealm.where(Dog.class).findAll();
                    mRealm.executeTransaction(realm -> dogs.deleteAllFromRealm());
                    break;
            }
            long end = System.currentTimeMillis();
            textView.setText(String.format("%s删除1w条数据耗时：%sms", type.toString(), end - start));
        });
        /**
         * 更新
         */
        update.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            switch (type) {
                case GREENDAO:
                    Student student = new Student((long) 2, "chenjl", 26);
                    StudentDaoOpe.updateData(this, student);
                    break;
                case LITEPAL:
                    ContentValues values = new ContentValues();
                    values.put("name", "chenjl");
                    LitePal.update(User.class, values, 2);
                    break;
                case REALM:
                    Realm mRealm = Realm.getDefaultInstance();
                    Dog dog = mRealm.where(Dog.class).equalTo("id", 2).findFirst();
                    mRealm.executeTransaction(realm -> dog.setName("Lucy"));
                    break;
            }
            long end = System.currentTimeMillis();
            textView.setText(String.format("%s修改1条数据耗时：%sms", type.toString(), end - start));
        });
        /**
         * 查询全部
         */
        check.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            long end = 0;
            StringBuilder sb = new StringBuilder();
            switch (type) {
                case GREENDAO:
                    List<Student> students = StudentDaoOpe.queryAll(this);
                    end = System.currentTimeMillis();
                    for (int i = 0; i < students.size(); i++) {
                        Log.i("Log", students.get(i).toString());
                        sb.append(students.get(i).getName()).append(" ");
                    }
                    break;
                case LITEPAL:
                    List<User> list = LitePal.findAll(User.class);
                    end = System.currentTimeMillis();
                    for (User user : list) {
                        Log.i("Log", user.toString());
                        sb.append(user.getName()).append(" ");
                    }
                    break;
                case REALM:
                    Realm mRealm = Realm.getDefaultInstance();
                    RealmResults<Dog> dogs = mRealm.where(Dog.class).findAll();
                    List<Dog> dogs1 = mRealm.copyFromRealm(dogs);
                    end = System.currentTimeMillis();
                    for (Dog dog : dogs1) {
                        Log.i("Log", dog.toString());
                        sb.append(dog.getName()).append(" ");
                    }
                    break;
            }
            String time = String.format("%s查询1w条数据耗时：%sms \n %s", type.toString(), end - start, sb);
            textView.setText(time);
        });
        /**
         * 根据id查询
         */
        checkId.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            long end = 0;
            String result = "";
            switch (type) {
                case GREENDAO:
                    Student student = StudentDaoOpe.queryForId(this, 50);
                    if (student != null) {
                        result = student.toString();
                    }
                    end = System.currentTimeMillis();
                    break;
                case LITEPAL:
                    User user = LitePal.find(User.class, 50);
                    if (user != null) {
                        result = user.toString();
                    }
                    end = System.currentTimeMillis();
                    break;
                case REALM:
                    Realm mRealm = Realm.getDefaultInstance();
                    Dog dog = mRealm.where(Dog.class).equalTo("id", 2).findFirst();
                    if (dog != null) {
                        result = dog.toString();
                    }
                    end = System.currentTimeMillis();
                    break;
            }
            textView.setText(String.format("%s查询1条数据耗时：%sms \n %s", type.toString(), end - start, result));
        });
    }
}
