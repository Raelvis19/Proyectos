

public class Producto{
    private int id;
    private String nombre;
    private double precio;
    private int stock;
    
    public Producto(int id, String nombre, double precio, int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio no puede ser menor que 0");
        }
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public double getPrecio(){
        return precio;
    }
    public int getStock(){
        return stock;
    }

    public void aumentarStock(int cant){
        stock += cant;
    }

    public void disminirStock(int cant){
        if (cant <= stock) {
            stock -= cant;
        }
    }


    


} 