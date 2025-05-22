<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Administración</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/admin.css">
</head>
<body>
    <p><a href="pagina-principal">Volver</a></p>
    
    <h1>Panel de Administración</h1>
    
    <h2>Usuarios registrados</h2>
    <ul>
        <c:forEach var="usuario" items="${usuarios}">
            <li>
                <form method="get" action="administracion">
                    <input type="hidden" name="usuarioId" value="${usuario.id}" />
                    <button type="submit">${usuario.nombre} (${usuario.email})</button>
                </form>
            </li>
        </c:forEach>
    </ul>

    <c:if test="${not empty publicaciones}">
        <h2>Publicaciones de ${usuarioSeleccionado.nombre}</h2>
        <ul>
            <c:forEach var="pub" items="${publicaciones}">
                <li>
                    <strong>${pub.titulo}</strong> - ${pub.tipo ? "Informativa" : "Proyecto"}
                    <form method="post" action="administracion" style="display:inline;">
                        <input type="hidden" name="accion" value="eliminarPublicacion" />
                        <input type="hidden" name="pubId" value="${pub.id}" />
                        <button type="submit">Eliminar publicación</button>
                    </form>
                </li>
            </c:forEach>
        </ul>

        <form method="post" action="administracion">
            <input type="hidden" name="accion" value="eliminarUsuario" />
            <input type="hidden" name="usuarioId" value="${usuarioSeleccionado.id}" />
            <button type="submit">Eliminar usuario y todo su contenido</button>
        </form>
    </c:if>
    <c:if test="${empty publicaciones}">
        <p>Este usuario no tiene publicaciones<p>
    </c:if>
</body>
</html>

