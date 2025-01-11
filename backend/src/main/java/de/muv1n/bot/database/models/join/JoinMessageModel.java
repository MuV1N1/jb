package de.muv1n.bot.database.models.join;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;

@MappedEntity("joinMessage")
@Introspected
public class JoinMessageModel {

    @GeneratedValue
    @Id
    private String id;

    @NonNull
    @NotBlank
    private String title;

    @NonNull
    @NotBlank
    private String message;
    @NonNull
    @NotBlank
    private boolean showAvatar;

    public JoinMessageModel(@NonNull @NotBlank String id, @NonNull @NotBlank String title, @NonNull @NotBlank String message, @NonNull @NotBlank boolean showAvatar) {
        this.title = title;
        this.message = message;
        this.showAvatar = showAvatar;
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return message;
    }
    public boolean getShowAvatar() {
        return showAvatar;
    }
    public String getId() {
        return id;
    }
}
