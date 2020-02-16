package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Element;
import ysaak.anima.data.ElementSubType;
import ysaak.anima.data.ElementType;
import ysaak.anima.view.dto.elements.ElementEditDto;

@Converter(from = ElementEditDto.class, to = Element.class)
public class ElementEditDtoToElementConverter extends AbstractConverter<ElementEditDto, Element> {

    @Override
    protected Element safeConvert(ElementEditDto object) {
        return new Element(
                object.getId(),
                object.getTitle(),
                toEnum(object.getType(), ElementType.class),
                toEnum(object.getSubType(), ElementSubType.class),
                object.getReleaseYear(),
                object.getSynopsis()
        );
    }

}
