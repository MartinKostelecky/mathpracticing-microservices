package cz.martinkostelecky.practicingservice.service;

import cz.martinkostelecky.practicingservice.model.Example;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.Optional;

public interface PracticingService {

    Optional<Example> getRandomExample(Example example);

    Boolean getResult(Example example);

    void playSound(Boolean result) throws UnsupportedAudioFileException, IOException, LineUnavailableException;
}
