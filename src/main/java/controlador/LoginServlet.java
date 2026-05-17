package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;
import modelo.dao.UsuariosDAO;
import modelo.dao.PacienteDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	BitacoraDAO bdao = new BitacoraDAO();
    private UsuariosDAO udao = new UsuariosDAO();
    private PacienteDAO pdao = new PacienteDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userName = req.getParameter("usuario");
        String password = req.getParameter("password");

        // ============================
        // 1) Verificar si el usuario existe
        // ============================
        UsuariosDTO userCheck = udao.buscarUsuarioPorNombre(userName);

        if (userCheck == null) {
            resp.sendRedirect("app.jsp?error=" +
                    URLEncoder.encode("No existe el usuario", StandardCharsets.UTF_8));
            return;
        }

        // ============================
        // 2) Validar contraseña
        // ============================
        UsuariosDTO u = udao.login(userName, password);

        if (u == null) {
            resp.sendRedirect("app.jsp?error=" +
                    URLEncoder.encode("Contraseña incorrecta", StandardCharsets.UTF_8));
            return;
        }

        // ============================
        // 3) Si es paciente, cargar ID del paciente
        // ============================
        if (u.getIdRol() == 5) {
            // Es un paciente → necesitamos su id_paciente
            PacienteDAO pdao = new PacienteDAO();
            int idPac = pdao.obtenerIdPorUsuario(u.getIdUsuario());
            u.setIdPaciente(idPac);
        }
        // ============================
        // 4) Guardar en sesión
        // ============================
        HttpSession sesion = req.getSession();
        sesion.setAttribute("usuario", u);

        resp.sendRedirect("app.jsp");
    }
}