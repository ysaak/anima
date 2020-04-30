package ysaak.anima.dao.converter.tag;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Tag;

@Converter(from = TagModel.class, to = Tag.class)
public class TagModelToEntityConverter extends AbstractConverter<TagModel, Tag> {

    @Override
    public Tag safeConvert(TagModel tagModel) {
        return new Tag(
                tagModel.getId(),
                tagModel.getName(),
                tagModel.getDescription()
        );
    }

}
