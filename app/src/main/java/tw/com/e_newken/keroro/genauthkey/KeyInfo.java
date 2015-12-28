package tw.com.e_newken.keroro.genauthkey;

/**
 * Created by keroro on 2015/12/28.
 */
public class KeyInfo {
    //region private variables
    private String name;
    private String file;
    private String aes_ks;
    private String aes_iv;
    private byte[] aes_ks_bytes;
    private byte[] aes_iv_bytes;
    private String createdDate;
    private String enabledDate;
    private String keypair;
    private String keypair_nonce;
    private String authkey;


    private byte[] characteristicBytes;
    private String comment;
    //endregion
}
