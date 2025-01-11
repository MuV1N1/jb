package de.muv1n.bot.database.models.join;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;

@MappedEntity("joinChannel")
@Introspected
public class JoinChannelModel {

    @Id
    private String id;

    @NonNull
    @NotBlank
    private String name;

    public JoinChannelModel(@NonNull @NotBlank String id, @NonNull @NotBlank String name) {
        this.name = name;
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }
}
