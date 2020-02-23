package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.dao.model.TagModel;
import ysaak.anima.data.Element;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.view.dto.elements.ElementEditDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Converter(from = Element.class, to = ElementEditDto.class)
public class ElementToElementEditDtoConverter extends AbstractConverter<Element, ElementEditDto> {

    @Override
    protected ElementEditDto safeConvert(Element object) {
        List<String> tagList;
        if (CollectionUtils.isNotEmpty(object.getTagList())) {
            tagList = object.getTagList().stream()
                    .sorted(Comparator.comparing(TagModel::getName))
                    .map(TagModel::getId)
                    .collect(Collectors.toList());
        }
        else {
            tagList = new ArrayList<>();
        }


        ElementEditDto dto = new ElementEditDto();
        dto.setId(object.getId());
        dto.setType(fromEnum(object.getType()));
        dto.setTitle(object.getTitle());
        dto.setSubType(fromEnum(object.getSubType()));
        dto.setReleaseYear(object.getReleaseYear());
        dto.setSynopsis(object.getSynopsis());
        dto.setTagList(tagList);
        return dto;
    }

}
