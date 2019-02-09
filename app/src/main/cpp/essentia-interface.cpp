#include <jni.h>
#include <iostream>

#include <essentia/algorithmfactory.h>
#include <essentia/pool.h>

// for __android_log_print(ANDROID_LOG_INFO, "YourApp", "formatted message");
#include <android/log.h>




void Java_com_kjipo_eartraining_recorder_Recorder_initializeEssentia(JNIEnv* env, jclass clazz) {

    // Parameters
    uint framesize = 4096;
    uint hopsize = 2048;
    uint zeropadding = 0;

    __android_log_print(ANDROID_LOG_INFO, "C++", "Initializing Essentia");

    essentia::init();

    essentia::Pool pool;


}




jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.

    jclass class1 = env->FindClass("com/kjipo/eartraining/recorder/Recorder");

    JNINativeMethod methods[] = {
            {"initializeEssentia", "()V", (void *)&Java_com_kjipo_eartraining_recorder_Recorder_initializeEssentia},
    };

    env->RegisterNatives(class1, methods, sizeof(methods)/sizeof(methods[0]));

    return JNI_VERSION_1_6;
}