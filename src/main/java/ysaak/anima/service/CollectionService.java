package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.CollectionRepository;
import ysaak.anima.data.Collection;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.exception.error.GenericErrorCode;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;

@Service
public class CollectionService implements IAnimaComponent {
    private final CollectionRepository collectionRepository;

    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Collection findById(final String id) throws FunctionalException {
        return collectionRepository.findById(id)
                .orElseThrow(() -> GenericErrorCode.NOT_FOUND.functional(id));
    }

    public Collection save(Collection collection) throws DataValidationException {
        Preconditions.checkNotNull(collection);
        validator().validate(collection);

        return collectionRepository.save(collection);
    }

    public List<Collection> findAll() {
        return CollectionUtils.toList(
                collectionRepository.findAll(Sort.by("name"))
        );
    }

    public void delete(String id) throws FunctionalException {
        Collection collectionToDelete = findById(id);
        collectionRepository.delete(collectionToDelete);
    }
}
