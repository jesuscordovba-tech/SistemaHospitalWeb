package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	BitacoraDAO bdao = new BitacoraDAO();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession(false);

        if (sesion != null) {
            sesion.invalidate(); // destruye sesión
        }

        response.sendRedirect("app.jsp"); // vuelve a cargar el login
    }
}