package ysaak.anima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.converter.ConverterService;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.dao.repository.TagRepository;
import ysaak.anima.data.Tag;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.rules.TagRules;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TagService implements IAnimaComponent {

    private final ConverterService converterService;

    private final TagRepository tagRepository;

    @Autowired
    public TagService(ConverterService converterService, TagRepository tagRepository) {
        this.converterService = converterService;

        this.tagRepository = tagRepository;
    }

    public Optional<Tag> findById(final String id) {
        Optional<TagModel> tagModel = tagRepository.findById(id);

        return tagModel.map(m -> converterService.convert(m, Tag.class));
    }

    public List<Tag> findById(List<String> idList) {
        List<TagModel> modelList = tagRepository.findByIdIn(idList);
        return converterService.convert(modelList, Tag.class);
    }

    public List<Tag> findAll() {
        Iterable<TagModel> modelIterable = tagRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return converterService.convert(CollectionUtils.toList(modelIterable), Tag.class);
    }

    public Tag save(Tag tag) throws FunctionalException {
        TagRules.validate(tag);

        TagModel model = converterService.convert(tag, TagModel.class);
        model = tagRepository.save(model);

        return converterService.convert(model, Tag.class);
    }

    public void delete(Tag tagToDelete) {
        tagRepository.deleteById(tagToDelete.getId());
    }
}
