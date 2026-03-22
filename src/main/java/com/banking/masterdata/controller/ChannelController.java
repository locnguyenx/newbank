package com.banking.masterdata.controller;

import com.banking.masterdata.dto.request.CreateChannelRequest;
import com.banking.masterdata.dto.request.UpdateChannelRequest;
import com.banking.masterdata.dto.response.ChannelResponse;
import com.banking.masterdata.service.ChannelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master-data/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping
    public ResponseEntity<ChannelResponse> createChannel(@Valid @RequestBody CreateChannelRequest request) {
        ChannelResponse response = channelService.createChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getActiveChannels() {
        List<ChannelResponse> response = channelService.getActiveChannels();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}/deactivate")
    public ResponseEntity<ChannelResponse> deactivateChannel(@PathVariable String code) {
        ChannelResponse response = channelService.deactivateChannel(code);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ChannelResponse> updateChannel(
            @PathVariable String code,
            @Valid @RequestBody UpdateChannelRequest request) {
        ChannelResponse response = channelService.updateChannel(code, request);
        return ResponseEntity.ok(response);
    }
}
