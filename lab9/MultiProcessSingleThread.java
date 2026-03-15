import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MultiProcessSingleThread {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 0 && args[0].equals("worker")) {
            int processId = Integer.parseInt(args[1]);
            writeWordsToFile(processId);
        } else {
            int numberOfProcesses = 3;

            Process[] processes = new Process[numberOfProcesses];

            for (int i = 0; i < numberOfProcesses; i++) {
                int processId = i + 1;
                ProcessBuilder pb = new ProcessBuilder(
                        "java",
                        "-cp",
                        System.getProperty("java.class.path"),
                        "MultiProcessSingleThread",
                        "worker",
                        String.valueOf(processId)
                );
                pb.inheritIO();
                processes[i] = pb.start();
            }

            for (Process p : processes) {
                p.waitFor();
            }

            System.out.println("\nВсі процеси завершили запис у файл!\n");

            analyzeFile();
        }
    }

    private static void writeWordsToFile(int processId) {
        String fileName = "output.txt";
        String[] words = {"Java", "Python", "C++", "Go", "Rust", "Kotlin"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            for (String word : words) {
                writer.write("Process " + processId + ": " + word);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Process " + processId + " завершив запис!");
    }

    private static void analyzeFile() {
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("output.txt"))) {
            String line;
            int count = 0;
            System.out.println("Вміст файлу:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                count++;
            }
            System.out.println("Загальна кількість рядків у файлі: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}