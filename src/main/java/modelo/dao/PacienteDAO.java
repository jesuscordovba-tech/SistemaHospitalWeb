package modelo.dao;

import config.Conexion;
import modelo.dto.PacienteDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    /** MAPEO RESULTSET → DTO */
    private PacienteDTO map(ResultSet rs) throws SQLException {
        PacienteDTO p = new PacienteDTO();
        p.setIdPaciente(rs.getInt("id_paciente"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        p.setSexo(rs.getString("sexo"));
        p.setDireccion(rs.getString("direccion"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        return p;
    }

    /** LISTAR PACIENTES */
    public List<PacienteDTO> listar() {
        List<PacienteDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY id_paciente DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(map(rs));

        } catch (Exception e) {
            System.out.println("Listar pacientes: " + e.getMessage());
        }

        return lista;
    }

    /** OBTENER PACIENTE POR ID */
    public PacienteDTO obtener(int id) {
        String sql = "SELECT * FROM pacientes WHERE id_paciente=?";
        PacienteDTO p = null;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) p = map(rs);

        } catch (Exception e) {
            System.out.println("Obtener paciente: " + e.getMessage());
        }

        return p;
    }

    /** INSERTAR PACIENTE */
    public boolean insertar(PacienteDTO p) {
        String sql = """
            INSERT INTO pacientes(nombre, apellido, fecha_nacimiento, sexo, direccion, telefono, email)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getFechaNacimiento());
            ps.setString(4, p.getSexo());
            ps.setString(5, p.getDireccion());
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getEmail());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Insertar paciente: " + e.getMessage());
            return false;
        }
    }

    /** ÚLTIMO ID PACIENTE */
    public int ultimoIdPaciente() {
        int id = -1;
        String sql = "SELECT MAX(id_paciente) AS id FROM pacientes";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) id = rs.getInt("id");

        } catch (Exception e) {
            System.out.println("Error ultimoIdPaciente(): " + e.getMessage());
        }
        return id;
    }

    /** OBTENER ID PACIENTE POR ID_USUARIO */
    public int obtenerIdPorUsuario(int idUsuario) {
        int idPaciente = 0;

        String sql = "SELECT id_paciente FROM pacientes WHERE id_usuario = ? LIMIT 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) idPaciente = rs.getInt("id_paciente");

        } catch (Exception e) {
            System.out.println("Error obtener ID paciente: " + e.getMessage());
        }

        return idPaciente;
    }

    /** ACTUALIZAR PACIENTE */
    public boolean actualizar(PacienteDTO p) {
        String sql = """
            UPDATE pacientes SET
                nombre=?, apellido=?, fecha_nacimiento=?, sexo=?, 
                direccion=?, telefono=?, email=?
            WHERE id_paciente=?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getFechaNacimiento());
            ps.setString(4, p.getSexo());
            ps.setString(5, p.getDireccion());
            ps.setString(6, p.getTelefono());
            ps.setString(7, p.getEmail());
            ps.setInt(8, p.getIdPaciente());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    /** OBTENER PACIENTE DESDE USUARIO (RELACIÓN CORRECTA) */
    public PacienteDTO obtenerPorUsuario(int idUsuario) {
        PacienteDTO p = null;

        String sql = """
            SELECT p.*
            FROM pacientes p
            WHERE p.id_usuario = ?
            LIMIT 1
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) p = map(rs);

        } catch (Exception e) {
            System.out.println("Error obtenerPorUsuario: " + e.getMessage());
        }

        return p;
    }

    /** ELIMINAR PACIENTE */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id_paciente=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Eliminar paciente: " + e.getMessage());
            return false;
        }
    }
}