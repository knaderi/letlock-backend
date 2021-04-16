package com.landedexperts.letlock.filetransfer.backend.installer;

public enum InstallerPlatform {
        WIN ("latest.yml"),
        MAC ("latest-mac.yml"),
        WIN_LITE ("latest-lite.yml"),
        MAC_LITE ("latest-lite-mac.yml");

        
        private final String config;
        
        InstallerPlatform (String config) {
            this.config = config;
        }
        
        public String getConfig() {
            return this.config;
        }

}
