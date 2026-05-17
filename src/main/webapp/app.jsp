<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>

<%@ page import="modelo.dto.UsuariosDTO" %>
<%@ page import="modelo.dto.PacienteDTO" %>
<%@ page import="modelo.dto.CitaDTO" %>
<%@ page import="modelo.dto.AtencionDTO" %>
<%@ page import="modelo.dto.SignosDTO" %>
<%@ page import="modelo.dto.LaboratorioDTO" %>
<%@ page import="modelo.dto.ResultadoDTO" %>

<%@ page import="modelo.dao.UsuariosDAO" %>
<%@ page import="modelo.dao.PacienteDAO" %>
<%@ page import="modelo.dao.CitasDAO" %>
<%@ page import="modelo.dao.AtencionDAO" %>
<%@ page import="modelo.dao.SignosDAO" %>
<%@ page import="modelo.dao.LaboratorioDAO" %>
<%@ page import="modelo.dao.ResultadoDAO" %>
<%@ page import="modelo.dao.BitacoraDAO" %>
<%@ page import="modelo.dto.BitacoraDTO" %>
<%@ page import="utils.SecurityUtils" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    HttpSession sesion = request.getSession(false);
    UsuariosDTO usuario = null;

    if (sesion != null) {
        usuario = (UsuariosDTO) sesion.getAttribute("usuario");
    }

    String mod = request.getParameter("mod");

    /* ================================
         MOSTRAR LOGIN SI NO HAY SESIÓN
       ================================ */
    if (usuario == null && mod == null) {
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hospital - Inicio de Sesión</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Roboto:wght@300;400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Roboto', sans-serif;
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            padding: 20px;
        }
        
        .login-container {
            display: flex;
            width: 100%;
            max-width: 1000px;
            background: white;
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            min-height: 600px;
        }
        
        .login-left {
            flex: 1;
            background: linear-gradient(135deg, #0c2461 0%, #1e3799 100%);
            color: white;
            padding: 50px 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        
        .login-right {
            flex: 1;
            padding: 60px 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        
        .hospital-logo {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }
        
        .hospital-logo i {
            font-size: 42px;
            margin-right: 15px;
            color: #4cd137;
        }
        
        .hospital-logo h1 {
            font-family: 'Poppins', sans-serif;
            font-size: 28px;
            font-weight: 700;
            color: white;
        }
        
        .welcome-text h2 {
            font-family: 'Poppins', sans-serif;
            font-size: 32px;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .welcome-text p {
            font-size: 16px;
            line-height: 1.6;
            opacity: 0.9;
            margin-bottom: 40px;
        }
        
        .features {
            margin-top: 30px;
        }
        
        .feature {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }
        
        .feature i {
            font-size: 20px;
            margin-right: 15px;
            color: #4cd137;
        }
        
        .login-form {
            width: 100%;
        }
        
        .login-form h3 {
            font-family: 'Poppins', sans-serif;
            font-size: 28px;
            color: #333;
            margin-bottom: 30px;
            font-weight: 600;
        }
        
        .form-group {
            margin-bottom: 25px;
            position: relative;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
            font-size: 15px;
        }
        
        .input-with-icon {
            position: relative;
        }
        
        .input-with-icon i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: #777;
            font-size: 18px;
        }
        
        .form-control {
            width: 100%;
            padding: 15px 15px 15px 50px;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 16px;
            transition: all 0.3s;
            background-color: #f8f9fa;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #3498db;
            background-color: white;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }
        
        .btn-login {
            width: 100%;
            padding: 16px;
            background: linear-gradient(to right, #3498db, #2c80b9);
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 10px;
            letter-spacing: 0.5px;
        }
        
        .btn-login:hover {
            background: linear-gradient(to right, #2980b9, #2573a7);
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(52, 152, 219, 0.3);
        }
        
        .error-message {
            background-color: #ffebee;
            color: #c62828;
            padding: 12px 15px;
            border-radius: 8px;
            margin-top: 20px;
            text-align: center;
            font-size: 14px;
            border-left: 4px solid #c62828;
        }
        
        .copyright {
            text-align: center;
            margin-top: 30px;
            color: #777;
            font-size: 14px;
        }
        
        @media (max-width: 768px) {
            .login-container {
                flex-direction: column;
                max-width: 450px;
            }
            
            .login-left {
                padding: 40px 30px;
            }
            
            .login-right {
                padding: 40px 30px;
            }
        }
    </style>
</head>
<body>

<div class="login-container">
    <div class="login-left">
        <div class="hospital-logo">
            <i class="fas fa-hospital-alt"></i>
            <h1>Sistema Hospitalario</h1>
        </div>
        
        <div class="welcome-text">
            <h2>Bienvenido</h2>
            <p>Acceda al sistema de gestión integral del hospital para administrar pacientes, citas, historiales médicos y más.</p>
        </div>
        
        <div class="features">
            <div class="feature">
                <i class="fas fa-shield-alt"></i>
                <div>
                    <h4>Sistema Seguro</h4>
                    <p>Protección de datos médicos confidenciales</p>
                </div>
            </div>
            
            <div class="feature">
                <i class="fas fa-user-md"></i>
                <div>
                    <h4>Gestión Integral</h4>
                    <p>Administración completa de pacientes y personal</p>
                </div>
            </div>
            
            <div class="feature">
                <i class="fas fa-history"></i>
                <div>
                    <h4>Historial Clínico</h4>
                    <p>Acceso rápido al historial médico completo</p>
                </div>
            </div>
        </div>
    </div>
    
    <div class="login-right">
        <form class="login-form" action="LoginServlet" method="post">
            <h3>Iniciar Sesión</h3>
            
            <div class="form-group">
                <label for="usuario">Usuario</label>
                <div class="input-with-icon">
                    <i class="fas fa-user"></i>
                    <input type="text" class="form-control" id="usuario" name="usuario" placeholder="Ingrese su usuario" required>
                </div>
            </div>
            
            <div class="form-group">
                <label for="password">Contraseña</label>
                <div class="input-with-icon">
                    <i class="fas fa-lock"></i>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Ingrese su contraseña" required>
                </div>
            </div>
            
            <button type="submit" class="btn-login">
                <i class="fas fa-sign-in-alt"></i> Entrar al Sistema
            </button>
            
            <% if (request.getParameter("error") != null) { %>
                <div class="error-message">
                    <i class="fas fa-exclamation-circle"></i> Usuario o contraseña incorrectos
                </div>
            <% } %>
            
            <div class="copyright">
                <p>© 2023 Sistema Hospitalario. Todos los derechos reservados.</p>
            </div>
        </form>
    </div>
</div>

</body>
</html>

<%
        return;
    }
%>


<%-- ===========================================================
       SISTEMA PRINCIPAL (SI HAY SESIÓN)
   =========================================================== --%>
   
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Sistema Hospitalario</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <style>
        body {
            font-family: Arial;
            margin: 0; padding: 0;
            background: #f4f6f9;
            display: flex;
        }

        /* ---------- SIDEBAR ---------- */
        .sidebar {
            width: 250px;
            background: #0c2461;
            height: 100vh;
            color: white;
            position: fixed;
        }
        .sidebar h2 {
            padding: 20px;
            text-align: center;
            background: #1e3799;
            margin: 0;
        }
        .sidebar a {
            display: block;
            padding: 15px 20px;
            color: white;
            text-decoration: none;
        }
        .sidebar a:hover, .sidebar .active {
            background: #1e3799;
        }

        .profile-box {
            padding: 20px;
            text-align: center;
            background: rgba(255,255,255,0.1);
        }
        .profile-box strong { font-size: 18px; }

        /* ---------- MAIN CONTENT ---------- */
        .main-content {
            margin-left: 250px;
            padding: 20px;
            width: calc(100% - 250px);
        }

        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
        }
        .card {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
        }
        .card h3 { margin: 0 0 10px; }
        .card .value { font-size: 32px; font-weight: bold; }
    </style>
</head>

<body>

    <!-- Sidebar -->
    <div class="sidebar">
        <h2><i class="fas fa-hospital-user"></i> Hospital</h2>

        <div class="profile-box">
            <strong><%= SecurityUtils.h(usuario.getNombre()) %> <%= SecurityUtils.h(usuario.getApellido()) %></strong><br>
            Rol: 
            <%
                switch(usuario.getIdRol()){
                    case 1: out.print("Administrador"); break;
                    case 2: out.print("Médico"); break;
                    case 3: out.print("Enfermero"); break;
                    case 4: out.print("Laboratorista"); break;
                    case 5: out.print("Paciente"); break;
                }
            %>
        </div>

        <a href="app.jsp" class="<%= (mod==null) ? "active" : "" %>"><i class="fas fa-home"></i> Inicio</a>

        <% if(usuario.getIdRol()==1){ %>
            <a href="app.jsp?mod=usuarios" class="<%= "usuarios".equals(mod) ? "active" : "" %>">
                <i class="fas fa-users"></i> Usuarios
            </a>
        <% } %>

        <% if(usuario.getIdRol()==1){ %>
    <a href="app.jsp?mod=pacientes" class="<%= "pacientes".equals(mod) ? "active" : "" %>">
        <i class="fas fa-user-injured"></i> Pacientes
    </a>
<% } %>

<% if(usuario.getIdRol()==1 || usuario.getIdRol()==2 || usuario.getIdRol()==3){ %>
    <a href="app.jsp?mod=citas" class="<%= "citas".equals(mod) ? "active" : "" %>">
        <i class="fas fa-calendar-check"></i> Citas
    </a>
<% } %>

        <% if(usuario.getIdRol()==1 || usuario.getIdRol()==2){ %>
            <a href="app.jsp?mod=atenciones" class="<%= "atenciones".equals(mod) ? "active" : "" %>">
                <i class="fas fa-stethoscope"></i> Atenciones
            </a>
        <% } %>

        <% if(usuario.getIdRol()==1 || usuario.getIdRol()==3){ %>
            <a href="app.jsp?mod=signos" class="<%= "signos".equals(mod) ? "active" : "" %>">
                <i class="fas fa-heartbeat"></i> Signos Vitales
            </a>
        <% } %>

     <%-- LABORATORIOS --%>
<% if(usuario.getIdRol() != 3){ %>  <%-- Enfermero NO ve laboratorios --%>
    <a href="app.jsp?mod=laboratorios" class="<%= "laboratorios".equals(mod) ? "active" : "" %>">
        <i class="fas fa-flask"></i> Laboratorios
    </a>
<% } %>

<%-- RESULTADOS --%>
<% if(usuario.getIdRol() != 3){ %> <%-- Enfermero tampoco ve resultados --%>
    <a href="app.jsp?mod=resultados" class="<%= "resultados".equals(mod) ? "active" : "" %>">
        <i class="fas fa-file-medical-alt"></i> Resultados
    </a>
<% } %>

<% if(usuario.getIdRol()==1){ %>
    <a href="BitacoraServlet" class="<%= "bitacora".equals(mod) ? "active" : "" %>">
        <i class="fas fa-book"></i> Bitácora
    </a>
<% } %>
        <a href="LogoutServlet"><i class="fas fa-sign-out-alt"></i> Cerrar Sesión</a>
    </div>

    <!-- MAIN CONTENT -->
    <div class="main-content">

<%
    /* ========================================================
       PANEL PRINCIPAL (si no se selecciona módulo)
       ======================================================== */
    if (mod == null) {
%>

<h2>Panel Principal</h2>

<div class="dashboard-cards">

    <div class="card">
        <h3><i class="fas fa-user-injured"></i> Pacientes</h3>
        <div class="value">
            <%= new PacienteDAO().listar().size() %>
        </div>
    </div>

    <div class="card">
        <h3><i class="fas fa-calendar-check"></i> Citas</h3>
        <div class="value">
            <%= new CitasDAO().listarCitas().size() %>
        </div>
    </div>

    <div class="card">
        <h3><i class="fas fa-flask"></i> Laboratorios</h3>
        <div class="value">
            <%= new LaboratorioDAO().listar().size() %>
        </div>
    </div>

    <div class="card">
        <h3><i class="fas fa-file-medical-alt"></i> Resultados</h3>
        <div class="value">
            <%= new ResultadoDAO().listar().size() %>
        </div>
    </div>

</div>

<% } %>
 <%
/* ============================================================
      MÓDULO: USUARIOS  
      Acceso: SOLO ADMIN (id_rol = 1)
   ============================================================ */
if ("usuarios".equals(mod) && usuario.getIdRol() == 1) {

    UsuariosDAO udao = new UsuariosDAO();
    String actionUser = request.getParameter("action");
    if (actionUser == null) actionUser = "listar";
%>

<div class="card">
    <h2>Gestión de Usuarios</h2>
    <p>Administre los usuarios con acceso al sistema.</p>
</div>

<%--------------------------------------------------------------
    FORMULARIO NUEVO USUARIO
  --------------------------------------------------------------%>
<%
if ("nuevo".equals(actionUser)) {
%>

<div class="card">
    <h3>Registrar Nuevo Usuario</h3>

    <form action="UsuarioServlet" method="post">
        <input type="hidden" name="accion" value="guardar">

        <label>Nombre de Usuario</label>
        <input type="text" name="nombre_usuario" required>

        <label>Contraseña</label>
        <input type="password" name="password" required>

        <label>Nombre</label>
        <input type="text" name="nombre" required>

        <label>Apellido</label>
        <input type="text" name="apellido" required>

        <label>Correo</label>
        <input type="email" name="correo">

        <label>Teléfono</label>
        <input type="text" name="telefono">

        <label>Rol</label>
        <select name="id_rol" required>
            <option value="">Seleccione rol</option>
            <option value="1">Administrador</option>
            <option value="2">Médico</option>
            <option value="3">Enfermero</option>
            <option value="4">Laboratorista</option>
            <option value="5">Paciente</option>
        </select>

        <button type="submit" class="btn-primary">Guardar</button>
        <a href="app.jsp?mod=usuarios" class="btn-outline">Cancelar</a>
    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
    FORMULARIO EDITAR USUARIO
  --------------------------------------------------------------%>
<%
if ("editar".equals(actionUser)) {

    int id = Integer.parseInt(request.getParameter("id"));
    UsuariosDTO u = udao.obtenerUsuario(id);
%>

<div class="card">
    <h3>Editar Usuario</h3>

    <form action="UsuarioServlet" method="post">
        <input type="hidden" name="accion" value="actualizar">
        <input type="hidden" name="id_usuario" value="<%= u.getIdUsuario() %>">

        <label>Nombre de Usuario</label>
        <input type="text" name="nombre_usuario" value="<%= SecurityUtils.h(u.getNombreUsuario()) %>" required>

        <label>Contraseña</label>
        <input type="password" name="password" value="<%= SecurityUtils.h(u.getPassword()) %>" required>

        <label>Nombre</label>
        <input type="text" name="nombre" value="<%= SecurityUtils.h(u.getNombre()) %>" required>

        <label>Apellido</label>
        <input type="text" name="apellido" value="<%= SecurityUtils.h(u.getApellido()) %>" required>

        <label>Correo</label>
        <input type="email" name="correo" value="<%= SecurityUtils.h(u.getCorreo()) %>">

        <label>Teléfono</label>
        <input type="text" name="telefono" value="<%= SecurityUtils.h(u.getTelefono()) %>">

        <label>Rol</label>
        <select name="id_rol" required>
            <option value="1" <%= (u.getIdRol()==1?"selected":"") %>>Administrador</option>
            <option value="2" <%= (u.getIdRol()==2?"selected":"") %>>Médico</option>
            <option value="3" <%= (u.getIdRol()==3?"selected":"") %>>Enfermero</option>
            <option value="4" <%= (u.getIdRol()==4?"selected":"") %>>Laboratorista</option>
            <option value="5" <%= (u.getIdRol()==5?"selected":"") %>>Paciente</option>
        </select>

        <button type="submit" class="btn-primary">Actualizar</button>
        <a href="app.jsp?mod=usuarios" class="btn-outline">Cancelar</a>
    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
    TABLA LISTADO DE USUARIOS
  --------------------------------------------------------------%>

<div class="card">
    <div style="display:flex; justify-content:space-between;">
        <h3>Usuarios Registrados</h3>
        <a href="app.jsp?mod=usuarios&action=nuevo" class="btn-success">
            <i class="fas fa-plus"></i> Nuevo Usuario
        </a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Nombre Completo</th>
                <th>Correo</th>
                <th>Rol</th>
                <th>Acciones</th>
            </tr>
        </thead>

        <tbody>
        <%
            List<UsuariosDTO> lista = udao.listarUsuarios();
            for (UsuariosDTO u : lista) {
        %>
            <tr>
                <td><%= u.getIdUsuario() %></td>
                <td><%= SecurityUtils.h(u.getNombreUsuario()) %></td>
                <td><%= SecurityUtils.h(u.getNombre()) %> <%= SecurityUtils.h(u.getApellido()) %></td>
                <td><%= SecurityUtils.h(u.getCorreo()) %></td>

                <td>
                    <%
                        switch(u.getIdRol()){
                            case 1: out.print("Administrador"); break;
                            case 2: out.print("Médico"); break;
                            case 3: out.print("Enfermero"); break;
                            case 4: out.print("Laboratorista"); break;
                            case 5: out.print("Paciente"); break;
                        }
                    %>
                </td>

                <td>
                    <a class="btn-warning btn-sm"
                       href="app.jsp?mod=usuarios&action=editar&id=<%= u.getIdUsuario() %>">
                        Editar
                    </a>

                    <a class="btn-danger btn-sm"
                       onclick="return confirm('¿Eliminar usuario?');"
                       href="UsuarioServlet?accion=eliminar&id=<%= u.getIdUsuario() %>">
                        Eliminar
                    </a>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

<%
} // FIN módulo usuarios
%>
<%
/* ============================================================
      MÓDULO: PACIENTES  
      Acceso: Admin (1), Médico (2), Enfermero (3)
   ============================================================ */
if ("pacientes".equals(mod) && (usuario.getIdRol()==1 || usuario.getIdRol()==2 || usuario.getIdRol()==3)) {

    PacienteDAO pdao = new PacienteDAO();
    String actionPac = request.getParameter("action");
    if (actionPac == null) actionPac = "listar";
%>

<div class="card">
    <h2>Gestión de Pacientes</h2>
</div>

<%--------------------------------------------------------------
     FORMULARIO NUEVO PACIENTE
  --------------------------------------------------------------%>
<%
if ("nuevo".equals(actionPac)) {
%>

<div class="card">
    <h3>Registrar Nuevo Paciente</h3>

    <form action="PacienteServlet" method="post">
        <input type="hidden" name="accion" value="guardar">

        <label>Nombre</label>
        <input type="text" name="nombre" required>

        <label>Apellido</label>
        <input type="text" name="apellido" required>

        <label>Fecha de Nacimiento</label>
        <input type="date" name="fecha_nacimiento" required>

        <label>Sexo</label>
        <select name="sexo" required>
            <option value="">Seleccione</option>
            <option value="M">Masculino</option>
            <option value="F">Femenino</option>
            <option value="Otro">Otro</option>
        </select>

        <label>Dirección</label>
        <input type="text" name="direccion">

        <label>Teléfono</label>
        <input type="text" name="telefono">

        <label>Email</label>
        <input type="email" name="email">

        <button type="submit" class="btn-primary">Guardar</button>
        <a href="app.jsp?mod=pacientes" class="btn-outline">Cancelar</a>
    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
     FORMULARIO EDITAR PACIENTE
  --------------------------------------------------------------%>
<%
if ("editar".equals(actionPac)) {

    int id = Integer.parseInt(request.getParameter("id"));
    PacienteDTO p = pdao.obtener(id);
%>

<div class="card">
    <h3>Editar Paciente</h3>

    <form action="PacienteServlet" method="post">
        <input type="hidden" name="accion" value="actualizar">
        <input type="hidden" name="id_paciente" value="<%= p.getIdPaciente() %>">

        <label>Nombre</label>
        <input type="text" name="nombre" value="<%= SecurityUtils.h(p.getNombre()) %>" required>

        <label>Apellido</label>
        <input type="text" name="apellido" value="<%= SecurityUtils.h(p.getApellido()) %>" required>

        <label>Fecha de Nacimiento</label>
        <input type="date" name="fecha_nacimiento" value="<%= SecurityUtils.h(p.getFechaNacimiento()) %>" required>

        <label>Sexo</label>
        <select name="sexo" required>
            <option value="M" <%= p.getSexo().equals("M")?"selected":"" %>>Masculino</option>
            <option value="F" <%= p.getSexo().equals("F")?"selected":"" %>>Femenino</option>
            <option value="Otro" <%= p.getSexo().equals("Otro")?"selected":"" %>>Otro</option>
        </select>

        <label>Dirección</label>
        <input type="text" name="direccion" value="<%= SecurityUtils.h(p.getDireccion()) %>">

        <label>Teléfono</label>
        <input type="text" name="telefono" value="<%= SecurityUtils.h(p.getTelefono()) %>">

        <label>Email</label>
        <input type="email" name="email" value="<%= SecurityUtils.h(p.getEmail()) %>">

        <button type="submit" class="btn-primary">Actualizar</button>
        <a href="app.jsp?mod=pacientes" class="btn-outline">Cancelar</a>
    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
     LISTA DE PACIENTES
  --------------------------------------------------------------%>

<%
List<PacienteDTO> listaPac = pdao.listar();
%>

<div class="card">
    <div style="display:flex; justify-content:space-between;">
        <h3>Listado de Pacientes</h3>
        <a href="app.jsp?mod=pacientes&action=nuevo" class="btn-success">Nuevo Paciente</a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Paciente</th>
                <th>Sexo</th>
                <th>Teléfono</th>
                <th>Email</th>
                <th>Opciones</th>
            </tr>
        </thead>

        <tbody>
        <% for(PacienteDTO p : listaPac) { %>
            <tr>
                <td><%= p.getIdPaciente() %></td>
                <td><%= SecurityUtils.h(p.getNombre()) + " " + SecurityUtils.h(p.getApellido()) %></td>
                <td><%= SecurityUtils.h(p.getSexo()) %></td>
                <td><%= SecurityUtils.h(p.getTelefono()) %></td>
                <td><%= SecurityUtils.h(p.getEmail()) %></td>

                <td>
                    <a class="btn-warning btn-sm"
                       href="app.jsp?mod=pacientes&action=editar&id=<%= p.getIdPaciente() %>">Editar</a>

                    <a class="btn-danger btn-sm"
                       onclick="return confirm('¿Eliminar paciente?');"
                       href="PacienteServlet?accion=eliminar&id=<%= p.getIdPaciente() %>">
                       Eliminar
                    </a>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

<%
} // FIN módulo pacientes
%>
<%
/* ============================================================
      MÓDULO: CITAS MÉDICAS
      Acceso:
        - Admin (1)
        - Médico (2)
        - Enfermero (3)
   ============================================================ */
if ("citas".equals(mod) && (usuario.getIdRol()==1 || usuario.getIdRol()==2 || usuario.getIdRol()==3)) {

    CitasDAO cdao = new CitasDAO();
    PacienteDAO pdao = new PacienteDAO();
    UsuariosDAO udao = new UsuariosDAO();

    String actionCit = (request.getParameter("action") == null)
                        ? "listar"
                        : request.getParameter("action");
%>

<div class="card">
    <h2>Gestión de Citas</h2>
</div>

<%--------------------------------------------------------------
    FORMULARIO NUEVA CITA
  --------------------------------------------------------------%>
<%
if ("nuevo".equals(actionCit)) {

    List<PacienteDTO> pacientes = pdao.listar();
    List<UsuariosDTO> medicos   = udao.listarMedicos();
%>

<div class="card">
    <h3>Registrar Nueva Cita Médica</h3>

    <form method="POST" action="CitaServlet">

        <input type="hidden" name="accion" value="guardar">

        <label>Paciente</label>
        <select name="id_paciente" required>
            <option value="">Seleccione</option>
            <% for (PacienteDTO p : pacientes) { %>
                <option value="<%= p.getIdPaciente() %>">
                    <%= SecurityUtils.h(p.getNombre()) %> <%= SecurityUtils.h(p.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Médico</label>
        <select name="id_medico" required>
            <option value="">Seleccione</option>
            <% for (UsuariosDTO m : medicos) { %>
                <option value="<%= m.getIdUsuario() %>">
                    <%= SecurityUtils.h(m.getNombre()) %> <%= SecurityUtils.h(m.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Fecha</label>
        <input type="date" name="fecha_cita" required>

        <label>Hora</label>
        <input type="time" name="hora_cita" required>

        <label>Motivo</label>
        <input type="text" name="motivo" required>

        <label>Estado</label>
        <select name="estado">
            <option value="Pendiente">Pendiente</option>
            <option value="Atendida">Atendida</option>
            <option value="Cancelada">Cancelada</option>
        </select>

        <label>Observaciones</label>
        <textarea name="observaciones"></textarea>

        <button type="submit" class="btn-primary">Guardar</button>
        <a href="app.jsp?mod=citas" class="btn-outline">Cancelar</a>

    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
    FORMULARIO EDITAR CITA
  --------------------------------------------------------------%>
<%
if ("editar".equals(actionCit)) {

    int id = Integer.parseInt(request.getParameter("id"));
    CitaDTO cita = cdao.obtenerCita(id);

    List<PacienteDTO> pacientes = pdao.listar();
    List<UsuariosDTO> medicos   = udao.listarMedicos();
%>

<div class="card">
    <h3>Editar Cita Médica</h3>

    <form method="POST" action="CitaServlet">

        <input type="hidden" name="accion" value="actualizar">
        <input type="hidden" name="id_cita" value="<%= cita.getIdCita() %>">

        <label>Paciente</label>
        <select name="id_paciente" required>
            <% for (PacienteDTO p : pacientes) { %>
                <option value="<%= p.getIdPaciente() %>"
                    <%= (cita.getIdPaciente()==p.getIdPaciente()) ? "selected" : "" %>>
                    <%= SecurityUtils.h(p.getNombre()) %> <%= SecurityUtils.h(p.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Médico</label>
        <select name="id_medico" required>
            <% for (UsuariosDTO m : medicos) { %>
                <option value="<%= m.getIdUsuario() %>"
                    <%= (cita.getIdMedico()==m.getIdUsuario()) ? "selected" : "" %>>
                    <%= SecurityUtils.h(m.getNombre()) %> <%= SecurityUtils.h(m.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Fecha</label>
        <input type="date" name="fecha_cita" value="<%= SecurityUtils.h(cita.getFechaCita()) %>" required>

        <label>Hora</label>
        <input type="time" name="hora_cita" value="<%= SecurityUtils.h(cita.getHoraCita()) %>" required>

        <label>Motivo</label>
        <input type="text" name="motivo" value="<%= SecurityUtils.h(cita.getMotivo()) %>" required>

        <label>Estado</label>
        <select name="estado">
            <option value="Pendiente"  <%= cita.getEstado().equals("Pendiente")?"selected":"" %>>Pendiente</option>
            <option value="Atendida"   <%= cita.getEstado().equals("Atendida")?"selected":"" %>>Atendida</option>
            <option value="Cancelada"  <%= cita.getEstado().equals("Cancelada")?"selected":"" %>>Cancelada</option>
        </select>

        <label>Observaciones</label>
        <textarea name="observaciones"><%= SecurityUtils.h(cita.getObservaciones()) %></textarea>

        <button type="submit" class="btn-primary">Actualizar</button>
        <a href="app.jsp?mod=citas" class="btn-outline">Cancelar</a>

    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
    LISTADO SEGÚN ROL
  --------------------------------------------------------------%>

<%
List<CitaDTO> listaCitas;

if (usuario.getIdRol() == 2) {  
    listaCitas = cdao.listarCitasPorMedico(usuario.getIdUsuario());
}
else if (usuario.getIdRol() == 5) {
    listaCitas = cdao.listarPorPaciente(usuario.getIdPaciente());
}
else {
    listaCitas = cdao.listarCitas();
}
%>

<div class="card">
    <div style="display:flex; justify-content:space-between;">
        <h3>Listado de Citas</h3>
        <a class="btn-success" href="app.jsp?mod=citas&action=nuevo">Nueva Cita</a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Paciente</th>
                <th>Médico</th>
                <th>Fecha</th>
                <th>Hora</th>
                <th>Motivo</th>
                <th>Estado</th>
                <th>Opciones</th>
            </tr>
        </thead>

        <tbody>
        <% for (CitaDTO c : listaCitas) { %>
            <tr>
                <td><%= c.getIdCita() %></td>
                <td><%= SecurityUtils.h(c.getNombrePaciente()) %></td>
                <td><%= SecurityUtils.h(c.getNombreMedico()) %></td>
                <td><%= SecurityUtils.h(c.getFechaCita()) %></td>
                <td><%= SecurityUtils.h(c.getHoraCita()) %></td>
                <td><%= SecurityUtils.h(c.getMotivo()) %></td>
                <td><%= SecurityUtils.h(c.getEstado()) %></td>

                <td>
                    <a class="btn-warning btn-sm"
                       href="app.jsp?mod=citas&action=editar&id=<%= c.getIdCita() %>">Editar</a>

                    <a class="btn-danger btn-sm"
                       onclick="return confirm('¿Eliminar cita?')"
                       href="CitaServlet?accion=eliminar&id=<%= c.getIdCita() %>">Eliminar</a>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

<%
} // FIN módulo citas
%>
<%
/* ============================================================
      MÓDULO: ATENCIONES MÉDICAS
      Acceso:
        - Admin (1)
        - Médico (2)
   ============================================================ */
if ("atenciones".equals(mod) && (usuario.getIdRol()==1 || usuario.getIdRol()==2)) {

    AtencionDAO adao = new AtencionDAO();
    PacienteDAO pdao = new PacienteDAO();
    CitasDAO cdao   = new CitasDAO();
    UsuariosDAO udao = new UsuariosDAO();

    String actionAt = (request.getParameter("action") == null)
                        ? "listar"
                        : request.getParameter("action");
%>

<div class="card">
    <h2>Gestión de Atenciones Médicas</h2>
</div>

<%--------------------------------------------------------------
    FORMULARIO NUEVA ATENCIÓN
  --------------------------------------------------------------%>
<%
if ("nuevo".equals(actionAt)) {

    List<PacienteDTO> pacientes = pdao.listar();
    List<CitaDTO> citas = cdao.listarCitas(); 
    List<UsuariosDTO> medicos = udao.listarMedicos();
%>

<div class="card">
    <h3>Nueva Atención Médica</h3>

    <form method="POST" action="AtencionServlet">
        <input type="hidden" name="action" value="insertar">

        <label>Paciente</label>
        <select name="id_paciente" required>
            <option value="">Seleccione</option>
            <% for (PacienteDTO p : pacientes) { %>
                <option value="<%= p.getIdPaciente() %>">
                    <%= SecurityUtils.h(p.getNombre()) %> <%= SecurityUtils.h(p.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Cita Asociada</label>
        <select name="id_cita" required>
            <option value="">Seleccione</option>
            <% for (CitaDTO c : citas) { %>
                <option value="<%= c.getIdCita() %>">
                    Cita #<%= c.getIdCita() %> - <%= SecurityUtils.h(c.getFechaCita()) %> <%= SecurityUtils.h(c.getHoraCita()) %>
                </option>
            <% } %>
        </select>

        <label>Médico</label>
        <select name="id_medico" required>
            <% for (UsuariosDTO m : medicos) { %>
                <option value="<%= m.getIdUsuario() %>">
                    <%= SecurityUtils.h(m.getNombre()) %> <%= SecurityUtils.h(m.getApellido()) %>
                </option>
            <% } %>
        </select>

        <label>Diagnóstico</label>
        <textarea name="diagnostico" required></textarea>

        <label>Tratamiento</label>
        <textarea name="tratamiento" required></textarea>

        <label>Receta</label>
        <textarea name="receta"></textarea>

        <label>Notas</label>
        <textarea name="notas"></textarea>

        <button type="submit" class="btn-primary">Guardar</button>
        <a href="app.jsp?mod=atenciones" class="btn-outline">Cancelar</a>

    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
    LISTADO CON FILTRO POR ROL
  --------------------------------------------------------------%>

<%
List<AtencionDTO> listaAten;

if (usuario.getIdRol() == 2) {  
    listaAten = adao.listarPorMedico(usuario.getIdUsuario());
}
else if (usuario.getIdRol() == 5) { 
    listaAten = adao.listarPorPaciente(usuario.getIdPaciente());
}
else { 
    listaAten = adao.listar();
}
%>

<div class="card">
    <div style="display:flex; justify-content:space-between;">
        <h3>Lista de Atenciones</h3>
        <a class="btn-success" href="app.jsp?mod=atenciones&action=nuevo">Nueva Atención</a>
    </div>

    <table class="data-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Paciente</th>
                <th>Médico</th>
                <th>ID Cita</th>
                <th>Diagnóstico</th>
                <th>Fecha</th>
                <th>Opciones</th>
            </tr>
        </thead>

        <tbody>
        <% for (AtencionDTO a : listaAten) { %>
            <tr>
                <td><%= a.getIdAtencion() %></td>
                <td><%= SecurityUtils.h(a.getNombrePaciente()) %></td>
                <td><%= SecurityUtils.h(a.getNombreMedico()) %></td>
                <td>#<%= a.getIdCita() %></td>
                <td><%= SecurityUtils.h(a.getDiagnostico()) %></td>
                <td><%= SecurityUtils.h(a.getFechaAtencion()) %></td>

                <td>
                    <a class="btn-warning btn-sm"
                       href="app.jsp?mod=atenciones&action=editar&id=<%= a.getIdAtencion() %>">Editar</a>

                    <a class="btn-danger btn-sm"
                       onclick="return confirm('¿Eliminar atención?')"
                       href="AtencionServlet?action=eliminar&id=<%= a.getIdAtencion() %>">Eliminar</a>
                </td>
            </tr>
        <% } %>
        </tbody>
    </table>
</div>

<%
} // FIN MÓDULO ATENCIONES
%>
<%
/* ============================================================
      MÓDULO: SIGNOS VITALES
      Acceso:
        - Enfermero (3)
        - Administrador (1)
   ============================================================ */

if ("signos".equals(mod) && (usuario.getIdRol()==1 || usuario.getIdRol()==3)) {

    SignosDAO sdao = new SignosDAO();
    PacienteDAO pdao = new PacienteDAO();
    CitasDAO cdao = new CitasDAO();

    String actionS = (request.getParameter("action") == null)
                        ? "listar"
                        : request.getParameter("action");

    int editId = (request.getParameter("edit") != null)
                    ? Integer.parseInt(request.getParameter("edit"))
                    : 0;

    SignosDTO signoEditar = (editId > 0) ? sdao.obtener(editId) : null;

    List<PacienteDTO> pacientes = pdao.listar();
    List<CitaDTO> citas = cdao.listarCitas();
    List<SignosDTO> lista = sdao.listar();
%>

<div class="card">
    <h2>Registro de Signos Vitales</h2>
</div>

<%--------------------------------------------------------------
    FORMULARIO NUEVO / EDITAR SIGNOS
  --------------------------------------------------------------%>

<%
if ("nuevo".equals(actionS) || "editar".equals(actionS)) {
%>

<div class="card">
    <h3><%= (signoEditar != null ? "Editar Signos Vitales" : "Registrar Signos Vitales") %></h3>

    <form method="post" action="SignosServlet">

        <input type="hidden" name="id_signo"
               value="<%= (signoEditar != null) ? signoEditar.getIdSigno() : "" %>">

        <input type="hidden" name="action"
               value="<%= (signoEditar != null) ? "actualizar" : "insertar" %>">

        <!-- PACIENTE -->
        <label>Paciente</label>
        <select name="id_paciente" required>
            <option value="">Seleccione</option>
            <% for (PacienteDTO p : pacientes) { %>
                <option value="<%= p.getIdPaciente() %>"
                    <%= (signoEditar != null && signoEditar.getIdPaciente() == p.getIdPaciente()) ? "selected" : "" %>>
                    <%= SecurityUtils.h(p.getNombre()) %> <%= SecurityUtils.h(p.getApellido()) %>
                </option>
            <% } %>
        </select>

        <!-- CITA -->
        <label>Cita Asociada</label>
        <select name="id_cita" required>
            <option value="">Seleccione</option>
            <% for (CitaDTO c : citas) { %>
                <option value="<%= c.getIdCita() %>"
                    <%= (signoEditar != null && signoEditar.getIdCita() == c.getIdCita()) ? "selected" : "" %>>
                    Cita #<%= c.getIdCita() %> — <%= SecurityUtils.h(c.getFechaCita()) %> <%= SecurityUtils.h(c.getHoraCita()) %>
                </option>
            <% } %>
        </select>

        <!-- CAMPOS DE SIGNOS -->
        <label>Temperatura (°C)</label>
        <input type="number" step="0.1" name="temperatura"
               value="<%= signoEditar != null ? signoEditar.getTemperatura() : "" %>" required>

        <label>Presión Arterial</label>
        <input type="text" name="presion"
               value="<%= signoEditar != null ? SecurityUtils.h(signoEditar.getPresion()) : "" %>" required>

        <label>Frecuencia Cardíaca (bpm)</label>
        <input type="number" name="frecuencia_cardiaca"
               value="<%= signoEditar != null ? signoEditar.getFrecuenciaCardiaca() : "" %>" required>

        <label>Frecuencia Respiratoria (rpm)</label>
        <input type="number" name="frecuencia_respiratoria"
               value="<%= signoEditar != null ? signoEditar.getFrecuenciaRespiratoria() : "" %>" required>

        <label>Saturación (%)</label>
        <input type="number" name="saturacion"
               value="<%= signoEditar != null ? signoEditar.getSaturacion() : "" %>" required>

        <button type="submit" class="btn-primary"
            style="margin-top:10px;">
            <%= (signoEditar != null) ? "Actualizar Registro" : "Guardar Registro" %>
        </button>

        <a href="app.jsp?mod=signos" class="btn-outline">Cancelar</a>

    </form>
</div>

<%
    return;
}
%>

<%--------------------------------------------------------------
          TABLA DE LISTADO DE SIGNOS VITALES
  --------------------------------------------------------------%>

<div class="card">
    <h3>Signos Vitales Registrados</h3>

    <a href="app.jsp?mod=signos&action=nuevo">
        <button class="btn-success" style="margin-bottom:10px;">+ Nuevo Registro</button>
    </a>

    <table width="100%" border="1">
        <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Cita</th>
            <th>Temp</th>
            <th>Presión</th>
            <th>FC</th>
            <th>FR</th>
            <th>Sat (%)</th>
            <th>Fecha</th>
            <th>Opciones</th>
        </tr>

        <% for (SignosDTO s : lista) { %>
        <tr>
            <td><%= s.getIdSigno() %></td>
            <td><%= SecurityUtils.h(s.getNombrePaciente()) %></td>
            <td>#<%= s.getIdCita() %></td>
            <td><%= s.getTemperatura() %> °C</td>
            <td><%= SecurityUtils.h(s.getPresion()) %></td>
            <td><%= s.getFrecuenciaCardiaca() %></td>
            <td><%= s.getFrecuenciaRespiratoria() %></td>
            <td><%= s.getSaturacion() %>%</td>
            <td><%= SecurityUtils.h(s.getFechaRegistro()) %></td>

            <td>
                <a class="btn-warning"
                   href="app.jsp?mod=signos&action=editar&edit=<%= s.getIdSigno() %>">Editar</a>

                <form action="SignosServlet" method="post" style="display:inline;">
    <input type="hidden" name="accion" value="eliminar">
    <input type="hidden" name="id" value="<%= s.getIdSigno() %>">
    <button onclick="return confirm('¿Eliminar?')" class="btn-danger">Eliminar</button>
</form>
            </td>
        </tr>
        <% } %>

    </table>
</div>

<%
} // FIN MÓDULO SIGNOS
%>
<%
/* ============================================================
      MÓDULO: LABORATORIOS
      Acceso:
        Admin (1) – CRUD completo
        Médico (2) – Solicitar examen + ver
        Laboratorista (4) – Solo lectura
        Paciente (5) – Solo lectura
        Enfermero (3) – Sin acceso
   ============================================================ */

if ("laboratorios".equals(mod)) {

    // Enfermero NO puede entrar
    if (usuario.getIdRol() == 3) {
        out.println("<h2 style='color:red;'>Acceso denegado</h2>");
        return;
    }

    LaboratorioDAO ldao = new LaboratorioDAO();
    PacienteDAO pdao = new PacienteDAO();
    UsuariosDAO udao = new UsuariosDAO();

    String actionLab = (request.getParameter("action") == null)
                        ? "listar"
                        : request.getParameter("action");

    // ---- para formulario editar ----
    int editId = (request.getParameter("id") != null)
                    ? Integer.parseInt(request.getParameter("id"))
                    : 0;
    LaboratorioDTO labEditar = (editId > 0) ? ldao.obtener(editId) : null;

    // ---- listado filtrado por rol ----
    List<LaboratorioDTO> lista;

    if (usuario.getIdRol() == 1) {                 // Admin
        lista = ldao.listar();
    } else if (usuario.getIdRol() == 2) {          // Médico
        lista = ldao.listarPorMedico(usuario.getIdUsuario());
    } else if (usuario.getIdRol() == 4) {          // Laboratorista
        lista = ldao.listar();
    } else {                                       // Paciente (5)
        lista = ldao.listarPorPaciente(usuario.getIdPaciente());
    }

    List<PacienteDTO> pacientes = pdao.listar();
    List<UsuariosDTO> medicos   = udao.listarMedicos();
%>

<div class="card">
    <h2>Gestión de Exámenes de Laboratorio</h2>
</div>

<%--------------------------------------------------------------
            LISTADO GENERAL
  --------------------------------------------------------------%>

<% if ("listar".equals(actionLab)) { %>

    <%-- SOLO admin y médico pueden crear nuevos exámenes --%>
    <% if (usuario.getIdRol() == 1 || usuario.getIdRol() == 2) { %>
        <a href="app.jsp?mod=laboratorios&action=nuevo">
            <button style="background:green;color:white;">+ Solicitar Examen</button>
        </a>
    <% } %>

    <table border="1" width="100%" style="margin-top:15px;">
        <tr>
            <th>ID</th>
            <th>Paciente</th>
            <th>Médico</th>
            <th>Tipo de Examen</th>
            <th>Estado</th>
            <th>Fecha Solicitud</th>

            <% if (usuario.getIdRol() == 1) { %>
                <th>Opciones</th>
            <% } %>
        </tr>

        <% for (LaboratorioDTO l : lista) { %>
        <tr>
            <td><%= l.getIdLaboratorio() %></td>
            <td><%= SecurityUtils.h(l.getNombrePaciente()) %></td>
            <td><%= (l.getNombreMedico() != null) ? SecurityUtils.h(l.getNombreMedico()) : "-" %></td>
            <td><%= SecurityUtils.h(l.getTipoExamen()) %></td>
            <td><%= SecurityUtils.h(l.getEstado()) %></td>
            <td><%= SecurityUtils.h(l.getFechaSolicitud()) %></td>

            <%-- SOLO ADMIN puede editar/eliminar --%>
            <% if (usuario.getIdRol() == 1) { %>
            <td>
                <a href="app.jsp?mod=laboratorios&action=editar&id=<%= l.getIdLaboratorio() %>">
                    Editar
                </a> |
                <a href="LaboratorioServlet?action=eliminar&id=<%= l.getIdLaboratorio() %>"
                   onclick="return confirm('¿Eliminar este examen?');"
                   style="color:red;">Eliminar</a>
            </td>
            <% } %>
        </tr>
        <% } %>

    </table>

<%  return; } %>

<%--------------------------------------------------------------
            FORMULARIO NUEVO / EDITAR
  --------------------------------------------------------------%>

<div class="card">
    <h3><%= (labEditar != null) ? "Editar Examen" : "Nuevo Examen" %></h3>

    <form method="post" action="LaboratorioServlet">

        <input type="hidden" name="id_laboratorio"
               value="<%= (labEditar != null) ? labEditar.getIdLaboratorio() : "" %>">

        <input type="hidden" name="action"
               value="<%= (labEditar != null) ? "actualizar" : "insertar" %>">

        <!-- PACIENTE -->
        <label>Paciente</label>
        <select name="id_paciente" required>
            <option value="">Seleccione</option>
            <% for (PacienteDTO p : pacientes) { %>
                <option value="<%= p.getIdPaciente() %>"
                    <%= (labEditar != null && labEditar.getIdPaciente() == p.getIdPaciente()) ? "selected" : "" %>>
                    <%= SecurityUtils.h(p.getNombre()) %> <%= SecurityUtils.h(p.getApellido()) %>
                </option>
            <% } %>
        </select>

        <!-- MÉDICO -->
        <label>Médico Solicitante</label>
        <select name="id_medico" required>
            <option value="">Seleccione</option>
            <% for (UsuariosDTO m : medicos) { %>
                <option value="<%= m.getIdUsuario() %>"
                    <%= (labEditar != null && m.getIdUsuario() == labEditar.getIdMedico()) ? "selected" : "" %>>
                    <%= SecurityUtils.h(m.getNombre()) %> <%= SecurityUtils.h(m.getApellido()) %>
                </option>
            <% } %>
        </select>

        <!-- TIPO EXAMEN -->
        <label>Tipo de Examen</label>
        <input type="text" name="tipo_examen"
               value="<%= (labEditar != null) ? SecurityUtils.h(labEditar.getTipoExamen()) : "" %>"
               required>

        <!-- ESTADO -->
        <label>Estado</label>
        <select name="estado" required>
            <option value="Pendiente"   <%= (labEditar != null && "Pendiente".equals(labEditar.getEstado())) ? "selected" : "" %>>Pendiente</option>
            <option value="Procesando"  <%= (labEditar != null && "Procesando".equals(labEditar.getEstado())) ? "selected" : "" %>>Procesando</option>
            <option value="Completo"    <%= (labEditar != null && "Completo".equals(labEditar.getEstado())) ? "selected" : "" %>>Completo</option>
        </select>

        <button type="submit" style="margin-top:10px;">
            <%= (labEditar != null) ? "Actualizar" : "Guardar" %>
        </button>

        <a href="app.jsp?mod=laboratorios" style="margin-left:10px;">Cancelar</a>
    </form>
</div>

<% } // FIN MÓDULO LABORATORIOS %>

<%
/* ============================================================
      MÓDULO: RESULTADOS DE LABORATORIO
      Acceso:
        Admin (1) – CRUD
        Laboratorista (4) – CRUD
        Médico (2) – Solo lectura
        Paciente (5) – Solo lectura
        Enfermero (3) – Sin acceso
   ============================================================ */

if ("resultados".equals(mod)) {

    // Enfermero NO puede entrar
    if (usuario.getIdRol() == 3) {
        out.println("<h2 style='color:red;'>Acceso denegado</h2>");
        return;
    }

    ResultadoDAO rdao = new ResultadoDAO();
    LaboratorioDAO ldao = new LaboratorioDAO();

    String actionRes = (request.getParameter("action") == null)
                        ? "listar"
                        : request.getParameter("action");

    // ---- para formulario editar ----
    int editId = (request.getParameter("edit") != null)
                    ? Integer.parseInt(request.getParameter("edit"))
                    : 0;
    ResultadoDTO resEditar = (editId > 0) ? rdao.obtener(editId) : null;

    // ---- listado filtrado por rol ----
    List<ResultadoDTO> lista;

    if (usuario.getIdRol() == 1 || usuario.getIdRol() == 4) {      // Admin o Laboratorista
        lista = rdao.listar();
    } else if (usuario.getIdRol() == 2) {                          // Médico
        lista = rdao.listarResultadosPorMedico(usuario.getIdUsuario());
    } else {                                                        // Paciente
        lista = rdao.listarResultadosPorPaciente(usuario.getIdPaciente());
    }

    List<LaboratorioDTO> examenes = ldao.listar();
%>

<div class="card">
    <h2>Resultados de Laboratorio</h2>
</div>

<%--------------------------------------------------------------
     LISTADO (VISTA GENERAL)
  --------------------------------------------------------------%>

<% if ("listar".equals(actionRes)) { %>

    <% if (usuario.getIdRol() == 1 || usuario.getIdRol() == 4) { %>
    <a href="app.jsp?mod=resultados&action=nuevo">
        <button style="background:green;color:white;">+ Nuevo Resultado</button>
    </a>
    <% } %>

    <table border="1" width="100%" style="margin-top:15px;">
        <tr>
            <th>ID</th>
            <th>Examen</th>
            <th>Valor</th>
            <th>Unidad</th>
            <th>Fecha</th>

            <% if (usuario.getIdRol() == 1 || usuario.getIdRol() == 4) { %>
                <th>Opciones</th>
            <% } %>
        </tr>

        <% for (ResultadoDTO r : lista) { 
               LaboratorioDTO lab = ldao.obtener(r.getIdLaboratorio());
        %>

        <tr>
            <td><%= r.getIdResultado() %></td>
            <td>Examen #<%= lab.getIdLaboratorio() %> - <%= SecurityUtils.h(lab.getTipoExamen()) %></td>
            <td><%= SecurityUtils.h(r.getValorResultado()) %></td>
            <td><%= SecurityUtils.h(r.getUnidadMedida()) %></td>
            <td><%= SecurityUtils.h(r.getFechaRegistro()) %></td>

            <% if (usuario.getIdRol() == 1 || usuario.getIdRol() == 4) { %>
            <td>
                <a href="app.jsp?mod=resultados&action=editar&edit=<%= r.getIdResultado() %>">
                    Editar
                </a> |
                <a href="ResultadoServlet?action=eliminar&id=<%= r.getIdResultado() %>"
                   onclick="return confirm('¿Eliminar resultado?');"
                   style="color:red;">Eliminar</a>
            </td>
            <% } %>
        </tr>

        <% } %>

    </table>

<%  return; } %>

<%--------------------------------------------------------------
        FORMULARIO NUEVO / EDITAR RESULTADO
  --------------------------------------------------------------%>

<div class="card">
    <h3><%= (resEditar != null) ? "Editar Resultado" : "Nuevo Resultado" %></h3>

    <form method="POST" action="ResultadoServlet">

        <input type="hidden" name="id_resultado"
               value="<%= (resEditar != null) ? resEditar.getIdResultado() : "" %>">

        <input type="hidden" name="action"
               value="<%= (resEditar != null) ? "actualizar" : "insertar" %>">

        <!-- LAB ASOCIADO -->
       <label>Laboratorio (solo exámenes pendientes)</label>
<select name="id_laboratorio" required>
    <option value="">Seleccione un examen pendiente</option>

    <%
        for (LaboratorioDTO ex : examenes) {

            // Si estoy creando un resultado nuevo (resEditar == null)
            // solo muestro exámenes en estado "Pendiente"
            if (resEditar == null && !"Pendiente".equalsIgnoreCase(ex.getEstado())) {
                continue; // saltar este examen
            }

            // Si estoy EDITANDO, sí dejo ver todos, pero marco seleccionado el suyo
            boolean seleccionado = (resEditar != null &&
                                    resEditar.getIdLaboratorio() == ex.getIdLaboratorio());
    %>
        <option value="<%= ex.getIdLaboratorio() %>" <%= seleccionado ? "selected" : "" %>>
            Examen #<%= ex.getIdLaboratorio() %> - <%= SecurityUtils.h(ex.getTipoExamen()) %>
            (Paciente: <%= SecurityUtils.h(ex.getNombrePaciente()) %>, Fecha: <%= SecurityUtils.h(ex.getFechaSolicitud()) %>)
        </option>
    <%
        }
    %>
</select>

        <!-- DESCRIPCIÓN -->
       <label>Descripción del resultado</label>
<textarea name="descripcion"
          placeholder="Ej.: Hemograma completo, perfil lipídico, glucosa en ayunas"
          required><%= (resEditar != null) ? SecurityUtils.h(resEditar.getDescripcion()) : "" %></textarea>
        
        
        <label>Valor del resultado</label>
<input type="text" name="valor_resultado"
       placeholder="Ej.: 13.5, 95, 120"
       value="<%= (resEditar != null) ? SecurityUtils.h(resEditar.getValorResultado()) : "" %>" required>
        <!-- UNIDAD -->
       
<label>Unidad de medida</label>
<input type="text" name="unidad_medida"
       placeholder="Ej.: g/dL, mg/dL, %"
       value="<%= (resEditar != null) ? SecurityUtils.h(resEditar.getUnidadMedida()) : "" %>" required>
        <button type="submit" style="margin-top:10px;">
            <%= (resEditar != null) ? "Actualizar" : "Guardar" %>
        </button>

        <a href="app.jsp?mod=resultados" style="margin-left:10px;">Cancelar</a>
    </form>
</div>

<% } // FIN MÓDULO RESULTADOS %>
<%
/* ============================================================
      MÓDULO: BITÁCORA
      Acceso: SOLO ADMIN (rol = 1)
   ============================================================ */
if ("bitacora".equals(mod) && usuario.getIdRol() == 1) {

    BitacoraDAO bdao = new BitacoraDAO();
    List<BitacoraDTO> listaBit = (List<BitacoraDTO>) request.getAttribute("listaBitacora");

    if (listaBit == null) {
        // Si alguien accede directo a app.jsp?mod=bitacora
        listaBit = bdao.listarBitacora();
    }
%>

<div class="card">
    <h2><i class="fas fa-book"></i> Bitácora del Sistema</h2>
    <p>Historial de acciones realizadas por los usuarios del sistema.</p>
</div>

<div class="card">

    <table class="data-table" width="100%">
        <thead>
            <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Acción</th>
                <th>Descripción</th>
                <th>Fecha y Hora</th>
            </tr>
        </thead>

        <tbody>
        <% for (BitacoraDTO b : listaBit) { %>
            <tr>
                <td><%= b.getIdBitacora() %></td>
               <td><%= SecurityUtils.h(b.getNombreUsuario()) %></td>
                <td><%= SecurityUtils.h(b.getAccion()) %></td>
                <td><%= SecurityUtils.h(b.getDetalle()) %></td>
                <td><%= SecurityUtils.h(b.getFechaAccion()) %></td>
            </tr>
        <% } %>
        </tbody>
    </table>

</div>

<%
    return;
} // FIN MÓDULO BITÁCORA
%>