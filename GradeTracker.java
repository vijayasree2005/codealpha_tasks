import java.util.ArrayList;
import java.util.Scanner;

public class GradeTracker {
    private ArrayList<Student> students = new ArrayList<>();

    public void addStudent(String name, double grade) {
        students.add(new Student(name, grade));
    }

    public double getAverageGrade() {
        double total = 0;
        for (Student s : students) {
            total += s.getGrade();
        }
        return total / students.size();
    }

    public double getHighestGrade() {
        double highest = Double.MIN_VALUE;
        for (Student s : students) {
            if (s.getGrade() > highest) {
                highest = s.getGrade();
            }
        }
        return highest;
    }

    public double getLowestGrade() {
        double lowest = Double.MAX_VALUE;
        for (Student s : students) {
            if (s.getGrade() < lowest) {
                lowest = s.getGrade();
            }
        }
        return lowest;
    }

    public void displayReport() {
        System.out.println("\n--- Student Grade Report ---");
        for (Student s : students) {
            System.out.println(s.getName() + ": " + s.getGrade());
        }
        System.out.println("-----------------------------");
        System.out.println("Average Grade: " + getAverageGrade());
        System.out.println("Highest Grade: " + getHighestGrade());
        System.out.println("Lowest Grade: " + getLowestGrade());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GradeTracker tracker = new GradeTracker();

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter student name: ");
            String name = sc.next();
            System.out.print("Enter grade: ");
            double grade = sc.nextDouble();
            tracker.addStudent(name, grade);
        }

        tracker.displayReport();
        sc.close();
    }
}
