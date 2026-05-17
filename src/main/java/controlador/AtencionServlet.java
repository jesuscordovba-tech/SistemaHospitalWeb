package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.AtencionDAO;
import modelo.dto.AtencionDTO;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;
import modelo.dao.CitasDAO;

@WebServlet("/AtencionServlet")
public class AtencionServlet extends HttpServlet {

    BitacoraDAO bdao = new BitacoraDAO();
    AtencionDAO dao = new AtencionDAO();
    CitasDAO cdao = new CitasDAO(); // ← Necesario para actualizar estado

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if (accion == null) accion = req.getParameter("action");

        if ("eliminar".equals(accion)) {
            eliminar(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "GET solo permite eliminar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if (accion == null) accion = req.getParameter("action");

        if (accion == null || accion.isEmpty()) {
            resp.sendRedirect("app.jsp?mod=atenciones&error=Acción no especificada");
            return;
        }

        switch (accion) {

            case "guardar":
            case "insertar":
                guardar(req, resp);
                break;

            case "actualizar":
                actualizar(req, resp);
                break;

            case "eliminar":
                eliminar(req, resp);
                break;

            default:
                resp.sendRedirect("app.jsp?mod=atenciones&error=Acción inválida");
        }
    }

    // =======================================================
    // GUARDAR ATENCIÓN
    // =======================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AtencionDTO a = new AtencionDTO();

        a.setIdCita(Integer.parseInt(req.getParameter("id_cita")));
        a.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        a.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        a.setDiagnostico(req.getParameter("diagnostico"));
        a.setTratamiento(req.getParameter("tratamiento"));
        a.setReceta(req.getParameter("receta"));
        a.setNotas(req.getParameter("notas"));

        dao.insertar(a);

        // 🔥 MARCAR CITA COMO ATENDIDA
        cdao.actualizarEstado(a.getIdCita(), "Realizada");

        // BITÁCORA
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "REGISTRAR ATENCIÓN",
                "Paciente ID: " + a.getIdPaciente() + " | Cita ID: " + a.getIdCita()
            );
        }

        resp.sendRedirect("app.jsp?mod=atenciones&msg=Atención guardada");
    }

    // =======================================================
    // ACTUALIZAR ATENCIÓN
    // =======================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        AtencionDTO a = new AtencionDTO();

        a.setIdAtencion(Integer.parseInt(req.getParameter("id_atencion")));
        a.setIdCita(Integer.parseInt(req.getParameter("id_cita")));
        a.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        a.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        a.setDiagnostico(req.getParameter("diagnostico"));
        a.setTratamiento(req.getParameter("tratamiento"));
        a.setReceta(req.getParameter("receta"));
        a.setNotas(req.getParameter("notas"));

        dao.actualizar(a);

        // BITÁCORA
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ACTUALIZAR ATENCIÓN",
                "Atención ID: " + a.getIdAtencion()
            );
        }

        resp.sendRedirect("app.jsp?mod=atenciones&msg=Atención actualizada");
    }

    // =======================================================
    // ELIMINAR ATENCIÓN
    // =======================================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        dao.eliminar(id);

        // BITÁCORA
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ELIMINAR ATENCIÓN",
                "ID Atención: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=atenciones&msg=Atención eliminada");
    }
}