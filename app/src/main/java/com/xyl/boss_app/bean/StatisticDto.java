package com.xyl.boss_app.bean;

/**
 * @ClassName StatisticDto
 * @Description:首页统计数据 Created by yx on 2015/12/18 0018.
 */
public class StatisticDto extends BaseDto {

    private StatisticInfo data;

    public StatisticInfo getData() {
        return data;
    }

    public void setData(StatisticInfo data) {
        this.data = data;
    }


    public static class StatisticInfo {
        private double receivableMoney;// 应收
        private double receivedMoney;// 实收
        private double payableMoney;//应付
        private double prepaidMoney;//实付
        private double grossProfit;//利润
        private StartBizEntity startBiz;//起运港
        private OnBizEntity onBiz;//海上运输
        private EndBizEntity endBiz;//目的港

        public void setReceivableMoney(double receivableMoney) {
            this.receivableMoney = receivableMoney;
        }

        public void setReceivedMoney(double receivedMoney) {
            this.receivedMoney = receivedMoney;
        }

        public void setPayableMoney(double payableMoney) {
            this.payableMoney = payableMoney;
        }

        public void setPrepaidMoney(double prepaidMoney) {
            this.prepaidMoney = prepaidMoney;
        }

        public void setGrossProfit(double grossProfit) {
            this.grossProfit = grossProfit;
        }

        public void setStartBiz(StartBizEntity startBiz) {
            this.startBiz = startBiz;
        }

        public void setOnBiz(OnBizEntity onBiz) {
            this.onBiz = onBiz;
        }

        public double getReceivableMoney() {
            return receivableMoney;
        }

        public double getReceivedMoney() {
            return receivedMoney;
        }

        public double getPayableMoney() {
            return payableMoney;
        }

        public double getPrepaidMoney() {
            return prepaidMoney;
        }

        public double getGrossProfit() {
            return grossProfit;
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

        public void setEndBiz(EndBizEntity endBiz) {
            this.endBiz = endBiz;
        }

        public static class StartBizEntity {
            private int bizNum;
            private int HQ_40;
            private int GP_20;
            private int GP_40;

            public void setBizNum(int bizNum) {
                this.bizNum = bizNum;
            }

            public void setHQ_40(int HQ_40) {
                this.HQ_40 = HQ_40;
            }

            public void setGP_20(int GP_20) {
                this.GP_20 = GP_20;
            }

            public void setGP_40(int GP_40) {
                this.GP_40 = GP_40;
            }

            public int getBizNum() {
                return bizNum;
            }

            public int getHQ_40() {
                return HQ_40;
            }

            public int getGP_20() {
                return GP_20;
            }

            public int getGP_40() {
                return GP_40;
            }
        }

        public static class OnBizEntity {
            private int bizNum;
            private int HQ_40;
            private int GP_20;
            private int GP_40;

            public void setBizNum(int bizNum) {
                this.bizNum = bizNum;
            }

            public void setHQ_40(int HQ_40) {
                this.HQ_40 = HQ_40;
            }

            public void setGP_20(int GP_20) {
                this.GP_20 = GP_20;
            }

            public void setGP_40(int GP_40) {
                this.GP_40 = GP_40;
            }

            public int getBizNum() {
                return bizNum;
            }

            public int getHQ_40() {
                return HQ_40;
            }

            public int getGP_20() {
                return GP_20;
            }

            public int getGP_40() {
                return GP_40;
            }
        }

        public static class EndBizEntity {
            private int bizNum;
            private int HQ_40;
            private int GP_20;
            private int GP_40;

            public void setBizNum(int bizNum) {
                this.bizNum = bizNum;
            }

            public void setHQ_40(int HQ_40) {
                this.HQ_40 = HQ_40;
            }

            public void setGP_20(int GP_20) {
                this.GP_20 = GP_20;
            }

            public void setGP_40(int GP_40) {
                this.GP_40 = GP_40;
            }

            public int getBizNum() {
                return bizNum;
            }

            public int getHQ_40() {
                return HQ_40;
            }

            public int getGP_20() {
                return GP_20;
            }

            public int getGP_40() {
                return GP_40;
            }
        }
    }
}
