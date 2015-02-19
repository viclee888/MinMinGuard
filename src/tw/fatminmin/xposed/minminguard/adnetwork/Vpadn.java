package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Vpadn {
    public final static String banner = "com.vpadn.ads.VpadnBanner";
    public final static String bannerPrefix = "com.vpadn.ads";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adView = XposedHelpers.findClass("com.vpadn.ads.VpadnBanner", lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adView, "loadAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect VpadnBanner loadAd in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            
            Class<?> InterAds = XposedHelpers.findClass("com.vpadn.ads.VpadnInterstitialAd", lpparam.classLoader);
            XposedBridge.hookAllMethods(InterAds, "show" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect VpadnInterstitialAd show in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });
            
            Util.log(packageName, packageName + " uses Vpadn");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
