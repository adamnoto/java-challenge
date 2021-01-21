package jp.co.axa.apidemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class represents all the applications that could
 * access the microservice. (This microservice assumes that
 * accessors are managed through the database).
 */
@Entity
@Table(name = "APP")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class App {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="SECRET_USERNAME", unique = true)
    private String secretUserName;

    @Column(name="SECRET_PASSWORD")
    private String secretPassword;

    @Column(name="RIGHTS")
    @Convert(converter = AppRight.ListConverter.class)
    private List<AppRight> rights;

    @CreatedDate
    private Timestamp createdAt;

    @LastModifiedDate
    private Timestamp updatedAt;

    /**
     * Creates an instance of an {@link App}.
     *
     * @param name the name of the App
     * @param secretUserName the username for authentication
     * @param secretPassword the password for authentication
     * @param rights the privileges of the app
     */
    public App(final String name, final String secretUserName, final String secretPassword, final List<AppRight> rights) {
        this(
            null,
            name,
            secretUserName,
            secretPassword,
            rights,
            null,
            null
        );
    }

    /**
     * Creates an instance of an {@link App}. App created from this constructor
     * will by default has the right to only read data.
     *
     * @param name the name of the App
     * @param secretUserName the username for authentication
     * @param secretPassword the password for authentication
     */
    public App(final String name, final String secretUserName, final String secretPassword) {
        this(
            name,
            secretUserName,
            secretPassword,
            new ArrayList<>(Arrays.asList(AppRight.READ))
        );
    }

    /**
     * Creates an instance of an App. With this constructor, the
     * secret username and password necessary for authentication will
     * be generated randomly using UUID.
     *
     * @param name the name of the App
     */
    public App(final String name) {
        this(
            name,
            UUID.randomUUID().toString().split("-")[0],
            KeyGenerators.string().generateKey(),
            new ArrayList<>(Arrays.asList(AppRight.READ))
        );


    }

    /**
     * Add rights that this app instance is allowed to do
     * @param rights the rights
     */
    public void addRights(List<AppRight> rights) {
        this.rights.addAll(rights);
    }

    /**
     * Remove specified rights so that this app would no longer be
     * able to perform the specified rights anymore
     * @param rights the rights
     */
    public void removeRights(List<AppRight> rights) {
        this.rights.removeAll(rights);
    }
}
