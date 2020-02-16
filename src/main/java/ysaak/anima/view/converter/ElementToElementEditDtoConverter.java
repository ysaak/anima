package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Element;
import ysaak.anima.view.dto.elements.ElementEditDto;

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
        return dto;
    }

}
