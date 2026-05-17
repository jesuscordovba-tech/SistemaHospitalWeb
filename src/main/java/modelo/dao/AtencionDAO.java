package modelo.dao;

import config.Conexion;
import modelo.dto.AtencionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AtencionDAO {

    private AtencionDTO map(ResultSet rs) throws SQLException {
        AtencionDTO a = new AtencionDTO();

        a.setIdAtencion(rs.getInt("id_atencion"));
        a.setIdPaciente(rs.getInt("id_paciente"));
        a.setIdMedico(rs.getInt("id_medico"));
        a.setIdCita(rs.getInt("id_cita"));
        a.setDiagnostico(rs.getString("diagnostico"));
        a.setTratamiento(rs.getString("tratamiento"));
        a.setReceta(rs.getString("receta"));
        a.setNotas(rs.getString("notas"));

        Timestamp fecha = rs.getTimestamp("fecha_atencion");
        a.setFechaAtencion(fecha != null ? fecha.toString() : null);

        return a;
    }

    // ======================================================
    // LISTAR
    // ======================================================
    public List<AtencionDTO> listar() {
        List<AtencionDTO> lista = new ArrayList<>();

        String sql = """
            SELECT a.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM atenciones a
            INNER JOIN pacientes p ON p.id_paciente = a.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = a.id_medico
            ORDER BY a.id_atencion DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AtencionDTO a = map(rs);
                a.setNombrePaciente(rs.getString("paciente"));
                a.setNombreMedico(rs.getString("medico")); // 👈 YA NO SERÁ NULL
                lista.add(a);
            }

        } catch (Exception e) {
            System.out.println("Error listar atenciones: " + e.getMessage());
        }

        return lista;
    }

    // ======================================================
    // OBTENER
    // ======================================================
    public AtencionDTO obtenerAtencion(int id) {

        String sql = """
            SELECT a.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM atenciones a
            INNER JOIN pacientes p ON p.id_paciente = a.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = a.id_medico
            WHERE a.id_atencion = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    AtencionDTO a = map(rs);
                    a.setNombrePaciente(rs.getString("paciente"));
                    a.setNombreMedico(rs.getString("medico"));
                    return a;
                }
            }

        } catch (Exception e) {
            System.out.println("Error obtener atencion: " + e.getMessage());
        }

        return null;
    }

    // ======================================================
    // INSERTAR
    // ======================================================
    public boolean insertar(AtencionDTO a) {

        String sql = """
            INSERT INTO atenciones(id_paciente, id_medico, id_cita,
                                   diagnostico, tratamiento, receta, notas)
            VALUES (?,?,?,?,?,?,?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getIdPaciente());
            ps.setInt(2, a.getIdMedico());
            ps.setInt(3, a.getIdCita());
            ps.setString(4, a.getDiagnostico());
            ps.setString(5, a.getTratamiento());
            ps.setString(6, a.getReceta());
            ps.setString(7, a.getNotas());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error insertar atencion: " + e.getMessage());
            return false;
        }
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    public boolean actualizar(AtencionDTO a) {

        String sql = """
            UPDATE atenciones SET
                id_paciente = ?,
                id_medico = ?,
                id_cita = ?,
                diagnostico = ?,
                tratamiento = ?,
                receta = ?,
                notas = ?
            WHERE id_atencion = ?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getIdPaciente());
            ps.setInt(2, a.getIdMedico());
            ps.setInt(3, a.getIdCita());
            ps.setString(4, a.getDiagnostico());
            ps.setString(5, a.getTratamiento());
            ps.setString(6, a.getReceta());
            ps.setString(7, a.getNotas());
            ps.setInt(8, a.getIdAtencion());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizar: " + e.getMessage());
            return false;
        }
    }
    public List<AtencionDTO> listarPorPaciente(int idPaciente) {
        List<AtencionDTO> lista = new ArrayList<>();

        String sql = """
            SELECT a.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM atenciones a
            INNER JOIN pacientes p ON p.id_paciente = a.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = a.id_medico
            WHERE a.id_paciente = ?
            ORDER BY a.id_atencion DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AtencionDTO a = map(rs);
                    a.setNombrePaciente(rs.getString("paciente"));
                    a.setNombreMedico(rs.getString("medico"));
                    lista.add(a);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarPorPaciente: " + e.getMessage());
        }

        return lista;
    }
    public List<AtencionDTO> listarPorMedico(int idMedico) {
        List<AtencionDTO> lista = new ArrayList<>();

        String sql = """
            SELECT a.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS medico
            FROM atenciones a
            INNER JOIN pacientes p ON p.id_paciente = a.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = a.id_medico
            WHERE a.id_medico = ?
            ORDER BY a.id_atencion DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AtencionDTO a = map(rs);
                    a.setNombrePaciente(rs.getString("paciente"));
                    a.setNombreMedico(rs.getString("medico"));
                    lista.add(a);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarPorMedico: " + e.getMessage());
        }

        return lista;
    }
    // ======================================================
    // ELIMINAR
    // ======================================================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM atenciones WHERE id_atencion = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error eliminar: " + e.getMessage());
            return false;
        }
    }
}