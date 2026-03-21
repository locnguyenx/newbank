package com.banking.masterdata.service;

import com.banking.masterdata.domain.entity.Channel;
import com.banking.masterdata.dto.request.CreateChannelRequest;
import com.banking.masterdata.dto.response.ChannelResponse;
import com.banking.masterdata.exception.ChannelAlreadyExistsException;
import com.banking.masterdata.exception.ChannelNotFoundException;
import com.banking.masterdata.mapper.MasterDataMapper;
import com.banking.masterdata.repository.ChannelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final MasterDataMapper masterDataMapper;

    public ChannelService(ChannelRepository channelRepository, MasterDataMapper masterDataMapper) {
        this.channelRepository = channelRepository;
        this.masterDataMapper = masterDataMapper;
    }

    public ChannelResponse createChannel(CreateChannelRequest request) {
        if (channelRepository.existsById(request.getCode())) {
            throw new ChannelAlreadyExistsException(request.getCode());
        }

        Channel channel = masterDataMapper.toEntity(request);
        Channel savedChannel = channelRepository.save(channel);

        return masterDataMapper.toResponse(savedChannel);
    }

    @Transactional(readOnly = true)
    public List<ChannelResponse> getActiveChannels() {
        List<Channel> channels = channelRepository.findByIsActiveTrue();
        return channels.stream()
                .map(masterDataMapper::toResponse)
                .toList();
    }

    public ChannelResponse deactivateChannel(String code) {
        Channel channel = channelRepository.findById(code)
                .orElseThrow(() -> new ChannelNotFoundException(code));

        channel.setActive(false);
        Channel savedChannel = channelRepository.save(channel);
        return masterDataMapper.toResponse(savedChannel);
    }
}
