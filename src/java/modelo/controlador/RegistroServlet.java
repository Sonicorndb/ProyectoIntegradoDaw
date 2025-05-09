package modelo.controlador;

import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.entidades.Usuario;

/**
 *
 * @author sonic
 */
@WebServlet(name = "registroServlet", urlPatterns = {"/registroServlet"})
public class RegistroServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        EntityManager em = emf.createEntityManager();
        
        try {
            // Comprobar si el email ya existe
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            query.setParameter("email", email);

            Usuario usuarioExistente = null;
            try {
                usuarioExistente = query.getSingleResult(); //si obtenemos un resultado mediante el query, el email está en uso
            } catch (NoResultException e) {
                
            }

            if (usuarioExistente != null) {
                //Redirigimos con un mensaje de error
                response.sendRedirect("registro.jsp?error=El email ya está registrado.");
                return;
            }else{
                
                 // Si no existe, creamos el usuario
                em.getTransaction().begin();

                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setEmail(email);
                nuevoUsuario.setPassword(password);
                nuevoUsuario.setTipo("normal"); //Usuario normal por defecto

                em.persist(nuevoUsuario);
                em.getTransaction().commit();

                // Redirigir a login con mensaje de éxito
                response.sendRedirect("login.jsp?mensaje=Registro exitoso. Inicia sesión.");
            }

           
            
        } finally {
            em.close();
        }
    }
}
