package com.baiwangmaoyi.benchmark.dto;

public class RiakBenOptionDto {
    private int threadCount;
    private int writeCount;
    private int fileSize;

    private String riakHost;
    private int maxConn = -1;
    private int minConn = -1;
    private String bucketType;
    private String bucketName;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getWriteCount() {
        return writeCount;
    }

    public void setWriteCount(int writeCount) {
        this.writeCount = writeCount;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getRiakHost() {
        return riakHost;
    }

    public void setRiakHost(String riakHost) {
        this.riakHost = riakHost;
    }

    public int getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public int getMinConn() {
        return minConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public String getBucketType() {
        return bucketType;
    }

    public void setBucketType(String bucketType) {
        this.bucketType = bucketType;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }


    public static final class RiakBenOptionDtoBuilder {
        private int threadCount;
        private int writeCount;
        private int fileSize;
        private String riakHost;
        private int maxConn;
        private int minConn;
        private String bucketType;
        private String bucketName;

        private RiakBenOptionDtoBuilder() {
        }

        public static RiakBenOptionDtoBuilder aRiakBenOptionDto() {
            return new RiakBenOptionDtoBuilder();
        }

        public RiakBenOptionDtoBuilder withThreadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        public RiakBenOptionDtoBuilder withWriteCount(int writeCount) {
            this.writeCount = writeCount;
            return this;
        }

        public RiakBenOptionDtoBuilder withFileSize(int fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public RiakBenOptionDtoBuilder withRiakHost(String riakHost) {
            this.riakHost = riakHost;
            return this;
        }

        public RiakBenOptionDtoBuilder withMaxConn(int maxConn) {
            this.maxConn = maxConn;
            return this;
        }

        public RiakBenOptionDtoBuilder withMinConn(int minConn) {
            this.minConn = minConn;
            return this;
        }

        public RiakBenOptionDtoBuilder withBucketType(String bucketType) {
            this.bucketType = bucketType;
            return this;
        }

        public RiakBenOptionDtoBuilder withBucketName(String bucketName) {
            this.bucketName = bucketName;
            return this;
        }

        public RiakBenOptionDto build() {
            RiakBenOptionDto riakBenOptionDto = new RiakBenOptionDto();
            riakBenOptionDto.setThreadCount(threadCount);
            riakBenOptionDto.setWriteCount(writeCount);
            riakBenOptionDto.setFileSize(fileSize);
            riakBenOptionDto.setRiakHost(riakHost);
            riakBenOptionDto.setMaxConn(maxConn);
            riakBenOptionDto.setMinConn(minConn);
            riakBenOptionDto.setBucketType(bucketType);
            riakBenOptionDto.setBucketName(bucketName);
            return riakBenOptionDto;
        }
    }
}
