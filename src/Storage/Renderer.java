package Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Renderer {
    public static Path PATH = Paths.get("C:\\Users\\bikch\\IdeaProjects\\try_catch\\src\\data.txt");
    public static BufferedReader BUFFREAD = new BufferedReader(new InputStreamReader(System.in));
    public static boolean closeProgram = false;

    public static void consoleAppStart() throws IOException {
        List<String> lines = Files.readAllLines(PATH, StandardCharsets.UTF_8);
        List <String[]> role;
        String name;
        boolean forArray;

        System.out.println("Добро пожаловать на склад ресторана из сериала Кухня\nВведите имя пользователя");
        name = BUFFREAD.readLine();
        role = roleDefinition(name,lines);
        forArray = role.isEmpty();
        while (forArray){
            String option;
            System.out.println("""
                    Такого пользователя нет в базе данных.
                    Выберите действие:
                    1.Попробовать еще раз
                    Любая другая клавиша - завершение работы программы.""");
            option = BUFFREAD.readLine();
            if (Integer.parseInt(option) == 1){
                System.out.println("Введите имя:");
                name = BUFFREAD.readLine();
                role = roleDefinition(name,lines);
                forArray = role.isEmpty();
            } else {
                forArray = false;
            }
        }
        if (!role.isEmpty()){
            System.out.println("Добро пожаловать,"+role.getFirst()[0]+","+role.getFirst()[1].trim());
            switch (role.getFirst()[1].trim()) {
                case "admin": {
                    AdminOpportunities.adminChanges(lines);
                    break;
                }
                case "manager":{
                    ManagerOpportunities.managerChanges(lines);
                    break;
                }
                case "employee":{
                    EmployeeOpportunities.employeeChanges(lines);
                }
            }


        }
    }

    private static List<String[]> roleDefinition(String name, List<String> lines) {
        return lines.stream()
                .map(line -> line.trim().split(","))
                .filter(line -> line[0].equalsIgnoreCase(name.trim()))
                .collect(Collectors.toList());

    }

}
