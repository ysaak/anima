package ysaak.anima.view.converter;

import com.google.common.base.Splitter;
import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.dto.elements.ElementEditDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter(from = ElementEditDto.class, to = Element.class)
public class ElementEditDtoToElementConverter extends AbstractConverter<ElementEditDto, Element> {

    @Override
    protected Element safeConvert(ElementEditDto object) {

        final List<TagModel> tagList = CollectionUtils.getNotNull(object.getTagList())
                .stream()
                .map(tagId -> {
                    TagModel tag = new TagModel();
                    tag.setId(tagId);
                    return tag;
                })
                .collect(Collectors.toList());

        Element element = new Element(
                object.getId(),
                object.getTitle(),
                toEnum(object.getType(), ElementType.class),
                toEnum(object.getSubType(), ElementSubType.class),
                object.getReleaseYear(),
                object.getSynopsis()
        );
        element.setTagList(tagList);
        return element;
    }

}
