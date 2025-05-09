package prueba;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modelo.entidades.Publicacion;
import modelo.entidades.Comentario;
import modelo.entidades.Usuario;
import modelo.servicio.ServicioComentario;
import modelo.servicio.ServicioPublicacion;
import modelo.servicio.ServicioUsuario;

@WebServlet(name = "CrearDatos", urlPatterns = {"/CrearDatos"})
public class CrearDatos extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PI");

        ServicioUsuario su = new ServicioUsuario(emf);
        ServicioPublicacion sp = new ServicioPublicacion(emf);
        ServicioComentario sc = new ServicioComentario(emf);

        EntityManager em = emf.createEntityManager();
        long count = (long) em.createQuery("SELECT COUNT(u) FROM Usuario u").getSingleResult();

        if (count == 0) {  // Si no hay registros en la tabla Usuario
        
            // Usuario 1: Administrador sin publicaciones ni comentarios
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword("admin");
            admin.setTipo("admin");
            su.create(admin);

            // Usuario 2: Con publicaciones de ambos tipos
            Usuario creador = new Usuario();
            creador.setNombre("Creador");
            creador.setEmail("creador@gmail.com");
            creador.setPassword("1234");
            creador.setTipo("normal");
            su.create(creador);

            // Publicación tipo informativa
            Publicacion pubInf = new Publicacion();
            pubInf.setTitulo("Las nuevas tecnologías");
            pubInf.setTipo(true); // true = informativa
            pubInf.setContenido("La importancia de las nuevas tecnologías enfocadas al desarrollo");
            pubInf.setRuta("");
            pubInf.setFechaPublicacion(new Date());
            pubInf.setUsuario(creador);
            sp.create(pubInf);

            // Publicación tipo proyecto
            Publicacion pubPro = new Publicacion();
            pubPro.setTitulo("CaramboloVR");
            pubPro.setTipo(false); // false = proyecto
            pubPro.setContenido("");
            pubPro.setRuta("projects/CaramboloVR");
            pubPro.setFechaPublicacion(new Date());
            pubPro.setUsuario(creador);
            sp.create(pubPro);

            // Usuario 3: Comentarista sin publicaciones
            Usuario comentarista = new Usuario();
            comentarista.setNombre("Usuario que comenta");
            comentarista.setEmail("coment@gmail.com");
            comentarista.setPassword("4321");
            comentarista.setTipo("normal");
            su.create(comentarista);

            // Comentarios del comentarista en ambas publicaciones
            Comentario com1 = new Comentario();
            com1.setUsuario(comentarista);
            com1.setPublicacion(pubInf);
            com1.setContenido("Muy interesante");
            sc.create(com1);

            Comentario com2 = new Comentario();
            com2.setUsuario(comentarista);
            com2.setPublicacion(pubPro);
            com2.setContenido("Me gusta mucho la idea");
            sc.create(com2);
        } else {
            System.out.println("La base de datos ya contiene datos. No se ejecutará el código.");
        }
        
        emf.close();
        response.sendRedirect("pagina-principal");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Datos de prueba creados</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Se han creado los datos de prueba correctamente</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet que crea datos de prueba para usuarios, publicaciones y comentarios";
    }
}
