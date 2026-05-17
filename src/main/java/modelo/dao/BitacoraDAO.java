package modelo.dao;

import config.Conexion;
import modelo.dto.BitacoraDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {

    /** Mapea los nombres reales de BD a tu DTO modificado */
	private BitacoraDTO map(ResultSet rs) throws SQLException {
	    BitacoraDTO b = new BitacoraDTO();

	    b.setIdBitacora(rs.getInt("id_bitacora"));
	    b.setIdUsuario(rs.getInt("id_usuario"));
	    b.setAccion(rs.getString("accion"));
	    b.setDetalle(rs.getString("detalle"));  // ← antes error
	    b.setFechaAccion(rs.getString("fecha_accion"));
	    b.setNombreUsuario(rs.getString("usuario"));

	    return b;
	}

    /** LISTAR BITÁCORA */
    public List<BitacoraDTO> listarBitacora() {
        List<BitacoraDTO> lista = new ArrayList<>();

        String sql = """
            SELECT 
                b.id_bitacora,
                b.id_usuario,
                b.accion,
                b.detalle,
                b.fecha_accion,
                CONCAT(u.nombre, ' ', u.apellido) AS usuario
            FROM bitacora b
            LEFT JOIN usuarios u ON b.id_usuario = u.id_usuario
            ORDER BY b.id_bitacora DESC
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error listarBitacora(): " + e.getMessage());
        }

        return lista;
    }

    /** REGISTRAR EN BITÁCORA */
    public void registrar(int idUsuario, String accion, String detalle) {
        String sql = """
            INSERT INTO bitacora(id_usuario, accion, detalle)
            VALUES (?,?,?)
        """;

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, accion);
            ps.setString(3, detalle);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error registrar bitácora: " + e.getMessage());
        }
    }
}