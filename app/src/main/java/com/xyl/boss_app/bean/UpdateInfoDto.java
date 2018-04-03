package com.xyl.boss_app.bean;

public class UpdateInfoDto extends BaseDto {

    private UpdateAppInfo data;

    public UpdateAppInfo getData() {
        return data;
    }

    public void setData(UpdateAppInfo data) {
        this.data = data;
    }

    public class UpdateAppInfo {
        private String downloadUrl;
        private String desc;
        private String versionName;
        private int forceUpdate;
        private int version;
        private int dbversion;

        public int getDbversion() {
            return dbversion;
        }

        public void setDbversion(int dbversion) {
            this.dbversion = dbversion;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getForceUpdate() {
            return forceUpdate;
        }

        public void setForceUpdate(int forceUpdate) {
            this.forceUpdate = forceUpdate;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getDownloadUrl() {
            return downloadUrl == null ? "" : downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getDesc() {
            return desc == null ? "" : desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
