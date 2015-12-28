package tw.com.e_newken.keroro.genauthkey;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by keroro on 2015/12/24.
 */
public class AuthenticationFile {
    //region final parameters
    private final String TAG = "AuthenticationFile";
    private final String PLATFORMS_FILE = "/platforms.xml";

    //endregion

    //region private variables
    private String defaultDirectory;


    private ArrayList<PlatformInfo> platformInfoArrayList;

    //endregion

    //region Constructor
    public AuthenticationFile(Context context) throws NameNotFoundException {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        PackageInfo p = m.getPackageInfo(s, 0);
        defaultDirectory = p.applicationInfo.dataDir;
    }
    //endregion

    //region private functions
    private boolean duplicateResources(Context context, File f) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
        BufferedInputStream in = new BufferedInputStream(context.getResources().openRawResource(R.raw.platforms));

        // Transfer bytes from in to out
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

        return true;
    }
    //endregion

    //region public functions
    public final ArrayList<PlatformInfo> enumeratePlatforms(Context context) throws IOException, SAXException, ParserConfigurationException {
        File platformsXmlFile = new File(defaultDirectory, PLATFORMS_FILE);

        if (!platformsXmlFile.exists()) {
            if (!duplicateResources(context, platformsXmlFile)) {
                String msg = "Duplicate " + defaultDirectory + "/" + PLATFORMS_FILE + " fail.";
                Log.e(TAG, msg);
                return null;
            }
        }

        PlatformXmlHandler handler = new PlatformXmlHandler(platformsXmlFile);

        platformInfoArrayList = handler.enumeratePlatforms();
        return platformInfoArrayList;
    }

    //endregion
}
