package deu.repository;

import deu.model.entity.User;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static final UserRepository instance = new UserRepository();
    private final List<User> users = new ArrayList<>();
    private final String FILE_PATH = System.getProperty("user.dir") + File.separator + "data" + File.separator + "users.yaml";
    private final Yaml yaml;



}
