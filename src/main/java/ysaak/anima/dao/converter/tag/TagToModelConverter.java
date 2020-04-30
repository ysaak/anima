package ysaak.anima.dao.converter.tag;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Tag;

@Converter(from = Tag.class, to = TagModel.class)
public class TagToModelConverter extends AbstractConverter<Tag, TagModel> {
    @Override
    public TagModel safeConvert(Tag tag) {
        return new TagModel(
                tag.getId(),
                tag.getName(),
                tag.getDescription()
        );
    }
}
