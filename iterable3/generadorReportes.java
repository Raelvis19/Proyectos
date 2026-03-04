public class generadorReportes implements Runnable {

    private Sistema sistema;
    private volatile boolean activo = true;

    public generadorReportes(Sistema sistema) {
        this.sistema = sistema;
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {

        while (activo) {
            try {

                
                Thread.sleep(10000);

            
                sistema.generarReporteSistema();

                System.out.println("Reporte automático generado.");

            } catch (InterruptedException e) {
                System.out.println("Hilo de reportes interrumpido.");
            }
        }
    }
}