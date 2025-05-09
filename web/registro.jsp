<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registro</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/formulario.css">

</head>
<body>
    
    <!-- Enlace a página principal -->
    <p><a href="pagina-principal">Volver</a></p>
    
    <h2>Registro de nuevo usuario</h2>
    
    <div class="contenedorForm">
        <form action="registroServlet" method="post">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre" required>

            <label for="email">Email:</label>
            <input type="email" id="email" name="email" required>

            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Registrarse</button>
        </form>
    </div>

    <p>¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>
</body>
</html>
