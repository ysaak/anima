package ysaak.anima.view.converter;

import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Tag;
import ysaak.anima.view.dto.admin.TagEditDto;

import java.util.stream.Collectors;

@Converter(from = Tag.class, to = TagEditDto.class)
public class TagToTagEditDtoConverter extends AbstractConverter<Tag, TagEditDto> {

    @Override
    protected TagEditDto safeConvert(Tag tag) {
        final String anidbEquivalence = tag.getEquivalenceMap().get(Tag.TagEquivalenceOrigin.ANIDB)
                .stream()
                .sorted()
                .collect(Collectors.joining("\n"));

        return new TagEditDto(
                tag.getId(),
                tag.getName(),
                tag.getDescription(),
                anidbEquivalence
        );
    }
}
