<%-- 
    Document   : login
    Created on : 26 feb 2025, 10:48:54
    Author     : usuario
--%>

<%@page contentType="text/html" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>JSP Page</title>
        <link rel="stylesheet" href="css/index.css">
        <link rel="stylesheet" href="css/formulario.css">
    </head>
    <body>
        <!-- Enlace a p�gina principal -->
        <p><a href="pagina-principal">Volver</a></p>
            
        <h2>Inicio de sesi�n</h2>
        
        <div class="contenedorForm">
            <form action="loginServlet" method="post">
                <label>Email:</label>
                <input type="email" name="email" required>
                <label>Contrase�a:</label>
                <input type="password" name="password" required>
                <button type="submit">Iniciar sesi�n</button>
            </form>
        </div>
        
        <p>Si no tienes una cuenta <a href="registro.jsp">registrate aqu�</a></p>
    
        <h2 class="mensajeError"></h2>
    </body>
</html>
