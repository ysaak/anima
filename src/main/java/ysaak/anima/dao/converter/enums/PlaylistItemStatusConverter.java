package ysaak.anima.dao.converter.enums;

import ysaak.anima.dao.converter.AbstractSerializableEnumConverter;
import ysaak.anima.data.playlist.PlaylistItemStatus;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class PlaylistItemStatusConverter extends AbstractSerializableEnumConverter<PlaylistItemStatus> {
    public PlaylistItemStatusConverter() {
        super(PlaylistItemStatus.class);
    }
}
