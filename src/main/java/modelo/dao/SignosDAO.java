package modelo.dao;

import config.Conexion;
import modelo.dto.SignosDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignosDAO {

    /** ============================
           MAPEO → DTO
        ============================ */
    private SignosDTO map(ResultSet rs) throws SQLException {
        SignosDTO s = new SignosDTO();

        s.setIdSigno(rs.getInt("id_signo"));
        s.setIdCita(rs.getInt("id_cita"));
        s.setIdPaciente(rs.getInt("id_paciente"));

        int enf = rs.getInt("id_enfermero");
        s.setIdEnfermero(rs.wasNull() ? null : enf);

        s.setTemperatura(rs.getDouble("temperatura"));
        s.setPresion(rs.getString("presion"));
        s.setFrecuenciaCardiaca(rs.getInt("frecuencia_cardiaca"));
        s.setFrecuenciaRespiratoria(rs.getInt("frecuencia_respiratoria"));
        s.setSaturacion(rs.getInt("saturacion"));
        s.setFechaRegistro(rs.getString("fecha_registro"));

        return s;
    }

    /** ============================
           LISTAR
        ============================ */
    public List<SignosDTO> listar() {
        List<SignosDTO> lista = new ArrayList<>();

        String sql = """
            SELECT s.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS enfermero
            FROM signos_vitales s
            INNER JOIN pacientes p ON p.id_paciente = s.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = s.id_enfermero
            ORDER BY s.id_signo DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SignosDTO s = map(rs);
                s.setNombrePaciente(rs.getString("paciente"));
                s.setNombreEnfermero(rs.getString("enfermero"));
                lista.add(s);
            }

        } catch (Exception e) {
            System.out.println("Error listar signos: " + e.getMessage());
        }

        return lista;
    }

    /** ============================
           OBTENER
        ============================ */
    public SignosDTO obtener(int id) {
        String sql = "SELECT * FROM signos_vitales WHERE id_signo=?";
        SignosDTO s = null;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) s = map(rs);
            }

        } catch (Exception e) {
            System.out.println("Error obtener signo: " + e.getMessage());
        }

        return s;
    }

    /** ============================
           INSERTAR
        ============================ */
    public boolean insertar(SignosDTO s) {

        String sql = """
            INSERT INTO signos_vitales
            (id_cita, id_paciente, id_enfermero, temperatura, presion,
             frecuencia_cardiaca, frecuencia_respiratoria, saturacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getIdCita());
            ps.setInt(2, s.getIdPaciente());

            if (s.getIdEnfermero() == null)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, s.getIdEnfermero());

            ps.setDouble(4, s.getTemperatura());
            ps.setString(5, s.getPresion());
            ps.setInt(6, s.getFrecuenciaCardiaca());
            ps.setInt(7, s.getFrecuenciaRespiratoria());
            ps.setInt(8, s.getSaturacion());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error insertar signos: " + e.getMessage());
            return false;
        }
    }

    /** ============================
           ACTUALIZAR
        ============================ */
    public boolean actualizar(SignosDTO s) {

        String sql = """
            UPDATE signos_vitales SET
                temperatura=?, presion=?, frecuencia_cardiaca=?,
                frecuencia_respiratoria=?, saturacion=?
            WHERE id_signo=?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, s.getTemperatura());
            ps.setString(2, s.getPresion());
            ps.setInt(3, s.getFrecuenciaCardiaca());
            ps.setInt(4, s.getFrecuenciaRespiratoria());
            ps.setInt(5, s.getSaturacion());
            ps.setInt(6, s.getIdSigno());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizar signos: " + e.getMessage());
            return false;
        }
    }

    /** ============================
           ELIMINAR
        ============================ */
    public boolean eliminar(int id) {

        String sql = "DELETE FROM signos_vitales WHERE id_signo=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Error eliminar signos: " + e.getMessage());
            return false;
        }
    }

    /** ============================
      LISTAR POR PACIENTE
    ============================ */
    public List<SignosDTO> listarPorPaciente(int idPaciente) {
        List<SignosDTO> lista = new ArrayList<>();

        String sql = """
            SELECT s.*,
                   CONCAT(p.nombre,' ',p.apellido) AS paciente,
                   CONCAT(u.nombre,' ',u.apellido) AS enfermero
            FROM signos_vitales s
            INNER JOIN pacientes p ON p.id_paciente = s.id_paciente
            LEFT JOIN usuarios u ON u.id_usuario = s.id_enfermero
            WHERE s.id_paciente = ?
            ORDER BY s.id_signo DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SignosDTO s = map(rs);
                    s.setNombrePaciente(rs.getString("paciente"));
                    s.setNombreEnfermero(rs.getString("enfermero"));
                    lista.add(s);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listar signos por paciente: " + e.getMessage());
        }

        return lista;
    }

    /** ============================
      LISTAR POR ENFERMERO
    ============================ */
    public List<SignosDTO> listarPorEnfermero(int idEnfermero) {
        List<SignosDTO> lista = new ArrayList<>();

        String sql = "SELECT * FROM signos_vitales WHERE id_enfermero=? ORDER BY id_signo DESC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEnfermero);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }

        } catch (Exception e) {
            System.out.println("Error listar por enfermero: " + e.getMessage());
        }

        return lista;
    }

    /** ============================
      LISTAR POR MÉDICO
    ============================ */
    public List<SignosDTO> listarPorMedico(int idMedico) {
        List<SignosDTO> lista = new ArrayList<>();

        String sql = """
            SELECT s.*
            FROM signos_vitales s
            INNER JOIN citas c ON c.id_cita = s.id_cita
            WHERE c.id_medico = ?
            ORDER BY s.id_signo DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(map(rs));
            }

        } catch (Exception e) {
            System.out.println("Error listar por médico: " + e.getMessage());
        }

        return lista;
    }
}