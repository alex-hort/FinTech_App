

@Data
@
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String recipient;

    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();

}
