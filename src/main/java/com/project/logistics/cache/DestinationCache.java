package com.project.logistics.cache;

import com.project.logistics.dao.Destination;
import com.project.logistics.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestinationCache {

    private final DestinationRepository destinationRepository;

    private Map<Long, Destination> destinationsById;

    private void populate() {
        destinationsById = destinationRepository.findAll().stream().collect((Collectors.toMap(Destination::getId, Function.identity())));
    }

    private Map<Long, Destination> getCacheData() {
        if (this.destinationsById == null) {
            populate();
        }

        return this.destinationsById;
    }

    public Optional<Destination> findById(Long destinationId) {
        return Optional.ofNullable(getCacheData().get(destinationId));
    }

    public Optional<Destination> findByName(String destinationName) {
        return getCacheData().values().stream().filter(destination -> destination.getName().equals(destinationName)).findAny();
    }

    public void put(Destination destination) {
        getCacheData().put(destination.getId(), destination);
    }

    public void delete(Destination destination) {
        getCacheData().remove(destination.getId());
    }
}
