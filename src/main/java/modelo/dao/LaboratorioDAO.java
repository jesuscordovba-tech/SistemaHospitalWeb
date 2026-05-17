package modelo.dao;

import config.Conexion;
import modelo.dto.LaboratorioDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaboratorioDAO {

    /** MAPEO RESULTSET → DTO (sin nombres de paciente/médico) */
    private LaboratorioDTO map(ResultSet rs) throws SQLException {
        LaboratorioDTO l = new LaboratorioDTO();

        l.setIdLaboratorio(rs.getInt("id_laboratorio"));
        l.setIdPaciente(rs.getInt("id_paciente"));

        // id_medico puede ser NULL → evitar error
        int idMedico = rs.getInt("id_medico");
        l.setIdMedico(rs.wasNull() ? null : idMedico);

        l.setTipoExamen(rs.getString("tipo_examen"));

        // fecha_solicitud es DATE → lo convertimos a String
        Date fecha = rs.getDate("fecha_solicitud");
        l.setFechaSolicitud(fecha != null ? fecha.toString() : null);

        l.setEstado(rs.getString("estado"));

        return l;
    }

    // ======================================================
    // LISTAR POR PACIENTE (para el Módulo "Mi Panel")
    // ======================================================
    public List<LaboratorioDTO> listarPorPaciente(int idPaciente) {
        List<LaboratorioDTO> lista = new ArrayList<>();

        String sql = """
            SELECT l.*,
                   p.nombre AS nombre_paciente, p.apellido AS apellido_paciente,
                   u.nombre AS nombre_medico, u.apellido AS apellido_medico
            FROM laboratorios l
            INNER JOIN pacientes p ON l.id_paciente = p.id_paciente
            LEFT JOIN usuarios u ON l.id_medico = u.id_usuario
            WHERE l.id_paciente = ?
            ORDER BY l.id_laboratorio DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LaboratorioDTO l = map(rs);

                    l.setNombrePaciente(
                        rs.getString("nombre_paciente") + " " +
                        rs.getString("apellido_paciente")
                    );

                    String nomMed = rs.getString("nombre_medico");
                    String apeMed = rs.getString("apellido_medico");

                    if (nomMed != null) {
                        l.setNombreMedico(nomMed + " " + apeMed);
                    } else {
                        l.setNombreMedico("No asignado");
                    }

                    lista.add(l);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarPorPaciente (Laboratorio): " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // LISTAR PARA LABORATORISTA
    // ======================================================
    public List<LaboratorioDTO> listarPorLaboratorista() {
        List<LaboratorioDTO> lista = new ArrayList<>();

        String sql = """
            SELECT l.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente
            FROM laboratorios l
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            ORDER BY l.id_laboratorio DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LaboratorioDTO l = map(rs);
                l.setNombrePaciente(rs.getString("paciente"));
                lista.add(l);
            }

        } catch (Exception e) {
            System.out.println("Listar labs laboratorista: " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // LISTAR TODOS
    // ======================================================
    public List<LaboratorioDTO> listar() {
        List<LaboratorioDTO> lista = new ArrayList<>();

        String sql = """
            SELECT l.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM laboratorios l
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = l.id_medico
            ORDER BY l.id_laboratorio DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LaboratorioDTO l = map(rs);
                l.setNombrePaciente(rs.getString("paciente"));
                l.setNombreMedico(rs.getString("medico"));
                lista.add(l);
            }

        } catch (Exception e) {
            System.out.println("Listar labs: " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // LISTAR POR MÉDICO
    // ======================================================
    public List<LaboratorioDTO> listarPorMedico(int idMedico) {
        List<LaboratorioDTO> lista = new ArrayList<>();

        String sql = """
            SELECT l.*,
                   p.nombre AS nombre_paciente, p.apellido AS apellido_paciente,
                   u.nombre AS nombre_medico, u.apellido AS apellido_medico
            FROM laboratorios l
            INNER JOIN pacientes p ON l.id_paciente = p.id_paciente
            LEFT JOIN usuarios u ON l.id_medico = u.id_usuario
            WHERE l.id_medico = ?
            ORDER BY l.id_laboratorio DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LaboratorioDTO l = map(rs);

                    l.setNombrePaciente(
                        rs.getString("nombre_paciente") + " " +
                        rs.getString("apellido_paciente")
                    );

                    String nomMed = rs.getString("nombre_medico");
                    String apeMed = rs.getString("apellido_medico");

                    if (nomMed != null) {
                        l.setNombreMedico(nomMed + " " + apeMed);
                    } else {
                        l.setNombreMedico("No asignado");
                    }

                    lista.add(l);
                }
            }

        } catch (Exception e) {
            System.out.println("ERROR listarPorMedico(): " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // OBTENER
    // ======================================================
    public LaboratorioDTO obtener(int id) {
        LaboratorioDTO l = null;

        String sql = """
            SELECT l.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM laboratorios l
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = l.id_medico
            WHERE l.id_laboratorio = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    l = map(rs);
                    l.setNombrePaciente(rs.getString("paciente"));
                    l.setNombreMedico(rs.getString("medico"));
                }
            }

        } catch (Exception e) {
            System.out.println("Obtener lab: " + e.getMessage());
        }

        return l;
    }

    // ======================================================
    // INSERTAR
    // ======================================================
    public boolean insertar(LaboratorioDTO l) {
        String sql = """
            INSERT INTO laboratorios(id_paciente, id_medico, tipo_examen, estado)
            VALUES (?,?,?,?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, l.getIdPaciente());

            if (l.getIdMedico() == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, l.getIdMedico());

            ps.setString(3, l.getTipoExamen());
            ps.setString(4, l.getEstado());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Insertar lab: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    public boolean actualizar(LaboratorioDTO l) {
        String sql = """
            UPDATE laboratorios SET
                id_paciente=?, 
                id_medico=?, 
                tipo_examen=?, 
                estado=?
            WHERE id_laboratorio=?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, l.getIdPaciente());

            if (l.getIdMedico() == null)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, l.getIdMedico());

            ps.setString(3, l.getTipoExamen());
            ps.setString(4, l.getEstado());
            ps.setInt(5, l.getIdLaboratorio());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Actualizar lab: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // ELIMINAR
    // ======================================================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM laboratorios WHERE id_laboratorio=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Eliminar lab: " + e.getMessage());
            return false;
        }
    }
}