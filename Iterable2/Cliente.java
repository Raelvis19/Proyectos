

public abstract class Cliente {
    private String Nombre;
    private int id;

    public Cliente(int id , String nombre) {
        this.Nombre = nombre;
        this.id = id;
    }
    public String getNombre(){
        return Nombre;
    }
    public int getId(){
        return id;
    }

    public abstract double calcularDescuento(double subtotal);
    
}
