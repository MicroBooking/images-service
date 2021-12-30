package beans;

import classes.Image;
import converters.ImageConverter;
import entities.ImageEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class ImagesBean {
    private Logger log = Logger.getLogger(ImagesBean.class.getName());

    @Inject
    private EntityManager em;


    public List<Image> getImages(){
        TypedQuery<ImageEntity> query = em.createNamedQuery(
                "ImageEntity.getAll", ImageEntity.class);
        List<ImageEntity> resultList = query.getResultList();

        return resultList.stream().map(ImageConverter::toDto).collect(Collectors.toList());
    }

    public List<Image> getImagesForListing(Integer listingId){
        TypedQuery<ImageEntity> query = em.createNamedQuery(
                "ImageEntity.getByListingId", ImageEntity.class).setParameter("id", listingId);
        List<ImageEntity> resultList = query.getResultList();
        return resultList.stream().map(ImageConverter::toDto).collect(Collectors.toList());
    }

    public Image createImage(Image image) {
        ImageEntity imageEntity = ImageConverter.toEntity(image);

        try {
            beginTx();
            em.persist(imageEntity);
            commitTx();
        }
        catch (Exception e){
            rollbackTx();
        }

        return ImageConverter.toDto(imageEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }




}

