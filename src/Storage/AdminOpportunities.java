package Storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static Storage.Renderer.*;

public class AdminOpportunities {
    public static void adminChanges (List<String> lines) throws IOException {
        int choice;
        while(!closeProgram){
            System.out.println("""
                    Выберите действие:
                    1.Добавить товар
                    2.Удалить товар
                    3.Изменить количество товара
                    4.Добавить пользователя
                    5.Удалить пользователя
                    6.Вывести отсортированный по названию список товаров
                    7.Вывести отсортированный по количеству список товаров
                    8.Вывести список товаров с определенным количеством
                    Любая другая клавиша - завершить работу""");
            choice = Integer.parseInt(BUFFREAD.readLine());
            switch(choice){
                case 1: {
                    lines = addingProduct(lines);
                    break;
                }
                case 2: {
                    lines = changingQuantityOrRemovingProduct(lines,true);
                    break;
                }
                case 3: {
                    lines = changingQuantityOrRemovingProduct(lines,false);
                    break;
                }
                case 4: {
                    lines = addingUser(lines);
                    break;
                }
                case 5: {
                    lines = removingUser(lines);
                    break;
                }
                case 6: {
                    EmployeeOpportunities.listSortedByName(lines);
                    break;
                }
                case 7:{
                    EmployeeOpportunities.listSortedByQuantity(lines);
                    break;
                }
                case 8:{
                    EmployeeOpportunities.listWithCertainQuantity(lines);
                    break;
                }
                default:{
                    closeProgram = true;
                    break;
                }

            }
        }

    }

    private static List<String> removingUser(List<String> lines) throws IOException {
        final String name;
        List<String> listForRemoving;

        System.out.println("Впишите имя пользователя, которого нужно удалить");
        name = BUFFREAD.readLine();
        listForRemoving = lines
                .stream()
                .filter(line -> line.split(",")[0].equalsIgnoreCase(name))
                .toList();
        if (listForRemoving.isEmpty()){
            System.out.println("Пользователя с таким именем нет в базе данных,действие отменено");
        } else {
            lines.remove(listForRemoving.getFirst());
            Files.write(PATH, lines, StandardCharsets.UTF_8);
            System.out.println("Пользователь удален");
        }
        return lines;
    }

    private static List<String> addingUser(List<String> lines) throws IOException {
        String name;
        String full;
        int choice;
        String [] roles = {"","admin","manager","employee"};

        System.out.println("Впишите имя нового пользователя");
        name = BUFFREAD.readLine();
        System.out.println("""
                Выберите роль нового пользователя:
                1.admin
                2.manager
                3.employee""");
        choice = Integer.parseInt(BUFFREAD.readLine());
        full = name + "," + roles[choice];
        if ((choice == 1 || choice == 2 || choice == 3) && lines.stream()
                .filter(line -> line.trim().equalsIgnoreCase(full.trim()))
                .toList()
                .isEmpty()) {
            lines.add(full);
            Files.writeString(PATH, "\n"+full, StandardOpenOption.APPEND);
            System.out.println("Пользователь добавлен");
        } else {
            System.out.println("Неправильный ввод, либо такой пользователь уже есть в базе данных, действие отменено");
        }
        return lines;
    }

    private static List<String> changingQuantityOrRemovingProduct(List<String> lines, boolean removing) throws IOException {
        final int quantity;
        final String name;
        List<String> listForRemoving;
        final String type;

        if (removing){
            System.out.println("Введите название товара, который хотите удалить");
        } else {
            System.out.println("Введите название товара, количество которого необходимо изменить");
        }
        name = BUFFREAD.readLine();
        listForRemoving = lines
                .stream()
                .filter(line -> line.split(",")[0].trim().equalsIgnoreCase(name.trim()))
                .toList();
        if (listForRemoving.size() > 1) {
            List<String> purchase;

            System.out.println("Обнаружено больше 1 товара с таким именем, они выведены на экран:");
            listForRemoving.forEach(System.out::println);
            if (removing){
                System.out.println("Выберите тип товара, который хотите удалить:");
            } else {
                System.out.println("Выберите тип товара, у которого необходимо изменить количество:");
            }
            type = BUFFREAD.readLine();
            purchase = listForRemoving.stream()
                    .filter(line -> !(line.trim().isEmpty() && line.startsWith("#")))
                    .filter(line -> line.split(",")[1].trim().equalsIgnoreCase(type.trim()))
                    .toList();
            if (purchase.isEmpty()) {
                System.out.println("Товара с таким типом нет в базе данных, действие отменено");
            } else {
                lines.remove(purchase.getFirst());
                if(!removing){
                    System.out.println("Введите новое количество товара:");
                    quantity = Integer.parseInt(BUFFREAD.readLine());
                    lines.add(purchase.getFirst().split(",")[0]+","+purchase.getFirst().split(",")[1]+","+quantity);
                    System.out.println("Количество товара изменено успешно");
                } else {
                    System.out.println("Товар успешно удален");
                }
                Files.write(PATH, lines, StandardCharsets.UTF_8);
            }
        } else if (listForRemoving.isEmpty()){
            System.out.println("Товара с таким типом нет в базе данных, действие отменено");
        } else {
            lines.remove(listForRemoving.getFirst());
            if(!removing){
                System.out.println("Введите новое количество товара:");
                quantity = Integer.parseInt(BUFFREAD.readLine());
                lines.add(listForRemoving.getFirst().split(",")[0]+","+listForRemoving.getFirst().split(",")[1]+","+quantity);
                System.out.println("Количество товара изменено успешно");
            } else {
                System.out.println("Товар успешно удален");
            }
            Files.write(PATH, lines, StandardCharsets.UTF_8);

        }
        return lines;
    }


    private static List<String> addingProduct(List<String> lines) throws IOException {
        final String name;
        final String type;
        final int quantity;
        final String full;

        System.out.println("Впишите имя нового товара");
        name = BUFFREAD.readLine();
        System.out.println("Впишите тип товара");
        type = BUFFREAD.readLine();
        System.out.println("Впишите количество товара");
        quantity = Integer.parseInt(BUFFREAD.readLine());
        full = name+","+type+","+quantity;
        if (lines.stream()
                .filter(line -> !(line.trim().isEmpty() && line.startsWith("#")))
                .filter(line -> line.split(",")[0].trim().equalsIgnoreCase(name.trim()) &&
                        line.split(",")[1].trim().equalsIgnoreCase(type.trim()))
                .toList()
                .isEmpty()) {
            lines.add(full);
            Files.writeString(PATH, full, StandardOpenOption.APPEND);
            System.out.println("Товар успешно добавлен");
        } else {
            System.out.println("Товар с таким именем и типом уже существует в файле, действие отменено");
        }
        return lines;
    }

}
