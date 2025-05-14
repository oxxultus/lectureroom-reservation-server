/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.repository;

import deu.model.entity.LectureList;
import deu.model.yaml.LectureYaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author oixikite
 */

public class LectureRepository {
    private static LectureRepository instance;

    private LectureRepository() {}

    public static LectureRepository getInstance() {
        if (instance == null) {
            instance = new LectureRepository();
        }
        return instance;
    }

    public LectureList loadLectures() {
        File file = new File(LectureYaml.FILE_PATH);
        if (!file.exists()) {
            System.out.println("파일이 존재하지 않습니다.");
            return null;
        }

        try (InputStream input = new FileInputStream(file)) {
            Yaml yaml = new Yaml(new Constructor(LectureList.class));
            return yaml.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveLectures(LectureList lectureList) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(LectureYaml.FILE_PATH), StandardCharsets.UTF_8)) {
            DumperOptions options = new DumperOptions();
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

            Yaml yaml = new Yaml(options);
            yaml.dump(lectureList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}