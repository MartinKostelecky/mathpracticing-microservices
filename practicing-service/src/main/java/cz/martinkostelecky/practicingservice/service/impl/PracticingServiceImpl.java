package cz.martinkostelecky.practicingservice.service.impl;

import cz.martinkostelecky.practicingservice.model.Example;
import cz.martinkostelecky.practicingservice.feign.FeignInterface;
import cz.martinkostelecky.practicingservice.service.PracticingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PracticingServiceImpl implements PracticingService {

    Random random = new Random();
    private final FeignInterface feignInterface;
    private final Map<String, Long> cache = new HashMap<>();

    @Override
    public Optional<Example> getRandomExample(Example example) {

        // Find all examples by category
        List<Example> examples = feignInterface.getExamplesByCategory(example.getCategory());

        if (examples.isEmpty()) {
            return Optional.empty();
        }

        List<Long> ids = examples.stream().map(Example::getId).toList();

        Long id = ids.get((int) random.nextLong(ids.size()));

        // Check if the ID is already in the cache
        if (cache.containsKey("lastUsedId") && cache.get("lastUsedId").equals(id)) {
            id = ids.get((int) random.nextLong(ids.size()));
        }

        // Store the current ID in the cache
        cache.put("lastUsedId", id);
        log.info("Random example id: {}", id);

        return Optional.ofNullable(feignInterface.getExampleById(id));
    }

    @Override
    public Boolean getResult(Example example) {
        return feignInterface.getResult(example);
    }

    @Override
    public void playSound(Boolean result) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        Clip clip = AudioSystem.getClip();

        if (result) {
            URL successSoundUrl = getClass().getResource("/audio/success.wav");
            assert successSoundUrl != null;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(successSoundUrl);
            clip.open(audioIn);

        } else {
            URL failureSoundUrl = getClass().getResource("/audio/failure.wav");
            assert failureSoundUrl != null;
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(failureSoundUrl);
            clip.open(audioIn);
        }
        clip.start();
    }
}
