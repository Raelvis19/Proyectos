package Proyectos.GestionDePedido;

public class ClienteRegular extends Cliente {

    public ClienteRegular(int id , String nombre) {
        super(id , nombre);
    }

    @Override
    public double calcularDescuento(double subtotal) {
        return 0;
    }
}

