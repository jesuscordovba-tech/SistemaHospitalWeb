package modelo.dto;

public class UsuariosDTO {

    private int idUsuario;
    private String nombreUsuario;
    private String password;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String especialidad;
    private String areaAsignada;
    private String turno;
    private int idRol;

    // 🔥 Relación usuario ↔ paciente
    private Integer idPaciente;

    public UsuariosDTO() {}
    

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getAreaAsignada() { return areaAsignada; }
    public void setAreaAsignada(String areaAsignada) { this.areaAsignada = areaAsignada; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }
}