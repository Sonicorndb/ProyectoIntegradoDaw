package modelo.servicio;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.entidades.Publicacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.entidades.Usuario;
import modelo.servicio.exceptions.NonexistentEntityException;

public class ServicioUsuario implements Serializable {

    public ServicioUsuario(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getPublicaciones() == null) {
            usuario.setPublicaciones(new ArrayList<Publicacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Publicacion> attachedPublicaciones = new ArrayList<>();
            for (Publicacion publicacionToAttach : usuario.getPublicaciones()) {
                publicacionToAttach = em.getReference(publicacionToAttach.getClass(), publicacionToAttach.getId());
                attachedPublicaciones.add(publicacionToAttach);
            }
            usuario.setPublicaciones(attachedPublicaciones);
            em.persist(usuario);
            for (Publicacion publicacion : usuario.getPublicaciones()) {
                Usuario oldUsuarioOfPublicacion = publicacion.getUsuario();
                publicacion.setUsuario(usuario);
                publicacion = em.merge(publicacion);
                if (oldUsuarioOfPublicacion != null) {
                    oldUsuarioOfPublicacion.getPublicaciones().remove(publicacion);
                    em.merge(oldUsuarioOfPublicacion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getId());
            List<Publicacion> publicacionesOld = persistentUsuario.getPublicaciones();
            List<Publicacion> publicacionesNew = usuario.getPublicaciones();
            List<Publicacion> attachedPublicacionesNew = new ArrayList<>();
            for (Publicacion publicacionToAttach : publicacionesNew) {
                publicacionToAttach = em.getReference(publicacionToAttach.getClass(), publicacionToAttach.getId());
                attachedPublicacionesNew.add(publicacionToAttach);
            }
            publicacionesNew = attachedPublicacionesNew;
            usuario.setPublicaciones(publicacionesNew);
            usuario = em.merge(usuario);
            for (Publicacion publicacionOld : publicacionesOld) {
                if (!publicacionesNew.contains(publicacionOld)) {
                    publicacionOld.setUsuario(null);
                    em.merge(publicacionOld);
                }
            }
            for (Publicacion publicacionNew : publicacionesNew) {
                if (!publicacionesOld.contains(publicacionNew)) {
                    Usuario oldUsuario = publicacionNew.getUsuario();
                    publicacionNew.setUsuario(usuario);
                    publicacionNew = em.merge(publicacionNew);
                    if (oldUsuario != null && !oldUsuario.equals(usuario)) {
                        oldUsuario.getPublicaciones().remove(publicacionNew);
                        em.merge(oldUsuario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (usuario.getId() == null || findUsuario(usuario.getId()) == null) {
                throw new NonexistentEntityException("El usuario con id " + usuario.getId() + " ya no existe.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El usuario con id " + id + " ya no existe.", enfe);
            }
            List<Publicacion> publicaciones = usuario.getPublicaciones();
            for (Publicacion publicacion : publicaciones) {
                publicacion.setUsuario(null);
                em.merge(publicacion);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
