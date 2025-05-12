<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="modelo.entidades.Publicacion"%>
<%@page import="modelo.entidades.Comentario"%>
<%@page import="modelo.entidades.Usuario"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/navbar.css">
</head>
<body>
    
    <% 
    HttpSession sessionObj = request.getSession(false);
    Usuario usuario = (sessionObj != null) ? (Usuario) sessionObj.getAttribute("usuario") : null;
    %>


    <div class="navbar">
    <div class="logo">VirtualWorks</div>

    <div class="nav-links">
        <% if (usuario != null) { %>
            <span class="saludo">
                <p>Bienvenido, ${usuario.getNombre()}</p>
            </span>

            <a href="crearPublicacion.jsp">Crear publicación</a>
            <a href="logout">Cerrar sesión</a>
            
            <% if ("admin".equals(usuario.getTipo())) { %>
                <a href="administracion" class="admin">Administración</a>
            <% } %>

        <% } else { %>
            <a href="login.jsp">Login</a>
            <a href="registro.jsp">Regístrate</a>
        <% } %>
    </div>
</div>

        
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
                        <c:if test="${usuario != null}">
                            <button id="botonJuego_${pub.id}" onclick="toggleJuego('${pub.id}', 'proyects/${pub.titulo}/index.html')">Jugar</button>
                            <div id="juegoContainer_${pub.id}" style="margin-top:20px;"></div>
                        </c:if>
                        <c:if test="${usuario == null}">
                            <p><em>Inicia sesión para jugar el proyecto.</em></p>
                        </c:if>
                    </c:otherwise>
                </c:choose>

            </div>

            <p id="fecha"><strong>Fecha de publicación:</strong> 
                <fmt:formatDate value="${pub.fechaPublicacion}" pattern="d/M/yy" />
            </p>
            <p id="autor"><strong>Autor:</strong> ${pub.usuario.nombre} (${pub.usuario.email})</p>

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
                    
                <c:if test="${usuario != null}">
                    <form action="CrearComentarioServlet" method="post" class="form-comentario">
                        <input type="hidden" name="idPublicacion" value="${pub.id}" />
                        <textarea name="contenido" placeholder="Escribe un comentario" required></textarea>
                        <button type="submit">Comentar</button>
                    </form>
                </c:if>
            </div>


        </div>
    </c:forEach>
</body>

<script>
    const juegosActivos = {};

    function toggleJuego(pubId, rutaJuego) {
        const contenedor = document.getElementById("juegoContainer_" + pubId);
        const boton = document.getElementById("botonJuego_" + pubId);

        if (!juegosActivos[pubId]) {
            contenedor.innerHTML = `<iframe src="${rutaJuego}" width="100%" height="650px" style="border:none;"></iframe>`;
            boton.textContent = "Cerrar juego";
            juegosActivos[pubId] = true;
        } else {
            contenedor.innerHTML = "";
            boton.textContent = "Jugar";
            juegosActivos[pubId] = false;
        }
    }
</script>



</html>
