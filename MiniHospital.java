g j5k3fd,7a.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class MiniHospital {
    static final Scanner in = new Scanner(System.in);
    static final AtomicLong idGen = new AtomicLong(1);
    static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    static class Patient {
        final long id;
        String name;
        int age;
        String contact;
        Patient(long id, String name, int age, String contact) {
            this.id = id; this.name = name; this.age = age; this.contact = contact;
        }
        public String toString() {
            return String.format("ID:%d  %s, %d y, %s", id, name, age, contact);
        }
    }

    static class Doctor {
        final long id;
        String name;
        String specialty;
        Doctor(long id, String name, String specialty) {
            this.id = id; this.name = name; this.specialty = specialty;
        }
        public String toString() {
            return String.format("ID:%d  Dr. %s (%s)", id, name, specialty);
        }
    }

    static class Appointment {
        final long id;
        final long patientId;
        final long doctorId;
        final LocalDateTime datetime;
        Appointment(long id, long patientId, long doctorId, LocalDateTime datetime) {
            this.id = id; this.patientId = patientId; this.doctorId = doctorId; this.datetime = datetime;
        }
        public String toString() {
            return String.format("ID:%d  Patient:%d  Doctor:%d  At:%s",
                id, patientId, doctorId, datetime.format(DTF));
        }
    }

    static final List<Patient> patients = new ArrayList<>();
    static final List<Doctor> doctors = new ArrayList<>();
    static final List<Appointment> appointments = new ArrayList<>();

    static long nextId() { return idGen.getAndIncrement(); }

    // Helpers
    static String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    static void addPatient() {
        String name = readLine("Patient name: ");
        if (name.isEmpty()) { System.out.println("Name required."); return; }
        int age = parseInt(readLine("Age: "), -1);
        String contact = readLine("Contact: ");
        Patient p = new Patient(nextId(), name, age, contact);
        patients.add(p);
        System.out.println("Added " + p);
    }

    static void listPatients() {
        if (patients.isEmpty()) { System.out.println("No patients."); return; }
        System.out.println("Patients:");
        patients.forEach(System.out::println);
    }

    static void addDoctor() {
        String name = readLine("Doctor name: ");
        if (name.isEmpty()) { System.out.println("Name required."); return; }
        String specialty = readLine("Specialty: ");
        Doctor d = new Doctor(nextId(), name, specialty);
        doctors.add(d);
        System.out.println("Added " + d);
    }

    static void listDoctors() {
        if (doctors.isEmpty()) { System.out.println("No doctors."); return; }
        System.out.println("Doctors:");
        doctors.forEach(System.out::println);
    }

    static void scheduleAppointment() {
        long pid = parseLong(readLine("Patient ID: "), -1);
        long did = parseLong(readLine("Doctor ID: "), -1);
        if (!existsPatient(pid) || !existsDoctor(did)) {
            System.out.println("Invalid patient or doctor ID.");
            return;
        }
        String dt = readLine("Datetime (YYYY-MM-DD HH:MM): ");
        LocalDateTime when;
        try {
            when = LocalDateTime.parse(dt, DTF);
        } catch (DateTimeParseException e) {
            System.out.println("Bad datetime format.");
            return;
        }
        Appointment a = new Appointment(nextId(), pid, did, when);
        appointments.add(a);
        System.out.println("Scheduled " + a);
    }

    static void listAppointments() {
        if (appointments.isEmpty()) { System.out.println("No appointments."); return; }
        System.out.println("Appointments:");
        appointments.forEach(System.out::println);
    }

    static boolean existsPatient(long id) {
        return patients.stream().anyMatch(p -> p.id == id);
    }

    static boolean existsDoctor(long id) {
        return doctors.stream().anyMatch(d -> d.id == id);
    }

    static int parseInt(String s, int fallback) {
        try { return Integer.parseInt(s); } catch (Exception e) { return fallback; }
    }

    static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }

    static void printMenu() {
        System.out.println("\n--- Mini Hospital Menu ---");
        System.out.println("1) Add patient");
        System.out.println("2) List patients");
        System.out.println("3) Add doctor");
        System.out.println("4) List doctors");
        System.out.println("5) Schedule appointment");
        System.out.println("6) List appointments");
        System.out.println("0) Exit");
    }

    public static void main(String[] args) {
        // small demo entries (optional) — comment out if you prefer empty start
        patients.add(new Patient(nextId(), "Alice Morgan", 29, "555-0101"));
        doctors.add(new Doctor(nextId(), "John Reed", "General"));
        // menu loop
        while (true) {
            printMenu();
            String choice = readLine("Choice: ");
            switch (choice) {
                case "1": addPatient(); break;
                case "2": listPatients(); break;
                case "3": addDoctor(); break;
                case "4": listDoctors(); break;
                case "5": scheduleAppointment(); break;
                case "6": listAppointments(); break;
                case "0": System.out.println("Bye."); return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}
