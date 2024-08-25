package ASM2;

public interface AbstractOder<T> {
    void offer( T element , int quantity);
    T poll( );
    T peek( );
    int size( );
    boolean isEmpty( );
}
