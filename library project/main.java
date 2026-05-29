import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//ui.java

class Main{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        LibraryService libraryService = new LibraryService();
        Map<String, User> userMap = new HashMap<>();

        User user = new User("Alice", "1001");User u2 = new User("Bob","1002");User u3 = new User("Charlie","1003");
        userMap.put("1001", user); userMap.put("1002",u2); userMap.put("1003",u3);

        AccessMode mode = AccessMode.LOG_IN_PAGE;

        while(mode == AccessMode.LOG_IN_PAGE){
            System.out.println("enter log in id : ");
            String userInput = sc.nextLine();
            if(userMap.containsKey(userInput)){
                user = userMap.get(userInput);
                mode = AccessMode.USER;
            }else if(userInput.equals("admin")){
                mode = AccessMode.ADMIN;
            }else if(userInput.equals("EXIT")){
                mode = AccessMode.EXIT;
                break;
            }else{
                System.out.println("Invalid input");
                continue;
            }

            while(mode == AccessMode.ADMIN){
                System.out.print("""
                1)Add a book to inventory (qty, id, title)
                2)Search for a book (id)
                3)Exit

                Enter option number :""");
                int option = sc.nextInt();
                sc.nextLine();
                String isbn;

                switch(option){
                    case 1:
                        int qty = sc.nextInt();
                        sc.nextLine();
                        isbn = sc.nextLine();
                        String title = sc.nextLine();
                        libraryService.add(qty, title, isbn);
                        break;
                    case 2:
                        isbn = sc.nextLine();
                        libraryService.search(isbn);
                        break;
                    case 3:
                        mode = AccessMode.LOG_IN_PAGE;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            }
            while(mode == AccessMode.USER){
                System.out.print("""
                1)Borrow book
                2)Return book
                3)Print borrowed books
                4)Exit

                Enter option number :""");
                int option = sc.nextInt();
                sc.nextLine();
                String isbn;

                switch(option){
                    case 1:
                        isbn = sc.nextLine();
                        libraryService.borrow(isbn, user);
                        break;
                    case 2:
                        isbn = sc.nextLine();
                        String input = sc.nextLine();
                        libraryService.returnBook(isbn, input, user);
                        break;
                    case 3:
                        List<Loan> userLoanList = user.getLoanList();
                        for(Loan i : userLoanList){
                            i.printDetails();
                        }
                        break;
                    case 4:
                        mode = AccessMode.LOG_IN_PAGE;
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;

                }
            }
        }
    }
}

public enum AccessMode{
    USER,
    ADMIN,
    LOG_IN_PAGE,
    EXIT
}

//services.java

class LibraryService{
    private static Map<String, Book> inventory = new HashMap<>();

    public static void add(int qty, String title, String isbn){


        if(qty < 1){
            System.out.println("Invalid quantity");
            return;
        }

        if(inventory.containsKey(isbn)){
            Book temp = inventory.get(isbn);
            temp.increaseQuantity(qty);
            System.out.println(temp.getQuantity() + " copies of " + title + " is in the inventory.\n");
            return;
        }

        Book newBook = new Book(isbn, title, qty);
        inventory.put(isbn, newBook);
        System.out.println(qty + " copies of " + title + " has been added to the inventory.\n");
    }

    public static void search(String isbn){

        if(inventory.containsKey(isbn)){
            Book temp = inventory.get(isbn);
            System.out.println(temp.getQuantity() + " copies of " + temp.getTitle() + " is available in the inventory.\n");
        }else{
            System.out.println(isbn + " not found.");
        }
    }

    public static void borrow(String isbn, User user){
        if(inventory.containsKey(isbn) && inventory.get(isbn).getQuantity()>0){
            if(user.containsLoan(isbn)&& user.getLoan(isbn).status() == LoanStatus.BORROWED){
                System.out.println("Book already borrowed" + user.getLoan(isbn).status());
                return;
            }else{
                inventory.get(isbn).borrowBook();

                Loan temp = new Loan(inventory.get(isbn));

                user.putLoan(isbn, temp);
            }
        }else{
            System.out.println("Book Unavailable\n");
        }
    }

    public static void returnBook(String isbn, String input, User user){
        LocalDate returnDateInput = LocalDate.parse(input);

        if(user.containsLoan(isbn)){
            inventory.get(isbn).returnBook();
            long fine = user.getLoan(isbn).late(returnDateInput);

            if(fine > 0){
                user.getLoan(isbn).setLoanStatus(LoanStatus.OVERDUE);
                System.out.println("Pay a fine of $" + fine);
            }else{
                System.out.println("No fines");
                user.getLoan(isbn).setLoanStatus(LoanStatus.RETURNED);
            }
        }else{
            System.out.println("Book Unavailable\n");
        }
    }

}

//models.java

public enum LoanStatus{
    BORROWED,
    RETURNED,
    OVERDUE
}

class User{
    private String name;
    private final String id;
    private Map<String, Loan> loanMap = new HashMap<>();

    public List<Loan> getLoanList(){
        List<Loan> loanList = new ArrayList<>(loanMap.values());
        return loanList;
    }

    public User(String name, String Id){
        this.name = name; this.id = Id;
    }

    public boolean containsLoan(String isbn){
        return loanMap.containsKey(isbn);
    }
    public Loan getLoan(String isbn){
        if(loanMap.containsKey(isbn)){
            return loanMap.get(isbn);
        }
        else return null;
    }
    public void putLoan(String isbn, Loan newLoan){
        loanMap.put(isbn, newLoan);
    }
}

class Loan {
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus loanStatus;
    private long fine;


    public Loan(Book book) {
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14);
        this.returnDate = null;
        this.loanStatus = LoanStatus.BORROWED;
        System.out.println(loanStatus);
    }
    public LoanStatus status(){
        return loanStatus;
    }
    public void setLoanStatus(LoanStatus newStatus){
        loanStatus = newStatus;
    }

    public long late(LocalDate returnDate){
        this.returnDate = returnDate;
        long daysLate =
            ChronoUnit.DAYS.between(
                dueDate,
                returnDate
            );
        fine = daysLate * 30;
        if(fine<0)return 0;
        return fine;
    }

    public void printDetails(){
        System.out.println(book.getTitle() + " " + book.getISBN() + " " + borrowDate + " " + (loanStatus==LoanStatus.BORROWED ? "null" : returnDate));
    }
}


class Book{
    private String title;
    private int quantity;
    private final String isbn;

    public String getTitle(){
        return this.title;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public void increaseQuantity(int q){
        this.quantity += q;
    }

    public void borrowBook(){
        quantity--;
    }
    public void returnBook(){
        quantity++;
    }
    public String getISBN(){
        return this.isbn;
    }
    public Book(String isbn, String title, int quantity){
        this.title = title;
        this.quantity = quantity;
        this.isbn = isbn;
    }
}
