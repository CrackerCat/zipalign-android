package io.github.rohitverma882.zipalign;

public class ZipAlign {
    static {
		System.loadLibrary("zipalign");
	}
	
	public static final int DEFAULT_ALIGNMENT = 4;
    
	/**
     * Generate a new, aligned, zip "output" from an "input" zip.
     * <p>
     * <b><em>NOTE:</em></b> If the APK is to be signed with schema v2 or later, the APK must be aligned <em>before</em>
     * signing it, and for v1 schema (AKA jar signing), the APK must be aligned <em>after</em> signing it.
     *
     * @param inZip - The zip file to be aligned.
     * @param outZip - File where the aligned zip file will be saved.
     * @param alignment - Alignment (in bytes) for uncompressed entries.
     * @param force - Overwrite output if it exists, fail otherwise.
	 * @param zopfli - Recompress compressed entries with more efficient algorithm.
	 *     Copy compressed entries as-is, and unaligned, otherwise.
	 * @param pageAlignSharedLibs - Align .so files to 4096 and other files to
	 *     alignTo, or all files to alignTo if false..
     * @return {@code true} on success.
     */
	public static native boolean process(String inZip, String outZip, int alignment, boolean force, boolean zopfli, boolean pageAlignSharedLibs);
	
	/**
     * Verify the alignment of a zip archive.
     *
     * @param zip - The zip file whose alignment has to be verified
     * @param alignment - Alignment (in bytes) for uncompressed entries.
	 * @param pageAlignSharedLibs - Align .so files to 4096 and other files to
	 *     alignTo, or all files to alignTo if false..
     * @return {@code true} on success.
     */
	public static native boolean verify(String zip, int alignment, boolean pageAlignSharedLibs); 
}
