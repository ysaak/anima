package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Collection;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.data.Tag;
import ysaak.anima.utils.CollectionUtils;
import ysaak.anima.view.dto.elements.ElementEditDto;

import java.util.List;
import java.util.stream.Collectors;

@Converter(from = ElementEditDto.class, to = Element.class)
public class ElementEditDtoToElementConverter extends AbstractConverter<ElementEditDto, Element> {

    @Override
    protected Element safeConvert(ElementEditDto object) {

        final List<Tag> tagList = CollectionUtils.getNotNull(object.getTagList())
                .stream()
                .map(Tag::new)
                .collect(Collectors.toList());

        final List<Collection> collectionList = CollectionUtils.getNotNull(object.getCollectionList())
                .stream()
                .map(collectionId -> {
                    Collection collection = new Collection();
                    collection.setId(collectionId);
                    return collection;
                })
                .collect(Collectors.toList());

        Element element = new Element(
                object.getId(),
                object.getTitle(),
                toEnum(object.getType(), ElementType.class),
                toEnum(object.getSubType(), ElementSubType.class),
                object.getReleaseYear(),
                object.getEpisodeCount(),
                object.getSynopsis()
        );
        element.setTagList(tagList);
        element.setCollectionList(collectionList);
        return element;
    }
}
