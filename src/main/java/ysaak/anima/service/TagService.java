package ysaak.anima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.converter.ConverterService;
import ysaak.anima.data.Tag;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.dao.repository.TagRepository;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;

@Service
public class TagService extends AbstractCrudService<Tag, TagModel, TagRepository> {


    @Autowired
    public TagService(ConverterService converterService, TagRepository tagRepository) {
        super(converterService, tagRepository);
    }

    @Override
    public List<Tag> findAll() {
        Iterable<TagModel> modelIterable = repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return converterService.convert(CollectionUtils.toList(modelIterable), Tag.class);
    }
}
