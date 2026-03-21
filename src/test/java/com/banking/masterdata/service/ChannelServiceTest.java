package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Channel;
import com.banking.masterdata.dto.request.CreateChannelRequest;
import com.banking.masterdata.dto.response.ChannelResponse;
import com.banking.masterdata.exception.ChannelAlreadyExistsException;
import com.banking.masterdata.exception.ChannelNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.ChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private MasterDataMapper masterDataMapper;

    private ChannelService channelService;

    @BeforeEach
    void setUp() {
        channelService = new ChannelService(channelRepository, masterDataMapper);
    }

    private CreateChannelRequest createRequest(String code, String name) {
        CreateChannelRequest request = new CreateChannelRequest();
        request.setCode(code);
        request.setName(name);
        return request;
    }

    @Test
    void createChannel_success() {
        CreateChannelRequest request = createRequest("MOBILE", "Mobile Banking");
        Channel channel = new Channel("MOBILE", "Mobile Banking");

        when(channelRepository.existsById("MOBILE")).thenReturn(false);
        when(masterDataMapper.toEntity(any(CreateChannelRequest.class))).thenReturn(channel);
        when(channelRepository.save(any())).thenReturn(channel);
        when(masterDataMapper.toResponse(any(Channel.class))).thenReturn(ChannelResponse.fromEntity(channel));

        ChannelResponse response = channelService.createChannel(request);

        assertEquals("MOBILE", response.getCode());
        assertEquals("Mobile Banking", response.getName());
        assertTrue(response.isActive());

        verify(channelRepository).save(any());
    }

    @Test
    void createChannel_duplicate_throws() {
        CreateChannelRequest request = createRequest("MOBILE", "Mobile Banking");

        when(channelRepository.existsById("MOBILE")).thenReturn(true);

        assertThrows(ChannelAlreadyExistsException.class, () ->
                channelService.createChannel(request));

        verify(channelRepository, never()).save(any());
    }

    @Test
    void getActiveChannels_excludesInactive() {
        Channel activeMobile = new Channel("MOBILE", "Mobile Banking");
        Channel activeWeb = new Channel("WEB", "Web Banking");

        when(channelRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(activeMobile, activeWeb));
        when(masterDataMapper.toResponse(activeMobile)).thenReturn(ChannelResponse.fromEntity(activeMobile));
        when(masterDataMapper.toResponse(activeWeb)).thenReturn(ChannelResponse.fromEntity(activeWeb));

        List<ChannelResponse> responses = channelService.getActiveChannels();

        assertEquals(2, responses.size());
        verify(channelRepository).findByIsActiveTrue();
    }

    @Test
    void deactivateChannel_success() {
        Channel activeChannel = new Channel("MOBILE", "Mobile Banking");
        Channel deactivatedChannel = new Channel("MOBILE", "Mobile Banking");
        deactivatedChannel.setActive(false);

        when(channelRepository.findById("MOBILE")).thenReturn(Optional.of(activeChannel));
        when(channelRepository.save(any())).thenReturn(deactivatedChannel);
        when(masterDataMapper.toResponse(deactivatedChannel)).thenReturn(ChannelResponse.fromEntity(deactivatedChannel));

        ChannelResponse response = channelService.deactivateChannel("MOBILE");

        assertEquals("MOBILE", response.getCode());
        assertFalse(response.isActive());
    }

    @Test
    void deactivateChannel_notFound_throws() {
        when(channelRepository.findById("INVALID")).thenReturn(Optional.empty());

        assertThrows(ChannelNotFoundException.class, () ->
                channelService.deactivateChannel("INVALID"));
    }
}
