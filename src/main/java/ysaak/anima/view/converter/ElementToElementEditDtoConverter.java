package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Collection;
import ysaak.anima.data.Element;
import ysaak.anima.data.Tag;
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
        ElementEditDto dto = new ElementEditDto();
        dto.setId(object.getId());
        dto.setType(fromEnum(object.getType()));
        dto.setTitle(object.getTitle());
        dto.setSubType(fromEnum(object.getSubType()));
        dto.setReleaseYear(object.getReleaseYear());
        dto.setSynopsis(object.getSynopsis());
        dto.setTagList(convertTagList(object.getTagList()));
        dto.setCollectionList(convertCollectionList(object.getCollectionList()));
        return dto;
    }

    private List<String> convertTagList(List<Tag> tagList) {
        final List<String> tagDtoList;

        if (CollectionUtils.isNotEmpty(tagList)) {
            tagDtoList = tagList.stream()
                    .sorted(Comparator.comparing(Tag::getName))
                    .map(Tag::getId)
                    .collect(Collectors.toList());
        }
        else {
            tagDtoList = new ArrayList<>();
        }

        return tagDtoList;
    }

    private List<String> convertCollectionList(List<Collection> collectionList) {
        final List<String> collectionDtoList;

        if (CollectionUtils.isNotEmpty(collectionList)) {
            collectionDtoList = collectionList.stream()
                    .sorted(Comparator.comparing(Collection::getName))
                    .map(Collection::getId)
                    .collect(Collectors.toList());
        }
        else {
            collectionDtoList = new ArrayList<>();
        }

        return collectionDtoList;
    }
}
