package Proyectos.GestionDePedido;

public class Pedido {

    private int id;
    public Cliente getCliente() {
        return cliente;
    }
    private Cliente cliente;
    private EstadoPedido estado;
    private DetallePedido[] detalles;
    private int indice;

    public Pedido(int id, Cliente cliente, int maxDetalles) {
        this.id = id;
        this.cliente = cliente;
        this.estado = EstadoPedido.BORRADOR;
        this.detalles = new DetallePedido[maxDetalles];
        this.indice = 0;
    }

    public void agregarProducto(Producto p, int cant) {
        if (estado != EstadoPedido.BORRADOR) return;
        detalles[indice++] = new DetallePedido(p, cant);
    }

    public double calcularSubtotal() {
        double total = 0;
        for (DetallePedido d : detalles) {
            if (d != null) total += d.getSubtotal();
        }
        return total;
    }

    public double calcularDescuento() {
        return cliente.calcularDescuento(calcularSubtotal());
    }

    public double calcularTotal() {
        return calcularSubtotal() - calcularDescuento();
    }

    public void confirmar() {
        if (indice == 0) return;

        for (DetallePedido d : detalles) {
            if (d != null) {
                d.getProducto().disminirStock(d.getCantidad());
            }
        }
        estado = EstadoPedido.CONFIRMADO;
    }

    public void cancelar() {
        if (estado == EstadoPedido.CONFIRMADO) {
            for (DetallePedido d : detalles) {
                if (d != null) {
                    d.getProducto().aumentarStock(d.getCantidad());
                }
            }
        }
        estado = EstadoPedido.CANCELADO;
    }

    public int getId(){ 
        return id;
    }
    public EstadoPedido getEstado(){
        return estado;
    }
    public void mostrarDetalle() {

    System.out.println("ID Pedido: " + id);
    System.out.println("Cliente: " + cliente.getNombre());
    System.out.println("Estado: " + estado);

    System.out.println("---- Productos ----");

    for (int i = 0; i < indice; i++) {
        DetallePedido d = detalles[i];
        System.out.println(
            d.getProducto().getNombre() +
            " x" + d.getCantidad() +
            " = " + d.getSubtotal()
        );
    }

    System.out.println("Subtotal: " + calcularSubtotal());
    System.out.println("Descuento: " + calcularDescuento());
    System.out.println("Total: " + calcularTotal());
    }
    public void cambiarEstado(EstadoPedido nuevoEstado) {

    if (estado == nuevoEstado) {
        System.out.println("Ya tiene ese estado");
        return;
    }

    // CONFIRMAR PEDIDO
    if (nuevoEstado == EstadoPedido.CONFIRMADO) {

        // verificar stock
        for (int i = 0; i < indice; i++) {
            DetallePedido d = detalles[i];
            if (d.getProducto().getStock() < d.getCantidad()) {
                System.out.println("Stock insuficiente");
                return;
            }
        }

        // descontar stock
        for (int i = 0; i < indice; i++) {
            DetallePedido d = detalles[i];
            d.getProducto().disminirStock(d.getCantidad());
        }

        estado = EstadoPedido.CONFIRMADO;
        System.out.println("Pedido confirmado");
    }

    // CANCELAR PEDIDO
    else if (nuevoEstado == EstadoPedido.CANCELADO) {

        if (estado == EstadoPedido.CONFIRMADO) {
            // devolver stock
            for (int i = 0; i < indice; i++) {
                DetallePedido d = detalles[i];
                d.getProducto().aumentarStock(d.getCantidad());
            }
        }

        estado = EstadoPedido.CANCELADO;
        System.out.println("Pedido cancelado");
    }
    }


}
