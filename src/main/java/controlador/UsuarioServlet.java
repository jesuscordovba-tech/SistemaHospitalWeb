package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.UsuariosDAO;
import modelo.dao.PacienteDAO;
import modelo.dto.UsuariosDTO;
import modelo.dto.PacienteDTO;
import modelo.dao.BitacoraDAO;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private UsuariosDAO udao = new UsuariosDAO();
    private PacienteDAO pdao = new PacienteDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        if (accion == null || accion.isEmpty()) {
            resp.sendRedirect("app.jsp?mod=usuarios&error=accion_no_definida");
            return;
        }

        switch (accion) {
            case "guardar":
                guardar(req, resp);
                break;

            case "actualizar":
                actualizar(req, resp);
                break;

            case "eliminar":
                eliminar(req, resp);
                break;

            case "actualizarperfil":
                actualizarPerfil(req, resp);
                break;

            case "cambiarpassword":
                cambiarPassword(req, resp);
                break;

            default:
                resp.sendRedirect("app.jsp?mod=usuarios&error=accion_invalida");
        }
    }

    // ====================================================
    // GUARDAR USUARIO
    // ====================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UsuariosDTO u = new UsuariosDTO();

        u.setNombreUsuario(req.getParameter("nombre_usuario"));
        u.setPassword(req.getParameter("password"));
        u.setNombre(req.getParameter("nombre"));
        u.setApellido(req.getParameter("apellido"));
        u.setCorreo(req.getParameter("correo"));
        u.setTelefono(req.getParameter("telefono"));
        u.setIdRol(Integer.parseInt(req.getParameter("id_rol")));

        boolean ok = udao.guardarUsuario(u);

        // Crear automáticamente ficha de paciente si rol = 5
        if (ok && u.getIdRol() == 5) {

            PacienteDTO p = new PacienteDTO();
            p.setNombre(u.getNombre());
            p.setApellido(u.getApellido());
            p.setTelefono(u.getTelefono());
            p.setEmail(u.getCorreo());
            p.setSexo("Otro"); // valor por defecto

            pdao.insertar(p);

            int idPaciente = pdao.ultimoIdPaciente();
            udao.vincularPaciente(u.getNombreUsuario(), idPaciente);
        }

        // Registrar en bitácora
        UsuariosDTO admin = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (admin != null) {
            bdao.registrar(
                    admin.getIdUsuario(),
                    "CREAR USUARIO",
                    "Usuario: " + u.getNombreUsuario() + " Rol: " + u.getIdRol()
            );
        }

        resp.sendRedirect("app.jsp?mod=usuarios&msg=Usuario registrado");
    }

    // ====================================================
    // ACTUALIZAR USUARIO
    // ====================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UsuariosDTO u = new UsuariosDTO();

        u.setIdUsuario(Integer.parseInt(req.getParameter("id_usuario")));
        u.setNombreUsuario(req.getParameter("nombre_usuario"));
        u.setPassword(req.getParameter("password"));
        u.setNombre(req.getParameter("nombre"));
        u.setApellido(req.getParameter("apellido"));
        u.setTelefono(req.getParameter("telefono"));
        u.setCorreo(req.getParameter("correo"));
        u.setIdRol(Integer.parseInt(req.getParameter("id_rol")));

        udao.actualizarUsuario(u);

        // Registrar en bitácora
        UsuariosDTO admin = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (admin != null) {
            bdao.registrar(
                    admin.getIdUsuario(),
                    "ACTUALIZAR USUARIO",
                    "ID usuario actualizado: " + u.getIdUsuario()
            );
        }

        resp.sendRedirect("app.jsp?mod=usuarios&msg=Usuario actualizado");
    }

    // ====================================================
    // ELIMINAR
    // ====================================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        udao.eliminarUsuario(id);

        UsuariosDTO admin = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (admin != null) {
            bdao.registrar(
                    admin.getIdUsuario(),
                    "ELIMINAR USUARIO",
                    "ID eliminado: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=usuarios&msg=Usuario eliminado");
    }

    // ====================================================
    // ACTUALIZAR PERFIL DEL USUARIO LOGUEADO
    // ====================================================
    private void actualizarPerfil(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UsuariosDTO u = new UsuariosDTO();

        u.setIdUsuario(Integer.parseInt(req.getParameter("id_usuario")));
        u.setNombre(req.getParameter("nombre"));
        u.setApellido(req.getParameter("apellido"));
        u.setTelefono(req.getParameter("telefono"));
        u.setCorreo(req.getParameter("correo"));

        udao.actualizarDatosPerfil(u);

        UsuariosDTO user = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (user != null) {
            bdao.registrar(
                    user.getIdUsuario(),
                    "ACTUALIZAR PERFIL",
                    "Se actualizó su información personal"
            );
        }

        resp.sendRedirect("app.jsp?mod=perfil&msg=Perfil actualizado");
    }

    // ====================================================
    // CAMBIAR CONTRASEÑA
    // ====================================================
    private void cambiarPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id_usuario"));
        String actual = req.getParameter("password_actual");
        String nueva = req.getParameter("password_nueva");

        udao.actualizarPassword(id, actual, nueva);

        UsuariosDTO user = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (user != null) {
            bdao.registrar(
                    user.getIdUsuario(),
                    "CAMBIAR PASSWORD",
                    "Usuario ID: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=perfil&msg=Contraseña actualizada");
    }
}