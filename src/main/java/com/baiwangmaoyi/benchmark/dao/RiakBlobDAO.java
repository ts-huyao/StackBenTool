package com.baiwangmaoyi.benchmark.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import com.google.common.base.Throwables;

public class RiakBlobDAO {

    private static final Logger log = LoggerFactory.getLogger(RiakBlobDAO.class);
    private RiakClient riakClient;
    private String BucketType;
    private String bucketName;

    public RiakBlobDAO(String riakHost, int MaxConn, int MinConn, String BucketType, String bucketName) {
        this.bucketName = bucketName;
        final String[] hostNameCluster = riakHost.split(",");
        if (BucketType == null) {
            throw new Error("Riak bucket type is not defined!");
        }

        this.BucketType = BucketType;
        List hostCluster = Arrays.asList(hostNameCluster);
        try {
            riakClient = new RiakClient(setUpCluster(hostCluster, MaxConn, MinConn));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RiakCluster setUpCluster(List<String> riakHost, int MaxConn, int MinConn)
            throws UnknownHostException {
        RiakNode.Builder builder = new RiakNode.Builder()
                .withMinConnections(MinConn)
                .withMaxConnections(MaxConn)
                .withBlockOnMaxConnections(true);
        List<RiakNode> nodes = RiakNode.Builder.buildNodes(builder, riakHost);

        RiakCluster cluster = new RiakCluster.Builder(nodes)
                .build();

        cluster.start();
        return cluster;
    }

    public void create(String id, byte[] content, String contentType) {

        RiakObject quoteObject = new RiakObject()
                .setContentType(contentType)
                .setValue(BinaryValue.create(content));

        Namespace quotesBucket = new Namespace(BucketType, getBucketName());
        Location quoteObjectLocation = new Location(quotesBucket, id);
        StoreValue storeOp = new StoreValue.Builder(quoteObject)
                .withLocation(quoteObjectLocation)
                .build();
        try {
            riakClient.execute(storeOp);
            log.info("Stored object {} in Riak", id);
        } catch (Exception e) {
            throw new RuntimeException(
                    "some problems happened in riak ,bucketType is needed to config in the riak server", e);
        }
    }

    public void create(String id, InputStream content, String contentType) {
        try {
            create(id, IOUtils.toByteArray(content), contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String id) {
        Namespace Bucket = new Namespace(getBucketName());
        Location quoteObjectLocation = new Location(Bucket, id);
        DeleteValue deleteOp = new DeleteValue.Builder(quoteObjectLocation)
                .build();
        try {
            riakClient.execute(deleteOp);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Delete {} failed from block, exception: {}", id, e);
            throw Throwables.propagate(e);
        }
        log.info("Quote object {} successfully deleted", id);
    }

    public byte[] get(String id) {
        Namespace quotesBucket = new Namespace(BucketType, getBucketName());
        Location quoteObjectLocation = new Location(quotesBucket, id);
        FetchValue fetchOp = new FetchValue.Builder(quoteObjectLocation)
                .build();
        try {
            RiakObject fetchedObject = riakClient.execute(fetchOp).getValue(RiakObject.class);
            if (fetchedObject != null && fetchedObject.getValue() != null) {
                log.info("object successfully fetched object {}", id);
                return fetchedObject.getValue().getValue();
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(
                    "some problems happened in riak ,bucketType is needed to config in the riak server", e);
        }
    }

    public void destroy() {
        if (riakClient != null) {
            riakClient.shutdown();
            riakClient = null;
        }
    }

    public String getPresignedUrl(String id) {
        throw new RuntimeException("Not supported");
    }

    private String getBucketName() {
        return this.bucketName;
    }
}
