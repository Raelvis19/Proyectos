public class ProductoNoEncontradoException extends Exception{
    public ProductoNoEncontradoException(String Mensaje){
        super("Producto No encontrado");
    }
}