#include <jni.h>
#include "CoutLog.h"

using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_com_sumauto_guard_utils_NativeBridge_hello(JNIEnv *env, jobject thiz) {
    CoutLog redirectLog;
    std::cout.rdbuf(&redirectLog);

    cout<<"tada!!"<<endl;
}