package Proyectos.GestionDePedido;

public class ClienteVIP extends Cliente{
    private double porcentaje;

    public ClienteVIP(int id, String nombre, double porcentaje) {
        super(nombre, id);
        this.porcentaje = porcentaje;
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return subtotal * porcentaje;
    }
}
    

