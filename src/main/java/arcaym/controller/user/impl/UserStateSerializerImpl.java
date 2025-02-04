package arcaym.controller.user.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import arcaym.common.utils.file.FileUtils;
import arcaym.controller.user.api.UserStateSerializer;
import arcaym.model.user.impl.UserStateInfo;

/**
 * Implementation of {@link UserStateSerializer}. 
 */
public class UserStateSerializerImpl implements UserStateSerializer {

    private static final String EXTENSION = ".json";
    private static final String FILENAME = "user_data";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStateSerializer.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean save(final UserStateInfo userState) {
        FileUtils.createUserDirectory();
        validateFileName(FILENAME);
        try {
            Files.writeString(
                getPathOf(FILENAME),
                new Gson().toJson(userState),
                StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("An error occurred while WRITING '" + FILENAME + "' file.", e);
            return false;
        }
        return true;
    }

    /* Utility function  */
    private Optional<UserStateInfo> load() {
        validateFileName(FILENAME);
        final var rawState = FileUtils.readFromPath(getPathOf(FILENAME));
        if (rawState.isEmpty()) {
            LOGGER.error("An error occurred while READING '" + FILENAME + "' file.");
            return Optional.empty();
        }
        return FileUtils.convertToObj(UserStateInfo.class, rawState.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserStateInfo getUpdatedState() {
        final var previousSave = load();
        if (previousSave.isPresent()) {
            return previousSave.get();
        }
        final var defaultState = UserStateInfo.getDefaultState();
        save(defaultState);
        return defaultState;
    }

    private Path getPathOf(final String fileName) {
        return Path.of(FileUtils.USER_FOLDER, fileName.concat(EXTENSION));
    }

    private void validateFileName(final String fileName) {
        if (Objects.isNull(fileName) || fileName.isBlank() || fileName.isEmpty()) {
            throw new IllegalArgumentException("Invalid filename!");
        }
    }
}
