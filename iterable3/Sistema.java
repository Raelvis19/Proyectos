import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Iterator;

public class Sistema {
    
    private List<Producto> lProductos = new ArrayList<>();
    private List<Cliente> lClientes = new ArrayList<>();
    private List<Pedido> lPedidos = new ArrayList<>();
    private Thread hiloProcesador;
    private Thread hiloReportes;
    public int pedidosEnProceso = 0;

    public synchronized int getPedidosEnProceso() {
        return pedidosEnProceso;
    }

    private volatile boolean sistemaActivo = true;

    


    
    public Sistema() {
        
    }

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
            System.out.println("9. Pedidos en procesamiento");
            System.out.println("10. Generar Reporte de sistema");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {

                case 1:
                 System.out.println("---- REGISTRAR PRODUCTO ----");

                 System.out.print("ID: ");
                 int id = sc.nextInt();
                 sc.nextLine(); 

                 System.out.print("Nombre: ");
                 String nombre = sc.nextLine();

                 System.out.print("Precio: ");
                 double precio = sc.nextDouble();

                 System.out.print("Stock: ");
                 int stock = sc.nextInt();

                 registrarProducto(id, nombre, precio, stock);
                 guardarProductos();
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
                    guardarClientes();
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

                 try {
                    agregarProductoAPedido(idPedido2, idProducto, cantidad);
                    System.out.println("Producto agregado correctamente");
                    }
                     catch (ProductoNoEncontradoException |
                     StockInsuficienteException |
                    PedidoInvalidoException e) {

                     System.out.println("Error: " + e.getMessage());
                     }
                     finally {
                     System.out.println("Operación finalizada.\n");
                    }

                     break;

                case 5:

                 System.out.println("--- VER DETALLE DE PEDIDO ---");

                 System.out.print("ID del pedido: ");
                 int idPedido3 = sc.nextInt();
                 try{
                    verDetallePedido(idPedido3);
                    
                 } catch (PedidoInvalidoException e) {
                    System.out.println("Error: " + e.getMessage());
                 }finally{
                    System.out.println("Operacion finalizada");
                 }
                 
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

                 try{
                 if (opEstado == 1) {
                 cambiarEstadoPedido(idPedido4, EstadoPedido.CONFIRMADO);
                 } 
                 else if (opEstado == 2) {
                 cambiarEstadoPedido(idPedido4, EstadoPedido.CANCELADO);
                 } 
                 else {
                 System.out.println("Opción inválida");
                 }
                 }catch(PedidoInvalidoException e){
                    System.out.println("Error: " + e.getMessage());
                 }
                 break;
                case 9:
                 System.out.println(
                    "Pedidos en procesamiento: " + getPedidosEnProceso()
                 );
                    break;
                case 10:
                    generarReporteSistema();
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

    public synchronized void registrarProducto(int id, String nombre, double precio, int stock)
        throws IllegalArgumentException {

    if (!textoValido(nombre)) {
        throw new IllegalArgumentException("El nombre del producto no puede estar vacio.");
    }

    Producto nuevo = new Producto(id, nombre.trim(), precio, stock);
    lProductos.add(nuevo);
    }



    public synchronized void registrarClienteRegular(int id, String nombre)
        throws IllegalArgumentException {

    if (!textoValido(nombre)) {
        throw new IllegalArgumentException("Nombre de cliente invalido.");
    }

    ClienteRegular nuevo = new ClienteRegular(id, nombre.trim());
    lClientes.add(nuevo);
    


}


    public synchronized void registrarClienteVIP(int id , String Nombre , double descuento){
        
        try{
            ClienteVIP nuevo1 = new ClienteVIP(id, Nombre, descuento);
            lClientes.add(nuevo1);
            System.out.println("Cliente Resgistrado");

        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    public synchronized void crearPedido(int idPedido, int idCliente , int maxDetalles) {

        

        
        Cliente clienteEncontrado = buscarClientePorId(idCliente);

    

        if (clienteEncontrado == null) {
        System.out.println("Cliente no existe");
        return;
        }

        Pedido nuevoPedido = new Pedido(idPedido, clienteEncontrado , maxDetalles);
        lPedidos.add(nuevoPedido);

        System.out.println("Pedido creado en estado BORRADOR");
    }

    
    public synchronized void agregarProductoAPedido(int idPedido, int idProducto, int cantidad)
        throws ProductoNoEncontradoException, StockInsuficienteException, PedidoInvalidoException {

    Pedido pedido = buscarPedidoPorId(idPedido);

    if (pedido == null) {
        throw new PedidoInvalidoException("El pedido no existe.");
    }

    Producto producto = buscarProductoPorId(idProducto);

    if (producto.getStock() < cantidad) {
        throw new StockInsuficienteException("Stock insuficiente para el producto " + producto.getNombre());
    }

    pedido.agregarProducto(producto, cantidad);
    }


    public void verDetallePedido(int idPedido)
        throws PedidoInvalidoException {

    Pedido pedido = buscarPedidoPorId(idPedido);

    pedido.mostrarDetalle();
    }

    public void listarProductos() {

        if (lProductos.isEmpty()) {
        System.out.println("No hay productos registrados");
        return;
        }

        System.out.println("-----LISTA DE PRODUCTOS-----");
        for (Producto p : lProductos) {
            System.out.println(p);
            
        }


    }

    public synchronized void listarPedidos() {

        if (lPedidos.isEmpty()) {
        System.out.println("No hay pedidos registrados");
        return;
        }

     System.out.println("---- LISTA DE PEDIDOS ----");

        for (Pedido p : lPedidos) {

    

        System.out.println(
            "ID: " + p.getId() +
            " | Cliente: " + p.getCliente().getNombre() +
            " | Estado: " + p.getEstado() +
            " | Fecha: " + p.getFechaFormateada() +
            " | Total: " + p.calcularTotal()
        );
        }
    }

    public synchronized void cambiarEstadoPedido(int idPedido, EstadoPedido nuevoEstado)
        throws PedidoInvalidoException {

    Pedido pedido = buscarPedidoPorId(idPedido);

    if (nuevoEstado == EstadoPedido.CONFIRMADO) {
        pedido.confirmar(); 
    } else {
        pedido.cambiarEstado(nuevoEstado);
    }

    System.out.println("Estado actualizado");
    }


    private Cliente buscarClientePorId(int idCliente) {

    for (Cliente c : lClientes) {
        if (c.getId() == idCliente) {
            return c;
        }
    }

    return null;
    }

    private Pedido buscarPedidoPorId(int idPedido) throws PedidoInvalidoException {

    for (Pedido p : lPedidos) {
        if (p.getId() == idPedido) {
            return p;
        }
    }

    throw new PedidoInvalidoException("Pedido con ID " + idPedido + " no existe.");
    }


    private Producto buscarProductoPorId(int idProducto)
        throws ProductoNoEncontradoException {

    for (Producto p : lProductos) {
        if (p.getId() == idProducto) {
            return p;
        }
    }

    throw new ProductoNoEncontradoException(
        "Producto con ID " + idProducto + " no existe."
    );
    }


    public synchronized void eliminarProducto(int idProducto) {

    Iterator<Producto> it = lProductos.iterator();

    while (it.hasNext()) {
        Producto p = it.next();

        if (p.getId() == idProducto) {
            it.remove();
            System.out.println("Producto eliminado");
            return;
        }
    }

    System.out.println("Producto no encontrado");
    }

    private boolean textoValido(String texto) {

    if (texto == null) {
        return false;
    }

    if (texto.trim().isEmpty()) {
        return false;
    }

    return true;
    }

    public synchronized void buscarProductoPorNombreParcial(String textoBusqueda) {

    if (!textoValido(textoBusqueda)) {
        System.out.println("Texto de busqueda invalido");
        return;
    }

    textoBusqueda = textoBusqueda.trim().toLowerCase();

    boolean encontrado = false;

    for (Producto p : lProductos) {

        String nombreProducto = p.getNombre().toLowerCase();

        if (nombreProducto.contains(textoBusqueda)) {
            System.out.println(p);
            encontrado = true;
        }
    }

    if (!encontrado) {
        System.out.println("No se encontraron productos");
    }
 }


   public synchronized void listarPedidosPorFecha(String fechaTexto) {

    try {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date fechaBuscada = sdf.parse(fechaTexto);

        boolean encontrado = false;

        for (Pedido p : lPedidos) {

            String fechaPedido = sdf.format(p.getFechaCreacion());
            String fechaComparar = sdf.format(fechaBuscada);

            if (fechaPedido.equals(fechaComparar)) {
                System.out.println(p);
                encontrado = true;
            }
        }

        if (!encontrado) {
            System.out.println("No hay pedidos en esa fecha");
        }

    } catch (Exception e) {
        System.out.println("Formato de fecha invalido. Use dd/MM/yyyy");
    }
    }

    public synchronized void procesarPedidosConfirmados() {
    for (Pedido p : lPedidos) {

        if (p.getEstado() == EstadoPedido.CONFIRMADO) {

            try {
                pedidosEnProceso++;

                System.out.println(
                    "Procesando pedido: " + p.getId()
                );

                
                Thread.sleep(2500);

                p.cambiarEstado(EstadoPedido.PROCESADO);

                System.out.println(
                    "Pedido procesado: " + p.getId()
                );

                pedidosEnProceso--;

                

            } catch (InterruptedException e) {
                System.out.println("Error al procesar pedido");
                }
            }
        }
    
    }
    public synchronized void generarReporteSistema() {

    int totalProductos = lProductos.size();
    int totalClientes = lClientes.size();
    int pendientes = 0;
    int confirmados = 0;
    int procesados = 0;
    double totalIngresos = 0;

    for (Pedido p : lPedidos) {

        switch (p.getEstado()) {

            case PENDIENTE:
                pendientes++;
                break;

            case CONFIRMADO:
                confirmados++;
                totalIngresos += p.calcularTotal();
                break;

            case PROCESADO:
                procesados++;
                totalIngresos += p.calcularTotal();
                break;
        }
    }

    System.out.println("\n===== REPORTE DEL SISTEMA =====");
    System.out.println("Productos: " + totalProductos);
    System.out.println("Clientes: " + totalClientes);
    System.out.println("Pedidos Pendientes: " + pendientes);
    System.out.println("Pedidos Confirmados: " + confirmados);
    System.out.println("Pedidos Procesados: " + procesados);
    System.out.println("Total Ingresos: $" + totalIngresos);
    System.out.println("================================\n");
 }

    public synchronized void guardarProductos() {

    try (DataOutputStream dos =
            new DataOutputStream(new FileOutputStream("productos.dat"))) {

        for (Producto p : lProductos) {

            dos.writeInt(p.getId());
            dos.writeUTF(p.getNombre());
            dos.writeDouble(p.getPrecio());
            dos.writeInt(p.getStock());
        }

        System.out.println("Productos guardados correctamente.");

    } catch (IOException e) {
        System.out.println("Error guardando productos: " + e.getMessage());
    }
    }

    public synchronized void cargarProductos() {

    File archivo = new File("productos.dat");

    if (!archivo.exists()) {
        System.out.println("Archivo de productos no existe, se creara.");
        return;
    }

    try (DataInputStream dis =
            new DataInputStream(new FileInputStream(archivo))) {

        lProductos.clear();

        while (true) {

            int id = dis.readInt();
            String nombre = dis.readUTF();
            double precio = dis.readDouble();
            int stock = dis.readInt();

            Producto p = new Producto(id, nombre, precio, stock);
            lProductos.add(p);
        }

    } catch (EOFException e) {
        System.out.println("Productos cargados correctamente.");
    } catch (IOException e) {
        System.out.println("Error cargando productos: " + e.getMessage());
    }
    }

    public synchronized void guardarClientes() {

    try (DataOutputStream dos =
            new DataOutputStream(new FileOutputStream("clientes.dat"))) {

        for (Cliente c : lClientes) {

            dos.writeInt(c.getId());
            dos.writeUTF(c.getNombre());
            
        }

        System.out.println("Clientes guardados correctamente.");

    } catch (IOException e) {
        System.out.println("Error guardando clientes: " + e.getMessage());
    }
    }
    public synchronized void cargarClientes() {

    File archivo = new File("clientes.dat");

    if (!archivo.exists()) {
        System.out.println("Archivo de clientes no existe, se creara.");
        return;
    }

    try (DataInputStream dis =
            new DataInputStream(new FileInputStream(archivo))) {

        lClientes.clear();

        while (true) {

            int id = dis.readInt();
            String nombre = dis.readUTF();
            String direccion = dis.readUTF();

            ClienteRegular c = new ClienteRegular(id, nombre);
            lClientes.add(c);
        }

    } catch (EOFException e) {
        System.out.println("Clientes cargados correctamente.");
    } catch (IOException e) {
        System.out.println("Error cargando clientes: " + e.getMessage());
    }
    }
    public synchronized void guardarPedidos() {

    try (PrintWriter pw = new PrintWriter(
            new BufferedWriter(
                    new FileWriter("pedidos.txt")))) {

        for (Pedido p : lPedidos) {

            pw.println(
                    p.getId() + "|" +
                    p.getCliente().getId() + "|" +
                    p.getFechaCreacion() + "|" +
                    p.getEstado() + "|" +
                    p.calcularTotal()
            );
        }

        System.out.println("Pedidos guardados correctamente.");

    } catch (IOException e) {
        System.out.println("Error guardando pedidos: " + e.getMessage());
    }
  }
        public synchronized void cargarPedidos() {

        File archivo = new File("pedidos.txt");

      if (!archivo.exists()) {
        System.out.println("Archivo de pedidos no existe, se creara.");
        return;
     }

       try (BufferedReader br = new BufferedReader(
            new FileReader(archivo))) {

         lPedidos.clear();

         String linea;

         while ((linea = br.readLine()) != null) {

            String[] partes = linea.split("\\|");

            int idPedido = Integer.parseInt(partes[0]);
            int idCliente = Integer.parseInt(partes[1]);
            String fecha = partes[2];
            String estado = partes[3];
            double total = Double.parseDouble(partes[4]);

            Cliente cliente = buscarClientePorId(idCliente);

            
        }

        System.out.println("Pedidos cargados correctamente.");

      } catch (IOException e) {
        System.out.println("Error cargando pedidos: " + e.getMessage());
      }
}










}


    

