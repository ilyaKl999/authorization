package com.example.chats.controllers;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

//Users: Класс, представляющий пользователя с уникальным ID, именем и паролем.
//Реализует интерфейс Serializable для возможности сериализации.
//ID: Уникальный идентификатор пользователя.
//Name: Имя пользователя.
//password: Пароль пользователя.
//equals: Переопределен для сравнения объектов Users.
//hashCode: Переопределен для корректного сравнения объектов в коллекциях.
//toString: Переопределен для удобного вывода информации о пользователе.
//SimpleLogger: Класс для логирования информации с указанием времени, класса, метода и строки вызова.
//log: Метод для логирования сообщений.
//AuthorizationClass: Класс для управления пользователями.
//FILE_PATH: Константа, содержащая путь к файлу для сохранения коллекции пользователей.
//checkArrayList: Метод для проверки наличия файла и создания пустой коллекции, если файла нет.
//saveUsers: Метод для сохранения коллекции пользователей в файл.
//loadUsers: Метод для загрузки коллекции пользователей из файла.
//addUser: Метод для добавления нового пользователя в коллекцию.
//findUser: Метод для поиска пользователя в коллекции по имени и паролю.
//clearUsers(ArrayList<Users> users): Этот метод очищает коллекцию пользователей, удаляя все элементы из списка.
//SimpleLogger.log: Используется для логирования информации о начале и завершении очистки коллекции.


// Класс Users представляет пользователя с уникальным ID, именем и паролем
class Users implements Serializable {
    private final long ID; // Уникальный идентификатор пользователя
    private String Name; // Имя пользователя
    private String password; // Пароль пользователя

    // Конструктор для создания объекта Users
    public Users(long ID, String name, String password) {
        this.ID = ID;
        this.Name = name;
        this.password = password;
        SimpleLogger.log("[Users created: ID = " + ID + " Name = " + name + "  Password = " + password + " ]");
    }

    // Геттер для получения ID пользователя
    public long getID() {
        return ID;
    }

    // Геттер для получения имени пользователя
    public String getName() {
        return Name;
    }

    // Сеттер для установки имени пользователя
    public void setName(String name) {
        this.Name = name;
    }

    // Геттер для получения пароля пользователя
    public String getPassword() {
        return password;
    }

    // Сеттер для установки пароля пользователя
    public void setPassword(String password) {
        this.password = password;
    }

    // Переопределение метода equals для сравнения объектов Users
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return ID == users.ID && Objects.equals(Name, users.Name) && Objects.equals(password, users.password);
    }

    // Переопределение метода hashCode для корректного сравнения объектов в коллекциях
    @Override
    public int hashCode() {
        return Objects.hash(ID, Name, password);
    }

    // Переопределение метода toString для удобного вывода информации о пользователе
    @Override
    public String toString() {
        return "Users{" + "ID=" + ID + ", Name='" + Name + '\'' + ", password='" + password + '\'' + '}';
    }
}


// Класс SimpleLogger для логирования информации
class SimpleLogger {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Метод для логирования сообщений с указанием времени, класса, метода и строки вызова
    public static void log(String message, Object... args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2]; // Индекс 2 соответствует вызывающему методу
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        String lineNumber = String.valueOf(caller.getLineNumber());
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String formattedMessage = String.format(message, args);
        System.out.printf("[%s] [%s.%s:%s] %s%n", timestamp, className, methodName, lineNumber, formattedMessage);
    }
}


// Класс AuthorizationClass для управления пользователями
class AuthorizationClass {
    private static final String FILE_PATH = "users.dat"; // Путь к файлу для сохранения коллекции пользователей


    // Метод для проверки наличия файла и создания пустой коллекции, если файла нет
    static boolean checkArrayList() {
        SimpleLogger.log("Проверка файла на его наличие в директории");
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            SimpleLogger.log("Файл не существует, создание пустой коллекции");
            ArrayList<Users> emptyUsers = new ArrayList<>();
            saveUsers(emptyUsers);
            return false;
        }
        return true;
    }


    // Метод для сохранения коллекции пользователей в файл
    public static void saveUsers(ArrayList<Users> users) {
        SimpleLogger.log("Сохранение коллекции в файл");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
            SimpleLogger.log("Коллекция пользователей сохранена в файл: " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Метод для загрузки коллекции пользователей из файла
    public static ArrayList<Users> loadUsers() {
        SimpleLogger.log("Загрузка коллекции из файла");
        ArrayList<Users> users = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
                users = (ArrayList<Users>) ois.readObject();
                SimpleLogger.log("Коллекция пользователей загружена из файла: " + FILE_PATH + "Колличество объектов:" + users.size() );
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            SimpleLogger.log("Файл не существует: " + FILE_PATH);
        }
        return users;
    }


    // Метод для добавления нового пользователя в коллекцию
    public static void addUser(ArrayList<Users> users, long id, String name, String password) {
        Users newUser = new Users(id, name, password);
        users.add(newUser);
        saveUsers(users);
        SimpleLogger.log("Пользователь добавлен: ID = %d, Name = %s", id, name);
    }


    // Метод для поиска пользователя в коллекции по имени и паролю
    public static Users findUser(ArrayList<Users> users, String name, String password) {
        SimpleLogger.log("Поиск пользователя в коллекции");
        for (Users user : users) {
            if (user.getName().equals(name) && user.getPassword().equals(password)) {
                SimpleLogger.log("Пользователь найден: ID = %d, Name = %s", user.getID(), user.getName());
                return user;
            }
        }
        SimpleLogger.log("Пользователь не найден");
        return null;
    }

    //Метод, который очищает коллекцию пользователей
    public static void clearUsers(ArrayList<Users> users) {
        SimpleLogger.log("Очистка коллекции пользователей");
        users.clear();
        SimpleLogger.log("Коллекция пользователей очищена");
    }
}
