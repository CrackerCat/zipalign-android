#include <jni.h>
#include <cstdlib>

#include "io_github_rohitverma882_zipalign_ZipAlign.h"
#include "ZipAlign.h"

using namespace android;

JNIEXPORT jboolean JNICALL Java_io_github_rohitverma882_zipalign_ZipAlign_process
  (JNIEnv *env, jclass clazz, jstring inZip, jstring outZip, jint alignment, jboolean force, jboolean zopfli, jboolean pageAlignSharedLibs) {
    const char *inName = env->GetStringUTFChars(inZip, 0);
    const char *outName = env->GetStringUTFChars(outZip, 0);
	int rc = 0;
	if (!inName || !outName) rc = 1;
    rc = process(inName, outName, alignment, force, zopfli, pageAlignSharedLibs);
	if (rc == 0) {
        rc = verify(outName, alignment, false, pageAlignSharedLibs);
    }
	env->ReleaseStringUTFChars(inZip, inName);
	env->ReleaseStringUTFChars(outZip, outName);
    return rc == 0 ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_io_github_rohitverma882_zipalign_ZipAlign_verify
  (JNIEnv *env, jclass clazz, jstring inZip, jint alignment, jboolean pageAlignSharedLibs) {
    const char *zip = env->GetStringUTFChars(inZip, 0);
    int rc = 0;
	if (!zip) rc = 1;
    rc = verify(zip, alignment, false, pageAlignSharedLibs);
	env->ReleaseStringUTFChars(inZip, zip);
    return rc == 0 ? JNI_TRUE : JNI_FALSE;
}
