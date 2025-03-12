package com.example;

import com.example.entity.Address;
import com.example.entity.Student;
import com.example.entity.Course;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StudentAddressApp {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("studentPU");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EntityManager em = emf.createEntityManager();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Student");
            System.out.println("2. Add Address");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Remove Student");
            System.out.println("6. Remove Address");
            System.out.println("7. Update Address");
            System.out.println("8. Add Course");
            System.out.println("9. Add Course to Student");
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addStudent(em, scanner);
                    break;
                case 2:
                    addAddress(em, scanner);
                    break;
                case 3:
                    searchStudent(em, scanner);
                    break;
                case 4:
                    updateStudent(em, scanner);
                    break;
                case 5:
                    removeStudent(em, scanner);
                    break;
                case 6:
                    removeAddress(em, scanner);
                    break;
                case 7:
                    updateAddress(em, scanner);
                    break;
                case 8:
                    addCourse(em, scanner);
                    break;
                case 9:
                    addCourseToStudent(em, scanner);
                    break;
                case 10:
                    System.out.println("Exiting...");
                    em.close();
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option! Try again.");
            }
        }
    }

    // Method to add a student
    private static void addStudent(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter filiere: ");
        String filiere = scanner.nextLine();
        System.out.print("Enter module: ");
        String module = scanner.nextLine();

        Student student = new Student();
        student.setName(name);
        student.setFiliere(filiere);
        student.setModule(module);

        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();

        System.out.println("Student added successfully!");
    }

    // Method to add an address
    private static void addAddress(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to add address: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter street address: ");
        String street = scanner.nextLine();
        System.out.print("Enter city: ");
        String city = scanner.nextLine();

        Student student = em.find(Student.class, studentId);
        if (student != null) {
            Address address = new Address();
            address.setStreet(street);
            address.setCity(city);
            address.setStudent(student);

            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();

            System.out.println("Address added to student successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    // Method to search a student by ID
    private static void searchStudent(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to search: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        if (student != null) {
            System.out.println("Student found: " + student.getName() + ", Filiere: " + student.getFiliere() + ", Module: " + student.getModule());
            if (student.getAddress() != null) {
                System.out.println("Address: " + student.getAddress().getStreet() + ", " + student.getAddress().getCity());
            } else {
                System.out.println("No address found for this student.");
            }
            if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                System.out.println("Courses:");
                for (Course course : student.getCourses()) {
                    System.out.println("- " + course.getCourseName());
                }
            } else {
                System.out.println("No courses found for this student.");
            }
        } else {
            System.out.println("Student not found!");
        }
    }

    // Method to update a student
    private static void updateStudent(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to update: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        if (student != null) {
            System.out.print("Enter new name: ");
            student.setName(scanner.nextLine());
            System.out.print("Enter new filiere: ");
            student.setFiliere(scanner.nextLine());
            System.out.print("Enter new module: ");
            student.setModule(scanner.nextLine());

            em.getTransaction().begin();
            em.merge(student);
            em.getTransaction().commit();

            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    // Method to remove a student
    private static void removeStudent(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to remove: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        if (student != null) {
            // Remove the associated address first due to foreign key constraint
            if (student.getAddress() != null) {
                em.getTransaction().begin();
                em.remove(student.getAddress());
                em.getTransaction().commit();
                System.out.println("Address removed successfully.");
            }

            em.getTransaction().begin();
            em.remove(student);
            em.getTransaction().commit();
            System.out.println("Student removed successfully!");
        } else {
            System.out.println("Student not found!");
        }
    }

    // Method to remove an address
    private static void removeAddress(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to remove address: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        if (student != null && student.getAddress() != null) {
            Address address = student.getAddress();
            em.getTransaction().begin();
            em.remove(address);
            em.getTransaction().commit();
            System.out.println("Address removed successfully!");
        } else {
            System.out.println("No address found for this student.");
        }
    }

    // Method to update an address
    private static void updateAddress(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID to update address: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        if (student != null && student.getAddress() != null) {
            Address address = student.getAddress();
            System.out.print("Enter new street address: ");
            address.setStreet(scanner.nextLine());
            System.out.print("Enter new city: ");
            address.setCity(scanner.nextLine());

            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
            System.out.println("Address updated successfully!");
        } else {
            System.out.println("No address found for this student.");
        }
    }

    // Method to add a course
    private static void addCourse(EntityManager em, Scanner scanner) {
        System.out.print("Enter course name: ");
        String courseName = scanner.nextLine();

        Course course = new Course();
        course.setCourseName(courseName);

        em.getTransaction().begin();
        em.persist(course);
        em.getTransaction().commit();

        System.out.println("Course added successfully!");
    }

    // Method to add a course to a student
    private static void addCourseToStudent(EntityManager em, Scanner scanner) {
        System.out.print("Enter student's ID: ");
        Long studentId = scanner.nextLong();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter course ID: ");
        Long courseId = scanner.nextLong();
        scanner.nextLine();  // Consume newline

        Student student = em.find(Student.class, studentId);
        Course course = em.find(Course.class, courseId);

        if (student != null && course != null) {
            Set<Course> courses = student.getCourses();
            if (courses == null) {
                courses = new HashSet<>();
            }
            courses.add(course);
            student.setCourses(courses);

            em.getTransaction().begin();
            em.merge(student);
            em.getTransaction().commit();

            System.out.println("Course added to student successfully!");
        } else {
            System.out.println("Student or Course not found!");
        }
    }
}