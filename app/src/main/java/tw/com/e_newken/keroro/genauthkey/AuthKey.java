package tw.com.e_newken.keroro.genauthkey;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.UUID;

/**
 * Created by keroro on 2015/12/28.
 */
public class AuthKey {

    //NOTICE: authkey 144-bytes
//    struct authkey {
//        char id[16];
//        char prj_name[16];
//        char uuid[16];
//        char release_date[32];
//        char enable_date[32];
//        int num_keys;
//        unsigned int flags;
//        unsigned long keypair_timestamp;
//        int keypair_key_num;
//        char reserved[12];
//    };

    public static final int LIMIT_PRJNAME = 15;
    //region final parameters
    private static final String AUTHKEY_ID = "AUTHKEY_CFG";
    private static final int UUID_BYTES = 16;


    //endregion
    //region private variables
    private String projectName;
    private String uuid;
    private byte[] uuid_bytes;
    private String releasedDate;
    private String enabledDate;
    private int keys;
    private int flags;
    private int timestamp;
    //endregion

    //region Constructor
    public AuthKey(String prjName, int numberOfKeys) {
        if (prjName.length() > LIMIT_PRJNAME) {
            projectName = prjName.substring(0, LIMIT_PRJNAME - 1);
        } else {
            projectName = prjName;
        }

        UUID randomUUID = UUID.randomUUID();
        long leastSignificantBits = randomUUID.getLeastSignificantBits();
        long mostSignificantBits = randomUUID.getMostSignificantBits();

        ByteBuffer networkByteBuffer = ByteBuffer.allocate(UUID_BYTES);
        networkByteBuffer.order(ByteOrder.BIG_ENDIAN);
        networkByteBuffer.putLong(mostSignificantBits);
        networkByteBuffer.putLong(leastSignificantBits);
        uuid_bytes = networkByteBuffer.array();
        uuid = Utils.bytesToHex(uuid_bytes);

        releasedDate = (new Date()).toString();
        keys = numberOfKeys;
        flags = 0;

    }

    public AuthKey(String file, String aes_iv) {

    }
    //endregion

    //region properties
    public final String getProjectName() {
        return projectName;
    }

    public final String getUUID() {
        return uuid;
    }

    public final String getReleasedDate() {
        return releasedDate;
    }

    public final String getEnabledDate() {
        return enabledDate;
    }

    public final int getKeys() {
        return keys;
    }

    public final int getFlags() {
        return flags;
    }

    public final int getTimestamp() {
        return timestamp;
    }
    //endregion

    //region private functions
    private boolean parser() {
        return true;
    }
    //endregion

    //region public functions
    public boolean export(String file) {
        return true;
    }
    //endregion


}
