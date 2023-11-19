package Phase2.helperClasses;

import java.io.InputStream;
import javax.sound.sampled.*;
import java.io.IOException;

/**
 * The SoundPlayerUsingClip class is responsible for playing audio files using the Clip API.
 * It implements the LineListener interface to handle events related to audio playback.
 */
public class SoundPlayerUsingClip implements LineListener {
    public boolean isPlaybackCompleted;
    private SourceDataLine audioLine;
    private boolean isPaused = false;
    private FloatControl volumeControl;

    /**
     * This method is called when a LineEvent is triggered.
     * It handles the logic for starting and stopping playback of a sound.
     * 
     * @param event The LineEvent that triggered the update.
     */
    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
            System.out.println("Playback started.");
        }
    }

    /**
     * Plays music from the specified file path.
     * 
     * @param path the path of the music file to be played
     * @throws LineUnavailableException if a line cannot be opened due to resource restrictions
     */
    public void playMusic(String path) throws LineUnavailableException {
        try {
            InputStream inputStream = SoundPlayerUsingClip.class.getClassLoader().getResourceAsStream(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);

            AudioFormat audioFormat = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            audioLine = (SourceDataLine) AudioSystem.getLine(info);
            SoundPlayerUsingClip soundPlayer = new SoundPlayerUsingClip();
            audioLine.addLineListener(soundPlayer);
            audioLine.open(audioFormat);
            audioLine.start();

            if (audioLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) audioLine.getControl(FloatControl.Type.MASTER_GAIN);
            }

            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesRead = 0;

            while ((bytesRead = audioStream.read(buffer, 0, buffer.length)) != -1) {
                if (!isPaused) {
                    audioLine.write(buffer, 0, bytesRead);
                }
            }

            audioLine.drain();
            audioLine.close();
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the audio playback.
     * If the audio line is currently playing, it will be stopped and closed.
     * The isPaused flag will be set to false.
     */
    public void stop() {
        if (audioLine != null) {
            audioLine.stop();
            audioLine.close();
            isPaused = false;
        }
    }

    /**
     * Pauses the audio playback if it is currently playing.
     * If the audio line is open and not already paused, it will be stopped and the playback will be paused.
     */
    public void pause() {
        if (audioLine != null && audioLine.isOpen() && !isPaused) {
            audioLine.stop();
            isPaused = true;
            System.out.println("Playback paused.");
        }
    }

    /**
     * Resumes the audio playback if it was previously paused.
     * If the audio line is open and in a paused state, this method starts the playback and sets the isPaused flag to false.
     * Prints a message to the console indicating that the playback has been resumed.
     */
    public void resume() {
        if (audioLine != null && audioLine.isOpen() && isPaused) {
            audioLine.start();
            isPaused = false;
            System.out.println("Playback resumed.");
        }
    }

    /**
     * Decreases the volume of the sound player by the specified decrement value.
     * 
     * @param decrementValue the value by which the volume should be decreased
     */
    public void decreaseVolume(float decrementValue) {
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            volumeControl.setValue(Math.max(volumeControl.getMinimum(), currentVolume - decrementValue));
        }
    }

    /**
     * Increases the volume of the sound player by the specified increment value.
     * 
     * @param incrementValue the value by which to increase the volume
     */
    public void increaseVolume(float incrementValue) {
        if (volumeControl != null) {
            float currentVolume = volumeControl.getValue();
            volumeControl.setValue(Math.min(volumeControl.getMaximum(), currentVolume + incrementValue));
        }
    }
}
