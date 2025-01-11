package de.muv1n.bot.database.services.defaultServices.join;

import de.muv1n.bot.database.models.join.JoinRoleModel;
import de.muv1n.bot.database.repositorys.join.JoinRoleRepository;
import de.muv1n.bot.database.services.interfaces.join.JoinRoleService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class DefaultJoinRoleService implements JoinRoleService {

    private final JoinRoleRepository roleRepository;

    public DefaultJoinRoleService(JoinRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void clearAll() {
        roleRepository.deleteAll();
    }

    @Override
    public Iterable<JoinRoleModel> list() {
        return roleRepository.findAll();
    }

    @Override
    public JoinRoleModel save(JoinRoleModel roleModel) {
        return roleRepository.save(roleModel);
    }

    @Override
    public Optional<JoinRoleModel> findById(String id){
        return roleRepository.findById(id);
    }

    @Override
    public Iterable<JoinRoleModel> findByRoleNameInList(List<String> names) {
        return roleRepository.findByNameInList(names);
    }

}
