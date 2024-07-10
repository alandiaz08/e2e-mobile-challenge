package environment;

import static com.google.common.io.Files.asByteSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

/**
 * Configuration class for initializing the testing environment.
 * This class loads configuration settings from YAML files.
 */
public final class EnvironmentConfig {

  private static final Logger logger = LogManager.getLogger(EnvironmentConfig.class);

  private static Map<String, Object> configMap;
  private static final String PROD = "prod";

  private static String testingEnvironment;

  private EnvironmentConfig() {}

  /**
   * Initializes the environment settings from the configuration file.
   * The environment is set to 'prod' by default.
   */
  public static void initializeEnvironment() {
    testingEnvironment = System.getProperty("environment", PROD).toLowerCase();
    logger.info("Testing environment set to prod by default");

    String environmentConfig = testingEnvironment + ".yaml";

    configMap = loadConfigFile(environmentConfig);
  }

  /**
   * Loads the settings from the specified configuration file.
   *
   * @param filename the name of the configuration file to load
   * @return the settings as a map
   */
  private static Map<String, Object> loadConfigFile(String filename) {
    String path = "config/" + filename;
    logger.debug("Config file: {}", path);

    File initialFile = new File(path);
    try (InputStream targetStream = asByteSource(initialFile).openStream()) {
      Yaml yaml = new Yaml();
      Map<String, Object> map = yaml.load(targetStream);
      logger.debug("Content of config file: {}", map);
      return map;
    } catch (IOException e) {
      logger.error("Problem when opening the config file", e);
      throw new IllegalArgumentException();
    }
  }
}
