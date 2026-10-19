import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.stream.Collectors;

public class BloodBankSystem {

    static final String FILE = "donors.txt";
    static final String[] GROUPS = {
            "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
    };

    static Scanner sc = new Scanner(System.in);

    interface Record {
        void display();
    }

    static class Person {
        private String name;
        private int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    static class Donor extends Person
            implements Record, Comparable<Donor> {

        private int id;
        private String group, phone, city;

        Donor(int id, String name, int age, String group,
                String phone, String city) {
            super(name, age);
            this.id = id;
            this.group = group;
            this.phone = phone;
            this.city = city;
        }

        public String getGroup() {
            return group;
        }

        public int getId() {
            return id;
        }

        @Override
        public void display() {
            System.out.println(this);
        }

        @Override
        public String toString() {
            return String.format(
                    "ID:%d | Name:%s | Age:%d | Group:%s | City:%s",
                    id, getName(), getAge(), group, city);
        }

        String toFile() {
            return String.join("|", String.valueOf(id),
                    getName(), String.valueOf(getAge()),
                    group, phone, city);
        }

        
        static Donor fromFile(String line) {
            String[] p = line.split("\\|", -1);
            if (p.length != 6) {
                throw new IllegalArgumentException("Invalid record");
            }

            return new Donor(
                    Integer.parseInt(p[0]), p[1],
                    Integer.parseInt(p[2]), p[3], p[4], p[5]);
        }

        
        @Override
        public int compareTo(Donor other) {
            return getName().compareToIgnoreCase(other.getName());
        }
    }

    static boolean validGroup(String group) {
        for (String g : GROUPS) {
            if (g.equals(group))
                return true;
        }
        return false;
    }

    static boolean compatible(String recipient, String donor) {
        switch (recipient) {
            case "O-":
                return donor.equals("O-");
            case "O+":
                return donor.equals("O-") ||
                        donor.equals("O+");
            case "A-":
                return donor.equals("O-") ||
                        donor.equals("A-");
            case "A+":
                return donor.equals("O-") ||
                        donor.equals("O+") ||
                        donor.equals("A-") ||
                        donor.equals("A+");
            case "B-":
                return donor.equals("O-") ||
                        donor.equals("B-");
            case "B+":
                return donor.equals("O-") ||
                        donor.equals("O+") ||
                        donor.equals("B-") ||
                        donor.equals("B+");
            case "AB-":
                return donor.equals("O-") ||
                        donor.equals("A-") ||
                        donor.equals("B-") ||
                        donor.equals("AB-");
            case "AB+":
                return true;
            default:
                return false;
        }
    }


    static ArrayList<Donor> loadDonors() {
        ArrayList<Donor> list = new ArrayList<>();

        try {
            Path path = Paths.get(FILE);

            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    try {
                        Donor d = Donor.fromFile(line.trim());
                        if (validGroup(d.getGroup())) {
                            list.add(d);
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Skipping invalid record.");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File read error: " + e.getMessage());
        }

        return list;
    }

    
    static void saveDonor(Donor d) {
        try {
            Files.write(
                    Paths.get(FILE),
                    Collections.singletonList(d.toFile()),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("File save error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        
        ArrayList<Donor> donors = loadDonors();
        HashMap<String, Integer> stock = new HashMap<>();

        
        Arrays.stream(GROUPS).forEach(g -> stock.put(g, 0));

        
        while (true) {
            System.out.println("\n=== BLOOD BANK SYSTEM ===");
            System.out.println("1. Add donor");
            System.out.println("2. View donors");
            System.out.println("3. View inventory");
            System.out.println("4. Add blood units");
            System.out.println("5. Issue blood units");
            System.out.println("6. Find compatible donors");
            System.out.println("7. Search donor by name");
            System.out.println("8. Sorted donor report");
            System.out.println("9. Exit");
            System.out.print("Enter choice: ");

            try {
                int choice = Integer.parseInt(sc.nextLine().trim());

                switch (choice) {
                    case 1: {
                        
                        System.out.print("Name: ");
                        String name = sc.nextLine().trim();

                        System.out.print("Age: ");
                        int age = Integer.parseInt(sc.nextLine().trim());

                        System.out.print("Blood group: ");
                        String group = sc.nextLine().trim()
                                .toUpperCase();

                        System.out.print("Phone: ");
                        String phone = sc.nextLine().trim();

                        System.out.print("City: ");
                        String city = sc.nextLine().trim();

                        
                        if (name.isEmpty() || age < 18 ||
                                !validGroup(group) ||
                                phone.isEmpty() || city.isEmpty()) {
                            System.out.println(
                                    "Invalid details. Check age (18+) and group.");
                            break;
                        }

                        
                        Donor d = new Donor(
                                donors.size() + 1001, name, age,
                                group, phone, city);

                        donors.add(d);
                        saveDonor(d);
                        System.out.println("Donor registered.");
                        break;
                    }

                    case 2:
                        
                        for (Donor d : donors)
                            d.display();
                        if (donors.isEmpty())
                            System.out.println("No donor records.");
                        break;

                    case 3:
                        
                        stock.forEach((g, units) -> System.out.println(g + " : " +
                                units + " units"));
                        break;

                    case 4: {
                        System.out.print("Blood group: ");
                        String g = sc.nextLine().trim().toUpperCase();

                        System.out.print("Units to add: ");
                        int units = Integer.parseInt(
                                sc.nextLine().trim());

                        if (!validGroup(g) || units <= 0) {
                            System.out.println("Invalid input.");
                            break;
                        }

                        stock.put(g, stock.get(g) + units);
                        System.out.println("Stock updated.");
                        break;
                    }

                    case 5: {
                        System.out.print("Blood group: ");
                        String g = sc.nextLine().trim().toUpperCase();

                        System.out.print("Units to issue: ");
                        int units = Integer.parseInt(
                                sc.nextLine().trim());

                        if (!validGroup(g) || units <= 0) {
                            System.out.println("Invalid input.");
                        } else if (stock.get(g) >= units) {
                            stock.put(g, stock.get(g) - units);
                            System.out.println("Stock updated.");
                        } else {
                            System.out.println("Insufficient stock.");
                        }
                        break;
                    }

                    case 6: {
                        System.out.print("Recipient group: ");
                        String recipient = sc.nextLine()
                                .trim().toUpperCase();

                        if (!validGroup(recipient)) {
                            System.out.println("Invalid group.");
                            break;
                        }

                        
                        List<Donor> matches = donors.stream()
                                .filter(d -> compatible(
                                        recipient, d.getGroup()))
                                .collect(Collectors.toList());

                        matches.forEach(Donor::display);

                        if (matches.isEmpty())
                            System.out.println("No matching donors.");
                        break;
                    }

                    case 7: {
                        System.out.print("Enter name to search: ");
                        String name = sc.nextLine().trim();

                        
                        List<Donor> found = donors.stream()
                                .filter(d -> d.getName().equalsIgnoreCase(name))
                                .collect(Collectors.toList());

                        found.forEach(Donor::display);
                        if (found.isEmpty())
                            System.out.println("Donor not found.");
                        break;
                    }

                    case 8: {
                        
                        List<Donor> sorted = donors.stream()
                                .sorted()
                                .collect(Collectors.toList());

                        System.out.println("Total donors: " +
                                sorted.size());
                        sorted.forEach(Donor::display);
                        break;
                    }

                    case 9:
                        System.out.println("Thank you!");
                        return;

                    default:
                        System.out.println("Choose 1 to 9.");
                }

            } catch (NumberFormatException e) {
                
                System.out.println("Please enter a valid number.");
            }
        }
    }
}  
