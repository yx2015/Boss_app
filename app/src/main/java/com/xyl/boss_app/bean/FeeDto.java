package com.xyl.boss_app.bean;

import java.util.List;

/**
 * Created by yx on 2015/12/17 0017.
 */
public class FeeDto extends BaseDto {

    private FeeDtoInfo data;

    public FeeDtoInfo getData() {
        return data;
    }

    public void setData(FeeDtoInfo data) {
        this.data = data;
    }

    public static class FeeDtoInfo {
        private double totalReceivableMoney;// 总的应收
        private double totalReceivedMoney;//总的实收
        private double totalPayableMoney;// 总的应付
        private double totalPrepaidMoney;//总的实付
        private double totalGrossProfit;//总的利润
        private List<FeeInfo> list;//

        public void setTotalReceivableMoney(double totalReceivableMoney) {
            this.totalReceivableMoney = totalReceivableMoney;
        }

        public void setTotalReceivedMoney(double totalReceivedMoney) {
            this.totalReceivedMoney = totalReceivedMoney;
        }

        public void setTotalPayableMoney(double totalPayableMoney) {
            this.totalPayableMoney = totalPayableMoney;
        }

        public void setTotalPrepaidMoney(double totalPrepaidMoney) {
            this.totalPrepaidMoney = totalPrepaidMoney;
        }

        public void setTotalGrossProfit(double totalGrossProfit) {
            this.totalGrossProfit = totalGrossProfit;
        }

        public void setList(List<FeeInfo> list) {
            this.list = list;
        }

        public double getTotalReceivableMoney() {
            return totalReceivableMoney;
        }

        public double getTotalReceivedMoney() {
            return totalReceivedMoney;
        }

        public double getTotalPayableMoney() {
            return totalPayableMoney;
        }

        public double getTotalPrepaidMoney() {
            return totalPrepaidMoney;
        }

        public double getTotalGrossProfit() {
            return totalGrossProfit;
        }

        public List<FeeInfo> getList() {
            return list;
        }

        public static class FeeInfo {
            private String checkObject;// 公司名称
            private double receivableMoney;//应收
            private double receivedMoney;//实收
            private double payableMoney;//应付
            private double prepaidMoney;//实付
            private double grossProfit;//利润
            private int bizGP20Num;//20GP柜量
            private int bizGP40Num;//40GP柜量
            private int bizHQ40Num;//40HQ柜量

            public void setCheckObject(String checkObject) {
                this.checkObject = checkObject;
            }

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

            public void setBizGP20Num(int bizGP20Num) {
                this.bizGP20Num = bizGP20Num;
            }

            public void setBizGP40Num(int bizGP40Num) {
                this.bizGP40Num = bizGP40Num;
            }

            public void setBizHQ40Num(int bizHQ40Num) {
                this.bizHQ40Num = bizHQ40Num;
            }

            public String getCheckObject() {
                return checkObject;
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

            public int getBizGP20Num() {
                return bizGP20Num;
            }

            public int getBizGP40Num() {
                return bizGP40Num;
            }

            public int getBizHQ40Num() {
                return bizHQ40Num;
            }
        }
    }


}
