package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.PacienteDAO;
import modelo.dto.PacienteDTO;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;

@WebServlet("/PacienteServlet")
public class PacienteServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private PacienteDAO dao = new PacienteDAO();

    // ======================================================
    // GET → SOLO ELIMINAR
    // ======================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        if ("eliminar".equalsIgnoreCase(accion)) {
            eliminar(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "GET solo permite eliminar pacientes.");
    }

    // ======================================================
    // POST → GUARDAR / ACTUALIZAR / ELIMINAR
    // ======================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {
            resp.sendRedirect("app.jsp?mod=pacientes&error=Accion_no_valida");
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

            default:
                resp.sendRedirect("app.jsp?mod=pacientes&error=Accion_no_valida");
        }
    }

    // ======================================================
    // GUARDAR
    // ======================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PacienteDTO p = new PacienteDTO();

        p.setNombre(req.getParameter("nombre"));
        p.setApellido(req.getParameter("apellido"));
        p.setFechaNacimiento(req.getParameter("fecha_nacimiento"));
        p.setSexo(req.getParameter("sexo"));
        p.setDireccion(req.getParameter("direccion"));
        p.setTelefono(req.getParameter("telefono"));
        p.setEmail(req.getParameter("email"));

        dao.insertar(p);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "CREAR PACIENTE",
                    "Paciente: " + p.getNombre() + " " + p.getApellido()
            );
        }

        resp.sendRedirect("app.jsp?mod=pacientes&msg=Paciente_registrado");
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        PacienteDTO p = new PacienteDTO();

        p.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        p.setNombre(req.getParameter("nombre"));
        p.setApellido(req.getParameter("apellido"));
        p.setFechaNacimiento(req.getParameter("fecha_nacimiento"));
        p.setSexo(req.getParameter("sexo"));
        p.setDireccion(req.getParameter("direccion"));
        p.setTelefono(req.getParameter("telefono"));
        p.setEmail(req.getParameter("email"));

        dao.actualizar(p);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "ACTUALIZAR PACIENTE",
                    "Paciente ID: " + p.getIdPaciente()
            );
        }

        resp.sendRedirect("app.jsp?mod=pacientes&msg=Paciente_actualizado");
    }

    // ======================================================
    // ELIMINAR
    // ======================================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        dao.eliminar(id);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "ELIMINAR PACIENTE",
                    "Paciente ID: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=pacientes&msg=Paciente_eliminado");
    }
}