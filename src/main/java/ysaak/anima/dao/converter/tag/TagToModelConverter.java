package ysaak.anima.dao.converter.tag;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagEquivalenceModel;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Tag;
import ysaak.anima.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Converter(from = Tag.class, to = TagModel.class)
public class TagToModelConverter extends AbstractConverter<Tag, TagModel> {
    @Override
    public TagModel safeConvert(Tag tag) {
        final TagModel model = new TagModel(
                tag.getId(),
                tag.getName(),
                tag.getDescription()
        );

        final List<TagEquivalenceModel> equivalenceModelList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tag.getEquivalenceMap())) {
            for (Map.Entry<Tag.TagEquivalenceOrigin, String> equivalence : tag.getEquivalenceMap().entries()) {
                equivalenceModelList.add(new TagEquivalenceModel(
                        model,
                        fromEnum(equivalence.getKey()),
                        equivalence.getValue()
                ));
            }
        }
        model.setEquivalenceList(equivalenceModelList);

        return model;
    }
}
