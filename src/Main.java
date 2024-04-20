import java.io.*;
import java.util.Scanner;

public class Main {
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
                        totalLength += length;
                        maxLength = Math.max(maxLength, length);
                        minLength = Math.min(minLength, length);
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

        System.out.println("Общее количество строк в файлах: " + totalCount);
        System.out.println("Длина самой длинной строки в файлах: " + maxLength);
        System.out.println("Длина самой короткой строки в файлах: " + minLength);
    }
}
