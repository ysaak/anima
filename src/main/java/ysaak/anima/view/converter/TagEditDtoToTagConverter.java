package ysaak.anima.view.converter;

import com.google.common.collect.Multimap;
import ysaak.anima.converter.AbstractConverter;
import ysaak.anima.converter.Converter;
import ysaak.anima.data.Tag;
import ysaak.anima.utils.StringUtils;
import ysaak.anima.view.dto.admin.TagEditDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Converter(from = TagEditDto.class, to = Tag.class)
public class TagEditDtoToTagConverter extends AbstractConverter<TagEditDto, Tag> {
    @Override
    protected Tag safeConvert(TagEditDto dto) {
        final Tag tag = new Tag(
                StringUtils.isNotBlank(dto.getId()) ? dto.getId() : null,
                dto.getName(),
                dto.getDescription()
        );

        return tag;
    }
}
