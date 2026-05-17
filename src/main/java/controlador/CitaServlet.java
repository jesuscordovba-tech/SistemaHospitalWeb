package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.BitacoraDAO;
import modelo.dao.CitasDAO;
import modelo.dto.CitaDTO;
import modelo.dto.UsuariosDTO;

@WebServlet("/CitaServlet")
public class CitaServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private CitasDAO dao = new CitasDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Habilitar eliminar vía GET (si tu JSP lo usa así)
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if (accion == null) accion = req.getParameter("action");

        if (accion == null || accion.trim().isEmpty()) {
            resp.sendRedirect("app.jsp?mod=citas&error=Accion_no_definida");
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
                resp.sendRedirect("app.jsp?mod=citas&error=Accion_invalida");
        }
    }

    // =========================================
    //   GUARDAR NUEVA CITA
    // =========================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CitaDTO c = new CitaDTO();

        c.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        c.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        c.setFechaCita(req.getParameter("fecha_cita"));
        c.setHoraCita(req.getParameter("hora_cita"));
        c.setMotivo(req.getParameter("motivo"));
        c.setEstado(req.getParameter("estado"));
        c.setObservaciones(req.getParameter("observaciones"));

        dao.insertar(c);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(), 
                "CREAR CITA", 
                "Paciente ID: " + c.getIdPaciente() + ", Médico ID: " + c.getIdMedico()
            );
        }

        resp.sendRedirect("app.jsp?mod=citas&msg=Cita_guardada");
    }

    // =========================================
    //   ACTUALIZAR CITA
    // =========================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        CitaDTO c = new CitaDTO();

        c.setIdCita(Integer.parseInt(req.getParameter("id_cita")));
        c.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        c.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        c.setFechaCita(req.getParameter("fecha_cita"));
        c.setHoraCita(req.getParameter("hora_cita"));
        c.setMotivo(req.getParameter("motivo"));
        c.setEstado(req.getParameter("estado"));
        c.setObservaciones(req.getParameter("observaciones"));

        dao.actualizar(c);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ACTUALIZAR CITA",
                "Cita ID: " + c.getIdCita()
            );
        }

        resp.sendRedirect("app.jsp?mod=citas&msg=Cita_actualizada");
    }

    // =========================================
    //   ELIMINAR CITA
    // =========================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        dao.eliminar(id);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ELIMINAR CITA",
                "Cita ID: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=citas&msg=Cita_eliminada");
    }
}