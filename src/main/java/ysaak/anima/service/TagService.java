package ysaak.anima.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ysaak.anima.IAnimaComponent;
import ysaak.anima.dao.repository.TagRepository;
import ysaak.anima.data.Tag;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.rules.TagRules;
import ysaak.anima.utils.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TagService implements IAnimaComponent {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Optional<Tag> findById(final String id) {
        return tagRepository.findById(id);
    }

    public List<Tag> findById(List<String> idList) {
        return tagRepository.findByIdIn(idList);
    }

    public List<Tag> findAll() {
        Iterable<Tag> tagIterable = tagRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return CollectionUtils.toList(tagIterable);
    }

    public Tag save(Tag tag) throws FunctionalException {
        TagRules.validate(tag);

        return tagRepository.save(tag);
    }

    public void delete(Tag tagToDelete) {
        tagRepository.deleteById(tagToDelete.getId());
    }
}
