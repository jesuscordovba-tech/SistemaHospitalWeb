package modelo.dao;

import config.Conexion;
import modelo.dto.UsuariosDTO;
import modelo.dto.PacienteDTO;
import utils.SecurityUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // ===========================================================
    // LOGIN (con BCrypt)
    // ===========================================================
    public UsuariosDTO login(String usuario, String pass) {
        UsuariosDTO u = null;

        String sql = "SELECT * FROM usuarios WHERE nombre_usuario=?";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                if (SecurityUtils.checkPassword(pass, hashed)) {
                    u = mapUsuario(rs);
                    if (!SecurityUtils.isBcrypt(hashed)) {
                        migrarPassword(rs.getInt("id_usuario"), pass);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return u;
    }

    private void migrarPassword(int idUsuario, String plainPass) {
        String sql = "UPDATE usuarios SET password=? WHERE id_usuario=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, SecurityUtils.hashPassword(plainPass));
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Migrar password: " + e.getMessage());
        }
    }

    // ===========================================================
    // MAPEO DE USUARIO
    // ===========================================================
    private UsuariosDTO mapUsuario(ResultSet rs) throws SQLException {
        UsuariosDTO u = new UsuariosDTO();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setPassword(rs.getString("password"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setTelefono(rs.getString("telefono"));
        u.setCorreo(rs.getString("correo"));
        u.setEspecialidad(rs.getString("especialidad"));
        u.setAreaAsignada(rs.getString("area_asignada"));
        u.setTurno(rs.getString("turno"));
        u.setIdRol(rs.getInt("id_rol"));
        u.setIdPaciente((Integer) rs.getObject("id_paciente"));
        return u;
    }

    // ===========================================================
    // BUSCAR SI EXISTE USUARIO POR NOMBRE
    // ===========================================================
    public UsuariosDTO buscarUsuarioPorNombre(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario=?";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombreUsuario);

            rs = ps.executeQuery();
            if (rs.next()) {
                return mapUsuario(rs);
            }

        } catch (Exception e) {
            System.out.println("Buscar usuario: " + e.getMessage());
        }

        return null;
    }

    // ===========================================================
    // LISTAR TODOS LOS USUARIOS
    // ===========================================================
    public List<UsuariosDTO> listarUsuarios() {
        List<UsuariosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapUsuario(rs));
            }

        } catch (Exception e) {
            System.out.println("Listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // ===========================================================
    // LISTAR MÉDICOS (id_rol = 2)
    // ===========================================================
    public List<UsuariosDTO> listarMedicos() {
        List<UsuariosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE id_rol = 2";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapUsuario(rs));
            }

        } catch (Exception e) {
            System.out.println("Listar médicos: " + e.getMessage());
        }

        return lista;
    }

    // ===========================================================
    // LISTAR ENFERMEROS (id_rol = 4)
    // ===========================================================
    public List<UsuariosDTO> listarEnfermeros() {
        List<UsuariosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE id_rol = 4";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapUsuario(rs));
            }

        } catch (Exception e) {
            System.out.println("Listar enfermeros: " + e.getMessage());
        }

        return lista;
    }

    // ===========================================================
    // LISTAR PACIENTES (usuarios con id_paciente != null)
    // ===========================================================
    public List<UsuariosDTO> listarUsuariosPacientes() {
        List<UsuariosDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE id_paciente IS NOT NULL";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) lista.add(mapUsuario(rs));

        } catch (Exception e) {
            System.out.println("Listar usuarios pacientes: " + e.getMessage());
        }

        return lista;
    }

    // ===========================================================
    // OBTENER USUARIO POR ID
    // ===========================================================
    public UsuariosDTO obtenerUsuario(int id) {
        UsuariosDTO u = null;
        String sql = "SELECT * FROM usuarios WHERE id_usuario=?";

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) u = mapUsuario(rs);

        } catch (Exception e) {
            System.out.println("Obtener usuario: " + e.getMessage());
        }

        return u;
    }

    // ===========================================================
    // GUARDAR USUARIO (CREAR PACIENTE SI ROL = 5)
    // ===========================================================
    public boolean guardarUsuario(UsuariosDTO u) {

        try {
            con = Conexion.conectar();
            con.setAutoCommit(false);

            Integer nuevoIdPaciente = null;

            if (u.getIdRol() == 5) {
                // Crear paciente
                String sqlPac = """
                    INSERT INTO pacientes(nombre, apellido, telefono, email, sexo)
                    VALUES (?, ?, ?, ?, 'M')
                """;

                ps = con.prepareStatement(sqlPac, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, u.getNombre());
                ps.setString(2, u.getApellido());
                ps.setString(3, u.getTelefono());
                ps.setString(4, u.getCorreo());
                ps.executeUpdate();

                rs = ps.getGeneratedKeys();
                if (rs.next()) nuevoIdPaciente = rs.getInt(1);
            }

            // Insert usuario asociado
            String sql = """
                INSERT INTO usuarios
                (nombre_usuario, password, nombre, apellido, telefono, correo,
                 especialidad, area_asignada, turno, id_rol, id_paciente)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, SecurityUtils.hashPassword(u.getPassword()));
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellido());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getEspecialidad());
            ps.setString(8, u.getAreaAsignada());
            ps.setString(9, u.getTurno());
            ps.setInt(10, u.getIdRol());
            ps.setObject(11, nuevoIdPaciente);

            ps.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Guardar usuario: " + e.getMessage());
            return false;
        }
    }
    public boolean vincularPaciente(String nombreUsuario, int idPaciente) {

        String sql = """
            UPDATE usuarios 
            SET id_paciente = ? 
            WHERE nombre_usuario = ?
        """;

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ps.setString(2, nombreUsuario);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error vincularPaciente(): " + e.getMessage());
            return false;
        }
    }
    public boolean actualizarDatosPerfil(UsuariosDTO u) {

        String sql = """
            UPDATE usuarios 
            SET nombre = ?, apellido = ?, telefono = ?, correo = ?
            WHERE id_usuario = ?
        """;

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getCorreo());
            ps.setInt(5, u.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizarDatosPerfil(): " + e.getMessage());
            return false;
        }
    }
    public boolean actualizarPassword(int idUsuario, String passActual, String passNueva) {

        String sqlCheck = "SELECT password FROM usuarios WHERE id_usuario = ?";
        String sqlUpdate = "UPDATE usuarios SET password = ? WHERE id_usuario = ?";

        try {
            con = Conexion.conectar();

            // 1️⃣ Verificar contraseña actual
            ps = con.prepareStatement(sqlCheck);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Usuario no encontrado para cambio de password");
                return false; 
            }

            String passBD = rs.getString("password");

            if (!SecurityUtils.checkPassword(passActual, passBD)) {
                System.out.println("Password actual incorrecto");
                return false;
            }

            // 2️⃣ Actualizar contraseña nueva
            ps = con.prepareStatement(sqlUpdate);
            ps.setString(1, SecurityUtils.hashPassword(passNueva));
            ps.setInt(2, idUsuario);
            ps.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Error actualizarPassword(): " + e.getMessage());
            return false;
        }
    }

    // ===========================================================
    // ACTUALIZAR
    // ===========================================================
    public boolean actualizarUsuario(UsuariosDTO u) {
        String sql = """
            UPDATE usuarios SET 
            nombre_usuario=?, password=?, nombre=?, apellido=?, telefono=?, correo=?,
            especialidad=?, area_asignada=?, turno=?, id_rol=?
            WHERE id_usuario=?
        """;

        try {
            con = Conexion.conectar();
            ps = con.prepareStatement(sql);

            ps.setString(1, u.getNombreUsuario());
            ps.setString(2, SecurityUtils.hashPassword(u.getPassword()));
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellido());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getCorreo());
            ps.setString(7, u.getEspecialidad());
            ps.setString(8, u.getAreaAsignada());
            ps.setString(9, u.getTurno());
            ps.setInt(10, u.getIdRol());
            ps.setInt(11, u.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Actualizar usuario: " + e.getMessage());
            return false;
        }
    }


    // ===========================================================
    // ELIMINAR
    // ===========================================================
    public boolean eliminarUsuario(int id) {
        try {
            con = Conexion.conectar();
            ps = con.prepareStatement("DELETE FROM usuarios WHERE id_usuario=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}