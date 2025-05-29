/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package modelo.controlador;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import modelo.entidades.Publicacion;
import modelo.entidades.Usuario;

/**
 *
 * @author sonic
 */
@WebServlet("/crearPublicacion")
@MultipartConfig
public class CrearPublicacionServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "PI";
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String titulo = request.getParameter("titulo");
        boolean tipo = Boolean.parseBoolean(request.getParameter("tipo"));
        String contenido = request.getParameter("contenido");

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Validación de usuario
        if (usuario == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setTipo(tipo);
        publicacion.setFechaPublicacion(new Date());
        publicacion.setUsuario(usuario);

        if (tipo) { // Si es informativa
            publicacion.setContenido(contenido);
        } else { // Si es proyecto
            Part carpeta = request.getPart("carpeta");
            String fileName = Paths.get(carpeta.getSubmittedFileName()).getFileName().toString();

            if (!fileName.endsWith(".zip")) {
                request.setAttribute("error", "El archivo debe estar comprimido en formato .zip");
                request.getRequestDispatcher("crearPublicacion.jsp").forward(request, response);
                return;
            }

            // Validar que el nombre del archivo coincida con el título
            String nombreSinExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            if (!nombreSinExtension.equalsIgnoreCase(titulo)) {
                request.setAttribute("error", "El nombre del archivo ZIP debe coincidir con el título del proyecto.");
                request.getRequestDispatcher("crearPublicacion.jsp").forward(request, response);
                return;
            }

            // Ruta de guardado
            String rutaBase = getServletContext().getRealPath("/web/proyects/");
            String rutaDestino = rutaBase + File.separator + titulo;
            File carpetaDestino = new File(rutaDestino);
            carpetaDestino.mkdirs();

            // Guardar ZIP
            File archivoZip = new File(carpetaDestino, fileName);
            try (InputStream is = carpeta.getInputStream();
                 OutputStream os = new FileOutputStream(archivoZip)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }

            publicacion.setRuta("proyects/" + titulo);
        }

        // Persistencia
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(publicacion);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al guardar la publicación.");
            request.getRequestDispatcher("crearPublicacion.jsp").forward(request, response);
            return;
        } finally {
            em.close();
        }


        response.sendRedirect("pagina-principal");
    }
}



