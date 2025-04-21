package modelo.controlador;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import modelo.entidades.Publicacion;

import java.io.IOException;
import java.util.List;
import modelo.entidades.Comentario;

@WebServlet(name= "PaginaPrincipal", urlPatterns = {"/pagina-principal"})
public class paginaPrincipalServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String queryParam = request.getParameter("query"); //parámetro del filtro de búsqueda
        List<Publicacion> publicaciones;
        
        List<Comentario> Comentarios;

        EntityManager em = emf.createEntityManager();
        
        try {
            if (queryParam != null && !queryParam.trim().isEmpty()) {
                // Filtrar publicaciones por nombre o descripción
                TypedQuery<Publicacion> query = em.createQuery(
                    "SELECT e FROM Publicacion e WHERE LOWER(e.titulo) LIKE LOWER(:query) OR LOWER(e.descripcion) LIKE LOWER(:query)",Publicacion.class);
                query.setParameter("query", "%" + queryParam + "%");
                publicaciones = query.getResultList();
            } else {
                // Si no hay búsqueda, mostrar todas las publicaciones
                TypedQuery<Publicacion> query = em.createQuery(
                    "SELECT e FROM Publicacion e", Publicacion.class);
                publicaciones = query.getResultList();
            }
            
            TypedQuery<Comentario> queryOp = em.createQuery(
                    "SELECT o FROM Comentario o", Comentario.class);
                Comentarios = queryOp.getResultList();

            request.setAttribute("publicaciones", publicaciones);
            request.setAttribute("Comentarios", Comentarios);
            request.getRequestDispatcher("paginaPrincipal.jsp").forward(request, response);
        } finally {
            em.close();
        }
    }

    @Override
    public void destroy() {
        emf.close();
    }
}
