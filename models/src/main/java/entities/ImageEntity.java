package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="image")
@NamedQueries(value = {
        @NamedQuery(name="ImageEntity.getAll",
                query="SELECT image FROM ImageEntity image"),
        @NamedQuery(name="ImageEntity.getById",
                query="SELECT image FROM ImageEntity image where image.id=:id"),
        @NamedQuery(name="ImageEntity.getByListingId",
                query="SELECT image FROM ImageEntity image where image.listingId=:id")
})
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name="listing_id")
    private Integer listingId;

    @Column(name="cloudinary_id")
    private String cloudinaryId;

    @Column(name="url")
    private String url;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public String getCloudinaryId() {
        return cloudinaryId;
    }

    public void setCloudinaryId(String cloudinaryId) {
        this.cloudinaryId = cloudinaryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
