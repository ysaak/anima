package ysaak.anima.service;

import com.google.common.base.Preconditions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.converter.ConverterService;
import ysaak.anima.data.Entity;
import ysaak.anima.exception.DataValidationException;
import ysaak.anima.exception.ResourceNotFoundException;
import ysaak.anima.utils.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractCrudService<ENTITY extends Entity, MODEL, R extends PagingAndSortingRepository<MODEL, String>> implements IAnimaComponent {

    final ConverterService converterService;
    final R repository;

    private final Class<ENTITY> entityClass;
    private final Class<MODEL> modelClass;

    @SuppressWarnings("unchecked")
    public AbstractCrudService(ConverterService converterService, R repository) {
        this.converterService = converterService;
        this.repository = repository;

        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.entityClass = (Class<ENTITY>) types[0];
        this.modelClass = (Class<MODEL>) types[1];
    }

    private void validate(ENTITY data) throws DataValidationException {
        Preconditions.checkNotNull(data);
        validator().validate(data);
    }

    public ENTITY save(ENTITY data) throws DataValidationException {
        validate(data);

        MODEL model = converterService.convert(data, modelClass);
        model = repository.save(model);

        return converterService.convert(model, entityClass);
    }

    public Optional<ENTITY> findById(String id) {
        Optional<MODEL> model = repository.findById(id);
        return model.map(m -> converterService.convert(m, entityClass));
    }

    public Page<ENTITY> findAll(Pageable pageable) {
        Page<MODEL> page = repository.findAll(pageable);
        return page.map(m -> converterService.convert(m, entityClass));
    }

    public List<ENTITY> findAll() {
        Iterable<MODEL> modelIterable = repository.findAll();

        return CollectionUtils.toList(modelIterable).stream().map(m -> converterService.convert(m, entityClass)).collect(Collectors.toList());

    }

    public void delete(String id) throws ResourceNotFoundException {
        final Optional<MODEL> optionalModelToDelete = repository.findById(id);
        MODEL model = optionalModelToDelete.orElseThrow(() -> new ResourceNotFoundException("No entity with id=" + id + " found"));
        repository.delete(model);
    }
}
