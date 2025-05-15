package deu.repository;

import deu.model.entity.User;

import java.util.List;
import java.io.*;
import java.util.ArrayList;

public class UserRepositoryImpl {
    //저장경로
    private static UserRepositoryImpl instance;
    private final List<User> users;
    private final String filePath = "user_data.ser";
    // singleton 패턴으로 구현한다
    public static UserRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }


    // 생성자


    // 파일에서 읽어오는 메서드


    // 파일에 저장하는 메서드


    // 저장 메서드


    // 삭제 메서드


    // 비교 메서드


    // 등 등
}
