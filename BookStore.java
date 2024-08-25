package ASM2;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Book {
    private int id;
    private String title;
    private String author;
    private double price;
    private int quantity;

    public Book(int id, String title, String author, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "\nBook{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

//Oder
class Order<T> implements AbstractOder<T> {
    private int id;
    private String customerName;
    private String customerAddress;
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private class Node<T> {
        private T element;
        private Node<T> next;
        private int quantity;

        public Node(T element, int quantity) {
            this.element = element;
            this.next = null;
            this.quantity = quantity;
        }
    }

    public Order(int id, String customerName, String customerAddress) {
        this.id = id;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public double getOrderPrice() {
        double totalPrice = 0;
        Node<T> current = head;
        while (current != null) {
            totalPrice += ((Book) current.element).getPrice() * current.quantity;
            current = current.next;
        }
        return totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    @Override
    public void offer(T element, int quantity) {
        Node<T> current = head;
        while (current != null) {
            if (current.element.equals(element)) {
                current.quantity += quantity; // Cập nhật số lượng nếu sách đã có trong đơn hàng
                return;
            }
            current = current.next;
        }
        Node<T> newNode = new Node<>(element, quantity);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public boolean remove(T element) {
        if (isEmpty()) {
            return false;
        }

        if (head.element.equals(element)) {
            poll(); // Remove the first element
            return true;
        }

        Node<T> current = head;
        while (current.next != null) {
            if (current.next.element.equals(element)) {
                current.next = current.next.next;
                if (current.next == null) {
                    tail = current; // Update tail if needed
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public T poll() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T oldNodeValue = head.element;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return oldNodeValue;
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return head.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public String toString() {
        StringBuilder results = new StringBuilder();
        results.append("Order{")
                .append("id=").append(id)
                .append(", customerName='").append(customerName).append('\'')
                .append(", customerAddress='").append(customerAddress).append('\'')
                .append(", bookCart=");

        Node<T> tempNode = head;
        while (tempNode != null) {
            results.append(tempNode.element).append(" (Quantity buy: ").append(tempNode.quantity).append(")");
            if (tempNode.next != null) {
                results.append(", ");
            }
            tempNode = tempNode.next;
        }

        results.append('}');
        return results.toString();
    }
}

// BookList
class BookList implements AbstractBookList {
    private static final int DEFAULT_CAPACITY = 5;
    private Book[] elements;
    private int size = 0;

    public BookList() {
        elements = new Book[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(Book book) {
        if (size == elements.length) {
            resize(elements.length * 2);
        }
        elements[size] = book;
        size++;
        return true;
    }

    @Override
    public Book remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bound: " + index);
        }

        Book oldElement = elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        size--;
        elements[size] = null;

        if (size > 0 && size < elements.length / 3) {
            resize(elements.length / 2);
        }

        return oldElement;
    }

    @Override
    public Book get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bound: " + index);
        }
        return elements[index];
    }

    @Override
    public Book set(int index, Book book) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bound: " + index);
        }

        Book oldElement = elements[index];
        elements[index] = book;
        return oldElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int indexOf(Book book) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(book)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Book book) {
        return indexOf(book) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void resize(int newCapacity) {
        Book[] newElements = new Book[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
    }

    @Override
    public String toString() {
        StringBuilder results = new StringBuilder();
        results.append("[");

        for (int i = 0; i < size; i++) {
            results.append(elements[i]);
            if (i < size - 1) {
                results.append(", ");
            }
        }

        results.append("]");
        return results.toString();
    }
    // Bubble sort by ID
    public void sortById() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (elements[j].getId() > elements[j + 1].getId()) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Bubble sort by title
    public void sortByTitle() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (elements[j].getTitle().compareTo(elements[j + 1].getTitle()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Bubble sort by author
    public void sortByAuthor() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (elements[j].getAuthor().compareTo(elements[j + 1].getAuthor()) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Bubble sort by price
    public void sortByPrice() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (elements[j].getPrice() > elements[j + 1].getPrice()) {
                    swap(j, j + 1);
                }
            }
        }
    }

    // Swap elements at indices i and j
    private void swap(int i, int j) {
        Book temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }
}

// main
public class BookStore {
    private static List<Order<Book>> orders = new ArrayList<>();
    private static int nextOrderId = 1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BookList bookList = new BookList();
        bookList.add(new Book(1, "Book One", "Author A", 29.99, 10));
        bookList.add(new Book(6, "Book Six", "Author A", 39.99, 7));
        bookList.add(new Book(2, "Book Two", "Author B", 19.99, 5));
        bookList.add(new Book(3, "Book Three", "Author B", 39.99, 8));
        bookList.add(new Book(4, "Book Four", "Author C", 39.99, 9));
        bookList.add(new Book(7, "Book Five", "Author C", 39.99, 4));
        bookList.add(new Book(9, "Book Five", "Author C", 39.99, 4));
        bookList.add(new Book(15, "Book Five", "Author C", 39.99, 4));
        bookList.add(new Book(10, "Book Five", "Author C", 39.99, 4));

        Order<Book> currentOrder = null;

        int choice;

        do {
            System.out.println("----- BOOK STORE MENU -----");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Search Book");
            System.out.println("4. Sort Books");
            System.out.println("5. Place Order");
            System.out.println("6. Search Order");
            System.out.println("7. Process Order");
            System.out.println("8. Display Orders");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            long startTime = System.currentTimeMillis(); // Thời gian bắt đầu

            switch (choice) {
                case 1:
                    boolean addingBooks = true;
                    while (addingBooks) {
                        int id;
                        boolean idExists;

                        do {
                            System.out.print("Enter book ID: ");
                            id = scanner.nextInt();
                            scanner.nextLine();

                            idExists = false;
                            for (int i = 0; i < bookList.size(); i++) {
                                if (bookList.get(i).getId() == id) {
                                    idExists = true;
                                    break;
                                }
                            }

                            if (idExists) {
                                System.out.println("ID already exists. Please enter a different ID.");
                            }
                        } while (idExists);

                        System.out.print("Enter book title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter book author: ");
                        String author = scanner.nextLine();
                        System.out.print("Enter book price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter book quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        bookList.add(new Book(id, title, author, price, quantity));
                        System.out.println("Book added successfully!");

                        System.out.print("Do you want to add another book? (yes/no): ");
                        addingBooks = scanner.nextLine().equalsIgnoreCase("yes");
                    }
                    break;


                case 2:
                    System.out.println("Books in store: " + bookList);
                    System.out.println("Enter 1 to return to menu");
                    scanner.nextInt();
                    scanner.nextLine();
                    break;

                case 3:
                    System.out.print("Enter book ID to search: ");
                    int searchId = scanner.nextInt();
                    scanner.nextLine();
                    Book foundBook = null;
                    for (int i = 0; i < bookList.size(); i++) {
                        Book book = bookList.get(i);
                        if (book.getId() == searchId) {
                            foundBook = book;
                            break;
                        }
                    }
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook);
                    } else {
                        System.out.println("Book not found with ID: " + searchId);
                    }
                    break;

                case 4:
                    System.out.println("Sort books by:");
                    System.out.println("1. Title");
                    System.out.println("2. Author");
                    System.out.println("3. Price");
                    System.out.println("4. ID");
                    System.out.print("Enter your choice: ");
                    int sortChoice = scanner.nextInt();
                    scanner.nextLine();

                    switch (sortChoice) {
                        case 1:
                            bookList.sortByTitle();
                            System.out.println("Books sorted by title: " + bookList);
                            break;

                        case 2:
                            bookList.sortByAuthor();
                            System.out.println("Books sorted by author: " + bookList);
                            break;

                        case 3:
                            bookList.sortByPrice();
                            System.out.println("Books sorted by price: " + bookList);
                            break;

                        case 4:
                            bookList.sortById();
                            System.out.println("Books sorted by ID: " + bookList);
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;

                case 5:
                    // Tạo đơn hàng với ID tự động
                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter Customer Address: ");
                    String customerAddress = scanner.nextLine();

                    currentOrder = new Order<>(nextOrderId, customerName, customerAddress);
                    placeOrder(scanner, bookList, currentOrder);
                    orders.add(currentOrder); // Thêm đơn hàng vào danh sách đơn hàng
                    nextOrderId++; // Tăng ID đơn hàng cho lần tạo tiếp theo
                    System.out.println("Order placed successfully!");
                    break;

                case 6:
                    searchOrder(bookList);
                    System.out.println("Enter 1 to return to menu");
                    scanner.nextInt();
                    scanner.nextLine();
                    break;

                case 7:
                    // Xử lý đơn hàng
                    if (currentOrder != null) {
                        System.out.println("Processing order: " + currentOrder);
                        currentOrder = null;
                    } else {
                        System.out.println("No order to process.");
                    }
                    break;

                case 8:
                    if (orders.isEmpty()) {
                        System.out.println("No orders available.");
                    } else {
                        System.out.println("Display orders by:");
                        System.out.println("1. Order ID");
                        System.out.println("2. Order Price");
                        System.out.print("Enter your choice: ");
                        int displayChoice = scanner.nextInt();
                        scanner.nextLine();

                        switch (displayChoice) {
                            case 1:
                                bubbleSortOrdersById();
                                break;
                            case 2:
                                bubbleSortOrdersByPrice();
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }

                        // Hiển thị các đơn hàng sau khi sắp xếp
                        for (Order<Book> order : orders) {
                            System.out.println(order); // Gọi phương thức toString() của Order để in thông tin đơn hàng
                        }
                    }
                    System.out.println("Enter 1 to return to menu");
                    scanner.nextInt();
                    scanner.nextLine();
                    break;



                case 9:
                    System.out.println("Exiting the application.");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            long endTime = System.currentTimeMillis(); // Thời gian kết thúc
            long duration = endTime - startTime; // Thời gian thực thi
            System.out.println("Time taken: " + (duration) + " ms");

        } while (choice != 9);

        scanner.close();
    }

    private static void placeOrder(Scanner scanner, BookList bookList, Order<Book> order) {
        System.out.println("Available books: " + bookList);
        boolean keepAdding = true;

        while (keepAdding) {
            System.out.print("Enter the book ID to add to order (or -1 to finish): ");
            int bookId = scanner.nextInt();
            scanner.nextLine();
            if (bookId == -1) {
                keepAdding = false;
            } else {
                Book selectedBook = null;
                for (int i = 0; i < bookList.size(); i++) {
                    if (bookList.get(i).getId() == bookId) {
                        selectedBook = bookList.get(i);
                        break;
                    }
                }
                if (selectedBook != null) {
                    System.out.print("Enter quantity to add to order: ");
                    int quantityToAdd = scanner.nextInt();
                    scanner.nextLine();

                    if (quantityToAdd > selectedBook.getQuantity()) {
                        System.out.println("Insufficient number of books available.");
                    } else {
                        // Giảm số lượng sách trong danh sách sách
                        selectedBook.setQuantity(selectedBook.getQuantity() - quantityToAdd);

                        // Thêm sách vào đơn hàng với số lượng cụ thể
                        order.offer(selectedBook, quantityToAdd);
                        System.out.println("Book added to cart: " + selectedBook + " with quantity: " + quantityToAdd);
                    }
                } else {
                    System.out.println("Book not found.");
                }
            }
        }
    }


    private static void searchOrder(BookList bookList) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search by:");
        System.out.println("   a) Order ID");
        System.out.println("   b) Customer Name");
        System.out.println("   c) Customer Address");
        System.out.print("Choose search option (a/b/c): ");
        char searchOrderChoice = scanner.next().charAt(0);
        scanner.nextLine();

        List<Order<Book>> foundOrders = new ArrayList<>(); // Danh sách lưu trữ đơn hàng tìm thấy

        switch (searchOrderChoice) {
            case 'a':
                System.out.print("Enter Order ID to search: ");
                int orderId = scanner.nextInt();
                scanner.nextLine();
                for (Order<Book> order : orders) {
                    if (order.getId() == orderId) {
                        foundOrders.add(order);
                    }
                }
                break;
            case 'b':
                System.out.print("Enter Customer Name to search: ");
                String customerName = scanner.nextLine();
                for (Order<Book> order : orders) {
                    if (order.getCustomerName().equalsIgnoreCase(customerName)) {
                        foundOrders.add(order);
                    }
                }
                break;
            case 'c':
                System.out.print("Enter Customer Address to search: ");
                String customerAddress = scanner.nextLine();
                for (Order<Book> order : orders) {
                    if (order.getCustomerAddress().equalsIgnoreCase(customerAddress)) {
                        foundOrders.add(order);
                    }
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (foundOrders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("Found Orders:");
            for (Order<Book> order : foundOrders) {
                System.out.println(order);
            }

            System.out.print("Enter Order ID to edit: ");
            int editOrderId = scanner.nextInt();
            scanner.nextLine();
            Order<Book> orderToEdit = null;
            for (Order<Book> order : foundOrders) {
                if (order.getId() == editOrderId) {
                    orderToEdit = order;
                    break;
                }
            }

            if (orderToEdit != null) {
                editOrder(scanner, orderToEdit, bookList); // Pass the current book list
            } else {
                System.out.println("Order ID not found.");
            }
        }
    }
    private static void bubbleSortOrdersById() {
        for (int i = 0; i < orders.size() - 1; i++) {
            for (int j = 0; j < orders.size() - i - 1; j++) {
                if (orders.get(j).getId() > orders.get(j + 1).getId()) {
                    Order<Book> temp = orders.get(j);
                    orders.set(j, orders.get(j + 1));
                    orders.set(j + 1, temp);
                }
            }
        }
    }

    private static void bubbleSortOrdersByPrice() {
        for (int i = 0; i < orders.size() - 1; i++) {
            for (int j = 0; j < orders.size() - i - 1; j++) {
                if (orders.get(j).getOrderPrice() > orders.get(j + 1).getOrderPrice()) {
                    Order<Book> temp = orders.get(j);
                    orders.set(j, orders.get(j + 1));
                    orders.set(j + 1, temp);
                }
            }
        }
    }


    private static void editOrder(Scanner scanner, Order<Book> order, BookList bookList) {
        boolean editing = true;
        while (editing) {
            System.out.println("Edit Order Menu:");
            System.out.println("1. Add Book to Order");
            System.out.println("2. Remove Book from Order");
            System.out.println("3. View Order Details");
            System.out.println("4. Return to Search Menu");
            System.out.print("Enter your choice: ");
            int editChoice = scanner.nextInt();
            scanner.nextLine();

            switch (editChoice) {
                case 1:
                    System.out.println("Available books: " + bookList);
                    System.out.print("Enter the book ID to add to order: ");
                    int bookId = scanner.nextInt();
                    scanner.nextLine();
                    Book selectedBook = null;
                    for (int i = 0; i < bookList.size(); i++) {
                        if (bookList.get(i).getId() == bookId) {
                            selectedBook = bookList.get(i);
                            break;
                        }
                    }
                    if (selectedBook != null) {
                        System.out.print("Enter quantity to add to order: ");
                        int quantityToAdd = scanner.nextInt();
                        scanner.nextLine();

                        if (quantityToAdd > selectedBook.getQuantity()) {
                            System.out.println("Insufficient number of books available.");
                        } else {
                            // Giảm số lượng sách trong danh sách sách
                            selectedBook.setQuantity(selectedBook.getQuantity() - quantityToAdd);

                            // Thêm sách vào đơn hàng với số lượng cụ thể
                            order.offer(selectedBook, quantityToAdd);
                            System.out.println("Book added to order: " + selectedBook + " with quantity: " + quantityToAdd);
                        }
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter the book ID to remove from order: ");
                    int removeBookId = scanner.nextInt();
                    scanner.nextLine();
                    Book bookToRemove = null;
                    for (int i = 0; i < order.size(); i++) {
                        Book book = (Book) order.peek(); // Duyệt qua các sách trong đơn hàng
                        if (book.getId() == removeBookId) {
                            bookToRemove = book;
                            break;
                        }
                    }
                    if (bookToRemove != null) {
                        boolean removed = order.remove(bookToRemove);
                        if (removed) {
                            System.out.println("Book removed from order: " + bookToRemove);
                        } else {
                            System.out.println("Book not found in the order.");
                        }
                    } else {
                        System.out.println("Book not found.");
                    }
                    break;

                case 3:
                    System.out.println("Order Details: " + order);
                    break;

                case 4:
                    editing = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}