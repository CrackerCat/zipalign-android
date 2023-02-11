package io.github.rohitverma882.zipalign;

public class ZipAlign {
    
    static {
		System.loadLibrary("zipalign");
	}
	
	public static final int DEFAULT_ALIGNMENT = 4;
    
	public static native boolean process(String inZip, String outZip, int alignment, boolean force, boolean zopfli, boolean pageAlignSharedLibs);
	
	public static native boolean verify(String zip, int alignment, boolean pageAlignSharedLibs); 
}
