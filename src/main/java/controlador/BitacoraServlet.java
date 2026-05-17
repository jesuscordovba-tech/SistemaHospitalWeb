package controlador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import modelo.dao.BitacoraDAO;
import modelo.dto.UsuariosDTO;

@WebServlet("/BitacoraServlet")
public class BitacoraServlet extends HttpServlet {

    private BitacoraDAO bdao = new BitacoraDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession sesion = req.getSession();
        UsuariosDTO user = (UsuariosDTO) sesion.getAttribute("usuario");

        // PROTECCIÓN – SOLO ADMIN
        if (user == null || user.getIdRol() != 1) {
            resp.sendRedirect("app.jsp?error=" +
                URLEncoder.encode("No tienes permisos para ver la bitácora", StandardCharsets.UTF_8));
            return;
        }

        // Registrar que el admin consultó la bitácora
        bdao.registrar(
                user.getIdUsuario(),
                "CONSULTA BITÁCORA",
                "El usuario ADMIN (" + user.getNombreUsuario() + 
                ", ID=" + user.getIdUsuario() + ") visualizó la bitácora del sistema."
        );

        // Listar bitácora
        req.setAttribute("listaBitacora", bdao.listarBitacora());

        // Mostrar módulo
        resp.sendRedirect("app.jsp?mod=bitacora");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Este servlet no recibe POST, redirige a GET
        resp.sendRedirect("BitacoraServlet");
    }
}