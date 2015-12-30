package tw.com.e_newken.keroro.genauthkey;

import java.io.UnsupportedEncodingException;

/**
 * Created by keroro on 2015/12/24.
 */
public class PlatformInfo {
    //region private final
    private static final String CHARSETENC = "ISO-8859-1";
    //endregion

    //region private variables
    private String name;
    private String createDate;
    private String characteristic;
    private byte[] characteristicBytes;
    private String comment;
    //endregion

    //region Constructor
    public PlatformInfo(String name) {

    }

    public PlatformInfo(String name, String createDate, String characteristic, String comment) throws UnsupportedEncodingException {
        name = new String(name);
        createDate = new String(createDate);
        characteristic = new String(characteristic);
        comment = new String(comment);

        characteristicBytes = characteristic.getBytes(CHARSETENC);
    }
    //endregion

    //region properties
    public final String getName() {
        return name;
    }

    public final String getCreateDate() {
        return createDate;
    }

    public final String getCharacteristic() {
        return characteristic;
    }

    public final byte[] getCharacteristicBytes() {
        return characteristicBytes;
    }

    public final String getComment() {
        return comment;
    }
    //endregion
}
