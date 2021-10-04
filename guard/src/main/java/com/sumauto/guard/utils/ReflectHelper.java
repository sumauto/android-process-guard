package com.sumauto.guard.utils;

import java.lang.reflect.Method;

public class ReflectHelper {
    private static Method sSetHiddenApiExemptions;
    private static Object sVMRuntime;

    static {
        try {
            XLog.d("ReflectHelper start ");

            Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethodMethod = Class.class.getDeclaredMethod(
                    "getDeclaredMethod", String.class, Class[].class);

            Class vmRuntimeClass = (Class) forNameMethod.invoke(null, "dalvik.system.VMRuntime");
            sSetHiddenApiExemptions = (Method) getDeclaredMethodMethod.invoke(vmRuntimeClass,
                    "setHiddenApiExemptions", new Class[]{String[].class});
            Method getVMRuntimeMethod = (Method) getDeclaredMethodMethod.invoke(vmRuntimeClass,
                    "getRuntime", null);
            sVMRuntime = getVMRuntimeMethod.invoke(null);
            XLog.d("ReflectHelper success "+sVMRuntime+" ex:"+sSetHiddenApiExemptions);


        } catch (Throwable th) {
            th.printStackTrace();
            XLog.error(th);
        }
    }

    public static boolean setExemptions(String... methods) {
        if ((sSetHiddenApiExemptions == null) || (sVMRuntime == null)) {
            return false;
        }
        XLog.d("invoke hidden");

        try {
            sSetHiddenApiExemptions.invoke(sVMRuntime, new Object[]{methods});
            XLog.d("invoke success");

            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            XLog.error(th);

            return false;
        }
    }

    public static boolean exemptAll() {
        XLog.d("Start execute exemptAll method ...");
        return setExemptions("L");
    }
}
