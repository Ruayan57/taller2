package ec.edu.poo;

/** * Clase que representa un Servidor, heredando de ActivoDigital. */
public class Servidor extends ActivoDigital {

    private String sistemaOperativo;

    /** Inicializa un nuevo servidor con sus atributos base y su sistema operativo. */
    public Servidor(String codigo, String nombre, int nivelRiesgo,
                    boolean parcheAplicado, String sistemaOperativo) {
        super(codigo, nombre, nivelRiesgo, parcheAplicado);
        this.sistemaOperativo = sistemaOperativo;
    }

    // --- Getters y Setters ---

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }
}