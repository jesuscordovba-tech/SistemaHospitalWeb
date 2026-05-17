package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.BitacoraDAO;
import modelo.dao.LaboratorioDAO;
import modelo.dto.LaboratorioDTO;
import modelo.dto.UsuariosDTO;

@WebServlet("/LaboratorioServlet")
public class LaboratorioServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private LaboratorioDAO dao = new LaboratorioDAO();

    // =====================================================
    //                  GET → Solo eliminar
    // =====================================================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("action");
        if (accion == null) accion = req.getParameter("accion");

        if ("eliminar".equalsIgnoreCase(accion)) {
            eliminar(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "GET solo permite eliminar registros.");
    }

    // =====================================================
    //                     POST → CRUD
    // =====================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("action");
        if (accion == null) accion = req.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {
            resp.sendRedirect("app.jsp?mod=laboratorios&error=Accion_no_definida");
            return;
        }

        switch (accion) {
            case "insertar":
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
                resp.sendRedirect("app.jsp?mod=laboratorios&error=Accion_no_valida");
        }
    }

    // =====================================================
    //                    GUARDAR NUEVO
    // =====================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LaboratorioDTO l = new LaboratorioDTO();

        l.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        l.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        l.setTipoExamen(req.getParameter("tipo_examen"));

        // Estado inicial obligatorio
        l.setEstado("Pendiente");

        dao.insertar(l);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "CREAR EXAMEN",
                "Paciente ID: " + l.getIdPaciente() + ", Médico ID: " + l.getIdMedico()
            );
        }

        resp.sendRedirect("app.jsp?mod=laboratorios&msg=Solicitud_creada");
    }

    // =====================================================
    //                    ACTUALIZAR
    // =====================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LaboratorioDTO l = new LaboratorioDTO();

        l.setIdLaboratorio(Integer.parseInt(req.getParameter("id_laboratorio")));
        l.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        l.setIdMedico(Integer.parseInt(req.getParameter("id_medico")));
        l.setTipoExamen(req.getParameter("tipo_examen"));
        l.setEstado(req.getParameter("estado"));

        dao.actualizar(l);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ACTUALIZAR EXAMEN",
                "Examen ID: " + l.getIdLaboratorio()
            );
        }

        resp.sendRedirect("app.jsp?mod=laboratorios&msg=Solicitud_actualizada");
    }

    // =====================================================
    //                    ELIMINAR
    // =====================================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        dao.eliminar(id);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ELIMINAR EXAMEN",
                "Examen ID: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=laboratorios&msg=Solicitud_eliminada");
    }
}