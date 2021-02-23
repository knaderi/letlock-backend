package com.landedexperts.letlock.filetransfer.backend.installer;

public enum InstallerPlatform {
        WIN ("latest.yml"),
        MAC ("latest-mac.yml");
        
        private final String config;
        
        InstallerPlatform (String config) {
            this.config = config;
        }
        
        public String getConfig() {
            return this.config;
        }

}
