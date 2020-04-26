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
import ysaak.anima.exception.error.TagErrorCode;
import ysaak.anima.rules.TagRules;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;

@Service
public class TagService implements IAnimaComponent {

    private final ConverterService converterService;

    private final TagRepository tagRepository;

    @Autowired
    public TagService(ConverterService converterService, TagRepository tagRepository) {
        this.converterService = converterService;

        this.tagRepository = tagRepository;
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

    public Tag findById(final String id) throws FunctionalException {
        TagModel tagModel = tagRepository.findById(id)
                .orElseThrow(() -> TagErrorCode.NOT_FOUND.functional(id));

        return converterService.convert(tagModel, Tag.class);
    }

    public void delete(String id) throws FunctionalException {
        TagModel tagToDelete = tagRepository.findById(id)
                .orElseThrow(() -> TagErrorCode.NOT_FOUND.functional(id));

        tagRepository.delete(tagToDelete);
    }
}
