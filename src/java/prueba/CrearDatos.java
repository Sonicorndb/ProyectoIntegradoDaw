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
            creador.setNombre("David.DB");
            creador.setEmail("david@gmail.com");
            creador.setPassword("1234");
            creador.setTipo("normal");
            su.create(creador);

            // Publicación tipo informativa
            Publicacion pubInf = new Publicacion();
            pubInf.setTitulo("Las nuevas tecnologías");
            pubInf.setTipo(true); // true = informativa
            pubInf.setContenido("Es la primera vez en la historia que una innovación avanza tan rápidamente como lo han hecho las tecnologías digitales: en apenas veinte años han llegado a cerca del 50 % de la población del mundo en desarrollo, y han transformado las sociedades. Al mejorar la conectividad, la inclusión financiera, el acceso al comercio y a los servicios públicos, la tecnología puede ser un gran elemento igualador.\n" +
"\n" +
"En el sector de la salud, por ejemplo, las tecnologías de vanguardia que utilizan inteligencia artificial ayudan a salvar vidas, diagnosticar enfermedades y prolongar la esperanza de vida. En el ámbito de la educación, los entornos virtuales de aprendizaje y la formación a distancia han llevado los programas educativos a estudiantes que, de otro modo, quedarían excluidos. Los servicios públicos también son cada vez más accesibles y responsables gracias a sistemas que utilizan las cadenas de bloques y la burocracia es menos gravosa gracias a la ayuda de la inteligencia artificial. Los macrodatos también pueden contribuir a que las políticas y los programas sean más pertinentes y precisos.\n" +
"\n" +
"Sin embargo, quienes aún no están conectados siguen aislados de los beneficios de esta nueva era y quedan aún más rezagados. Muchas de las personas que se quedan atrás son mujeres, ancianos, personas con discapacidad o miembros de minorías étnicas o lingüísticas, grupos indígenas y residentes de zonas pobres o remotas. El ritmo de la conectividad se está ralentizando, e incluso invirtiendo, en algunos grupos. Por ejemplo, a nivel mundial, la proporción de mujeres que utilizan Internet es un 12 % inferior a la de los hombres. Si bien esta diferencia se redujo en la mayoría de las regiones entre 2013 y 2017, en los países menos adelantados aumentó del 30 % al 33 %.\n");
            pubInf.setRuta("");
            pubInf.setFechaPublicacion(new Date());
            pubInf.setUsuario(creador);
            sp.create(pubInf);

            // Publicación tipo proyecto
            Publicacion pubPro = new Publicacion();
            pubPro.setTitulo("CaramboloVR");
            pubPro.setTipo(false); // false = proyecto
            pubPro.setContenido("");
            pubPro.setRuta("proyects/CaramboloVR");
            pubPro.setFechaPublicacion(new Date());
            pubPro.setUsuario(creador);
            sp.create(pubPro);

            // Usuario 3: Comentarista sin publicaciones
            Usuario comentarista = new Usuario();
            comentarista.setNombre("Pepe123");
            comentarista.setEmail("pepe@gmail.com");
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
