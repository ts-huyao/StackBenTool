package com.baiwangmaoyi.benchmark;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.baiwangmaoyi.benchmark.dao.BenResultDto;
import com.baiwangmaoyi.benchmark.dao.RiakBlobDAO;
import com.baiwangmaoyi.benchmark.dto.RiakBenOptionDto;

public class RiakBen {

    private static Logger logger = (Logger) LoggerFactory.getLogger(RiakBen.class);

    public static void main(String[] args) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

        RiakBen riakBen = new RiakBen();

        RiakBenOptionDto optionDto = riakBen.parrseOptions(args);

        if (optionDto == null) {
            System.exit(1);
        }

        BenResultDto result = riakBen.benchmark(optionDto);

        logger.info(result.toString());


    }

    public RiakBenOptionDto parrseOptions(String[] args) {
        Options options = new Options();

        Option optThread = new Option("th", "thread", true, "Concurrent threads");
        optThread.setRequired(true);
        optThread.setType(Number.class);
        options.addOption(optThread);

        Option optWrites = new Option("tw", "write", true, "Total writes");
        optWrites.setRequired(true);
        optWrites.setType(Number.class);
        options.addOption(optWrites);

        Option optFileSize = new Option("fs", "size", true, "File size in KB");
        optFileSize.setRequired(true);
        optFileSize.setType(Number.class);
        options.addOption(optFileSize);

        Option optRiakHosts = new Option("rh", "riakhost", true,
                "Riak hosts, exp: 127.0.0.1:8087 or 192.168.1.10:8087,192.168.1.11:8087");
        optRiakHosts.setRequired(true);
        optRiakHosts.setType(String.class);
        options.addOption(optRiakHosts);

        Option optRiakMaxConn = new Option("maxc", "maxconn", true,
                "Riak max connection");
        optRiakMaxConn.setRequired(false);
        optRiakMaxConn.setType(Number.class);
        options.addOption(optRiakMaxConn);

        Option optRiakMinCoon = new Option("minc", "maxconn", true,
                "Riak max connection");
        optRiakMinCoon.setRequired(false);
        optRiakMinCoon.setType(Number.class);
        options.addOption(optRiakMinCoon);

        Option optBucketType = new Option("bt", "buckettype", true,
                "Riak bucket type");
        optBucketType.setRequired(true);
        optBucketType.setType(String.class);
        options.addOption(optBucketType);

        Option optBucketName = new Option("bn", "bucketname", true,
                "Riak bucket name");
        optBucketName.setRequired(true);
        optBucketName.setType(String.class);
        options.addOption(optBucketName);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            RiakBenOptionDto.RiakBenOptionDtoBuilder builder = RiakBenOptionDto.RiakBenOptionDtoBuilder
                    .aRiakBenOptionDto()
                    .withThreadCount(((Number) cmd.getParsedOptionValue("thread")).intValue())
                    .withWriteCount(((Number) cmd.getParsedOptionValue("write")).intValue())
                    .withFileSize(((Number) cmd.getParsedOptionValue("size")).intValue())
                    .withRiakHost((String) (cmd.getParsedOptionValue("riakhost")))
                    .withBucketName((String) cmd.getParsedOptionValue("bucketname"))
                    .withBucketType((String) cmd.getParsedOptionValue("buckettype"));


            if (cmd.hasOption("maxconn")) {
                builder.withMaxConn(((Number) cmd.getParsedOptionValue("maxconn")).intValue());
            }

            if (cmd.hasOption("minconn")) {
                builder.withMaxConn(((Number) cmd.getParsedOptionValue("minconn")).intValue());
            }


            return builder.build();
        } catch (ParseException e) {
            //System.out.println(e.getMessage());
            formatter.printHelp("Riak Benchmark Tool", options);
        }
        return null;
    }

    public BenResultDto benchmark(RiakBenOptionDto optionDto) {
        final RiakBlobDAO dao = new RiakBlobDAO(optionDto.getRiakHost(),
                optionDto.getMaxConn(),
                optionDto.getMinConn(),
                optionDto.getBucketType(),
                optionDto.getBucketName());
        final int benTarget = optionDto.getWriteCount();

        final AtomicLong opCounter = new AtomicLong(0L);
        final AtomicLong errorCounter = new AtomicLong(0L);
        Random random = new Random();
        final byte[] data = new byte[optionDto.getFileSize() * 1024];
        random.nextBytes(data);
        StopWatch stopWatch = new StopWatch();
        stopWatch.reset();
        opCounter.set(0L);
        stopWatch.start();

/*        ExecutorService es = Executors.newFixedThreadPool(optionDto.getThreadCount());

        List<Callable<Boolean>> callableList = new ArrayList<>();

        for (int i = 0; i < benTarget; i++) {
            callableList.add(() -> {
                dao.create(UUID.randomUUID().toString(), data, "image/jpeg");
                return true;
            });
        }
        try {
            List<Future<Boolean>> futureList = es.invokeAll(callableList);
            for (Future<Boolean> future:futureList) {
                future.get();
            }
        } catch (InterruptedException |ExecutionException e) {
            logger.error("Error occurs.", e);
        }*/


        ForkJoinPool forkJoinPool = new ForkJoinPool(optionDto.getThreadCount());
        try {
            forkJoinPool.submit(() ->
                    IntStream.range(0, optionDto.getThreadCount()).parallel()
                            .forEach(p -> {
                                while (opCounter.incrementAndGet() <= benTarget) {
                                    try {
                                        dao.create(UUID.randomUUID().toString(), data, "image/jpeg");
                                    } catch (Throwable throwable){
                                        errorCounter.incrementAndGet();
                                        logger.error("Riak create error occurs.", throwable);
                                    }
                                }
                            })
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error occurs.", e);
        }


        stopWatch.stop();
        dao.destroy();

        return BenResultDto.BenResultDtoBuilder.aBenResultDto()
                .withBenType("Riak Write Benchmark")
                .withHitCount(benTarget)
                .withPeriodInMili(stopWatch.getTime())
                .build();
    }

}
