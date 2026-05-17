package modelo.dto;

public class BitacoraDTO {

    private int idBitacora;
    private Integer idUsuario;
    private String accion;
    private String fechaAccion;  // ← nombre correcto
    private String detalle;      // ← nombre correcto

    private String nombreUsuario;

    public int getIdBitacora() { return idBitacora; }
    public void setIdBitacora(int idBitacora) { this.idBitacora = idBitacora; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getFechaAccion() { return fechaAccion; }
    public void setFechaAccion(String fechaAccion) { this.fechaAccion = fechaAccion; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}