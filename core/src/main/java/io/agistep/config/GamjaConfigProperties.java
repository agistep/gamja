package io.agistep.config;

public class GamjaConfigProperties {

    private Core core;
    private CustomProperty custom;

    public GamjaConfigProperties() {
    }

    public CustomProperty getCustom() {
        return custom;
    }

    public void setCustom(CustomProperty custom) {
        this.custom = custom;
    }

    public Core getCore() {
        return core;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    public static class Core {
        private String basePackage;

        public String getBasePackage() {
            return basePackage;
        }

        public void setBasePackage(String basePackage) {
            this.basePackage = basePackage;
        }
    }

    public static class CustomProperty {
        private ConfigProperty config;

        public ConfigProperty getConfig() {
            return config;
        }

        public void setConfig(ConfigProperty config) {
            this.config = config;
        }

        public static class ConfigProperty {
            private String path;

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }
    }
}
