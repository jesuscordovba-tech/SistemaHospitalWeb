package modelo.dto;

public class AtencionDTO {

    private int idAtencion;
    private int idCita;
    private int idPaciente;
    private int idMedico;

    private String diagnostico;
    private String tratamiento;
    private String receta;
    private String notas;
    private String fechaAtencion;

    private String nombrePaciente;
    private String nombreMedico;

    public int getIdAtencion() { return idAtencion; }
    public void setIdAtencion(int idAtencion) { this.idAtencion = idAtencion; }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public String getReceta() { return receta; }
    public void setReceta(String receta) { this.receta = receta; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public String getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(String fechaAtencion) { this.fechaAtencion = fechaAtencion; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getNombreMedico() { return nombreMedico; }
    public void setNombreMedico(String nombreMedico) { this.nombreMedico = nombreMedico; }
}