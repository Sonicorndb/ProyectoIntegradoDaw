package modelo.entidades;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.entidades.Comentario;
import modelo.entidades.Usuario;

@Generated(value="EclipseLink-2.7.12.v20230209-rNA", date="2025-05-08T12:08:02")
@StaticMetamodel(Publicacion.class)
public class Publicacion_ { 

    public static volatile SingularAttribute<Publicacion, Boolean> tipo;
    public static volatile SingularAttribute<Publicacion, String> contenido;
    public static volatile SingularAttribute<Publicacion, String> ruta;
    public static volatile SingularAttribute<Publicacion, String> titulo;
    public static volatile SingularAttribute<Publicacion, Usuario> usuario;
    public static volatile SingularAttribute<Publicacion, Integer> id;
    public static volatile SingularAttribute<Publicacion, Date> fechaPublicacion;
    public static volatile ListAttribute<Publicacion, Comentario> comentarios;

}