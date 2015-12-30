package tw.com.e_newken.keroro.genauthkey;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by keroro on 2015/12/28.
 */
public class AuthKey {

    /*
NOTICE: authkey 144-bytes
struct authkey {
char id[16];
char prj_name[16];
char uuid[16];
char release_date[32];
char enable_date[32];
int num_keys;
unsigned int flags;
unsigned long keypair_timestamp;
int keypair_key_num;
char reserved[12];
};
*/


    //region final parameters
    public static final int LIMIT_PRJNAME = 15;

    private static final String TAG = "AuthKey";
    private static final String AUTHKEY_ID = "AUTHKEY_CFG";

    private static final int AUTHKEY_SIZE = 144;
    private static final int AUTHKEY_ID_BYTES = 16;
    private static final int AUTHKEY_PRJNAME_BYTES = 16;
    private static final int AUTHKEY_UUID_BYTES = 16;
    private static final int AUTHKEY_DATETIME_BYTES = 32;
    private static final int AUTHKEY_PADDING_BYTES = 12;


    private static final String CHARSETENC = "ISO-8859-1";

    //authkey flags
    private static final int F_AUTHKEY_ENABLE = (1);
    private static final int F_AUTHKEY_NO_KEYS = (1 << 8);
    private static final int F_AUTHKEY_CRITICAL = (1 << 15);
    private static final int F_AUTHKEY_LOCK = (1 << 16);

    //endregion
    //region private variables
    private String projectName;
    private String uuid;
    private byte[] uuidBytes;
    private String releasedDate;
    private String enabledDate;
    private int keys;
    private int flags;
    private long keypairTimestamp;
    private int keypairKeyNum;

    //endregion

    //region Constructor
    public AuthKey(String prjName, int numberOfKeys) {
        if (prjName.length() > LIMIT_PRJNAME) {
            projectName = prjName.substring(0, LIMIT_PRJNAME - 1);
        } else {
            projectName = prjName;
        }

        if (numberOfKeys <= 0) {
            throw new IllegalArgumentException("numberOfKeys <= 0");
        }

        UUID randomUUID = UUID.randomUUID();
        long leastSignificantBits = randomUUID.getLeastSignificantBits();
        long mostSignificantBits = randomUUID.getMostSignificantBits();

        ByteBuffer networkByteBuffer = ByteBuffer.allocate(AUTHKEY_UUID_BYTES);
        networkByteBuffer.order(ByteOrder.BIG_ENDIAN);
        networkByteBuffer.putLong(mostSignificantBits);
        networkByteBuffer.putLong(leastSignificantBits);
        uuidBytes = networkByteBuffer.array();
        uuid = Utils.bytesToHex(uuidBytes);

        releasedDate = Utils.getDateTime();
        keys = numberOfKeys;
        keypairTimestamp = 0;
        keypairKeyNum = 0;
        flags = 0;

    }

    public AuthKey(byte[] authkeyBytes) {
        if (authkeyBytes.length != AUTHKEY_SIZE) {
            throw new IllegalArgumentException("Length incorrect");
        }

        if (!parse(authkeyBytes)) {
            throw new IllegalArgumentException("Parse fail");
        }
    }
    //endregion

    //region properties
    public final String getProjectName() {
        return projectName;
    }

    public final String getUUID() {
        return uuid;
    }

    public final byte[] getUUIDBytes() {
        return uuidBytes;
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

    public final long getKeypairTimestamp() {
        return keypairTimestamp;
    }

    public final int getKeypairKeyNum() {
        return keypairKeyNum;
    }

    public final boolean isEnable() {
        return ((flags & F_AUTHKEY_ENABLE) != 0);
    }

    public final boolean isLock() {
        return ((flags & F_AUTHKEY_LOCK) != 0);
    }

    public final boolean isNoKeys() {
        return ((flags & F_AUTHKEY_NO_KEYS) != 0);
    }

    public final boolean isCritical() {
        return ((flags & F_AUTHKEY_CRITICAL) != 0);
    }

    //endregion

    //region private functions
    private boolean parse(byte[] authkeyBytes) {
        String id = new String(Arrays.copyOfRange(authkeyBytes, 0, AUTHKEY_ID_BYTES));
        if (id != AUTHKEY_ID)
            return false;

        projectName = new String(Arrays.copyOfRange(authkeyBytes, AUTHKEY_ID_BYTES, AUTHKEY_PRJNAME_BYTES));
        uuidBytes = Arrays.copyOfRange(authkeyBytes, AUTHKEY_ID_BYTES + AUTHKEY_PRJNAME_BYTES, AUTHKEY_UUID_BYTES);
        uuid = Utils.bytesToHex(uuidBytes);
        releasedDate = new String(Arrays.copyOfRange(authkeyBytes, AUTHKEY_ID_BYTES + AUTHKEY_PRJNAME_BYTES + AUTHKEY_UUID_BYTES,
                AUTHKEY_DATETIME_BYTES));
        enabledDate = new String(Arrays.copyOfRange(authkeyBytes, AUTHKEY_ID_BYTES + AUTHKEY_PRJNAME_BYTES + AUTHKEY_UUID_BYTES + AUTHKEY_DATETIME_BYTES,
                AUTHKEY_DATETIME_BYTES));

        int ofs = AUTHKEY_ID_BYTES + AUTHKEY_PRJNAME_BYTES + AUTHKEY_UUID_BYTES + (AUTHKEY_DATETIME_BYTES * 2);
        ByteBuffer byteBuffer = ByteBuffer.wrap(authkeyBytes, ofs, AUTHKEY_SIZE - ofs);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        keys = byteBuffer.getInt();
        flags = byteBuffer.getInt();
        keypairTimestamp = byteBuffer.getLong();
        keypairKeyNum = byteBuffer.getInt();

        return true;
    }
    //endregion

    //region public functions
    public byte[] export() throws UnsupportedEncodingException {

        ByteBuffer authkeyByteBuffer = ByteBuffer.allocate(AUTHKEY_SIZE);
        authkeyByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        authkeyByteBuffer.put(Arrays.copyOf(AUTHKEY_ID.getBytes(CHARSETENC), AUTHKEY_ID_BYTES));
        authkeyByteBuffer.put(Arrays.copyOf(projectName.getBytes(CHARSETENC), AUTHKEY_PRJNAME_BYTES));
        authkeyByteBuffer.put(uuidBytes);
        authkeyByteBuffer.put(Arrays.copyOf(releasedDate.getBytes(CHARSETENC), AUTHKEY_DATETIME_BYTES));

        byte[] enabledDatePadding = new byte[AUTHKEY_DATETIME_BYTES];
        Arrays.fill(enabledDatePadding, (byte) 0);
        authkeyByteBuffer.put(enabledDatePadding);

        authkeyByteBuffer.putInt(keys);
        authkeyByteBuffer.putInt(flags);
        authkeyByteBuffer.putLong(keypairTimestamp);
        authkeyByteBuffer.putInt(keypairKeyNum);

        byte[] padding = new byte[AUTHKEY_PADDING_BYTES];
        Arrays.fill(padding, (byte) 0);
        authkeyByteBuffer.put(padding);

//        try {
//            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fs));
//            out.write(authkeyByteBuffer.array(), 0, AUTHKEY_SIZE);
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        return authkeyByteBuffer.array();
    }
    //endregion


}
