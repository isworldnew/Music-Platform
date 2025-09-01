package ru.smirnov.musicplatform.finder;

import lombok.Getter;

@Getter
public enum ConsumerType {

   GUEST(false), USER(true);

   ConsumerType(boolean showUnavailableShortcuts) {
      this.showUnavailableShortcuts = showUnavailableShortcuts;
   }

   private final boolean showUnavailableShortcuts;

}
