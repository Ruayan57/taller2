package ec.edu.poo;

/** * Clase que representa un Firewall, heredando las propiedades de ActivoDigital. */
public class Firewall extends ActivoDigital {

    private int reglasActivas;

    /** Inicializa un nuevo firewall con sus atributos base y la cantidad de reglas activas. */
    public Firewall(String codigo, String nombre, int nivelRiesgo,
                    boolean parcheAplicado, int reglasActivas) {
        super(codigo, nombre, nivelRiesgo, parcheAplicado);
        this.reglasActivas = reglasActivas;
    }

    // --- Getters y Setters ---

    public int getReglasActivas() {
        return reglasActivas;
    }

    public void setReglasActivas(int reglasActivas) {
        this.reglasActivas = reglasActivas;
    }
}