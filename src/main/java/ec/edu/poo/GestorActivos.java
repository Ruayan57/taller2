package ec.edu.poo;

import java.util.ArrayList;

/** * Gestor central que administra la colección de activos digitales. */
public class GestorActivos {

    private ArrayList<ActivoDigital> activos;

    /** Inicializa la lista vacía de activos. */
    public GestorActivos() {
        this.activos = new ArrayList<>();
    }

    /** Vacía completamente la lista de activos. */
    public void reiniciar() {
        this.activos.clear();
    }

    /** Registra un nuevo activo si su código no está duplicado. */
    public boolean registrarActivo(ActivoDigital activo) {
        if (buscarPorCodigo(activo.getCodigo()) == null) {
            activos.add(activo);
            return true;
        }
        return false;
    }

    /** Busca y retorna un activo por su código exacto. */
    public ActivoDigital buscarPorCodigo(String codigo) {
        for (ActivoDigital activo : activos) {
            if (activo.getCodigo().equals(codigo)) {
                return activo;
            }
        }
        return null;
    }

    /** Cuenta cuántos activos tienen un nivel de riesgo igual o mayor a 8. */
    public int contarActivosCriticos() {
        int contador = 0;
        for (ActivoDigital activo : activos) {
            if (activo.getNivelRiesgo() >= 8) {
                contador++;
            }
        }
        return contador;
    }

    /** Calcula el promedio general de riesgo (retorna 0.0 si la lista está vacía). */
    public double calcularPromedioRiesgo() {
        if (activos.isEmpty()) {
            return 0.0;
        }

        double sumaRiesgos = 0;
        for (ActivoDigital activo : activos) {
            sumaRiesgos += activo.getNivelRiesgo();
        }

        return sumaRiesgos / activos.size();
    }

    /** Aplica el parche de seguridad a un activo buscando por su código. */
    public boolean aplicarParcheActivo(String codigo) {
        ActivoDigital activo = buscarPorCodigo(codigo);

        if (activo != null) {
            activo.setParcheAplicado(true);
            return true;
        }
        return false;
    }

    // --- Métodos de acceso ---

    /** Retorna la cantidad total de activos registrados en el sistema. */
    public int obtenerCantidadActivos() {
        return activos.size();
    }

    /** Retorna la lista completa de activos. */
    public ArrayList<ActivoDigital> obtenerActivos() {
        return activos;
    }
}