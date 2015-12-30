package tw.com.e_newken.keroro.genauthkey;

/**
 * Created by keroro on 2015/12/23.
 */
public class AuthenticationObj {
    //region final parameters
    private static final String TAG = "AuthenticationObj";
    private static final String CHARSETENC = "ISO-8859-1";
    //endregion

    //region private variables
    private String platformName;
    private String platformCharacteristic;
    private String platformCreatedDate;
    private byte[] platformCharacteristicBytes;
    private String platformComment;
    //endregion


    //region Constructor
    public AuthenticationObj(String name, String createdDate, String characteristic, String comment) {
        platformName = new String(name);
        platformCreatedDate = new String(createdDate);
        platformCharacteristic = new String(characteristic);
        platformCharacteristicBytes = Utils.HexStringToByteArray(platformCharacteristic);
        platformComment = new String(comment);
    }
    //endregion

    //region private member functions


    //endregion

    //region properties
    public final String getPlatformName() {
        return platformName;
    }

    public final String getPlatformComment() {
        return platformComment;
    }

    public final String getPlatformCharacteristic() {
        return platformCharacteristic;
    }

    public final byte[] getPlatformCharacteristicBytes() {
        return platformCharacteristicBytes;
    }

    public final String getPlatformCreatedDate() {
        return platformCreatedDate;
    }
    //endregion

    //region public members
    public boolean Import() {
        return true;
    }

    public boolean Export() {
        return true;
    }


    public boolean generateAuthKey(String prjName) {
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
