import java.util.ArrayList;
import java.util.Scanner;

class Donor {
    int donorId;
    String name;
    int age;
    String gender;
    String bloodGroup;
    String phone;
    String city;

    Donor(int donorId, String name, int age, String gender,
            String bloodGroup, String phone, String city) {

        this.donorId = donorId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.city = city;
    }

    void display() {
        System.out.println("----------------------------------");
        System.out.println("Donor ID     : " + donorId);
        System.out.println("Name         : " + name);
        System.out.println("Age          : " + age);
        System.out.println("Gender       : " + gender);
        System.out.println("Blood Group  : " + bloodGroup);
        System.out.println("Phone        : " + phone);
        System.out.println("City         : " + city);
    }
}

public class BloodBankSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Donor> donors = new ArrayList<>();

    static int[] bloodUnits = new int[8];

    static String[] bloodGroups = {
            "A+", "A-", "B+", "B-",
            "AB+", "AB-", "O+", "O-"
    };

    static int donorIdCounter = 1001;

    public static void main(String[] args) {

        // Sample donors
        donors.add(new Donor(
                donorIdCounter++, "Rahul", 22,
                "Male", "O+", "9876543210", "Hyderabad"));

        donors.add(new Donor(
                donorIdCounter++, "Priya", 24,
                "Female", "A+", "9876501234", "Hyderabad"));

        donors.add(new Donor(
                donorIdCounter++, "Arjun", 25,
                "Male", "B+", "9123456780", "Secunderabad"));

        // Sample inventory
        bloodUnits[0] = 5; // A+
        bloodUnits[1] = 2; // A-
        bloodUnits[2] = 4; // B+
        bloodUnits[3] = 1; // B-
        bloodUnits[4] = 3; // AB+
        bloodUnits[5] = 1; // AB-
        bloodUnits[6] = 6; // O+
        bloodUnits[7] = 2; // O-

        int choice;

        do {
            System.out.println("\n====================================");
            System.out.println("      BLOOD BANK MANAGEMENT SYSTEM");
            System.out.println("====================================");
            System.out.println("1. Add Donor");
            System.out.println("2. View All Donors");
            System.out.println("3. Search Donor");
            System.out.println("4. View Blood Inventory");
            System.out.println("5. Add Blood Units");
            System.out.println("6. Issue Blood Units");
            System.out.println("7. Find Compatible Donors");
            System.out.println("8. Check Blood Availability");
            System.out.println("9. Exit");
            System.out.println("====================================");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    addDonor();
                    break;

                case 2:
                    viewDonors();
                    break;

                case 3:
                    searchDonor();
                    break;

                case 4:
                    viewInventory();
                    break;

                case 5:
                    addBloodUnits();
                    break;

                case 6:
                    issueBloodUnits();
                    break;

                case 7:
                    findCompatibleDonors();
                    break;

                case 8:
                    checkAvailability();
                    break;

                case 9:
                    System.out.println("\nThank you for using the system.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 9);

        sc.close();
    }

    // Add donor
    static void addDonor() {

        System.out.println("\n----- ADD DONOR -----");

        System.out.print("Enter donor name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter blood group: ");
        String bloodGroup = sc.nextLine().toUpperCase();

        if (!isValidBloodGroup(bloodGroup)) {
            System.out.println("Invalid blood group.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = sc.nextLine();

        System.out.print("Enter city: ");
        String city = sc.nextLine();

        Donor donor = new Donor(
                donorIdCounter++,
                name,
                age,
                gender,
                bloodGroup,
                phone,
                city);

        donors.add(donor);

        System.out.println("\nDonor added successfully.");
        System.out.println("Donor ID: " + donor.donorId);
    }

    // Display donors
    static void viewDonors() {

        System.out.println("\n========== DONOR LIST ==========");

        if (donors.isEmpty()) {
            System.out.println("No donors available.");
            return;
        }

        for (Donor donor : donors) {
            donor.display();
        }
    }

    // Search donor
    static void searchDonor() {

        System.out.print("\nEnter blood group to search: ");
        String group = sc.nextLine().toUpperCase();

        boolean found = false;

        for (Donor donor : donors) {

            if (donor.bloodGroup.equals(group)) {
                donor.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No donor found for " + group);
        }
    }

    // View inventory
    static void viewInventory() {

        System.out.println("\n========== BLOOD INVENTORY ==========");

        for (int i = 0; i < bloodGroups.length; i++) {

            System.out.println(
                    bloodGroups[i] + " : " +
                            bloodUnits[i] + " units");
        }
    }

    // Add blood units
    static void addBloodUnits() {

        System.out.print("\nEnter blood group: ");
        String group = sc.nextLine().toUpperCase();

        int index = getBloodGroupIndex(group);

        if (index == -1) {
            System.out.println("Invalid blood group.");
            return;
        }

        System.out.print("Enter number of units: ");
        int units = sc.nextInt();

        if (units <= 0) {
            System.out.println("Invalid number of units.");
            return;
        }

        bloodUnits[index] += units;

        System.out.println(
                units + " units added successfully.");
    }

    // Issue blood units
    static void issueBloodUnits() {

        System.out.print("\nEnter blood group: ");
        String group = sc.nextLine().toUpperCase();

        int index = getBloodGroupIndex(group);

        if (index == -1) {
            System.out.println("Invalid blood group.");
            return;
        }

        System.out.print("Enter number of units required: ");
        int units = sc.nextInt();

        if (units <= 0) {
            System.out.println("Invalid number of units.");
            return;
        }

        if (bloodUnits[index] >= units) {

            bloodUnits[index] -= units;

            System.out.println(
                    units + " units issued successfully.");

        } else {

            System.out.println(
                    "Insufficient blood stock.");
        }
    }

    // Check blood availability
    static void checkAvailability() {

        System.out.print("\nEnter required blood group: ");
        String group = sc.nextLine().toUpperCase();

        int index = getBloodGroupIndex(group);

        if (index == -1) {
            System.out.println("Invalid blood group.");
            return;
        }

        System.out.println(
                "Available units of " + group +
                        " : " + bloodUnits[index]);
    }

    // Find compatible donors
    static void findCompatibleDonors() {

        System.out.print(
                "\nEnter recipient blood group: ");

        String recipient = sc.nextLine().toUpperCase();

        if (!isValidBloodGroup(recipient)) {
            System.out.println("Invalid blood group.");
            return;
        }

        System.out.println(
                "\nCompatible donors for " + recipient + ":");

        boolean found = false;

        for (Donor donor : donors) {

            if (isCompatible(
                    recipient,
                    donor.bloodGroup)) {

                donor.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No compatible donors found.");
        }
    }

    // Compatibility function
    static boolean isCompatible(
            String recipient,
            String donor) {

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

    // Validate blood group
    static boolean isValidBloodGroup(String group) {

        return getBloodGroupIndex(group) != -1;
    }

    // Get blood group index
    static int getBloodGroupIndex(String group) {

        for (int i = 0; i < bloodGroups.length; i++) {

            if (bloodGroups[i].equals(group)) {
                return i;
            }
        }

        return -1;
    }
}