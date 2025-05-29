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
        <form id="registroForm" action="registroServlet" method="post" onsubmit="return validarFormulario()">
            <label for="nombre">Nombre:</label>
            <input type="text" id="nombre" name="nombre">

            <label for="email">Email:</label>
            <input type="email" id="email" name="email">

            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password">

            <button type="submit">Registrarse</button>
            
            <div id="mensajeError" style="color: red; margin-top: 10px;"></div>
        </form>
        
    </div>

    <p>¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a></p>

    <script>
        function validarFormulario() {
            const nombre = document.getElementById("nombre").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();
            const mensajeError = document.getElementById("mensajeError");

            mensajeError.textContent = "";

            if (nombre === "" || email === "" || password === "") {
                mensajeError.textContent = "Todos los campos son obligatorios.";
                return false;
            }

            const emailRegex = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
            if (!emailRegex.test(email)) {
                mensajeError.textContent = "Introduce un email válido.";
                return false;
            }

            if (password.length < 6) {
                mensajeError.textContent = "La contraseña debe tener al menos 6 caracteres.";
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
