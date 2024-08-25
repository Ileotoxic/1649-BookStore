package ASM2;

public interface AbstractBookList {
    boolean add(Book book);
    Book remove(int index);
    Book get(int index);
    Book set(int index, Book book);
    int size();
    int indexOf(Book book);
    boolean contains(Book book);
    boolean isEmpty();
}
