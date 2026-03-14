import java.io.*;
import java.net.*;
import java.util.*;

class Currency implements Serializable {
    int r030;
    String txt;
    double rate;
    String cc;
    String exchangedate;

    @Override
    public String toString() {
        return txt + " (" + cc + "): " + rate + " грн, дата: " + exchangedate;
    }
}

public class CurrencyApp {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Використання: java CurrencyApp server|client");
            return;
        }

        if (args[0].equalsIgnoreCase("server")) {
            runServer();
        } else if (args[0].equalsIgnoreCase("client")) {
            runClient();
        } else {
            System.out.println("Невідомий режим: " + args[0]);
            System.out.println("Використання: java CurrencyApp server|client");
        }
    }

    private static void runServer() {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено на порту " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клієнт підключився: " + clientSocket.getInetAddress());

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

                URL bankUrl = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json");
                URLConnection connection = bankUrl.openConnection();
                connection.connect();

                Scanner bankOutput = new Scanner(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                while (bankOutput.hasNextLine()) {
                    jsonBuilder.append(bankOutput.nextLine());
                }

                List<Currency> currencyList = parseJsonCurrencies(jsonBuilder.toString());

                out.writeObject(currencyList.toArray(new Currency[0]));
                out.flush();

                clientSocket.close();
                System.out.println("Дані надіслано клієнту.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runClient() {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            Currency[] currencies = (Currency[]) in.readObject();

            for (Currency c : currencies) {
                System.out.println(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Currency> parseJsonCurrencies(String json) {
        List<Currency> list = new ArrayList<>();
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }

        String[] items = json.split("\\},\\{");

        for (String item : items) {
            item = item.replaceAll("[\\{\\}]", "");
            Currency c = new Currency();

            String[] fields = item.split(",");
            for (String field : fields) {
                String[] keyValue = field.split(":", 2);
                if (keyValue.length != 2) continue;
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim().replaceAll("\"", "");

                switch (key) {
                    case "r030": c.r030 = Integer.parseInt(value); break;
                    case "txt": c.txt = value; break;
                    case "rate": c.rate = Double.parseDouble(value); break;
                    case "cc": c.cc = value; break;
                    case "exchangedate": c.exchangedate = value; break;
                }
            }
            list.add(c);
        }
        return list;
    }
}
