public class ProcesadorPedidos implements Runnable {

    private Sistema sistema;
    private volatile boolean activo = true;

    public ProcesadorPedidos(Sistema sistema) {
        this.sistema = sistema;
    }

    public void detener() {
        activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                sistema.procesarPedidosConfirmados();

                // pausa entre ciclos
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                System.out.println("Procesador interrumpido");
            }
        }
    }
}