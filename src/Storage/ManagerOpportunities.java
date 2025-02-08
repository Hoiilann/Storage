package Storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static Storage.Renderer.*;

public class ManagerOpportunities {
    public static void managerChanges(List<String> lines) throws IOException {
        int choice;
        while (!closeProgram) {
            System.out.println("""
                    Выберите действие:
                    1.Изменить количество товара
                    2.Вывести отсортированный по названию список товаров
                    3.Вывести отсортированный по количеству список товаров
                    4.Вывести список товаров с определенным количеством
                    Любая другая клавиша - завершить работу""");
            choice = Integer.parseInt(BUFFREAD.readLine());
            switch (choice) {
                case 1: {
                    lines = changingProductQuantity(lines);
                    break;
                }
                case 2: {
                    EmployeeOpportunities.listSortedByName(lines);
                    break;
                }
                case 3: {
                    EmployeeOpportunities.listSortedByQuantity(lines);
                    break;
                }
                case 4: {
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
    private static List<String> changingProductQuantity(List<String> lines) throws IOException {
        final int quantity;
        final String name;
        List<String> listForRemoving;
        final String type;

        System.out.println("Введите название товара, количество которого необходимо изменить");
        name = BUFFREAD.readLine();
        listForRemoving = lines
                .stream()
                .filter(line -> line.split(",")[0].trim().equalsIgnoreCase(name))
                .toList();
        if (listForRemoving.size() > 1) {
            List<String> purchase;

            System.out.println("Обнаружено больше 1 товара с таким именем, они выведены на экран:");
            listForRemoving.forEach(System.out::println);
            System.out.println("Выберите тип товара, у которого необходимо изменить количество:");
            type = BUFFREAD.readLine();
            purchase = listForRemoving.stream()
                    .filter(line -> line.split(",")[1].trim().equalsIgnoreCase(type.trim()))
                    .toList();
            if (purchase.isEmpty()) {
                System.out.println("Товара с таким типом нет в базе данных, действие отменено");
            } else {
                lines.remove(purchase.getFirst());
                System.out.println("Введите новое количество товара:");
                quantity = Integer.parseInt(BUFFREAD.readLine());
                lines.add(purchase.getFirst().split(",")[0]+","+purchase.getFirst().split(",")[1]+","+quantity);
                Files.write(PATH, lines, StandardCharsets.UTF_8);
                System.out.println("Количество товара изменено успешно");
            }
        } else if (listForRemoving.isEmpty()){
            System.out.println("Товара с таким типом нет в базе данных, действие отменено");
        } else {
            lines.remove(listForRemoving.getFirst());
            System.out.println("Введите новое количество товара:");
            quantity = Integer.parseInt(BUFFREAD.readLine());
            lines.add(listForRemoving.getFirst().split(",")[0]+","+listForRemoving.getFirst().split(",")[1]+","+quantity);
            Files.write(PATH, lines, StandardCharsets.UTF_8);
            System.out.println("Количество товара изменено успешно");
        }
        return lines;
    }

}
