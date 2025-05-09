/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package modelo.controlador;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.entidades.Comentario;
import modelo.entidades.Publicacion;
import modelo.entidades.Usuario;

/**
 *
 * @author sonic
 */
@WebServlet(name = "CrearComentarioServlet", urlPatterns = {"/CrearComentarioServlet"})
public class CrearComentarioServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String contenido = request.getParameter("contenido");
        String idPublicacionStr = request.getParameter("idPublicacion");

        if (contenido == null || contenido.trim().isEmpty() || idPublicacionStr == null) {
            response.sendRedirect("pagina-principal");
            return;
        }

        try {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            
            int idPublicacion = Integer.parseInt(idPublicacionStr);
            Publicacion publicacion = em.find(Publicacion.class, idPublicacion);

            if (publicacion == null) {
                response.sendRedirect("pagina-principal");
                return;
            }

            Comentario comentario = new Comentario();
            comentario.setContenido(contenido.trim());
            comentario.setUsuario(usuario);
            comentario.setPublicacion(publicacion);

            // Persistencia
            em.persist(publicacion);
            em.getTransaction().commit();
            em.close();

            

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("pagina-principal");
    }
}