

public class main {

    public static void main(String[] args) {
       Sistema sistema = new Sistema();
       sistema.cargarProductos();
       sistema.cargarClientes();
    

        
        ProcesadorPedidos procesador = new ProcesadorPedidos(sistema);
        Thread hiloProcesador = new Thread(procesador);

        
        hiloProcesador.setPriority(Thread.NORM_PRIORITY + 1);
        generadorReportes generador = new generadorReportes(sistema);
        Thread hiloReportes = new Thread(generador);


        hiloReportes.setDaemon(true);

        hiloReportes.start();

        hiloProcesador.start();


      
        sistema.menu();
       
    }
    
}
