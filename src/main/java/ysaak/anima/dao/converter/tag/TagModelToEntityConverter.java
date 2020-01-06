package ysaak.anima.dao.converter.tag;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagEquivalenceModel;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Tag;
import ysaak.anima.utils.CollectionUtils;

@Converter(from = TagModel.class, to = Tag.class)
public class TagModelToEntityConverter extends AbstractConverter<TagModel, Tag> {

    @Override
    public Tag safeConvert(TagModel tagModel) {

        Multimap<Tag.TagEquivalenceOrigin, String> equivalenceMap = HashMultimap.create();

        if (CollectionUtils.isNotEmpty(tagModel.getEquivalenceList())) {
            for (TagEquivalenceModel equivalenceModel : tagModel.getEquivalenceList()) {

                Tag.TagEquivalenceOrigin origin = toEnum(equivalenceModel.getOrigin(), Tag.TagEquivalenceOrigin.class);
                equivalenceMap.put(origin, equivalenceModel.getEquivalence());
            }
        }

        return new Tag(
                tagModel.getId(),
                tagModel.getName(),
                tagModel.getDescription(),
                equivalenceMap
        );
    }

}
