/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package modelo.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.entidades.Publicacion;
import modelo.entidades.Usuario;

/**
 *
 * @author sonic
 */
@WebServlet(name = "administracion", urlPatterns = {"/administracion"})
public class AdministracionServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = emf.createEntityManager();
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        request.setAttribute("usuarios", usuarios);

        String usuarioIdStr = request.getParameter("usuarioId");
        if (usuarioIdStr != null) {
            Long usuarioId = Long.parseLong(usuarioIdStr);
            Usuario usuario = em.find(Usuario.class, usuarioId);
            request.setAttribute("usuarioSeleccionado", usuario);

            List<Publicacion> publicaciones = em.createQuery(
                "SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId", Publicacion.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();
            request.setAttribute("publicaciones", publicaciones);
        }

        em.close();
        request.getRequestDispatcher("administracion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        try {
            if ("eliminarPublicacion".equals(accion)) {
                Integer pubId = Integer.parseInt(request.getParameter("pubId"));
                Publicacion pub = em.find(Publicacion.class, pubId);
                if (pub != null) {
                    // Eliminar comentarios primero si los hay
                    em.createQuery("DELETE FROM Comentario c WHERE c.publicacion.id = :id")
                        .setParameter("id", pubId).executeUpdate();

                    em.remove(pub);
                }
            } else if ("eliminarUsuario".equals(accion)) {
                Long usuarioId = Long.parseLong(request.getParameter("usuarioId"));

                // Eliminar comentarios del usuario
                em.createQuery("DELETE FROM Comentario c WHERE c.usuario.id = :id")
                    .setParameter("id", usuarioId).executeUpdate();

                // Eliminar publicaciones y sus comentarios
                List<Publicacion> pubs = em.createQuery("SELECT p FROM Publicacion p WHERE p.usuario.id = :id", Publicacion.class)
                    .setParameter("id", usuarioId).getResultList();

                for (Publicacion p : pubs) {
                    em.createQuery("DELETE FROM Comentario c WHERE c.publicacion.id = :id")
                        .setParameter("id", p.getId()).executeUpdate();
                    em.remove(em.contains(p) ? p : em.merge(p));
                }

                // Eliminar usuario
                Usuario usuario = em.find(Usuario.class, usuarioId);
                if (usuario != null) {
                    em.remove(usuario);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new ServletException("Error en la eliminación", e);
        } finally {
            em.close();
        }

        response.sendRedirect("administracion");
    }
}
