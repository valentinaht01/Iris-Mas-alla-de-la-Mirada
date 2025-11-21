package com.mambo.iris.vr

import android.graphics.Bitmap
import android.opengl.*

object GLUtilsExt{
  fun loadTexture(b:Bitmap):Int{ val ids=IntArray(1); GLES20.glGenTextures(1,ids,0); GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,ids[0]); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR); GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,b,0); return ids[0] }
  fun compileShader(t:Int,s:String):Int{ val sh=GLES20.glCreateShader(t); GLES20.glShaderSource(sh,s); GLES20.glCompileShader(sh); val ok=IntArray(1); GLES20.glGetShaderiv(sh,GLES20.GL_COMPILE_STATUS,ok,0); if(ok[0]==0){ val log=GLES20.glGetShaderInfoLog(sh); GLES20.glDeleteShader(sh); throw RuntimeException(log) }; return sh }
  fun linkProgram(v:Int,f:Int):Int{ val p=GLES20.glCreateProgram(); GLES20.glAttachShader(p,v); GLES20.glAttachShader(p,f); GLES20.glLinkProgram(p); val ok=IntArray(1); GLES20.glGetProgramiv(p,GLES20.GL_LINK_STATUS,ok,0); if(ok[0]==0){ val log=GLES20.glGetProgramInfoLog(p); GLES20.glDeleteProgram(p); throw RuntimeException(log) }; return p }
}