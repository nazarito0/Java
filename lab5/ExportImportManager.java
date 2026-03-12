import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ExportImportManager {
    private static final String FILE_NAME = "export_import.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1 - Заповнити файл новими записами");
            System.out.println("2 - Додати записи");
            System.out.println("3 - Редагувати запис");
            System.out.println("4 - Переглянути записи");
            System.out.println("5 - Виконати вибірку (імпорт > експорт)");
            System.out.println("0 - Вихід");
            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> fillFile();
                case "2" -> appendToFile();
                case "3" -> editRecord();
                case "4" -> viewRecords();
                case "5" -> performQueryDetailed();
                case "0" -> {
                    System.out.println("Вихід з програми.");
                    return;
                }
                default -> System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }

    private static void fillFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            System.out.println("Заповнення файлу. Введіть дані про товари.");
            while (true) {
                String line = inputRecord();
                if (line == null) break;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Файл успішно заповнено.");
        } catch (IOException e) {
            System.out.println("Помилка при записі файлу: " + e.getMessage());
        }
    }

    private static void appendToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            System.out.println("Додавання нових записів.");
            while (true) {
                String line = inputRecord();
                if (line == null) break;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Записи успішно додані.");
        } catch (IOException e) {
            System.out.println("Помилка при додаванні записів: " + e.getMessage());
        }
    }

    private static String inputRecord() {
        System.out.println("\nВведіть дані про товар (або пустий рядок для завершення):");

        System.out.print("Назва товару: ");
        String product = scanner.nextLine();
        if (product.isEmpty()) return null;

        System.out.print("Країна-імпортер: ");
        String importCountry = scanner.nextLine();
        if (importCountry.isEmpty()) return null;

        int importQuantity;
        while (true) {
            System.out.print("Об'єм партії (імпорт, ціле число): ");
            String qtyStr = scanner.nextLine();
            try {
                importQuantity = Integer.parseInt(qtyStr);
                if (importQuantity < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть правильне ціле число більше або рівне 0.");
            }
        }

        System.out.print("Країна-експортер: ");
        String exportCountry = scanner.nextLine();
        if (exportCountry.isEmpty()) return null;

        int exportQuantity;
        while (true) {
            System.out.print("Об'єм партії (експорт, ціле число): ");
            String qtyStr = scanner.nextLine();
            try {
                exportQuantity = Integer.parseInt(qtyStr);
                if (exportQuantity < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть правильне ціле число більше або рівне 0.");
            }
        }

        return product + "," + importCountry + "," + importQuantity + "," + exportCountry + "," + exportQuantity;
    }

    private static void editRecord() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
            if (lines.isEmpty()) {
                System.out.println("Файл порожній.");
                return;
            }
            viewRecords();
            System.out.print("Введіть номер запису для редагування: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= lines.size()) {
                System.out.println("Невірний номер запису.");
                return;
            }
            System.out.println("Введіть нові дані для цього запису:");
            String newLine = inputRecord();
            if (newLine != null) {
                lines.set(index, newLine);
                Files.write(Paths.get(FILE_NAME), lines);
                System.out.println("Запис успішно оновлено.");
            } else {
                System.out.println("Редагування скасовано.");
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Помилка при редагуванні: " + e.getMessage());
        }
    }

    private static void viewRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int count = 1;
            System.out.println("\n--- Записи у файлі ---");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    System.out.printf("%d. Товар: %s | Імпортер: %s | Кількість імпорту: %s | Експортер: %s | Кількість експорту: %s%n",
                            count, parts[0], parts[1], parts[2], parts[3], parts[4]);
                }
                count++;
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні файлу: " + e.getMessage());
        }
    }

    // Нова детальна вибірка
    private static void performQueryDetailed() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            Map<String, Integer> importSums = new HashMap<>();
            Map<String, Integer> exportSums = new HashMap<>();
            Map<String, List<String>> importProducts = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 5) continue;

                String product = parts[0].trim();
                String importCountry = parts[1].trim();
                String exportCountry = parts[3].trim();

                int importQuantity;
                int exportQuantity;

                try {
                    importQuantity = Integer.parseInt(parts[2].trim());
                    exportQuantity = Integer.parseInt(parts[4].trim());
                } catch (NumberFormatException e) {
                    continue;
                }

                importSums.put(importCountry, importSums.getOrDefault(importCountry, 0) + importQuantity);
                exportSums.put(importCountry, exportSums.getOrDefault(importCountry, 0) + 0); // для імпортера не додаємо експорт
                exportSums.put(exportCountry, exportSums.getOrDefault(exportCountry, 0) + exportQuantity);

                importProducts.computeIfAbsent(importCountry, k -> new ArrayList<>()).add(product + " (" + importQuantity + ")");
            }

            System.out.println("\n--- Детальна вибірка: країни, де сумарний імпорт > сумарний експорт ---");
            boolean found = false;

            for (String country : importSums.keySet()) {
                int imp = importSums.getOrDefault(country, 0);
                int exp = exportSums.getOrDefault(country, 0);

                if (imp > exp) {
                    found = true;
                    System.out.println("\nКраїна-імпортер: " + country);
                    System.out.println("Сумарний імпорт: " + imp + ", сумарний експорт: " + exp);
                    System.out.println("Товари, що імпортує країна:");
                    for (String productInfo : importProducts.get(country)) {
                        System.out.println(" - " + productInfo);
                    }
                }
            }

            if (!found) {
                System.out.println("Записів, де імпорт перевищує експорт, не знайдено.");
            }

        } catch (IOException e) {
            System.out.println("Помилка при виконанні вибірки: " + e.getMessage());
        }
    }
}