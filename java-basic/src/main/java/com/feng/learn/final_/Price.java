package com.feng.learn.final_;

public class Price {

    public static class Price1 {
        static final int initPrice = 20;
        static final Price1 INSTANCE = new Price1(3);
        @lombok.Getter
        int currentPrice;

        public Price1(int cutPrice) {
            this.currentPrice = initPrice - cutPrice;
        }

        public static void main(String[] args) {
            // ?
            Price1.INSTANCE.getCurrentPrice();
        }

    }

    public static class Price2 {
        static final int initPrice;
        static final Price2 INSTANCE = new Price2(3);

        static {
            initPrice = 20;
        }

        @lombok.Getter
        int currentPrice;

        public Price2(int cutPrice) {
            this.currentPrice = initPrice - cutPrice;
        }

        public static void main(String[] args) {
            // ?
            Price2.INSTANCE.getCurrentPrice();
        }

    }

    public static class Price3 {
        static final Price3 INSTANCE = new Price3(3);
        static final int initPrice = 20;

        @lombok.Getter
        int currentPrice;

        public Price3(int cutPrice) {
            this.currentPrice = initPrice - cutPrice;
        }

    }

    public static class Price4 {
        static final Price4 INSTANCE = new Price4(3);

        static {
            initPrice = 20;
        }

        static final int initPrice;

        @lombok.Getter
        int currentPrice;

        public Price4(int cutPrice) {
            this.currentPrice = initPrice - cutPrice;
        }

    }

    public static class Price5 {

        static {
            initPrice = 20;
        }

        static final Price5 INSTANCE = new Price5(3);
        static final int initPrice;

        @lombok.Getter
        int currentPrice;

        public Price5(int cutPrice) {
            this.currentPrice = initPrice - cutPrice;
        }

    }
}
