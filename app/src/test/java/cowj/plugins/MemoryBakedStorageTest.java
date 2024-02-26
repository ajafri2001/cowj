package cowj.plugins;

import cowj.DataSource;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoryBakedStorageTest {
    static final String BUCKET_NAME = "foo";
    static MemoryBackedStorage ms = new MemoryBackedStorage();

    @BeforeClass
    public static void beforeClass() {
        Assert.assertFalse(ms.delete(BUCKET_NAME, "bar"));
        Assert.assertTrue(ms.createBucket(BUCKET_NAME, "", false));
        Assert.assertFalse(ms.createBucket(BUCKET_NAME, "", false));
    }

    @AfterClass
    public static void afterClass() {
        Assert.assertTrue(ms.deleteBucket(BUCKET_NAME));
        Assert.assertFalse(ms.deleteBucket(BUCKET_NAME));
        ms.dataMemory.clear();
    }

    @Test
    public void storageTest() {
        DataSource ds = MemoryBackedStorage.STORAGE.create("bar", Map.of(), () -> "");
        Assert.assertNotNull(ds);
        Assert.assertTrue(ds.proxy() instanceof MemoryBackedStorage);
    }

    @Test
    public void safeBooleanTest() {
        Assert.assertTrue(ms.safeBoolean(() -> {
        }));
        Assert.assertFalse(ms.safeBoolean(() -> {
            throw new RuntimeException();
        }));
    }

    @Test
    public void creationDeletionTest() {
        Assert.assertTrue(ms.dumpb(BUCKET_NAME, "bar", "hello!".getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals("hello!", ms.loads(BUCKET_NAME, "bar"));
        byte[] bytes = ms.loadb(BUCKET_NAME, "bar");
        Assert.assertNotNull(bytes);
        String s = new String(bytes);
        Assert.assertEquals("hello!", s);
        Assert.assertTrue(ms.fileExist(BUCKET_NAME, "bar"));
        Assert.assertTrue(ms.delete(BUCKET_NAME, "bar"));
        Assert.assertFalse(ms.delete(BUCKET_NAME, "bar"));
    }

    @Test
    public void streamTest(){
        final String streamBucket = "stream" ;
        Assert.assertTrue( ms.createBucket(streamBucket, "", false));
        final int max = 10;
        for ( int i=0; i< max; i++ ){
            Object data = Map.of("x", i );
            Assert.assertTrue( ms.dump(streamBucket, "x/" + i, data ));
        }
        Set<Integer> set = ms.allData( streamBucket , "x/" )
                .map( o -> (int)((Map)o).get("x") ).collect(Collectors.toSet());
        Assert.assertEquals( max, set.size() );
    }
}