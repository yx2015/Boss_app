package com.xyl.boss_app.bean;

import java.util.List;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class CabinetDto extends BaseDto {
    private CabinetDtoInfo data;

    public CabinetDtoInfo getData() {
        return data;
    }

    public void setData(CabinetDtoInfo data) {
        this.data = data;
    }

    public static class CabinetDtoInfo {
        private String totalBizGP20Num;// 总的20GP柜量
        private String totalBizGP40Num;// 总的40GP柜量
        private String totalBizHQ40Num;// 总的40HQ柜量
        private String totalCabinetNum;// 总的柜量
        private List<CabinetInfo> list;//

        public String getTotalBizGP20Num() {
            return totalBizGP20Num;
        }

        public void setTotalBizGP20Num(String totalBizGP20Num) {
            this.totalBizGP20Num = totalBizGP20Num;
        }

        public String getTotalBizGP40Num() {
            return totalBizGP40Num;
        }

        public void setTotalBizGP40Num(String totalBizGP40Num) {
            this.totalBizGP40Num = totalBizGP40Num;
        }

        public String getTotalBizHQ40Num() {
            return totalBizHQ40Num;
        }

        public void setTotalBizHQ40Num(String totalBizHQ40Num) {
            this.totalBizHQ40Num = totalBizHQ40Num;
        }

        public String getTotalCabinetNum() {
            return totalCabinetNum;
        }

        public void setTotalCabinetNum(String totalCabinetNum) {
            this.totalCabinetNum = totalCabinetNum;
        }

        public List<CabinetInfo> getList() {
            return list;
        }

        public void setList(List<CabinetInfo> list) {
            this.list = list;
        }

        public static class CabinetInfo {
            private String consignor;//公司名称
            private StartBizEntity startBiz;// 起运港
            private OnBizEntity onBiz;//海上运输
            private EndBizEntity endBiz;//目的港

            public void setConsignor(String consignor) {
                this.consignor = consignor;
            }

            public void setStartBiz(StartBizEntity startBiz) {
                this.startBiz = startBiz;
            }

            public void setOnBiz(OnBizEntity onBiz) {
                this.onBiz = onBiz;
            }

            public void setEndBiz(EndBizEntity endBiz) {
                this.endBiz = endBiz;
            }

            public String getConsignor() {
                return consignor;
            }

            public StartBizEntity getStartBiz() {
                return startBiz;
            }

            public OnBizEntity getOnBiz() {
                return onBiz;
            }

            public EndBizEntity getEndBiz() {
                return endBiz;
            }

            public static class StartBizEntity {
                private int bizNum;
                private int GP_20;
                private int GP_40;
                private int HQ_40;

                public void setBizNum(int bizNum) {
                    this.bizNum = bizNum;
                }

                public void setGP_20(int GP_20) {
                    this.GP_20 = GP_20;
                }

                public void setGP_40(int GP_40) {
                    this.GP_40 = GP_40;
                }

                public void setHQ_40(int HQ_40) {
                    this.HQ_40 = HQ_40;
                }

                public int getBizNum() {
                    return bizNum;
                }

                public int getGP_20() {
                    return GP_20;
                }

                public int getGP_40() {
                    return GP_40;
                }

                public int getHQ_40() {
                    return HQ_40;
                }
            }

            public static class OnBizEntity {
                private int bizNum;
                private int GP_20;
                private int GP_40;
                private int HQ_40;

                public void setBizNum(int bizNum) {
                    this.bizNum = bizNum;
                }

                public void setGP_20(int GP_20) {
                    this.GP_20 = GP_20;
                }

                public void setGP_40(int GP_40) {
                    this.GP_40 = GP_40;
                }

                public void setHQ_40(int HQ_40) {
                    this.HQ_40 = HQ_40;
                }

                public int getBizNum() {
                    return bizNum;
                }

                public int getGP_20() {
                    return GP_20;
                }

                public int getGP_40() {
                    return GP_40;
                }

                public int getHQ_40() {
                    return HQ_40;
                }
            }

            public static class EndBizEntity {
                private int bizNum;
                private int GP_20;
                private int GP_40;
                private int HQ_40;

                public void setBizNum(int bizNum) {
                    this.bizNum = bizNum;
                }

                public void setGP_20(int GP_20) {
                    this.GP_20 = GP_20;
                }

                public void setGP_40(int GP_40) {
                    this.GP_40 = GP_40;
                }

                public void setHQ_40(int HQ_40) {
                    this.HQ_40 = HQ_40;
                }

                public int getBizNum() {
                    return bizNum;
                }

                public int getGP_20() {
                    return GP_20;
                }

                public int getGP_40() {
                    return GP_40;
                }

                public int getHQ_40() {
                    return HQ_40;
                }
            }
        }
    }


}




