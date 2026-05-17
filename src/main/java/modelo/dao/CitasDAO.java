package modelo.dao;

import config.Conexion;
import modelo.dto.CitaDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitasDAO {

    // -------------------------
    // MAPEO RESULTSET → DTO
    // -------------------------
    private CitaDTO map(ResultSet rs) throws SQLException {
        CitaDTO c = new CitaDTO();

        c.setIdCita(rs.getInt("id_cita"));
        c.setIdPaciente(rs.getInt("id_paciente"));
        c.setIdMedico(rs.getInt("id_medico"));
        c.setFechaCita(rs.getString("fecha_cita"));
        c.setHoraCita(rs.getString("hora_cita"));
        c.setMotivo(rs.getString("motivo"));
        c.setEstado(rs.getString("estado"));
        c.setObservaciones(rs.getString("observaciones"));

        return c;
    }

    // ==========================================
    // LISTAR TODAS LAS CITAS
    // ==========================================
    public List<CitaDTO> listarCitas() {
        List<CitaDTO> lista = new ArrayList<>();

        String sql = """
            SELECT c.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM citas c
            INNER JOIN pacientes p ON p.id_paciente = c.id_paciente
            INNER JOIN usuarios u ON u.id_usuario = c.id_medico
            ORDER BY c.id_cita DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CitaDTO c = map(rs);
                c.setNombrePaciente(rs.getString("paciente"));
                c.setNombreMedico(rs.getString("medico"));
                lista.add(c);
            }

        } catch (Exception e) {
            System.out.println("ERROR listarCitas(): " + e.getMessage());
        }

        return lista;
    }
    public boolean actualizarEstado(int idCita, String nuevoEstado) {
        String sql = "UPDATE citas SET estado=? WHERE id_cita=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizarEstado(): " + e.getMessage());
            return false;
        }
    }
    // ==========================================
    // LISTAR POR PACIENTE
    // ==========================================
    public List<CitaDTO> listarPorPaciente(int idPaciente) {
        List<CitaDTO> lista = new ArrayList<>();

        String sql = """
            SELECT c.*, 
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM citas c
            INNER JOIN pacientes p ON p.id_paciente = c.id_paciente
            INNER JOIN usuarios u ON u.id_usuario = c.id_medico
            WHERE c.id_paciente = ?
            ORDER BY c.fecha_cita DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CitaDTO c = map(rs);
                c.setNombrePaciente(rs.getString("paciente"));
                c.setNombreMedico(rs.getString("medico"));
                lista.add(c);
            }

        } catch (Exception e) {
            System.out.println("ERROR listarPorPaciente(): " + e.getMessage());
        }

        return lista;
    }

    // ==========================================
    // LISTAR POR MÉDICO
    // ==========================================
    public List<CitaDTO> listarCitasPorMedico(int idMedico) {
        List<CitaDTO> lista = new ArrayList<>();

        String sql = """
            SELECT c.*, 
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM citas c
            INNER JOIN pacientes p ON p.id_paciente = c.id_paciente
            INNER JOIN usuarios u ON u.id_usuario = c.id_medico
            WHERE c.id_medico = ?
            ORDER BY c.fecha_cita DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CitaDTO c = map(rs);
                c.setNombrePaciente(rs.getString("paciente"));
                c.setNombreMedico(rs.getString("medico"));
                lista.add(c);
            }

        } catch (Exception e) {
            System.out.println("ERROR listarCitasPorMedico(): " + e.getMessage());
        }

        return lista;
    }

    // ==========================================
    // OBTENER
    // ==========================================
    public CitaDTO obtenerCita(int id) {
        CitaDTO c = null;
        String sql = "SELECT * FROM citas WHERE id_cita=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) c = map(rs);

        } catch (Exception e) {
            System.out.println("ERROR obtenerCita(): " + e.getMessage());
        }

        return c;
    }

    // ==========================================
    // INSERTAR
    // ==========================================
    public boolean insertar(CitaDTO c) {
        String sql = """
            INSERT INTO citas(id_paciente,id_medico,fecha_cita,hora_cita,motivo,estado,observaciones)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setString(3, c.getFechaCita());
            ps.setString(4, c.getHoraCita());
            ps.setString(5, c.getMotivo());
            ps.setString(6, c.getEstado());
            ps.setString(7, c.getObservaciones());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("ERROR insertar(): " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // ACTUALIZAR
    // ==========================================
    public boolean actualizar(CitaDTO c) {
        String sql = """
            UPDATE citas SET
            id_paciente=?, id_medico=?, fecha_cita=?, hora_cita=?,
            motivo=?, estado=?, observaciones=?
            WHERE id_cita=?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdMedico());
            ps.setString(3, c.getFechaCita());
            ps.setString(4, c.getHoraCita());
            ps.setString(5, c.getMotivo());
            ps.setString(6, c.getEstado());
            ps.setString(7, c.getObservaciones());
            ps.setInt(8, c.getIdCita());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("ERROR actualizar(): " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // ELIMINAR
    // ==========================================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM citas WHERE id_cita=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("ERROR eliminar(): " + e.getMessage());
            return false;
        }
    }
}