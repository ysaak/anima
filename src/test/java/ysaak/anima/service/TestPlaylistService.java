package ysaak.anima.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ysaak.anima.dao.repository.PlaylistItemRepository;
import ysaak.anima.data.Element;
import ysaak.anima.data.playlist.PlaylistItem;
import ysaak.anima.data.playlist.PlaylistItemStatus;
import ysaak.anima.exception.FunctionalException;
import ysaak.anima.testdata.PlaylistTestData;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPlaylistService {

    @MockBean
    private PlaylistItemRepository playlistItemRepository;

    @MockBean
    private ElementService elementService;

    @Autowired
    private PlaylistService playlistService;

    @Test(expected = FunctionalException.class)
    public void testAddToPlaylist_alreadyAdded() throws FunctionalException {
        // Given
        final String userId = "fakeUserId";
        final String elementId = "fakeElementId";

        final PlaylistItem existingItem = new PlaylistItem();
        existingItem.setStatus(PlaylistItemStatus.STARTED);
        when(playlistItemRepository.findByUserIdListAndElementId(anyString(), anyString())).thenReturn(Collections.singletonList(existingItem));

        // When
        playlistService.addToPlaylist(userId, elementId, false);

        // Then
    }

    @Test(expected = FunctionalException.class)
    public void testAddToPlaylist_elementNotFound() throws FunctionalException {
        // Given
        final String userId = "fakeUserId";
        final String elementId = "fakeElementId";

        final PlaylistItem existingItem = new PlaylistItem();
        existingItem.setStatus(PlaylistItemStatus.FINISHED);
        when(playlistItemRepository.findByUserIdListAndElementId(anyString(), anyString())).thenReturn(Collections.singletonList(existingItem));

        when(elementService.findById2(anyString())).thenReturn(Optional.empty());

        // When
        playlistService.addToPlaylist(userId, elementId, false);

        // Then
    }

    @Test
    public void testAddToPlaylist_successStarted() throws FunctionalException {
        // Given
        final String userId = "fakeUserId";
        final Element fakeElement = PlaylistTestData.mockedElement();

        final PlaylistItem existingItem = new PlaylistItem();
        existingItem.setStatus(PlaylistItemStatus.FINISHED);
        when(playlistItemRepository.findByUserIdListAndElementId(anyString(), anyString())).thenReturn(Collections.singletonList(existingItem));
        when(playlistItemRepository.save(any(PlaylistItem.class))).thenAnswer(i -> i.getArguments()[0]);

        when(elementService.findById2(anyString())).thenReturn(Optional.of(fakeElement));

        // When
        PlaylistItem item = playlistService.addToPlaylist(userId, fakeElement.getId(), false);

        // Then
        Assert.assertNotNull(item);
        Assert.assertEquals(userId, item.getUserIdList().get(0));
        Assert.assertEquals(fakeElement.getId(), item.getElement());
        Assert.assertNotNull(item.getLastUpdateTime());
        Assert.assertEquals(PlaylistItemStatus.STARTED, item.getStatus());
        Assert.assertNotNull(item.getStartDate());
        Assert.assertNotNull(item.getCurrentEpisode());
    }

    @Test
    public void testAddToPlaylist_successPlaned() throws FunctionalException {
        // Given
        final String userId = "fakeUserId";
        final Element fakeElement = PlaylistTestData.mockedElement();

        final PlaylistItem existingItem = new PlaylistItem();
        existingItem.setStatus(PlaylistItemStatus.FINISHED);
        when(playlistItemRepository.findByUserIdListAndElementId(anyString(), anyString())).thenReturn(Collections.singletonList(existingItem));
        when(playlistItemRepository.save(any(PlaylistItem.class))).thenAnswer(i -> i.getArguments()[0]);

        when(elementService.findById2(anyString())).thenReturn(Optional.of(fakeElement));

        // When
        PlaylistItem item = playlistService.addToPlaylist(userId, fakeElement.getId(), true);

        // Then
        Assert.assertNotNull(item);
        Assert.assertEquals(userId, item.getUserIdList().get(0));
        Assert.assertEquals(fakeElement.getId(), item.getElement());
        Assert.assertNotNull(item.getLastUpdateTime());
        Assert.assertEquals(PlaylistItemStatus.PLAN_TO_WATCH, item.getStatus());
        Assert.assertNull(item.getStartDate());
        Assert.assertNull(item.getCurrentEpisode());
    }
}
