/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.model.repository;

import deu.model.yaml.LectureYaml;
import java.io.*;

/**
 *
 * @author oixikite
 */

public class LectureRepository {
    private static LectureRepository instance;

    // private 생성자 (싱글톤 패턴)
    private LectureRepository() {
        System.out.println("LectureRepository 생성됨");
    }

    // 싱글톤 인스턴스 반환
    public static LectureRepository getInstance() {
        if (instance == null) {
            instance = new LectureRepository();
        }
        return instance;
    }

    // 파일 불러오기 구조
    public void loadFile() {
        File file = new File(LectureYaml.FILE_PATH);
        if (!file.exists()) {
            System.out.println("파일이 존재하지 않습니다.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            System.out.println("파일 내용:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 지금은 단순 출력
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일 저장 구조 (덮어쓰기)
    public void saveFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LectureYaml.FILE_PATH))) {
            writer.write(content);
            System.out.println("파일 저장 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}