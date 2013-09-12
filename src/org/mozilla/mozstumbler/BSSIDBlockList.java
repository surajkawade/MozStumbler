package org.mozilla.mozstumbler;

import android.net.wifi.ScanResult;

import java.util.Locale;
import java.util.regex.Pattern;

final class BSSIDBlockList {

    private static final String  NULL_BSSID        = "000000000000";
    private static final String  WILDCARD_BSSID    = "ffffffffffff";
    private static final Pattern BSSID_PATTERN     = Pattern.compile("([0-9a-f]{12})");

    private BSSIDBlockList() {
    }

    static boolean contains(ScanResult scanResult) {
        String BSSID = scanResult.BSSID;
        return BSSID == null || !isCanonicalBSSID(BSSID) || NULL_BSSID.equals(BSSID)
                || WILDCARD_BSSID.equals(BSSID);
    }

    static String canonicalizeBSSID(String BSSID) {
        if (BSSID == null) {
            return "";
        }

        if (isCanonicalBSSID(BSSID)) {
            return BSSID;
        }

        // Some devices may return BSSIDs with '-' or '.' delimiters.
        BSSID = BSSID.toLowerCase(Locale.US).replace(":", "")
                                            .replace("-", "")
                                            .replace(".", "");

        return isCanonicalBSSID(BSSID) ? BSSID : "";
    }

    private static boolean isCanonicalBSSID(String BSSID) {
        return BSSID_PATTERN.matcher(BSSID).matches();
    }
}
