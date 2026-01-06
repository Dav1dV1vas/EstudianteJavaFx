package ec.edu.espoch.estudiantejavafx;

import java.time.LocalDate;

public class Estudiante {

    private int id;
    private String nombres;
    private String apellidos;
    private String carrera;
    private int semestre;
    private LocalDate fechaNacimiento;
    private double promedio;

    public Estudiante(int id, String nombres, String apellidos, String carrera,
                      int semestre, LocalDate fechaNacimiento, double promedio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.carrera = carrera;
        this.semestre = semestre;
        this.fechaNacimiento = fechaNacimiento;
        this.promedio = promedio;
    }

    public int getId() { return id; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public double getPromedio() { return promedio; }

    public void setId(int id) { this.id = id; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setPromedio(double promedio) { this.promedio = promedio; }
}
