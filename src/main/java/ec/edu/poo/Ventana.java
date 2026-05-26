package ec.edu.poo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Ventana extends JFrame {

    // --- Variables generadas por el GUI Designer ---
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPane1;

    // Pestaña 1: Registrar
    private JComboBox<String> cmbTipoActivo;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtRiesgo;
    private JCheckBox chkParche;
    private JTextField txtExtra; // Tu nuevo campo agregado
    private JButton btnRegistrar;

    // Pestaña 2: Buscar
    private JTextField txtBuscarCodigo;
    private JButton btnBuscarCodigo;
    private JButton btnMostrarTodo;
    private JTextArea txtMostrarBusqueda;

    // Pestaña 3: Resultados
    private JButton btnPromedioRiesgo;
    private JButton btnContadorCriticos;
    private JTextArea txtMostrarResultados;

    // --- Backend: Instancia del Gestor ---
    private GestorActivos gestor;

    public Ventana() {
        // 1. Inicializamos la lógica del negocio
        gestor = new GestorActivos();

        // 2. Configuración de la Ventana (JFrame)
        setTitle("Sistema de Gestión de Activos Digitales");
        setContentPane(tabbedPane1); // Usamos tu tabbedPane como base
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar datos del ComboBox si no lo hiciste en la vista de diseño
        if (cmbTipoActivo.getItemCount() == 0) {
            cmbTipoActivo.addItem("Servidor");
            cmbTipoActivo.addItem("Firewall");
        }

        // --- EVENTOS DE LA PESTAÑA: REGISTRAR ---
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarActivo();
            }
        });

        // --- EVENTOS DE LA PESTAÑA: BUSCAR ---
        btnBuscarCodigo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarActivo();
            }
        });

        btnMostrarTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarTodosLosActivos();
            }
        });

        // --- EVENTOS DE LA PESTAÑA: RESULTADOS ---
        btnPromedioRiesgo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcularPromedio();
            }
        });

        btnContadorCriticos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contarCriticos();
            }
        });
    }

    // ==========================================
    //          MÉTODOS DE LÓGICA INTERNA
    // ==========================================

    private void registrarActivo() {
        try {
            // Leer los datos de los campos de texto
            String tipo = (String) cmbTipoActivo.getSelectedItem();
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String riesgoTexto = txtRiesgo.getText().trim();
            String extraTexto = txtExtra.getText().trim(); // Leemos el nuevo campo
            boolean parche = chkParche.isSelected();

            // Validación de campos vacíos (ahora incluimos extraTexto)
            if (codigo.isEmpty() || nombre.isEmpty() || riesgoTexto.isEmpty() || extraTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convertir el riesgo a número
            int riesgo = Integer.parseInt(riesgoTexto);
            if (riesgo < 0 || riesgo > 10) {
                JOptionPane.showMessageDialog(this, "El nivel de riesgo debe estar entre 0 y 10.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ActivoDigital nuevoActivo;

            // Verificamos qué tipo de activo es y usamos el valor de txtExtra adecuadamente
            if ("Servidor".equals(tipo)) {
                // Para servidor, el campo extra es texto (Sistema Operativo)
                nuevoActivo = new Servidor(codigo, nombre, riesgo, parche, extraTexto);
            } else {
                // Para firewall, el campo extra es número (Reglas Activas)
                try {
                    int reglas = Integer.parseInt(extraTexto);
                    nuevoActivo = new Firewall(codigo, nombre, riesgo, parche, reglas);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Para un Firewall, el campo 'Número de Reglas' debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Intentar registrar en el gestor
            if (gestor.registrarActivo(nuevoActivo)) {
                JOptionPane.showMessageDialog(this, " Activo registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormularioRegistro();
            } else {
                JOptionPane.showMessageDialog(this, " Ya existe un activo registrado con el código: " + codigo, "Error de Duplicado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El nivel de riesgo debe ser un número entero.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarActivo() {
        String codigoBuscado = txtBuscarCodigo.getText().trim();

        if (codigoBuscado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ActivoDigital activo = gestor.buscarPorCodigo(codigoBuscado);

        if (activo != null) {
            txtMostrarBusqueda.setText(" ACTIVO ENCONTRADO:\n");
            txtMostrarBusqueda.append("----------------------------\n");
            txtMostrarBusqueda.append("Código: " + activo.getCodigo() + "\n");
            txtMostrarBusqueda.append("Nombre: " + activo.getNombre() + "\n");
            txtMostrarBusqueda.append("Riesgo: " + activo.getNivelRiesgo() + "\n");
            txtMostrarBusqueda.append("Parcheado: " + (activo.isParcheAplicado() ? "Sí" : "No") + "\n");

            if (activo instanceof Servidor) {
                txtMostrarBusqueda.append("Tipo: Servidor\n");
                txtMostrarBusqueda.append("Sistema Operativo: " + ((Servidor) activo).getSistemaOperativo() + "\n");
            } else if (activo instanceof Firewall) {
                txtMostrarBusqueda.append("Tipo: Firewall\n");
                txtMostrarBusqueda.append("Reglas Activas: " + ((Firewall) activo).getReglasActivas() + "\n");
            }
        } else {
            txtMostrarBusqueda.setText(" No se encontró ningún activo con el código: " + codigoBuscado);
        }
    }

    private void listarTodosLosActivos() {
        ArrayList<ActivoDigital> lista = gestor.obtenerActivos();

        if (lista.isEmpty()) {
            txtMostrarBusqueda.setText("El sistema no tiene activos registrados actualmente.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTARIO COMPLETO (").append(gestor.obtenerCantidadActivos()).append(") ===\n\n");

        for (ActivoDigital a : lista) {
            sb.append("[").append(a.getCodigo()).append("] - ").append(a.getNombre());
            sb.append(" (Riesgo: ").append(a.getNivelRiesgo()).append(")\n");
        }

        txtMostrarBusqueda.setText(sb.toString());
    }

    private void calcularPromedio() {
        double promedio = gestor.calcularPromedioRiesgo();
        txtMostrarResultados.setText("ANÁLISIS DE RIESGO\n");
        txtMostrarResultados.append("----------------------------\n");
        txtMostrarResultados.append("El promedio de riesgo de la infraestructura es: " + String.format("%.2f", promedio));
    }

    private void contarCriticos() {
        int criticos = gestor.contarActivosCriticos();
        txtMostrarResultados.setText("ANÁLISIS DE CRITICIDAD\n");
        txtMostrarResultados.append("----------------------------\n");

        if (criticos > 0) {
            txtMostrarResultados.append("¡Alerta! Se han detectado " + criticos + " activo(s) en estado CRÍTICO (Riesgo >= 8).");
        } else {
            txtMostrarResultados.append("Excelente. No hay activos en estado crítico en este momento.");
        }
    }

    private void limpiarFormularioRegistro() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtRiesgo.setText("");
        txtExtra.setText(""); // Limpiamos el nuevo campo extra
        chkParche.setSelected(false);
        cmbTipoActivo.setSelectedIndex(0);
        txtCodigo.requestFocus();
    }

    // ==========================================
    //                MÉTODO MAIN
    // ==========================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Ventana v = new Ventana();
                v.setVisible(true);
            }
        });
    }
}

