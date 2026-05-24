import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

class Main{
    public static void add(Scanner sc, Map<String,Integer> inventory){
        String name = sc.nextLine();
        int qty = sc.nextInt();
        sc.nextLine();
        if(inventory.containsKey(name)){
            qty += inventory.get(name);
        }
        inventory.put(name, qty);
        System.out.println(qty + " copies of " + name + " has been added to the inventory.\n");
    }

    public static void search(Scanner sc, Map<String,Integer> inventory){
        String name = sc.nextLine();
        if(inventory.containsKey(name)){
            System.out.println(inventory.get(name) + " copies of " + name + " is available in the inventory.\n");
        }else{
            System.out.println(name + " not found.");
        }
    }

    public static void borrow(Scanner sc, Map<String, Integer> borrowedBooks, Map<String,Integer> inventory){
        String name = sc.nextLine();
        int days = sc.nextInt();
        sc.nextLine();

        if(inventory.containsKey(name) && inventory.get(name)>0){
            if(borrowedBooks.containsKey(name)){
                System.out.println("Book already borrowed.");
                return;
            }
            int qty = inventory.get(name) - 1;
            inventory.put(name, qty);
            borrowedBooks.put(name, days);
        }else{
            System.out.println("Book Unavailable\n");
        }
    }

    public static void returnBook(Scanner sc, Map<String, Integer> borrowedBooks, Map<String,Integer> inventory){
        String name = sc.nextLine();
        int days = sc.nextInt();
        sc.nextLine();
        int due = borrowedBooks.get(name);

        if(borrowedBooks.containsKey(name)){
            int qty = inventory.get(name) + 1;
            inventory.put(name, qty);
            borrowedBooks.remove(name);
            if(days > due){
                int fine = (days -due)*30;
                System.out.println("Pay a fine of $" + fine);
            }
            else System.out.println("No fines");
        }else{
            System.out.println("Book Unavailable\n");
        }
    }


    public static void main(String[] args){
        Map<String, Integer> inventory = new HashMap<>();
        Map<String, Integer> borrowedBooks = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        boolean loop = true;
        while(loop){
            System.out.print("""
            1)Add a book to inventory (name, qty)
            2)Search for a book (name)
            3)Borrow book (name, no. of days until due date)
            4)Return book (name, no. of days after borrow)
            5)Exit

            Enter option number :""");
            int option = sc.nextInt();
            sc.nextLine();

            switch(option){
                case 1:
                    add(sc, inventory);
                    break;
                case 2:
                    search(sc, inventory);
                    break;
                case 3:
                    borrow(sc, borrowedBooks,inventory);
                    break;
                case 4:
                    returnBook(sc, borrowedBooks, inventory);
                    break;
                case 5:
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;


            }

        }
    }
}
