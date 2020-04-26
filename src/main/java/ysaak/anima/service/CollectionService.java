package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.CollectionRepository;
import ysaak.anima.data.Collection;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.rules.CollectionRules;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionService implements IAnimaComponent {
    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Optional<Collection> findById(final String id) {
        return collectionRepository.findById(id);
    }

    public Collection save(Collection collection) throws FunctionalException {
        Preconditions.checkNotNull(collection);
        CollectionRules.validate(collection);

        return collectionRepository.save(collection);
    }

    public List<Collection> findAll() {
        return CollectionUtils.toList(
                collectionRepository.findAll(Sort.by("name"))
        );
    }

    public void delete(Collection collectionToDelete) {
        Preconditions.checkNotNull(collectionToDelete);
        collectionRepository.delete(collectionToDelete);
    }
}
