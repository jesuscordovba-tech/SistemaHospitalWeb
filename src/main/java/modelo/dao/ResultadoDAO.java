package modelo.dao;

import config.Conexion;
import modelo.dto.ResultadoDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoDAO {

    // ============================
    // MAPEO ResultSet → DTO
    // ============================
    private ResultadoDTO map(ResultSet rs) throws SQLException {
        ResultadoDTO r = new ResultadoDTO();

        r.setIdResultado(rs.getInt("id_resultado"));
        r.setIdLaboratorio(rs.getInt("id_laboratorio"));
        r.setDescripcion(rs.getString("descripcion"));
        r.setValorResultado(rs.getString("valor_resultado"));
        r.setUnidadMedida(rs.getString("unidad_medida"));
        r.setFechaRegistro(rs.getString("fecha_registro"));

        return r;
    }

    // ============================
    // LISTAR TODOS LOS RESULTADOS
    // ============================
    public List<ResultadoDTO> listar() {
        List<ResultadoDTO> lista = new ArrayList<>();

        String sql = """
            SELECT r.*,
                   CONCAT(p.nombre,' ',p.apellido) AS nombre_paciente,
                   l.tipo_examen
            FROM resultado_laboratorio r
            INNER JOIN laboratorios l ON l.id_laboratorio = r.id_laboratorio
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            ORDER BY r.id_resultado DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ResultadoDTO r = map(rs);
                r.setNombrePaciente(rs.getString("nombre_paciente"));
                r.setTipoExamen(rs.getString("tipo_examen"));
                lista.add(r);
            }

        } catch (Exception e) {
            System.out.println("Listar resultados: " + e.getMessage());
        }

        return lista;
    }

    // ============================
    // OBTENER POR ID
    // ============================
    public ResultadoDTO obtener(int id) {

        String sql = "SELECT * FROM resultado_laboratorio WHERE id_resultado=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error obtener resultado: " + e.getMessage());
        }

        return null;
    }

    // ============================
    // INSERTAR
    // ============================
    public boolean insertar(ResultadoDTO r) {
        String sql = """
            INSERT INTO resultado_laboratorio(id_laboratorio,descripcion,valor_resultado,unidad_medida)
            VALUES(?,?,?,?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getIdLaboratorio());
            ps.setString(2, r.getDescripcion());
            ps.setString(3, r.getValorResultado());
            ps.setString(4, r.getUnidadMedida());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Insertar resultado: " + e.getMessage());
        }

        return false;
    }

    // ============================
    // ACTUALIZAR
    // ============================
    public boolean actualizar(ResultadoDTO r) {
        String sql = """
            UPDATE resultado_laboratorio SET
            descripcion=?, valor_resultado=?, unidad_medida=?
            WHERE id_resultado=?
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getDescripcion());
            ps.setString(2, r.getValorResultado());
            ps.setString(3, r.getUnidadMedida());
            ps.setInt(4, r.getIdResultado());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Actualizar resultado: " + e.getMessage());
        }

        return false;
    }

    // ============================
    // LISTAR POR MÉDICO
    // ============================
    public List<ResultadoDTO> listarResultadosPorMedico(int idMedico) {
        List<ResultadoDTO> lista = new ArrayList<>();

        String sql = """
            SELECT r.*, 
                   l.tipo_examen,
                   p.nombre AS nombre_paciente, 
                   p.apellido AS apellido_paciente
            FROM resultado_laboratorio r
            INNER JOIN laboratorios l ON r.id_laboratorio = l.id_laboratorio
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            WHERE l.id_medico = ?
            ORDER BY r.fecha_registro DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMedico);
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ResultadoDTO r = map(rs);
                    r.setNombrePaciente(rs.getString("nombre_paciente") + " " + rs.getString("apellido_paciente"));
                    r.setTipoExamen(rs.getString("tipo_examen"));
                    lista.add(r);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarResultadosPorMedico: " + e.getMessage());
        }

        return lista;
    }

    // ============================
    // LISTAR POR PACIENTE
    // ============================
    public List<ResultadoDTO> listarResultadosPorPaciente(int idPaciente) {
        List<ResultadoDTO> lista = new ArrayList<>();

        String sql = """
            SELECT r.*, 
                   l.tipo_examen,
                   p.nombre AS nombre_paciente, 
                   p.apellido AS apellido_paciente
            FROM resultado_laboratorio r
            INNER JOIN laboratorios l ON r.id_laboratorio = l.id_laboratorio
            INNER JOIN pacientes p ON p.id_paciente = l.id_paciente
            WHERE l.id_paciente = ?
            ORDER BY r.fecha_registro DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ResultadoDTO r = map(rs);
                    r.setNombrePaciente(rs.getString("nombre_paciente") + " " + rs.getString("apellido_paciente"));
                    r.setTipoExamen(rs.getString("tipo_examen"));
                    lista.add(r);
                }
            }

        } catch (Exception e) {
            System.out.println("Error listarResultadosPorPaciente: " + e.getMessage());
        }

        return lista;
    }

    // ============================
    // ELIMINAR
    // ============================
    public boolean eliminar(int id) {
        String sql = "DELETE FROM resultado_laboratorio WHERE id_resultado=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            System.out.println("Eliminar resultado: " + e.getMessage());
        }

        return false;
    }
}