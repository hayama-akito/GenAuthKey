package tw.com.e_newken.keroro.genauthkey;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;


/**
 * Created by keroro on 2015/12/23.
 */
public class AuthenticationObj {
    //region final parameters
    private static final String TAG = "AuthenticationObj";
    private static final String CHARSET_NAME = "UTF-8";
    private static final int UUID_BYTES = 16;

    //endregion

    //region private variables
    private String platformName;
    private String platformCharacteristic;
    private byte[] platformCharacteristicBytes;
    private String platformComment;
    //endregion

    //region Constructor
    public AuthenticationObj(String name, String characteristic, String comment) throws UnsupportedEncodingException {
        platformName = new String(name);
        platformCharacteristic = new String(characteristic);
        platformComment = new String(comment);

        platformCharacteristicBytes = platformCharacteristic.getBytes(CHARSET_NAME);
    }
    //endregion

    //region private member functions
    private byte[] generateKeyBytes() {
        final UUID randomUUID = UUID.randomUUID();
        final long leastSignificantBits = randomUUID.getLeastSignificantBits();
        final long mostSignificantBits = randomUUID.getMostSignificantBits();

        ByteBuffer networkByteBuffer = ByteBuffer.allocate(UUID_BYTES);
        networkByteBuffer.order(ByteOrder.BIG_ENDIAN);
        networkByteBuffer.putLong(mostSignificantBits);
        networkByteBuffer.putLong(leastSignificantBits);
        return networkByteBuffer.array();
    }

    //endregion

    //region property
    public final String getPlatformName() {
        return platformName;
    }

    public final String getPlatformComment() {
        return platformComment;
    }

    public final String getPlatformCharacteristic() {
        return platformCharacteristic;
    }
    //endregion

    //region public members
    public boolean Import() {
        return true;
    }

    public boolean Export() {
        return true;
    }


    public boolean generateAuthKey(String fileName) {
        if (platformCharacteristicBytes == null)
            throw new NullPointerException();


        return true;
    }

    public boolean generateKeypair(String projectName, String fileName) {
        return true;
    }

    public boolean generateStateCode(String projectName, String characteristic) {
        return true;
    }
    //endregion
}
