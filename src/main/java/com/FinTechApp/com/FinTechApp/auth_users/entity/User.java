
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.FinTechApp.com.FinTechApp.role.entity.Role;
import com.FinTechApp.com.FinTechApp.account.entity.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Data
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firtsName;

    private String lastName;
    private String phoneNumber;

    @Email
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    private String password;
    private String profilePictureUrl;
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name= 'user_roles',
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    
    private List<Role> roles;

    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL)
    private List<Accout> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;


    
}


    


