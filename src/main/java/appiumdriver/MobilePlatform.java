package appiumdriver;

/**
 * Enum to represent the mobile platform.
 */
public enum MobilePlatform {
  ANDROID("android");

  private final String value;

  MobilePlatform(String value) {
    this.value = value;
  }

  /**
   * Gets the enum platform value.
   *
   * @param value value of the platform: android
   * @return the platform enum
   */
  public static MobilePlatform getEnum(String value) {
    for (MobilePlatform platform : values()) {
      if (platform.value.equals(value)) {
        return platform;
      }
    }
    throw new IllegalArgumentException("The platform [" + value + "] is not known.");
  }

  @Override
  public String toString() {
    return value;
  }
}

