package de.muv1n.bot.database.models;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@MappedEntity("status")
@Introspected
public class StatusModel {
    @Id
    @GeneratedValue
    private String id;

    @NotNull
    @NotBlank
    private String status;

    public StatusModel(@NotNull String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return status;
    }
}
