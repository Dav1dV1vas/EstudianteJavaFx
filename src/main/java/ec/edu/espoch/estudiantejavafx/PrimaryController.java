package ec.edu.espoch.estudiantejavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class PrimaryController {

    // Formulario
    @FXML private TextField txtId;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private ComboBox<String> cbCarrera;
    @FXML private Spinner<Integer> spSemestre;
    @FXML private DatePicker dpNacimiento;
    @FXML private TextField txtPromedio;

    // Tabla
    @FXML private TableView<Estudiante> tblEstudiantes;
    @FXML private TableColumn<Estudiante, Integer> colId;
    @FXML private TableColumn<Estudiante, String> colNombres;
    @FXML private TableColumn<Estudiante, String> colApellidos;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, Integer> colSemestre;
    @FXML private TableColumn<Estudiante, LocalDate> colNacimiento;
    @FXML private TableColumn<Estudiante, Double> colPromedio;

    private ObservableList<Estudiante> listaEstudiantes;
    private Estudiante estudianteEnEdicion = null;

    @FXML
    public void initialize() {
        cbCarrera.setItems(FXCollections.observableArrayList(
                "Tecnologías de la Información", "Software", "Redes", "Telecomunicaciones", "Electrónica", "Otra"
        ));

        spSemestre.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCarrera.setCellValueFactory(new PropertyValueFactory<>("carrera"));
        colSemestre.setCellValueFactory(new PropertyValueFactory<>("semestre"));
        colNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colPromedio.setCellValueFactory(new PropertyValueFactory<>("promedio"));

        listaEstudiantes = FXCollections.observableArrayList();
        tblEstudiantes.setItems(listaEstudiantes);

        // Datos de ejemplo (puedes borrarlos si no quieres)
        listaEstudiantes.addAll(
                new Estudiante(1, "Aylin", "Riera", "Tecnologías de la Información", 2, LocalDate.of(2004, 6, 10), 8.7),
                new Estudiante(2, "David", "Vivas", "Software", 2, LocalDate.of(2003, 11, 20), 9.1)
        );

        // Cargar formulario al seleccionar fila
        tblEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) cargarFormulario(newV);
        });
    }

    // NUEVO
    @FXML
    private void nuevo() {
        estudianteEnEdicion = null;
        tblEstudiantes.getSelectionModel().clearSelection();
        limpiar();
        mostrarInfo("Nuevo", "Listo ✅", "Ingresa datos y presiona Guardar.");
    }

    // GUARDAR (agrega o actualiza)
    @FXML
    private void guardar() {
        String error = validar();
        if (error != null) {
            mostrarAlerta("Validación", "Datos inválidos ❌", error);
            return;
        }

        int id = Integer.parseInt(txtId.getText().trim());
        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String carrera = cbCarrera.getValue();
        int semestre = spSemestre.getValue();
        LocalDate nacimiento = dpNacimiento.getValue();
        double promedio = Double.parseDouble(txtPromedio.getText().trim());

        if (estudianteEnEdicion == null) {
            boolean idExiste = listaEstudiantes.stream().anyMatch(e -> e.getId() == id);
            if (idExiste) {
                mostrarAlerta("Validación", "ID repetido ❌", "Ya existe un estudiante con ese ID.");
                return;
            }

            listaEstudiantes.add(new Estudiante(id, nombres, apellidos, carrera, semestre, nacimiento, promedio));
            mostrarInfo("Guardar", "Agregado ✅", "Estudiante registrado.");
            limpiar();
        } else {
            estudianteEnEdicion.setId(id);
            estudianteEnEdicion.setNombres(nombres);
            estudianteEnEdicion.setApellidos(apellidos);
            estudianteEnEdicion.setCarrera(carrera);
            estudianteEnEdicion.setSemestre(semestre);
            estudianteEnEdicion.setFechaNacimiento(nacimiento);
            estudianteEnEdicion.setPromedio(promedio);

            tblEstudiantes.refresh();
            mostrarInfo("Editar", "Actualizado ✅", "Cambios guardados.");
            estudianteEnEdicion = null;
            tblEstudiantes.getSelectionModel().clearSelection();
            limpiar();
        }
    }

    // EDITAR
    @FXML
    private void editar() {
        Estudiante seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Editar", "Sin selección ⚠️", "Selecciona un estudiante en la tabla.");
            return;
        }
        estudianteEnEdicion = seleccionado;
        cargarFormulario(seleccionado);
        mostrarInfo("Editar", "Modo edición ✏️", "Modifica y presiona Guardar.");
    }

    // ELIMINAR
    @FXML
    private void eliminar() {
        Estudiante seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Eliminar", "Sin selección ⚠️", "Selecciona un estudiante para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("¿Eliminar estudiante?");
        confirm.setContentText("Se eliminará: " + seleccionado.getNombres() + " " + seleccionado.getApellidos());

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            listaEstudiantes.remove(seleccionado);
            mostrarInfo("Eliminar", "Eliminado 🗑️", "Estudiante eliminado.");
            estudianteEnEdicion = null;
            limpiar();
        }
    }

    // LIMPIAR
    @FXML
    private void limpiar() {
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        cbCarrera.setValue(null);
        spSemestre.getValueFactory().setValue(1);
        dpNacimiento.setValue(null);
        txtPromedio.clear();
    }

    private void cargarFormulario(Estudiante e) {
        txtId.setText(String.valueOf(e.getId()));
        txtNombres.setText(e.getNombres());
        txtApellidos.setText(e.getApellidos());
        cbCarrera.setValue(e.getCarrera());
        spSemestre.getValueFactory().setValue(e.getSemestre());
        dpNacimiento.setValue(e.getFechaNacimiento());
        txtPromedio.setText(String.valueOf(e.getPromedio()));
    }

    // Validación mínima (obligatorios + rangos)
    private String validar() {
        if (txtId.getText().trim().isEmpty() ||
                txtNombres.getText().trim().isEmpty() ||
                txtApellidos.getText().trim().isEmpty() ||
                cbCarrera.getValue() == null ||
                dpNacimiento.getValue() == null ||
                txtPromedio.getText().trim().isEmpty()) {
            return "Completa todos los campos obligatorios.";
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            if (id <= 0) return "El ID debe ser positivo.";
        } catch (NumberFormatException e) {
            return "El ID debe ser un número entero válido.";
        }

        try {
            double prom = Double.parseDouble(txtPromedio.getText().trim());
            if (prom < 0 || prom > 10) return "El promedio debe estar entre 0 y 10.";
        } catch (NumberFormatException e) {
            return "El promedio debe ser un número válido (ej: 8.5).";
        }

        if (dpNacimiento.getValue().isAfter(LocalDate.now())) {
            return "La fecha de nacimiento no puede ser futura.";
        }

        return null;
    }

    private void mostrarAlerta(String titulo, String header, String mensaje) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(titulo);
        a.setHeaderText(header);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    private void mostrarInfo(String titulo, String header, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(header);
        a.setContentText(mensaje);
        a.showAndWait();
    }
}
