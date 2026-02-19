
import java.util.Scanner;

public class Sistema {
    Producto[] productos = new Producto[10];
    Cliente[] clientes = new Cliente[10];
    Pedido[] pedidos;
    int indiceProductos = 0;
    int indiceClientes = 0;
    int indicePedidos = 0;

    
    public void menu(){
        Scanner sc = new Scanner(System.in);
        int opcion;

    

        do {
            System.out.println("\n---- SISTEMA DE PEDIDOS ----");
            System.out.println("1. Registrar producto");
            System.out.println("2. Registrar cliente");
            System.out.println("3. Crear pedido");
            System.out.println("4. Agregar producto a pedido");
            System.out.println("5. Ver detalle de pedido");
            System.out.println("6. Listar productos");
            System.out.println("7. Listar pedidos");
            System.out.println("8. Cambiar estado de pedido");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                 System.out.println("---- REGISTRAR PRODUCTO ----");

                 System.out.print("ID: ");
                 int id = sc.nextInt();
                 sc.nextLine(); // limpiar buffer

                 System.out.print("Nombre: ");
                 String nombre = sc.nextLine();

                 System.out.print("Precio: ");
                 double precio = sc.nextDouble();

                 System.out.print("Stock: ");
                 int stock = sc.nextInt();

                 registrarProducto(id, nombre, precio, stock);
                 break;

                case 2:
                    System.out.println("---Registrar cliente---");
                    System.out.println("Ingrese el id del cliente:");
                    int id4 = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Ingrese el nombre del cliente:");
                    String nombre3 = sc.nextLine();
                    System.out.println("Ingrese el tipo de Cliente:");
                    System.out.println("1. Regular ");
                    System.out.println("2. VIP");
                    int tipo5 = sc.nextInt();
                    if(tipo5 == 1){
                        registrarClienteRegular(id4, nombre3);
                    } else if(tipo5 == 2){
                        System.out.println("Ingrese el descuento");
                        double descuento5 = sc.nextDouble();
                        registrarClienteVIP(id4, nombre3, descuento5);
                    }else{
                        System.out.println("Tipo de cliente invalido");
                    }
                    
                    break;

                case 3:
                 System.out.println("--- CREAR PEDIDO ---");

                 System.out.print("ID del pedido: ");
                 int idPedido = sc.nextInt();

                 System.out.print("ID del cliente: ");
                 int idCliente = sc.nextInt();

                 System.out.print("Cantidad máxima de productos del pedido: ");
                 int maxDetalles = sc.nextInt();

                 crearPedido(idPedido, idCliente, maxDetalles);
                    break;

                case 4:
                 System.out.println("--- AGREGAR PRODUCTO A PEDIDO ---");

                 System.out.print("ID del pedido: ");
                 int idPedido2 = sc.nextInt();

                 System.out.print("ID del producto: ");
                 int idProducto = sc.nextInt();

                 System.out.print("Cantidad: ");
                 int cantidad = sc.nextInt();

                 agregarProductoAPedido(idPedido2, idProducto, cantidad);
                
                 break;

                case 5:

                 System.out.println("--- VER DETALLE DE PEDIDO ---");

                 System.out.print("ID del pedido: ");
                 int idPedido3 = sc.nextInt();

                 verDetallePedido(idPedido3);
                 break;

                case 6:
                    listarProductos();
                    break;

                case 7:
                    listarPedidos();
                    break;

                case 8:
                    System.out.println("--- CAMBIAR ESTADO DE PEDIDO ---");

                 System.out.print("ID del pedido: ");
                 int idPedido4 = sc.nextInt();

                 System.out.println("Nuevo estado:");
                 System.out.println("1. Confirmar");
                 System.out.println("2. Cancelar");
                 int opEstado = sc.nextInt();

                 if (opEstado == 1) {
                 cambiarEstadoPedido(idPedido4, EstadoPedido.CONFIRMADO);
                 } 
                 else if (opEstado == 2) {
                 cambiarEstadoPedido(idPedido4, EstadoPedido.CANCELADO);
                 } 
                 else {
                 System.out.println("Opción inválida");
                 }
                 break;

                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opcion invalida");
            }

        } while (opcion != 0);

        sc.close();
    
    }

   public void registrarProducto(int id, String nombre, double precio, int stock) {
    try {
        Producto nuevo = new Producto(id, nombre, precio, stock);
        productos[indiceProductos] = nuevo;
        indiceProductos++;
        System.out.println("Producto registrado");
    } catch (IllegalArgumentException e) {
        System.out.println("Error: " + e.getMessage());
    }

    }

    public void registrarClienteRegular(int id , String Nombre){
        if (indiceClientes >= clientes.length) {
            System.out.println("No hay espacio para mas clientes");
            
        }
        try{
            ClienteRegular nuevo1 = new ClienteRegular(id, Nombre);
            clientes[indiceClientes] = nuevo1;
            indiceClientes++;
            System.out.println("Cliente Resgistrado");

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void registrarClienteVIP(int id , String Nombre , double descuento){
        if (indiceClientes >= clientes.length) {
            System.out.println("No hay espacio para mas clientes");
            
        }
        try{
            ClienteVIP nuevo1 = new ClienteVIP(id, Nombre, descuento);
            clientes[indiceClientes] = nuevo1;
            indiceClientes++;
            System.out.println("Cliente Resgistrado");

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void crearPedido(int idPedido, int idCliente , int maxDetalles) {

        if (indicePedidos >= pedidos.length) {
        System.out.println("No hay espacio para más pedidos");
        return;
        }

        // Buscar cliente
        Cliente clienteEncontrado = null;

     for (int i = 0; i < indiceClientes; i++) {
        if (clientes[i].getId() == idCliente) {
            clienteEncontrado = clientes[i];
            break;
        }
    }

        if (clienteEncontrado == null) {
        System.out.println("Cliente no existe");
        return;
        }

        Pedido nuevoPedido = new Pedido(idPedido, clienteEncontrado , maxDetalles);
        pedidos[indicePedidos] = nuevoPedido;
        indicePedidos++;

        System.out.println("Pedido creado en estado BORRADOR");
    }

    public void agregarProductoAPedido(int idPedido, int idProducto, int cantidad) {

        Pedido pedidoEncontrado = null;
        Producto productoEncontrado = null;

        // Buscar pedido
        for (int i = 0; i < indicePedidos; i++) {
        if (pedidos[i].getId() == idPedido) {
            pedidoEncontrado = pedidos[i];
            break;
        }
        }

        if (pedidoEncontrado == null) {
        System.out.println("Pedido no existe");
        return;
        }

        // Buscar producto
        for (int i = 0; i < indiceProductos; i++) {
        if (productos[i].getId() == idProducto) {
            productoEncontrado = productos[i];
            break;
        }
        }

        if (productoEncontrado == null) {
        System.out.println("Producto no existe");
        return;
        }

        // Validar stock
        if (productoEncontrado.getStock() < cantidad) {
        System.out.println("Stock insuficiente");
        return;
        }

        
        pedidoEncontrado.agregarProducto(productoEncontrado, cantidad);

        System.out.println("Producto agregado al pedido");
        }

    public void verDetallePedido(int idPedido) {

        Pedido pedidoEncontrado = null;

        for (int i = 0; i < indicePedidos; i++) {
        if (pedidos[i].getId() == idPedido) {
            pedidoEncontrado = pedidos[i];
            break;
        }
        }

        if (pedidoEncontrado == null) {
        System.out.println("Pedido no existe");
        return;
        }

        pedidoEncontrado.mostrarDetalle();
        }

    public void listarProductos() {

        if (indiceProductos == 0) {
        System.out.println("No hay productos registrados");
        return;
        }

        System.out.println("---- LISTA DE PRODUCTOS ----");

        for (int i = 0; i < indiceProductos; i++) {

        Producto p = productos[i];

        System.out.println(
            "ID: " + p.getId() +
            " | Nombre: " + p.getNombre() +
            " | Precio: " + p.getPrecio() +
            " | Stock: " + p.getStock()
        );
        }
    }

    public void listarPedidos() {

        if (indicePedidos == 0) {
        System.out.println("No hay pedidos registrados");
        return;
        }

     System.out.println("---- LISTA DE PEDIDOS ----");

     for (int i = 0; i < indicePedidos; i++) {

        Pedido p = pedidos[i];

        System.out.println(
            "ID: " + p.getId() +
            " | Cliente: " + p.getCliente().getNombre() +
            " | Estado: " + p.getEstado() +
            " | Total: " + p.calcularTotal()
        );
        }
    }

    public void cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado) {

        Pedido pedidoEncontrado = null;

     for (int i = 0; i < indicePedidos; i++) {
        if (pedidos[i].getId() == idPedido) {
            pedidoEncontrado = pedidos[i];
            break;
        }
        }

        if (pedidoEncontrado == null) {
        System.out.println("Pedido no existe");
        return;
     }

     pedidoEncontrado.cambiarEstado(nuevoEstado);
    }






}


    

