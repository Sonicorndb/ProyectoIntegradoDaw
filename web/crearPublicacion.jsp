<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Crear publicación</title>
    <link rel="stylesheet" href="css/crearPublicacion.css">
</head>
<body>
    <h2>Crear nueva publicación</h2>
    
    <c:if test = "${not empty error}">
        <div class="error">${error}</div>
    </c:if>
    <div class="contenedorForm">
        <form action="crearPublicacion" method="post" enctype="multipart/form-data">
            <label for="titulo">Título:</label>
            <input type="text" name="titulo" id="titulo" required><br><br>

            <label for="tipo">Tipo:</label>
            <select name="tipo" id="tipo" onchange="toggleCampos()">
                <option value="true">Informativa</option>
                <option value="false">Proyecto</option>
            </select><br><br>

            <div id="contenido">
                <label for="contenido">Contenido:</label>
                <textarea name="contenido" id="contenido" rows="5" cols="40"></textarea><br><br>
            </div>

            <div id="archivos" style="display: none;">
                <label for="carpeta">Carpeta del proyecto:</label>
                <input type="file" name="carpeta" id="carpeta" accept=".zip" ><br><br>
            </div>

            <button type="submit">Crear</button>
        </form>
    </div>

    <script>
        function toggleCampos() {
            const tipo = document.getElementById("tipo").value;
            document.getElementById("contenido").style.display = tipo === "true" ? "block" : "none";
            document.getElementById("archivos").style.display = tipo === "false" ? "block" : "none";
        }
        toggleCampos();
    </script>
</body>
</html>
