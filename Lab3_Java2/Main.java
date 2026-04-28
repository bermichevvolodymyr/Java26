package Lab3_Java2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Клас Працівник
class Worker {
    private String firstName;
    private String lastName;
    private double salary;

    public Worker(String firstName, String lastName, double salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + salary + ")";
    }
}

// Клас Відділ
class Department {
    private String name;
    private Worker head;
    private List<Worker> workers;

    public Department(String name, Worker head, List<Worker> workers) {
        this.name = name;
        this.head = head;
        this.workers = workers;
    }

    public String getName() {
        return name;
    }

    public Worker getHead() {
        return head;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    @Override
    public String toString() {
        return name;
    }
}

// Клас Фірма
class Company {
    private String name;
    private Worker director;
    private List<Department> departments;

    public Company(String name, Worker director, List<Department> departments) {
        this.name = name;
        this.director = director;
        this.departments = departments;
    }

    public Worker getDirector() {
        return director;
    }

    public List<Department> getDepartments() {
        return departments;
    }
}

public class Main {
    public static void main(String[] args) {
        // Підготовка тестових даних
        Worker director = new Worker("Іван", "Іванов", 50000);

        Worker headIT = new Worker("Петро", "Петров", 30000);
        Worker dev1 = new Worker("Олег", "Сидоров", 35000); // Зарплата більша, ніж у начальника
        Worker dev2 = new Worker("Анна", "Смирнова", 25000);
        Department itDept = new Department("IT", headIT, Arrays.asList(dev1, dev2));

        Worker headHR = new Worker("Марія", "Кузнєцова", 20000);
        Worker hr1 = new Worker("Олена", "Попова", 15000);
        Department hrDept = new Department("HR", headHR, Arrays.asList(hr1));

        Company company = new Company("TechCorp", director, Arrays.asList(itDept, hrDept));

        // Виконання задач

        // Задача 3: Скласти список усіх співробітників фірми, включаючи начальників та директора
        List<Worker> allEmployees = getAllEmployees(company);
        System.out.println("3) Список усіх співробітників:");
        allEmployees.forEach(System.out::println);
        System.out.println("-------------------------------------------------");

        // Задача 1: Знайти значення максимальної заробітної платні з усіх працівників
        double maxSalary = getMaxSalary(company);
        System.out.println("1) Максимальна заробітна платня: " + maxSalary);
        System.out.println("-------------------------------------------------");

        // Задача 2: Визначити відділ, в якому хоча б один з співробітників отримує з/п вищу за начальника
        List<Department> departmentsWithHighlyPaidWorkers = getDepartmentsWithHighlyPaidWorkers(company);
        System.out.println("2) Відділи, в яких співробітники отримують більше за начальника:");
        departmentsWithHighlyPaidWorkers.forEach(System.out::println);
    }

    // --- Реалізація методів з використанням Streams API ---

    // 1) Знайти значення максимальної заробітної платні з усіх працівників, включаючи начальників та директора
    public static double getMaxSalary(Company company) {
        return getAllEmployees(company).stream()
                .mapToDouble(Worker::getSalary)
                .max()
                .orElse(0.0);
    }

    // 2) Визначити, відділ, в якому хоча б один з співробітників отримує заробітну платню вищу за платню свого начальника
    public static List<Department> getDepartmentsWithHighlyPaidWorkers(Company company) {
        return company.getDepartments().stream()
                .filter(dept -> dept.getWorkers().stream()
                        .anyMatch(worker -> worker.getSalary() > dept.getHead().getSalary()))
                .collect(Collectors.toList());
    }

    // 3) Скласти список усіх співробітників фірми, включаючи начальників та директора
    public static List<Worker> getAllEmployees(Company company) {
        Stream<Worker> directorStream = Stream.of(company.getDirector());
        Stream<Worker> headsStream = company.getDepartments().stream().map(Department::getHead);
        Stream<Worker> workersStream = company.getDepartments().stream().flatMap(d -> d.getWorkers().stream());

        return Stream.concat(directorStream, Stream.concat(headsStream, workersStream))
                .collect(Collectors.toList());
    }
}