package de.muv1n.bot.database.services.interfaces.join;

import de.muv1n.bot.database.models.join.JoinRoleModel;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Optional;

public interface JoinRoleService {
    void clearAll();
    Iterable<JoinRoleModel> list();
    JoinRoleModel save(JoinRoleModel roleModel);
    @NonNull Optional<JoinRoleModel> findById(@NonNull String id);
    Iterable<JoinRoleModel> findByRoleNameInList(@NonNull List<String> names);
}
