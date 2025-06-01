<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page import="modelo.entidades.Publicacion"%>
<%@page import="modelo.entidades.Comentario"%>
<%@page import="modelo.entidades.Usuario"%>

<style>
.mensaje-exito {
    background-color: #F4A300;
    color: white;
    padding: 15px 20px;
    font-weight: bold;
    border-radius: 5px;
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 1000;
    box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    animation: desaparecer 4s forwards;
}

@keyframes desaparecer {
    0% { opacity: 1; }
    80% { opacity: 1; }
    100% { opacity: 0; display: none; }
}
</style>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="css/navbar.css">
    <link rel="stylesheet" href="css/index.css">
</head>
<body>
    
    <% 
    HttpSession sessionObj = request.getSession(false);
    Usuario usuario = (sessionObj != null) ? (Usuario) sessionObj.getAttribute("usuario") : null;
    %>


    <div class="navbar">
        <a href="pagina-principal" class="logo"><div class="logo">VirtualWorks</div></a>
        
        <div class="buscador">
            <form action="pagina-principal" method="get" class="buscador-form">
                <input type="text" name="query" placeholder="Buscar publicaciones..." value="<%= request.getParameter("query") != null ? request.getParameter("query") : "" %>">
                <button type="submit">Buscar</button>
            </form>
        </div>

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

    <c:if test="${param.success == '1' || param.success == 'publicacion'}">
        <div id="mensajeExito" class="mensaje-exito">
            ¡Publicación creada correctamente!
        </div>
    </c:if>

    <c:if test="${param.success == 'comentario'}">
        <div id="mensajeExito" class="mensaje-exito">
            ¡Comentario publicado correctamente!
        </div>
    </c:if>

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
                        <div class="contenido">
                            <strong>Contenido:</strong> ${pub.contenido}
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${usuario != null}">
                            <button id="botonJuego_${pub.id}" onclick="toggleJuego('${pub.id}', 'projects/${pub.titulo}/index.html')">Jugar</button>
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
                        <button type="submit" class="botonComentar">Comentar</button>
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
    const esMovil = window.innerWidth <= 800;

    // Función para salir del juego
    const cerrarJuego = () => {
        contenedor.innerHTML = "";
        boton.textContent = "Jugar";
        juegosActivos[pubId] = false;

        // Eliminar historial falso si fue añadido
        if (esMovil) {
            history.back();
        }
    };

    if (!juegosActivos[pubId]) {
        const iframe = document.createElement('iframe');
        iframe.src = rutaJuego;
        iframe.style.border = 'none';
        iframe.allowFullscreen = true;

        if (esMovil) {
            // Pantalla completa para móviles
            iframe.style.position = 'fixed';
            iframe.style.top = '0';
            iframe.style.left = '0';
            iframe.style.width = '100vw';
            iframe.style.height = '100vh';
            iframe.style.zIndex = '9999';
            iframe.style.backgroundColor = 'white';
            document.body.appendChild(iframe);

            // Añadir una entrada al historial para interceptar "atrás"
            history.pushState({ juego: true }, '');

            // Manejar botón atrás
            window.onpopstate = function (event) {
                if (event.state && event.state.juego) {
                    document.body.removeChild(iframe);
                    cerrarJuego();
                    window.onpopstate = null; // Restaurar comportamiento de botón atrás por defecto
                }
            };
        } else {
            // Escritorio: usar contenedor normal
            iframe.width = '1000px';
            iframe.height = '700px';
            contenedor.innerHTML = '';
            contenedor.appendChild(iframe);
        }

        boton.textContent = "Cerrar juego";
        juegosActivos[pubId] = true;
    } else {
        if (esMovil) {
            const iframe = document.querySelector("iframe[src='" + rutaJuego + "']");
            if (iframe) {
                document.body.removeChild(iframe);
            }
            history.back(); // Simula cerrar al retroceder
        } else {
            contenedor.innerHTML = "";
        }
        boton.textContent = "Jugar";
        juegosActivos[pubId] = false;
    }
}

setTimeout(() => {
        const mensaje = document.getElementById("mensajeExito");
        if (mensaje) mensaje.remove();
    }, 4000);

</script>



</html>
