package de.muv1n.bot.database.services.defaultServices;

import de.muv1n.bot.database.models.StatusModel;
import de.muv1n.bot.database.repositorys.StatusRepository;
import de.muv1n.bot.database.services.interfaces.StatusService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class DefaultStatusService implements StatusService {
    private final StatusRepository statusRepository;

    public DefaultStatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public Iterable<StatusModel> list() {
        return statusRepository.findAll();
    }

    @Override
    public void clearAll() {
        statusRepository.deleteAll();
    }

    @Override
    public StatusModel save(StatusModel statusModel) {
        if(statusModel.getId() == null) {
            return statusRepository.save(statusModel);
        }else{
            return statusRepository.update(statusModel);
        }
    }

    @Override
    public Optional<StatusModel> find(String id) {
        return statusRepository.findById(id);
    }

    @Override
    public Iterable<StatusModel> findByStatusInList(List<String> names) {
        return statusRepository.findByStatusInList(names);
    }
}
