package appiumdriver;

/**
 * Enum to represent the mode of Appium execution.
 */
public enum AppiumMode {
  LOCAL("local");

  private final String value;

  AppiumMode(String value) {
    this.value = value;
  }

  /**
   * Gets the enum using the mode value.
   *
   * @param value value of the selected mode: local
   * @return the selected mode
   */
  public static AppiumMode getEnum(String value) {
    for (AppiumMode mode : values()) {
      if (mode.value.equals(value)) {
        return mode;
      }
    }
    throw new IllegalArgumentException("The mode [" + value + "] is not known.");
  }

  @Override
  public String toString() {
    return value;
  }
}

