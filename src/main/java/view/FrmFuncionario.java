package view;

import dao.CargoDao;
import dao.DependenciaDao;
import dao.FuncionarioDao;
import dao.impl.CargoImpl;
import dao.impl.DependenciaImpl;
import dao.impl.FuncionarioImpl;
import exception.DatabaseException;
import exception.FuncionarioException;
import model.CargoPOJO;
import model.DependenciaPOJO;
import model.FuncionarioPOJO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.List;

public class FrmFuncionario extends JFrame {

    // Componentes del formulario
    private JTextField txtNombres, txtApellidos, txtDocumento, txtCorreo, txtTelefono, txtFechaIngreso;
    private JComboBox<String> comboEstado;
    private JComboBox<CargoPOJO> comboCargo;
    private JComboBox<DependenciaPOJO> comboDependencia;

    // Tabla y Modelo
    private JTable tabla;
    private DefaultTableModel modelo;

    // DAOs
    private FuncionarioDao funcionarioDao = new FuncionarioImpl();
    private CargoDao cargoDao = new CargoImpl();
    private DependenciaDao dependenciaDao = new DependenciaImpl();

    // Variable para guardar el ID al seleccionar en la tabla para edición
    private int idFuncionarioSeleccionado = -1;

    public FrmFuncionario() {
        configurarVentana();
        inicializarComponentes();
        cargarListasDesplegables();
        listarFuncionarios();
    }

    private void configurarVentana() {
        setTitle("Gestión de Funcionarios - Desarrollo Seguro");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10)); // Layout principal con separación

        // Aplicar un margen global (padding) a toda la ventana
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    private void inicializarComponentes() {
        // --- PANEL SUPERIOR (FORMULARIO) ---
        JPanel panelForm = new JPanel(new GridLayout(5, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Funcionario"));

        // Fila 1
        panelForm.add(new JLabel("Nombres:"));
        txtNombres = new JTextField();
        panelForm.add(txtNombres);

        panelForm.add(new JLabel("Apellidos:"));
        txtApellidos = new JTextField();
        panelForm.add(txtApellidos);

        // Fila 2
        panelForm.add(new JLabel("Documento:"));
        txtDocumento = new JTextField();
        panelForm.add(txtDocumento);

        panelForm.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelForm.add(txtCorreo);

        // Fila 3
        panelForm.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelForm.add(txtTelefono);

        // Placeholder para Fecha
        panelForm.add(new JLabel("Fecha Ingreso (YYYY-MM-DD):"));
        txtFechaIngreso = new JTextField();
        txtFechaIngreso.setToolTipText("Ejemplo: 2024-05-03");
        panelForm.add(txtFechaIngreso);

        // Fila 4
        panelForm.add(new JLabel("Cargo:"));
        comboCargo = new JComboBox<>();
        panelForm.add(comboCargo);

        panelForm.add(new JLabel("Dependencia:"));
        comboDependencia = new JComboBox<>();
        panelForm.add(comboDependencia);

        // Fila 5
        panelForm.add(new JLabel("Estado:"));
        comboEstado = new JComboBox<>(new String[] { "Activo", "Inactivo" });
        panelForm.add(comboEstado);

        // Espacios vacíos para mantener la simetría del Grid
        panelForm.add(new JLabel(""));
        panelForm.add(new JLabel(""));

        add(panelForm, BorderLayout.NORTH);

        // --- PANEL CENTRAL (TABLA) ---
        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen celdas haciendo doble clic
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Documento");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Fecha Ingreso");
        modelo.addColumn("Estado");
        modelo.addColumn("Cargo (ID)");
        modelo.addColumn("Dep (ID)");

        tabla = new JTable(modelo);
        // Evento: Al hacer clic en una fila, los datos suben al formulario
        tabla.getSelectionModel().addListSelectionListener(e -> seleccionarFila());
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- PANEL INFERIOR (BOTONES) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Actualizar Seleccionado");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar Formulario");

        // Colores y diseño básico a los botones
        btnGuardar.setBackground(new Color(46, 204, 113));
        btnGuardar.setForeground(Color.WHITE);

        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);

        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos de botones
        btnGuardar.addActionListener(e -> guardar());
        btnEditar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void cargarListasDesplegables() {
        try {
            // Llenar Combobox de Cargos
            List<CargoPOJO> cargos = cargoDao.listar();
            for (CargoPOJO c : cargos) {
                comboCargo.addItem(c);
            }

            // Llenar Combobox de Dependencias
            List<DependenciaPOJO> dependencias = dependenciaDao.listar();
            for (DependenciaPOJO d : dependencias) {
                comboDependencia.addItem(d);
            }
        } catch (DatabaseException e) {
            mostrarError("Error al conectar a la base de datos para cargar opciones.", e);
        }
    }

    private void listarFuncionarios() {
        modelo.setRowCount(0); // Limpiar tabla
        try {
            List<FuncionarioPOJO> lista = funcionarioDao.listar();
            for (FuncionarioPOJO f : lista) {
                modelo.addRow(new Object[] {
                        f.getIdFuncionario(),
                        f.getNombres(),
                        f.getApellidos(),
                        f.getDocumento(),
                        f.getCorreo(),
                        f.getTelefono(),
                        f.getFechaIngreso(),
                        f.getEstado(),
                        f.getIdCargo(),
                        f.getIdDependencia()
                });
            }
        } catch (DatabaseException e) {
            mostrarError("Error al listar los funcionarios.", e);
        }
    }

    // --- ACCIONES CRUD CON MANEJO SEGURO DE EXCEPCIONES ---

    private void guardar() {
        try {
            FuncionarioPOJO f = leerFormulario();
            funcionarioDao.ingresar(f);

            JOptionPane.showMessageDialog(this, "Funcionario guardado exitosamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            listarFuncionarios();
            limpiar();
        } catch (FuncionarioException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (DatabaseException ex) {
            mostrarError("Ocurrió un error grave al guardar en la BD.", ex);
        } catch (IllegalArgumentException ex) {
            mostrarAdvertencia("El formato de la fecha es incorrecto. Debe ser YYYY-MM-DD.");
        }
    }

    private void actualizar() {
        if (idFuncionarioSeleccionado == -1) {
            mostrarAdvertencia("Por favor selecciona un funcionario de la tabla primero.");
            return;
        }

        try {
            FuncionarioPOJO f = leerFormulario();
            f.setIdFuncionario(idFuncionarioSeleccionado);

            funcionarioDao.modificar(f);

            JOptionPane.showMessageDialog(this, "Funcionario actualizado exitosamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            listarFuncionarios();
            limpiar();
        } catch (FuncionarioException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (DatabaseException ex) {
            mostrarError("Ocurrió un error grave al actualizar.", ex);
        } catch (IllegalArgumentException ex) {
            mostrarAdvertencia("El formato de la fecha es incorrecto. Debe ser YYYY-MM-DD.");
        }
    }

    private void eliminar() {
        if (idFuncionarioSeleccionado == -1) {
            mostrarAdvertencia("Por favor selecciona un funcionario de la tabla primero.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este registro?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                funcionarioDao.eliminar(idFuncionarioSeleccionado);
                JOptionPane.showMessageDialog(this, "Funcionario eliminado.");
                listarFuncionarios();
                limpiar();
            } catch (FuncionarioException ex) {
                mostrarAdvertencia(ex.getMessage());
            } catch (DatabaseException ex) {
                mostrarError("Ocurrió un error al intentar eliminar.", ex);
            }
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private FuncionarioPOJO leerFormulario() throws IllegalArgumentException {
        FuncionarioPOJO f = new FuncionarioPOJO();
        f.setNombres(txtNombres.getText().trim());
        f.setApellidos(txtApellidos.getText().trim());
        f.setDocumento(txtDocumento.getText().trim());
        f.setCorreo(txtCorreo.getText().trim());
        f.setTelefono(txtTelefono.getText().trim());

        // Validar que no haya nulos de JComboBox
        if (comboCargo.getSelectedItem() != null) {
            f.setIdCargo(((CargoPOJO) comboCargo.getSelectedItem()).getIdCargo());
        }
        if (comboDependencia.getSelectedItem() != null) {
            f.setIdDependencia(((DependenciaPOJO) comboDependencia.getSelectedItem()).getIdDependencia());
        }
        f.setEstado(comboEstado.getSelectedItem().toString());

        // Esto arrojará IllegalArgumentException si está mal escrito
        String fecha = txtFechaIngreso.getText().trim();
        if (!fecha.isEmpty()) {
            f.setFechaIngreso(Date.valueOf(fecha));
        } else {
            throw new IllegalArgumentException("Fecha vacía");
        }

        return f;
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            idFuncionarioSeleccionado = Integer.parseInt(tabla.getValueAt(fila, 0).toString());

            txtNombres.setText(tabla.getValueAt(fila, 1).toString());
            txtApellidos.setText(tabla.getValueAt(fila, 2).toString());
            txtDocumento.setText(tabla.getValueAt(fila, 3).toString());
            txtCorreo.setText(tabla.getValueAt(fila, 4).toString());
            txtTelefono.setText(tabla.getValueAt(fila, 5).toString());
            txtFechaIngreso.setText(tabla.getValueAt(fila, 6).toString());
            comboEstado.setSelectedItem(tabla.getValueAt(fila, 7).toString());

            // Seleccionar los objetos correctos en los ComboBox usando el ID
            int idCargoT = Integer.parseInt(tabla.getValueAt(fila, 8).toString());
            for (int i = 0; i < comboCargo.getItemCount(); i++) {
                if (comboCargo.getItemAt(i).getIdCargo() == idCargoT) {
                    comboCargo.setSelectedIndex(i);
                    break;
                }
            }

            int idDepT = Integer.parseInt(tabla.getValueAt(fila, 9).toString());
            for (int i = 0; i < comboDependencia.getItemCount(); i++) {
                if (comboDependencia.getItemAt(i).getIdDependencia() == idDepT) {
                    comboDependencia.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void limpiar() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtFechaIngreso.setText("");
        comboEstado.setSelectedIndex(0);
        if (comboCargo.getItemCount() > 0)
            comboCargo.setSelectedIndex(0);
        if (comboDependencia.getItemCount() > 0)
            comboDependencia.setSelectedIndex(0);

        idFuncionarioSeleccionado = -1;
        tabla.clearSelection();
    }

    private void mostrarError(String mensaje, Exception e) {
        // Nunca mostrar 'e.getMessage()' directamente en el popup al usuario final
        // si es un error de infraestructura. Lo imprimimos en consola para el
        // desarrollador.
        System.err.println("CRITICAL ERROR: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, mensaje + "\nContacte a soporte técnico.", "Error Crítico",
                JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        // Estos son errores de lógica (duplicados, malos formatos)
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        // Aplicar el tema moderno Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus no está, usa el por defecto
        }

        // Ejecutar en el hilo seguro de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Auto-inicializar la base de datos si no existe
                config.DatabaseInitializer.init();

                // Si todo sale bien, abrir la ventana
                new FrmFuncionario().setVisible(true);
            } catch (Exception e) {
                System.err.println("CRITICAL STARTUP ERROR: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "No se pudo iniciar la aplicación.\n" +
                                "Verifique que PostgreSQL esté instalado y corriendo.\n" +
                                "Error: " + e.getMessage(),
                        "Error Fatal de Base de Datos",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
