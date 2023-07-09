package top.whiteleaf03.api.util;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;

/**
 * @author WhiteLeaf03
 */
public class SignUtil {
    public static String genSign(String timestamp, HashMap<String, Object> params, String secretKey) {
        String parasStr = JSONUtil.toJsonStr(params);
        String sourceStr = timestamp + "." + parasStr + "." + secretKey;
        return DigestUtil.sha256Hex(sourceStr);
    }

    public static String genSign(String timestamp, String params, String secretKey) {
        String sourceStr = timestamp + "." + params + "." + secretKey;
        return DigestUtil.sha256Hex(sourceStr);
    }
}
