import java.io.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    private static int yandexBotRequests = 0;
    private static int googleBotRequests = 0;
    private static int totalCount = 0;


    public static void main(String[] args) {

        int fileCount = 0;
        int totalCount = 0;
        int totalLength = 0;
        int maxLength = Integer.MIN_VALUE;
        int minLength = Integer.MAX_VALUE;
        String path;
        boolean exit = false;
        while (!exit) {
            System.out.println("Введите путь к файлу:");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (isDirectory) {
                System.out.println("Указанный путь является путём к папке, а не к файлу");
                continue;
            } else if (fileExists) {
                System.out.println("Путь указан верно");
                fileCount++;
                System.out.println("Это файл номер " + fileCount);
                try {
                    FileReader fileReader = new FileReader(path);
                    BufferedReader reader = new BufferedReader(fileReader);
                    String line;
                    int count = 0;
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        if (length > 1024) {
                            throw new RuntimeException("Превышена максимальная длина строки (1024 символа)");
                        }
                        parseLogLine(line);
                        totalLength += length;
                        count++;

                    }
                    reader.close();
                    totalCount += count;

                } catch (FileNotFoundException e) {
                    System.err.println("Файл не найден: " + e.getMessage());
                } catch (IOException e) {
                    System.err.println("Ошибка при чтении файла: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.err.println("Ошибка: " + e.getMessage());
                    break;
                }

                System.out.println("Продолжить? (да/нет)");
                String answer = new Scanner(System.in).nextLine();
                if (!answer.equalsIgnoreCase("да")) {
                    exit = true;
                }
            } else {
                System.out.println("Указанный файл не существует");
            }
        }


        double totalPercentage = (double) (yandexBotRequests + googleBotRequests) / totalCount * 100;
        double yandexBotPercentage = (double) yandexBotRequests / totalCount * 100;
        double googleBotPercentage = (double) googleBotRequests / totalCount * 100;

        System.out.println("Доля запросов от YandexBot: " + yandexBotPercentage + "%");
        System.out.println("Доля запросов от Googlebot: " + googleBotPercentage + "%");
        System.out.println("Общая доля запросов от YandexBot и Googlebot: " + totalPercentage + "%");
        }


        private static void parseLogLine(String line) {
            String regex = "^(\\S+) (\\S+) (\\S+) \\[([^\\]]+)\\] \"(\\S+) (\\S+) (\\S+)\" (\\d+) (\\d+) \"([^\"]*)\" \"([^\"]*)\"$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {

                String ipAddress = matcher.group(1);
                String dash1 = matcher.group(2);
                String dash2 = matcher.group(3);
                String timestamp = matcher.group(4);
                String method = matcher.group(5);
                String path = matcher.group(6);
                String protocol = matcher.group(7);
                int responseCode = Integer.parseInt(matcher.group(8));
                int bytesSent = Integer.parseInt(matcher.group(9));
                String referer = matcher.group(10);
                String userAgent = matcher.group(11);


                String[] parts = userAgent.split("\\(");
                if (parts.length >= 2) {
                    String[] fragments = parts[1].split(";");
                    if (fragments.length >= 2) {
                        String fragment = fragments[1].trim();
                        String botName = fragment.split("/")[0];
                        if (botName.equals("Googlebot")) {
                            googleBotRequests++;
                        } else if (botName.equals("YandexBot")) {
                            yandexBotRequests++;
                        }
                    }
                }
                totalCount++;
            }
        }
    }
