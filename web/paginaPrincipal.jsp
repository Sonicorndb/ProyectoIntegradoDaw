<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="modelo.entidades.Publicacion"%>
<%@page import="modelo.entidades.Comentario"%>
<%@page import="modelo.entidades.Usuario"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="index.css">
</head>
<body>
    <h1>Últimas publicaciones</h1>

    <c:forEach var="pub" items="${publicaciones}">
        <div class="publicacion">
            <h2>${pub.titulo}</h2>
            <p><strong>Tipo:</strong> <c:choose>
                <c:when test="${pub.tipo}">Informativa</c:when>
                <c:otherwise>Proyecto</c:otherwise>
            </c:choose></p>

            <div class="content">
                <c:choose>
                    <c:when test="${pub.tipo}">
                        <strong>Contenido:</strong> ${pub.contenido}
                    </c:when>
                    <c:otherwise>
                        <h2>CaramboloVR</h2>
                        <button id="botonJuego" onclick="toggleJuego()">Jugar</button>
                        <div id="juegoContainer" style="margin-top:20px;"></div>
                    </c:otherwise>
                </c:choose>
            </div>

            <p><strong>Fecha de publicación:</strong> ${pub.fechaPublicacion}</p>
            <p><strong>Autor:</strong> ${pub.usuario.nombre} (${pub.usuario.email})</p>

            <div class="comentarios">
                <h4>Comentarios:</h4>
                <c:set var="hayComentarios" value="false" />
                <ul>
                    <c:forEach var="comentario" items="${comentarios}">
                        <c:if test="${comentario.publicacion.id == pub.id}">
                            <c:set var="hayComentarios" value="true" />
                            <li>
                                <strong>${comentario.usuario.nombre}:</strong> ${comentario.contenido}
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
                <c:if test="${not hayComentarios}">
                    <p>No hay comentarios.</p>
                </c:if>
            </div>


        </div>
    </c:forEach>
</body>

<script>
    let juegoActivo = false;

        function toggleJuego() {
            const contenedor = document.getElementById("juegoContainer");
            const boton = document.getElementById("botonJuego");

            if (!juegoActivo) {
                contenedor.innerHTML = '<iframe src="projects/CaramboloVR/index.html" width="100%" height="650px" style="border:none;"></iframe>';
                boton.textContent = "Cerrar juego";
                juegoActivo = true;
            } else {
                contenedor.innerHTML = "";
                boton.textContent = "Jugar";
                juegoActivo = false;
            }
        }
</script>
</html>
