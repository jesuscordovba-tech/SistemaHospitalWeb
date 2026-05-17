package modelo.dto;

public class SignosDTO {

    private int idSigno;
    private int idCita;
    private int idPaciente;
    private Integer idEnfermero;

    private double temperatura;
    private String presion;

    private int frecuenciaCardiaca;
    private int frecuenciaRespiratoria;
    private int saturacion;
    private double peso;

    private String observaciones;   // ✔ AGREGADO

    private String fechaRegistro;

    // Extra para mostrar
    private String nombrePaciente;
    private String nombreEnfermero;

    // ============================
    // GETTERS / SETTERS
    // ============================

    public int getIdSigno() { return idSigno; }
    public void setIdSigno(int idSigno) { this.idSigno = idSigno; }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public Integer getIdEnfermero() { return idEnfermero; }
    public void setIdEnfermero(Integer idEnfermero) { this.idEnfermero = idEnfermero; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }

    public String getPresion() { return presion; }
    public void setPresion(String presion) { this.presion = presion; }

    public int getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(int frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public int getFrecuencia() { return frecuenciaCardiaca; }
    public void setFrecuencia(int frecuencia) { this.frecuenciaCardiaca = frecuencia; }

    public int getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(int frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }

    public int getSaturacion() { return saturacion; }
    public void setSaturacion(int saturacion) { this.saturacion = saturacion; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getObservaciones() { return observaciones; }   // ✔ NUEVO
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public String getNombreEnfermero() { return nombreEnfermero; }
    public void setNombreEnfermero(String nombreEnfermero) { this.nombreEnfermero = nombreEnfermero; }
}