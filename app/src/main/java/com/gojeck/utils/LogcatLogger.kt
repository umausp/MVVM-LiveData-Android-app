package com.gojeck.utils

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

class LogcatLogger {

    companion object {
        private val TAG = "GoJeck"

        fun d(tag: String?, message: String?) {
            Log.d(TAG, getLogString(tag, message))
        }

        fun e(tag: String?, t: Throwable?) {
            Log.e(TAG, getPrintStackTrace(tag, t))
        }

        private fun getLogString(tag: String?, message: String?): String {
            val sb = StringBuilder()
            if (tag != null && tag.isNotEmpty()) {
                sb.append("[").append(tag).append("] ")
            }
            if (message != null && message.isNotEmpty()) {
                sb.append(message)
            }
            val stackTraceElements =
                Thread.currentThread().stackTrace
            if (stackTraceElements.size >= 4) {
                val element = stackTraceElements[4]
                val fullClassName = element.fileName
                val lineNumber = element.lineNumber.toString()
                sb.append(" (").append(fullClassName).append(":").append(lineNumber).append(")")
            }
            return sb.toString()
        }

        private fun getPrintStackTrace(tag: String?, t: Throwable?): String {
            val sb = java.lang.StringBuilder()
            if (tag != null && tag.isNotEmpty()) {
                sb.append("[").append(tag).append("]")
            }
            if (t != null) {
                val sw = StringWriter()
                t.printStackTrace(PrintWriter(sw))
                sb.append(sw.toString())
            }
            return sb.toString()
        }
    }
}

