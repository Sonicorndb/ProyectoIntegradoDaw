package modelo.controlador;

import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.entidades.Usuario;

/**
 *
 * @author sonic
 */
@WebServlet(name= "loginServlet", urlPatterns = {"/loginServlet"})
public class loginServlet extends HttpServlet {
    
    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        EntityManager em = emf.createEntityManager();
        
        // Buscamos el usuario con el email y la contraseña
        TypedQuery<Usuario> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password", 
            Usuario.class
        );
        query.setParameter("email", email);
        query.setParameter("password", password);

        List<Usuario> usuarios = query.getResultList();
        
        if (!usuarios.isEmpty()) {
            Usuario usuario = usuarios.get(0); //guardamos el usuario
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                session.setAttribute("usuario", usuario); // Guardamos el usuario en sesión
                response.sendRedirect("pagina-principal");
        } else {
            response.sendRedirect("login.jsp?error=Credenciales_Inválidas");
        }
        
        em.close();
    }
}
