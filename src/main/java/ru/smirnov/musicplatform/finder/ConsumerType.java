package ru.smirnov.musicplatform.finder;


public enum ConsumerType {

   GUEST(false), USER(true);

   private final boolean ableToSaveAlbumsAndGetUnavailableShortcuts;

   ConsumerType(boolean ableToSaveAlbumsAndGetUnavailableShortcuts) {
      this.ableToSaveAlbumsAndGetUnavailableShortcuts = ableToSaveAlbumsAndGetUnavailableShortcuts;
   }

   public boolean ableToSaveAlbumsAndGetUnavailableShortcuts() {
      return ableToSaveAlbumsAndGetUnavailableShortcuts;
   }
}
