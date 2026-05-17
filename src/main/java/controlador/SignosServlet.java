package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.SignosDAO;
import modelo.dto.SignosDTO;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;

@WebServlet("/SignosServlet")
public class SignosServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private SignosDAO dao = new SignosDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("accion");
        if (accion == null) accion = req.getParameter("action");

        if (accion == null || accion.isEmpty()) {
            resp.sendRedirect("app.jsp?mod=signos&error=accion_no_definida");
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
                resp.sendRedirect("app.jsp?mod=signos&error=accion_invalida");
        }
    }

    // ======================================================
    // GUARDAR
    // ======================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        SignosDTO s = new SignosDTO();

        s.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        s.setIdCita(Integer.parseInt(req.getParameter("id_cita")));
        s.setTemperatura(Double.parseDouble(req.getParameter("temperatura")));
        s.setPresion(req.getParameter("presion"));
        s.setFrecuenciaCardiaca(Integer.parseInt(req.getParameter("frecuencia_cardiaca")));
        s.setFrecuenciaRespiratoria(Integer.parseInt(req.getParameter("frecuencia_respiratoria")));
        s.setSaturacion(Integer.parseInt(req.getParameter("saturacion")));

        dao.insertar(s);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "REGISTRAR SIGNOS",
                "Cita ID: " + s.getIdCita()
            );
        }

        resp.sendRedirect("app.jsp?mod=signos&msg=Signos registrados");
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        SignosDTO s = new SignosDTO();

        s.setIdSigno(Integer.parseInt(req.getParameter("id_signo")));
        s.setIdPaciente(Integer.parseInt(req.getParameter("id_paciente")));
        s.setIdCita(Integer.parseInt(req.getParameter("id_cita")));
        s.setTemperatura(Double.parseDouble(req.getParameter("temperatura")));
        s.setPresion(req.getParameter("presion"));
        s.setFrecuenciaCardiaca(Integer.parseInt(req.getParameter("frecuencia_cardiaca")));
        s.setFrecuenciaRespiratoria(Integer.parseInt(req.getParameter("frecuencia_respiratoria")));
        s.setSaturacion(Integer.parseInt(req.getParameter("saturacion")));

        dao.actualizar(s);

        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                u.getIdUsuario(),
                "ACTUALIZAR SIGNOS",
                "Signo ID: " + s.getIdSigno()
            );
        }

        resp.sendRedirect("app.jsp?mod=signos&msg=Signos actualizados");
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
                "ELIMINAR SIGNOS",
                "ID eliminado: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=signos&msg=Registro eliminado");
    }
}