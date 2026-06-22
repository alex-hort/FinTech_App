import com.FinTechApp.com.FinTechApp.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
   
}