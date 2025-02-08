package Storage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static Storage.Renderer.BUFFREAD;
import static Storage.Renderer.closeProgram;

public class EmployeeOpportunities {
    public static void employeeChanges(List<String> lines) throws IOException {
        int choice;
        while (!closeProgram) {
            System.out.println("""
                    Выберите действие:
                    1.Вывести отсортированный по названию список товаров
                    2.Вывести отсортированный по количеству список товаров
                    3.Вывести список товаров с определенным количеством
                    Любая другая клавиша - завершить работу""");
            choice = Integer.parseInt(BUFFREAD.readLine());
            switch (choice) {
                case 1: {
                    EmployeeOpportunities.listSortedByName(lines);
                    break;
                }
                case 2: {
                    EmployeeOpportunities.listSortedByQuantity(lines);
                    break;
                }
                case 3: {
                    EmployeeOpportunities.listWithCertainQuantity(lines);
                    break;
                }
                default: {
                    closeProgram = true;
                    break;
                }
            }
        }
    }
    public static void listSortedByName(List<String> lines) {
        lines.stream()
                .filter(line -> !(line.endsWith("admin")||line.endsWith("manager")||
                        line.endsWith("employee")||line.startsWith("#")||(line.trim().isEmpty())))
                .sorted()
                .forEach(System.out::println);
    }

    public static void listSortedByQuantity(List<String> lines) {
        lines.stream()
                .filter(line -> !(line.endsWith("admin")||line.endsWith("manager")||
                        line.endsWith("employee")||line.startsWith("#")||(line.trim().isEmpty())))
                .sorted(Comparator.comparing(line -> Integer.parseInt(line.split(",")[2].trim())))
                .forEach(System.out::println);
    }

    public static void listWithCertainQuantity(List<String> lines) throws IOException {
        int quantity;
        List<String> listSorted;

        System.out.println("Введите желаемое количество товара");
        quantity = Integer.parseInt(BUFFREAD.readLine());
        listSorted = lines.stream()
                .filter(line -> !(line.endsWith("admin")||line.endsWith("manager")||
                        line.endsWith("employee")||line.startsWith("#")||(line.trim().isEmpty())))
                .filter(line -> Integer.parseInt(line.split(",")[2].trim()) == quantity)
                .toList();
        if (listSorted.isEmpty()){
            System.out.println("Товара с таким количеством нет на складе");
        } else{
            listSorted.forEach(System.out::println);
        }
    }
}
