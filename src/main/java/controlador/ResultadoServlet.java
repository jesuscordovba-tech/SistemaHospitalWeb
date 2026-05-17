package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import modelo.dao.ResultadoDAO;
import modelo.dto.ResultadoDTO;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;

@WebServlet("/ResultadoServlet")
public class ResultadoServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();
    private ResultadoDAO dao = new ResultadoDAO();

    // ======================================================
    // GET → SOLO ELIMINAR
    // ======================================================
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
                "GET solo permite eliminar resultados.");
    }

    // ======================================================
    // POST → INSERTAR / ACTUALIZAR / ELIMINAR
    // ======================================================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String accion = req.getParameter("action");
        if (accion == null) accion = req.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {
            resp.sendRedirect("app.jsp?mod=resultados&error=accion_no_definida");
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
                resp.sendRedirect("app.jsp?mod=resultados&error=accion_no_valida");
        }
    }

    // ======================================================
    // GUARDAR
    // ======================================================
    private void guardar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ResultadoDTO r = new ResultadoDTO();

        r.setIdLaboratorio(Integer.parseInt(req.getParameter("id_laboratorio")));
        r.setValorResultado(req.getParameter("valor_resultado"));
        r.setUnidadMedida(req.getParameter("unidad_medida"));
        r.setDescripcion(req.getParameter("descripcion"));

        dao.insertar(r);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "REGISTRAR RESULTADO",
                    "Laboratorio ID: " + r.getIdLaboratorio()
            );
        }

        resp.sendRedirect("app.jsp?mod=resultados&msg=Resultado registrado");
    }

    // ======================================================
    // ACTUALIZAR
    // ======================================================
    private void actualizar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ResultadoDTO r = new ResultadoDTO();

        r.setIdResultado(Integer.parseInt(req.getParameter("id_resultado")));
        r.setIdLaboratorio(Integer.parseInt(req.getParameter("id_laboratorio")));
        r.setValorResultado(req.getParameter("valor_resultado"));
        r.setUnidadMedida(req.getParameter("unidad_medida"));
        r.setDescripcion(req.getParameter("descripcion"));

        dao.actualizar(r);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "ACTUALIZAR RESULTADO",
                    "Resultado ID: " + r.getIdResultado()
            );
        }

        resp.sendRedirect("app.jsp?mod=resultados&msg=Resultado actualizado");
    }

    // ======================================================
    // ELIMINAR
    // ======================================================
    private void eliminar(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        dao.eliminar(id);

        // Registrar en bitácora
        UsuariosDTO u = (UsuariosDTO) req.getSession().getAttribute("usuario");
        if (u != null) {
            bdao.registrar(
                    u.getIdUsuario(),
                    "ELIMINAR RESULTADO",
                    "Resultado ID: " + id
            );
        }

        resp.sendRedirect("app.jsp?mod=resultados&msg=Resultado eliminado");
    }
}