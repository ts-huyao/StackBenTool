package com.baiwangmaoyi.benchmark.dao;

public class BenResultDto {

    private String benType;
    private long hitCount;
    private long errorCount;
    private long periodInMili;

    public String getBenType() {
        return benType;
    }

    public void setBenType(String benType) {
        this.benType = benType;
    }

    public long getHitCount() {
        return hitCount;
    }

    public void setHitCount(long hitCount) {
        this.hitCount = hitCount;
    }

    public long getPeriodInMili() {
        return periodInMili;
    }

    public void setPeriodInMili(long periodInMili) {
        this.periodInMili = periodInMili;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Benchmark Result:").append(System.lineSeparator());
        sb.append(this.benType).append(":").append(System.lineSeparator());
        sb.append(this.getHitCount()).append(" tests are completed in ").append(this.getPeriodInMili()).append(" " +
                "miliseconds.").append(System.lineSeparator());
        double tps = ((double)this.getHitCount()*1000)/(double) this.getPeriodInMili();
        sb.append("TPS:").append(tps).append(System.lineSeparator());
        sb.append("Error:").append(this.getErrorCount());
        return sb.toString();
    }

    public long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(long errorCount) {
        this.errorCount = errorCount;
    }


    public static final class BenResultDtoBuilder {
        private String benType;
        private long hitCount;
        private long errorCount;
        private long periodInMili;

        private BenResultDtoBuilder() {
        }

        public static BenResultDtoBuilder aBenResultDto() {
            return new BenResultDtoBuilder();
        }

        public BenResultDtoBuilder withBenType(String benType) {
            this.benType = benType;
            return this;
        }

        public BenResultDtoBuilder withHitCount(long hitCount) {
            this.hitCount = hitCount;
            return this;
        }

        public BenResultDtoBuilder withErrorCount(long errorCount) {
            this.errorCount = errorCount;
            return this;
        }

        public BenResultDtoBuilder withPeriodInMili(long periodInMili) {
            this.periodInMili = periodInMili;
            return this;
        }

        public BenResultDto build() {
            BenResultDto benResultDto = new BenResultDto();
            benResultDto.setBenType(benType);
            benResultDto.setHitCount(hitCount);
            benResultDto.setErrorCount(errorCount);
            benResultDto.setPeriodInMili(periodInMili);
            return benResultDto;
        }
    }
}
