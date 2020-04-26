package ysaak.anima.view.converter;

import com.google.common.collect.HashMultimap;
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

        Multimap<Tag.TagEquivalenceOrigin, String> equivalenceMap = HashMultimap.create();

        if (StringUtils.isNotBlank(dto.getAnidbEquivalence())) {
            addToEquivalenceMap(equivalenceMap, Tag.TagEquivalenceOrigin.ANIDB, dto.getAnidbEquivalence());
        }

        tag.setEquivalenceMap(equivalenceMap);

        return tag;
    }

    private void addToEquivalenceMap(final Multimap<Tag.TagEquivalenceOrigin, String> equivalenceMap, Tag.TagEquivalenceOrigin origin, String data) {
        final List<String> codeList = Stream.of(data.split("\n"))
                .filter(StringUtils::isNotBlank)
                .sorted()
                .map(String::trim)
                .collect(Collectors.toList());

        equivalenceMap.putAll(origin, codeList);
    }
}
